package com.example.peoplesontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.ItemRequestBinding
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class RequestAdapter(private var listener: (Request, Boolean) -> Unit) :
    RecyclerView.Adapter<RequestAdapter.AppealHolder>(), Filterable {

    private var appealsFilter = mutableListOf<Request>()
    private val appeals = mutableListOf<Request>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestAdapter.AppealHolder {
        val binding =
            ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = AppealHolder(binding)
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION)
                listener(appealsFilter[viewHolder.adapterPosition], false)
        }
        return viewHolder
    }

    override fun getItemCount() = appealsFilter.size

    override fun onBindViewHolder(holder: AppealHolder, position: Int) {
        holder.bind(appealsFilter[position])
    }

    inner class AppealHolder(var binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(request: Request) {
            if(request.problem_categories.isEmpty()) {
                binding.nameRequest.text = "Категория"
            } else {
                binding.nameRequest.text = request.problem_categories[0].title
            }
            binding.descriptionRequest.text = request.description
            binding.addressRequest.text = "Адрес: " + request.location
            binding.idRequest.text = "№${request.requestId}"
            binding.btnAddRequest.setOnClickListener {
               listener(request, true)
            }
        }
    }

    fun setData(newData: List<Request>) {
        appealsFilter.clear()
        appealsFilter.addAll(newData)
        appeals.addAll(appealsFilter)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        appealsFilter.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, appealsFilter.size)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    appealsFilter = appeals
                } else {
                    val resultList: MutableList<Request> = ArrayList()
                    for (row in appeals) {
                        if (row.requestId.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                        if (row.description?.toLowerCase(Locale.ROOT)
                            !!.contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    appealsFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = appealsFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appealsFilter = results?.values as MutableList<Request>
                notifyDataSetChanged()
            }
        }
    }
}