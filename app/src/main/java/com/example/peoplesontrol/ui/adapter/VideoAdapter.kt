package com.example.peoplesontrol.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.databinding.ItemVideoBinding

class VideoAdapter(private var listener: (Int) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    private var videos = mutableListOf<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.VideoHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(videos[position], position)
    }

    inner class VideoHolder(var binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: String?, position: Int) {
            binding.video.text = uri
            binding.btnDelete.setOnClickListener {
                listener(position)
            }
        }
    }

    fun setData(newData: List<String?>) {
        videos.clear()
        videos.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        videos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, videos.size)
    }

    override fun getItemCount() = videos.size
}