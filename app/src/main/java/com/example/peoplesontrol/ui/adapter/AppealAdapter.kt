package com.example.peoplesontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.ItemAppealBinding
import java.util.*
import kotlin.collections.ArrayList

class AppealAdapter(private var listener: (Appeal) -> Unit) :
    RecyclerView.Adapter<AppealAdapter.AppealHolder>(), Filterable {

    private var appealsFilter = mutableListOf<Appeal>()
    private val appeals = mutableListOf<Appeal>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppealAdapter.AppealHolder {
        val binding =
            ItemAppealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = AppealHolder(binding)
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION)
                listener(appealsFilter[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = appealsFilter.size

    override fun onBindViewHolder(holder: AppealHolder, position: Int) {
        holder.bind(appealsFilter[position])
    }

    inner class AppealHolder(var binding: ItemAppealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appeal: Appeal) {
            binding.nameAppeal.text = appeal.nameProblem
            binding.descriptionAppeal.text = appeal.description
            binding.addressAppeal.text = "Адрес: " + appeal.address
            binding.coordinatesAppeal.text =
                "Координаты: " + appeal.latitude + " " + appeal.longitude
            binding.idAppeal.text = "№" + appeal.id.toString()
            binding.numAppeals.text = appeal.numAppeal.toString() + " заявок"
            binding.dateAppeal.text = appeal.date
            binding.statusAppeal.text = appeal.status
        }
    }

    fun setData(newData: List<Appeal>) {
        appealsFilter.clear()
        appealsFilter.addAll(newData)
        appeals.addAll(appealsFilter)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    appealsFilter = appeals
                } else {
                    val resultList: MutableList<Appeal> = ArrayList()
                    for (row in appeals) {
                        if (row.id.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                        if (row.nameProblem.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                        if (row.description.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
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
                appealsFilter = results?.values as MutableList<Appeal>
                notifyDataSetChanged()
            }
        }
    }
}