package com.example.legostore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legostore.adapter.CartAdapter
import com.example.legostore.data.AppDatabase
import com.example.legostore.databinding.FragmentCartBinding
import com.example.legostore.repository.CartRepository
import com.example.legostore.viewmodel.CartViewModel
import com.example.legostore.viewmodel.CartViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartRepository(AppDatabase.getDatabase(requireContext()).cartDao()))
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeCartItems()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncreaseQuantity = { cartItem ->
                cartViewModel.updateQuantity(cartItem.productId, cartItem.quantity + 1)
            },
            onDecreaseQuantity = { cartItem ->
                if (cartItem.quantity > 1) {
                    cartViewModel.updateQuantity(cartItem.productId, cartItem.quantity - 1)
                }
            },
            onRemoveItem = { cartItem ->
                cartViewModel.removeFromCart(cartItem.productId)
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartToCheckout())
        }
    }

    private fun observeCartItems() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)
            updateTotalAmount(cartItems)
        }
    }

    private fun updateTotalAmount(cartItems: List<CartItem>) {
        var total = 0.0
        cartItems.forEach { cartItem ->
            total += cartItem.price * cartItem.quantity
        }
        binding.tvTotalAmount.text = String.format("$%.2f", total)
        binding.btnCheckout.isEnabled = cartItems.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 