package com.example.peoplesontrol.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.databinding.ItemPhotoBinding

class PhotoAdapter(private var listener: (Int) -> Unit) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    private var photos = mutableListOf<Uri?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.PhotoHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(photos[position], position)
    }

    inner class PhotoHolder(var binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri?, position: Int) {
            binding.photo.setImageURI(uri)
            binding.btnDelete.setOnClickListener {
                listener(position)
            }
        }
    }

    fun setData(newData: List<Uri?>) {
        photos.clear()
        photos.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        photos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, photos.size)
    }

    override fun getItemCount() = photos.size
}