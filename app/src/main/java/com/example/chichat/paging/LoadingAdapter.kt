package com.example.chichat.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chichat.databinding.PageLoaderBinding

class LoadingAdapter : LoadStateAdapter<LoadingAdapter.LoadingViewHolder>() {

    class LoadingViewHolder(binding : PageLoaderBinding) : RecyclerView.ViewHolder(binding.root){
        val progressBar = binding.progressBar

        fun bind(loadState: LoadState){
            if(loadState is LoadState.Loading){
                progressBar.visibility = RecyclerView.VISIBLE
            }else{
                progressBar.visibility = RecyclerView.GONE
            }
        }

    }

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder(PageLoaderBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

}