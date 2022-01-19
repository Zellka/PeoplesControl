package com.example.peoplesontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.databinding.ItemRegionBinding
import java.util.*
import kotlin.collections.ArrayList

class ItemAdapter(private var listener: (String) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>(), Filterable {

    private var regionsFilter = mutableListOf<String>()
    private val regions = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        val binding = ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ItemHolder(binding)
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION)
                listener(regionsFilter[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = regionsFilter.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(regionsFilter[position])
    }

    inner class ItemHolder(var binding: ItemRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(region: String) {
            binding.item.text = region
        }
    }

    fun setData(newData: List<String>) {
        regionsFilter.clear()
        regionsFilter.addAll(newData)
        regions.addAll(regionsFilter)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    regionsFilter = regions
                } else {
                    val resultList: MutableList<String> = ArrayList()
                    for (row in regions) {
                        if (row.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    regionsFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = regionsFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                regionsFilter = results?.values as MutableList<String>
                notifyDataSetChanged()
            }
        }
    }
}