package com.example.wankerbank

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_helper.*
import java.util.jar.Manifest

class Helper : AppCompatActivity() {

    companion object {
        @JvmStatic
        private val REQUEST_CALL: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helper)

        imageCall.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val dial:String = "tel:0838088590"
            startActivity( Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CALL) {

        }
    }
}