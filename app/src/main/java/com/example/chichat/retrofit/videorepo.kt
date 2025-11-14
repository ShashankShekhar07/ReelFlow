package com.example.chichat.retrofit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.chichat.paging.videopagingsource
import javax.inject.Inject

class videorepo @Inject constructor(val ReelFlowAPI: ReelFlowAPI) {

    fun getVideos() = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30
        ),
        pagingSourceFactory = { videopagingsource(ReelFlowAPI) }
    ).liveData
}