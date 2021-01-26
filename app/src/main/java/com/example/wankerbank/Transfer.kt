package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.activity_transfer.fund
import kotlinx.android.synthetic.main.activity_transfer.cardId
import org.json.JSONObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Transfer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        confirm.setOnClickListener {
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
                                Toast.makeText(this, "Send successfully", Toast.LENGTH_LONG).show()

                                val intent = Intent(this@Transfer, Dashboard::class.java)

                                RequestApi.remainFund = RequestApi.JSONMap.get("cardSend_balance") as Int
                                startActivity(intent)

                                finish()
                            }
                            break
                        }
                    }
                }
            }
        }

        cancel.setOnClickListener {
            val intent = Intent(this@Transfer, Dashboard::class.java)

            startActivity(intent)

            finish()
        }
    }

    private fun validateLogin() {
        val cardIdText = cardId.editText?.text.toString()
        val fundText = fund.editText?.text.toString()

        val jsonObject = JSONObject()

        jsonObject.put("cardId", cardIdText)
        jsonObject.put("fund", fundText)

        val url = "https://wankerapi.azurewebsites.net/api/send-money"

        RequestApi.Request(url, jsonObject, RequestApi.tokenApi, "balance")

        Log.w("Warning", "Exit Request")
    }
}