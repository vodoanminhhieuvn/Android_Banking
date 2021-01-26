package com.example.wankerbank

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.util.Pair
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic private val SPLASH_SCREEN : Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val topAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        imageBackground.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)
        sologan.startAnimation(bottomAnim)

        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this@MainActivity, Login::class.java)

            val pairs = listOf<Pair<View, String>>(
                Pair<View, String>(imageBackground, "image"),
                Pair<View, String>(logo, "text"),
            )

            val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, pairs[0], pairs[1])

            startActivity(intent, options.toBundle())

            finish()
        }, SPLASH_SCREEN)
    }
}