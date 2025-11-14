package com.example.chichat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2
import com.example.chichat.databinding.FragmentPlayBinding
import com.example.chichat.model.ExoPlayerReady
import com.example.chichat.model.VideoList
import com.example.chichat.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayFragment : Fragment() {

    lateinit var binding: FragmentPlayBinding
    private lateinit var adapter: VideoPlayAdapter
    private val exoPlayerItems : ArrayList<ExoPlayerReady> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater)


        parentFragmentManager.setFragmentResultListener(AppConstants.VIDEO_ID, this) { key, bundle ->
            val videoUri = bundle.getInt(AppConstants.ID)
            val videoList = bundle.getSerializable(AppConstants.VIDEO) as VideoList
            videoList.videoList.sortedBy { it.id }
            startPlaying(videoUri, videoList)
        }



        return binding.root
    }

    private fun startPlaying(videoUri : Int, videoList : VideoList){
        var cur_video : Int?= null
        val vidList = videoList.videoList
        var videoUrlList = ArrayList<Pair<String,String>>()
        for(i in vidList.indices){

            if(vidList[i].id == videoUri){
                cur_video = i
            }

            val name = vidList[i].url.split("/")
            val na = name[name.size-2].split("-").toMutableList()
            var videoName = ""
            for(i in 0..na.size-2){
                videoName += na[i] + " "
            }

            for (type in vidList[i].video_files){
                if(type.quality == "sd"){
                    videoUrlList.add(Pair(videoName,type.link))
                    break
                }
            }
        }

        adapter = VideoPlayAdapter(requireContext(),videoUrlList , object : VideoPlayAdapter.OnVideoReadyListener{
            override fun onVideoReady(exoPlayerReady: ExoPlayerReady) {
                exoPlayerItems.add(exoPlayerReady)
            }
        })
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1){
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }

                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1){
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
        binding.viewPager.currentItem = cur_video!!

    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager.currentItem }
        if (index != -1){
            val player = exoPlayerItems[index].exoPlayer
            player.release()
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager.currentItem }
        if (index != -1){
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }

    }

    override fun onStart() {
        super.onStart()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager.currentItem }
        if (index != -1){
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        for (item in exoPlayerItems){
            item.exoPlayer.release()
        }
    }

}