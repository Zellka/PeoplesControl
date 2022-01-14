package com.example.peoplesontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.databinding.ItemRegionBinding

class RegionAdapter(private var listener: (String) -> Unit) :
    RecyclerView.Adapter<RegionAdapter.RegionHolder>() {

    private val regions = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionAdapter.RegionHolder {
        val binding = ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = RegionHolder(binding)
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION)
                listener(regions[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = regions.size

    override fun onBindViewHolder(holder: RegionHolder, position: Int) {
        holder.bind(regions[position])
    }

    inner class RegionHolder(var binding: ItemRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(region: String) {
            binding.region.text = region
        }
    }

    fun setData(newData: List<String>) {
        regions.clear()
        regions.addAll(newData)
        notifyDataSetChanged()
    }
}