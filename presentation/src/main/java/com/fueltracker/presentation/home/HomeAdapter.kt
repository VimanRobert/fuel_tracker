package com.fueltracker.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fueltracker.domain.model.HomeDestination
import com.fueltracker.domain.model.HomeItem
import com.fueltracker.presentation.databinding.ItemHomeBinding

class HomeAdapter(
    private val items: List<HomeItem>,
    private val onClick: (HomeDestination) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeItem) {
            binding.title.text = item.title
            //binding.icon.setImageResource(item.iconResId)
            binding.root.setOnClickListener { onClick(item.destination) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
