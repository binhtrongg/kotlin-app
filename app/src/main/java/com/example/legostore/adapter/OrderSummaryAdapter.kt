package com.example.legostore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.legostore.data.CartItem
import com.example.legostore.databinding.ItemOrderSummaryBinding

class OrderSummaryAdapter : ListAdapter<CartItem, OrderSummaryAdapter.OrderSummaryViewHolder>(OrderSummaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSummaryViewHolder {
        val binding = ItemOrderSummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderSummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderSummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderSummaryViewHolder(
        private val binding: ItemOrderSummaryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductName.text = cartItem.productName
                tvQuantity.text = cartItem.quantity.toString()
                tvPrice.text = String.format("$%.2f", cartItem.price * cartItem.quantity)
            }
        }
    }

    private class OrderSummaryDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
} 