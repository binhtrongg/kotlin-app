package com.example.legostore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legostore.R
import com.example.legostore.adapter.OrderSummaryAdapter
import com.example.legostore.data.AppDatabase
import com.example.legostore.data.Order
import com.example.legostore.databinding.FragmentCheckoutBinding
import com.example.legostore.repository.CartRepository
import com.example.legostore.repository.OrderRepository
import com.example.legostore.viewmodel.CartViewModel
import com.example.legostore.viewmodel.CartViewModelFactory
import com.example.legostore.viewmodel.OrderViewModel
import com.example.legostore.viewmodel.OrderViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartRepository(AppDatabase.getDatabase(requireContext()).cartDao()))
    }

    private val orderViewModel: OrderViewModel by viewModels {
        OrderViewModelFactory(OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao()))
    }

    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        loadOrderSummary()
    }

    private fun setupRecyclerView() {
        orderSummaryAdapter = OrderSummaryAdapter()
        binding.rvOrderSummary.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderSummaryAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnConfirmOrder.setOnClickListener {
            if (validateInputs()) {
                placeOrder()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        binding.apply {
            if (tilName.editText?.text.isNullOrBlank()) {
                tilName.error = getString(R.string.required_field)
                isValid = false
            } else {
                tilName.error = null
            }

            if (tilEmail.editText?.text.isNullOrBlank()) {
                tilEmail.error = getString(R.string.required_field)
                isValid = false
            } else {
                tilEmail.error = null
            }

            if (tilAddress.editText?.text.isNullOrBlank()) {
                tilAddress.error = getString(R.string.required_field)
                isValid = false
            } else {
                tilAddress.error = null
            }

            if (tilPhone.editText?.text.isNullOrBlank()) {
                tilPhone.error = getString(R.string.required_field)
                isValid = false
            } else {
                tilPhone.error = null
            }
        }

        return isValid
    }

    private fun loadOrderSummary() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            orderSummaryAdapter.submitList(cartItems)
            var total = 0.0
            cartItems.forEach { cartItem ->
                total += cartItem.price * cartItem.quantity
            }
            binding.tvTotalAmount.text = String.format("$%.2f", total)
        }
    }

    private fun placeOrder() {
        binding.apply {
            val order = Order(
                customerName = tilName.editText?.text.toString(),
                customerEmail = tilEmail.editText?.text.toString(),
                customerAddress = tilAddress.editText?.text.toString(),
                customerPhone = tilPhone.editText?.text.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                orderViewModel.placeOrder(order)
                cartViewModel.clearCart()

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), R.string.order_placed, Toast.LENGTH_LONG).show()
                    findNavController().navigate(CheckoutFragmentDirections.actionCheckoutToProductList())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 