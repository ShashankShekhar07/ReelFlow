package com.example.chichat.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chichat.R
import com.example.chichat.databinding.ItemLayoutBinding
import com.example.chichat.model.Video

class VideoPagingAdapter :
    PagingDataAdapter<Video, VideoPagingAdapter.VideoViewHolder>(VideoComparator) {

    class VideoViewHolder(binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val videoImage = binding.imageView
        val name = binding.nameVideo
        val maker = binding.makerName
        val duration = binding.duration
        val play = binding.playButton
    }
    private var onClickListener : OnClickListener ?= null

    companion object {
        val VideoComparator = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        if (video != null) {
            Glide.with(holder.itemView)
                .load(video.video_pictures[0].picture)
                .placeholder(R.drawable.play)
                .into(holder.videoImage)

            val name = video.url.split("/")
            val na = name[name.size-2].split("-").toMutableList()
            var videoName = ""
            for(i in 0..na.size-2){
                videoName += na[i] + " "
            }

            holder.name.text = videoName
            holder.maker.text = "By - ${video.user.name}"
            holder.duration.text = "${video.duration}s"

            holder.play.setOnClickListener{
                if (onClickListener != null){
                    onClickListener!!.onClick(position,video)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    interface OnClickListener{
        fun onClick(position: Int , modal : Video)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

}

