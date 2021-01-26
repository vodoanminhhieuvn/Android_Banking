package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_width_draw.*

class WidthDraw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_width_draw)

        BackToHome.setOnClickListener {
            val intent = Intent(this@WidthDraw, Dashboard::class.java)

            startActivity(intent)

            finish()
        }
    }
}