package com.example.chichat.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.RecyclerView
import com.example.chichat.databinding.VideoItemLayoutBinding
import com.example.chichat.model.ExoPlayerReady

class VideoPlayAdapter(var context: Context, var videos: ArrayList<Pair<String,String>>,
                       var videoReadyListener: OnVideoReadyListener) :
    RecyclerView.Adapter<VideoPlayAdapter.VideoPlayViewHolder>() {

    class VideoPlayViewHolder(
        val binding: VideoItemLayoutBinding,
        var context: Context,
        var videoReadyListener: OnVideoReadyListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var mediaSource: MediaSource

        val track_name = binding.trackName

        @androidx.media3.common.util.UnstableApi
        fun playVideo(Url: String) {
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Error Playing the Video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == ExoPlayer.STATE_BUFFERING) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else if (playbackState == ExoPlayer.STATE_READY) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })

            binding.playerView.player = exoPlayer

            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            binding.playerView.setOnClickListener {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            }

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Url))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()


            exoPlayer.playWhenReady = true
            exoPlayer.play()

            videoReadyListener.onVideoReady(ExoPlayerReady(exoPlayer, absoluteAdapterPosition))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayViewHolder {
        val binding = VideoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoPlayViewHolder(binding, context, videoReadyListener)
    }

    override fun getItemCount(): Int {
        return videos.size
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun onBindViewHolder(holder: VideoPlayViewHolder, position: Int) {
        val model = videos[position]
        holder.track_name.text = model.first
        holder.playVideo(model.second)
    }

    interface OnVideoReadyListener {
        fun onVideoReady(exoPlayerReady : ExoPlayerReady)
    }

}
