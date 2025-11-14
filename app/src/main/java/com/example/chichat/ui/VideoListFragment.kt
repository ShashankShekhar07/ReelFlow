package com.example.chichat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chichat.R
import com.example.chichat.databinding.FragmentVideoListBinding
import com.example.chichat.model.Video
import com.example.chichat.model.VideoList
import com.example.chichat.paging.LoadingAdapter
import com.example.chichat.paging.VideoPagingAdapter
import com.example.chichat.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListFragment : Fragment() {

    lateinit var binding : FragmentVideoListBinding
    lateinit var adapter: VideoPagingAdapter
    lateinit var viewModel: MainViewModel
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentVideoListBinding.inflate(layoutInflater)

        adapter = VideoPagingAdapter()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        adapter.setOnClickListener(object : VideoPagingAdapter.OnClickListener{
            override fun onClick(position: Int, modal: Video) {
                val bundle = Bundle()
                val list = adapter.snapshot().items
                list.sortedBy { it.id }
                bundle.putInt(AppConstants.ID,modal.id)
                bundle.putSerializable(AppConstants.VIDEO,VideoList(list))
                parentFragmentManager.setFragmentResult(AppConstants.VIDEO_ID , bundle)
                val k = parentFragmentManager.findFragmentById(R.id.video_fragment)
                Navigation.findNavController(k!!.requireView()).navigate(R.id.action_videoListFragment_to_playFragment)
            }
        })

        binding.searchResultRv.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResultRv.setHasFixedSize(true)
        binding.searchResultRv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingAdapter(),
            footer = LoadingAdapter()
        )

        viewModel.videoList.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        return binding.root
    }

}