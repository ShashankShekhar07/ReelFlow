package com.example.chichat.model

import androidx.media3.exoplayer.ExoPlayer

data class ExoPlayerReady (
    val exoPlayer: ExoPlayer,
    var position : Int
    )