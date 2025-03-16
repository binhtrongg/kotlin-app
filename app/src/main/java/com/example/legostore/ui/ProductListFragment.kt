package com.example.legostore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.legostore.R
import com.example.legostore.data.AppDatabase
import com.example.legostore.data.entity.Product
import com.example.legostore.databinding.FragmentProductListBinding
import com.example.legostore.repository.CartRepository
import com.example.legostore.repository.ProductRepository
import com.example.legostore.ui.adapter.ProductAdapter
import com.example.legostore.viewmodel.CartViewModel
import com.example.legostore.viewmodel.CartViewModelFactory
import com.example.legostore.viewmodel.ProductViewModel
import com.example.legostore.viewmodel.ProductViewModelFactory

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(ProductRepository(AppDatabase.getDatabase(requireContext()).productDao()))
    }

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartRepository(AppDatabase.getDatabase(requireContext()).cartDao()))
    }

    private val productAdapter = ProductAdapter(
        onProductClick = { product ->
            findNavController().navigate(
                ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(product.id)
            )
        },
        onAddToCartClick = { product ->
            addToCart(product)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvProducts.adapter = productAdapter
    }

    private fun setupClickListeners() {
        binding.fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_cartFragment)
        }
    }

    private fun observeViewModel() {
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }
    }

    private fun addToCart(product: Product) {
        cartViewModel.addToCart(product.id)
        Toast.makeText(requireContext(), R.string.product_added, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 