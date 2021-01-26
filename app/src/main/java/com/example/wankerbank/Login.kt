package com.example.wankerbank

import android.app.ActivityOptions
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.util.Pair
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.logo
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

//import com.squareup.okhttp.Response

//import com.beust.klaxon.Parser

import okhttp3.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        signIn.setOnClickListener {
            if (!RequestApi.isOnline(this)) {
                Log.w("Internet Connection is ", RequestApi.isOnline(this).toString())
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                Log.w("Internet Connection is: ", RequestApi.isOnline(this).toString())
            }

            val es: ExecutorService = Executors.newCachedThreadPool()
            es.execute {
                validateLogin()
            }

            es.shutdown()

            val finished = es.awaitTermination(3, TimeUnit.SECONDS)

            if (finished) {
                Log.w("First Warning", RequestApi.tokenApi)

                while (true) {
//                    Log.w("Token status", RequestApi.tokenApi.toString())
                    when(RequestApi.tokenStatus) {
                        "Error" -> {
                            Toast.makeText(this,"Connection error", Toast.LENGTH_LONG).show()
                            RequestApi.tokenStatus = "empty"
                            break;
                        }

                        "Not found" -> {
                            Toast.makeText(this, "CardID or PIN not found", Toast.LENGTH_LONG).show()
                            RequestApi.tokenStatus = "empty"
                            break;
                        }

                        "Accepted" -> {
                            Log.w("TokenAPi222", RequestApi.tokenStatus)
                            RequestApi.tokenStatus = "empty"
                            Handler(Looper.myLooper()!!).post{

                                val intent = Intent(this@Login, Dashboard::class.java)

                                val cardInfo: JSONObject = RequestApi.JSONMap.get("Card") as JSONObject

                                RequestApi.remainFund = cardInfo.get("Balance") as Int
                                startActivity(intent)

                                finish()
                            }
                            break
                        }
                    }
                }
            }
        }

        contact.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            val pairs = listOf<Pair<View, String>>(
                Pair<View, String>(logo, "image"),
                Pair<View, String>(Header, "text"),
                Pair<View, String>(subtitle, "text"),
                Pair<View, String>(cardId, "input"),
                Pair<View, String>(fund, "input"),
                Pair<View, String>(forgetPIN, "button"), Pair<View, String>(signIn, "button"),
                Pair<View, String>(contact, "button"),
            )
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@Login,
                pairs[0],
                pairs[1],
                pairs[2],
                pairs[3],
                pairs[4],
                pairs[5],
                pairs[6],
            )
            startActivity(intent, options.toBundle())

            finish()
        }
    }

    private fun validateLogin () {
        val cardIdText = cardId.editText?.text.toString()
        val pinText = fund.editText?.text.toString()

        val jsonObject = JSONObject()

        jsonObject.put("cardId", cardIdText)
        jsonObject.put("PIN", pinText)

        val url = "https://wankerapi.azurewebsites.net/api/user/card-login"

        RequestApi.Request(url, jsonObject)

        Log.w("Warning", "Exit Request")
   }
}