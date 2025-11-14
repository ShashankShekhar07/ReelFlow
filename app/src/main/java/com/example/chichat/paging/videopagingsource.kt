package com.example.chichat.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chichat.retrofit.ReelFlowAPI
import com.example.chichat.model.Video

class videopagingsource(val ReelFlowAPI: ReelFlowAPI) : PagingSource<Int, Video>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val currentLoadingPageKey = params.key ?: 1
            val response = ReelFlowAPI.getPopVideos(currentLoadingPageKey)
            val responseData = mutableListOf<Video>()
            responseData.addAll(response.videos)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1,
                nextKey = if (currentLoadingPageKey == response.total_results) null else currentLoadingPageKey + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    }
}