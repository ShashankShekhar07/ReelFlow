package com.example.chichat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.chichat.model.Video
import com.example.chichat.retrofit.videorepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(videorepo : videorepo) : ViewModel() {

    val videoList : LiveData<PagingData<Video>>  = videorepo.getVideos().cachedIn(viewModelScope)

}