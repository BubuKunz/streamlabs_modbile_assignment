package com.streamlabs.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.streamlabs.test.feed.VideoFeedFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.hostFragment, VideoFeedFragment())
            .commit()
    }
}