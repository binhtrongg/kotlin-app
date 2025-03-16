package com.example.legostore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.legostore.data.model.CartItemWithProduct
import com.example.legostore.databinding.ItemCartBinding

class CartAdapter(
    private val onQuantityChanged: (CartItemWithProduct, Int) -> Unit,
    private val onRemoveClick: (CartItemWithProduct) -> Unit
) : ListAdapter<CartItemWithProduct, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItemWithProduct) {
            val product = cartItem.product
            binding.tvProductName.text = product.name
            binding.tvPrice.text = String.format("$%.2f", product.price)
            binding.tvQuantity.text = cartItem.cartItem.quantity.toString()

            Glide.with(binding.ivProduct)
                .load(product.imageUrl)
                .centerCrop()
                .into(binding.ivProduct)

            binding.btnIncrease.setOnClickListener {
                val newQuantity = cartItem.cartItem.quantity + 1
                onQuantityChanged(cartItem, newQuantity)
            }

            binding.btnDecrease.setOnClickListener {
                val newQuantity = (cartItem.cartItem.quantity - 1).coerceAtLeast(1)
                onQuantityChanged(cartItem, newQuantity)
            }

            binding.btnRemove.setOnClickListener {
                onRemoveClick(cartItem)
            }
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<CartItemWithProduct>() {
        override fun areItemsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
            return oldItem.cartItem.id == newItem.cartItem.id
        }

        override fun areContentsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
            return oldItem == newItem
        }
    }
} 