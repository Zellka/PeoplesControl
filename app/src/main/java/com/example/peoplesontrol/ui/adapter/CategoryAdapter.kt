package com.example.peoplesontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.databinding.ItemCategoryBinding
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CategoryAdapter(private var listener: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>(), Filterable {

    private val categories = mutableListOf<Category>()
    private var categoriesFilter = mutableListOf<Category>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CategoryHolder(binding)
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION)
                listener(categoriesFilter[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = categoriesFilter.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(categoriesFilter[position])
    }

    inner class CategoryHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            if(category.icon != null) {
                Picasso.get().load("http://164.92.215.12:8644" + category.icon)
                    .error(R.drawable.placeholder_category)
                    .into(binding.imgCategory)
            }
            binding.titleCategory.text = category.title
        }
    }

    fun setData(newData: List<Category>) {
        categoriesFilter.clear()
        categoriesFilter.addAll(newData)
        categories.addAll(categoriesFilter)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    categoriesFilter = categories
                } else {
                    val resultList: MutableList<Category> = ArrayList()
                    for (row in categories) {
                        if (row.title.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    categoriesFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = categoriesFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoriesFilter = results?.values as MutableList<Category>
                notifyDataSetChanged()
            }
        }
    }
}