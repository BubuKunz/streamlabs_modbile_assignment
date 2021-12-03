//package com.streamlabs.test.feed
//
//import android.net.Uri
//import com.google.android.exoplayer2.SimpleExoPlayer
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
//import com.google.android.exoplayer2.source.ExtractorMediaSource
//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.source.ProgressiveMediaSource
//import com.google.android.exoplayer2.upstream.DefaultDataSource
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
//
//fun SimpleExoPlayer.prepareToPlayVideoFromUri(uri: Uri) {
//    val source: MediaSource = ExtractorMediaSource(
//        uri,
//        DefaultHttpDataSourceFactory("v-1.0"),
//        DefaultExtractorsFactory(),
//        null,
//        null
//    )
//    DefaultDataSource(
//        source
//    )
//    prepare(source)
////    val mediaSource =
////        ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory("v-1.0")).createMediaSource(uri)
////    prepare(mediaSource)
//}
