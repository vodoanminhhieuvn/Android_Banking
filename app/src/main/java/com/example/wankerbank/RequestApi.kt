package com.example.wankerbank


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.util.Log.WARN
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.beust.klaxon.json
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log
import kotlin.properties.Delegates

class RequestApi{
    companion object {

        @JvmStatic
        var isConnectionLost: Boolean = false

        @JvmStatic
        var tokenApi: String = "empty"

        @JvmStatic
        var tokenStatus = "empty"

        @JvmStatic
        lateinit var cardInfo: JSONObject

        @JvmStatic
        lateinit var userInfo: JSONObject

        @JvmStatic
        lateinit var JSONMap: JSONObject

        @JvmStatic
        var remainFund by Delegates.notNull<Int>()

        //        @SuppressLint("ServiceCast")
        @JvmStatic
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

                Log.w("This is internet", capabilities.toString())
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

        @JvmStatic
        fun Request(url: String, jsonObject: JSONObject) {
            var catchJsonObject: String
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            val body: RequestBody = jsonObject.toString().toRequestBody(mediaType)
            val request =  Request.Builder().url(url).post(body).build()
            Log.w("Warning", "Enter Request")
            val client = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.w("Warning", "Enter Json")
                    val strJSon = response.body?.string()

                    if (strJSon != null) {
                        Log.w("WARNING", strJSon)
                    }
//                            val parseJson: Parser = Parser.default()
                    try {
                        val jsonMap = JSONObject(strJSon)
                        catchJsonObject = jsonMap.get("Token") as String
                        JSONMap = jsonMap
                        tokenApi = catchJsonObject
                        tokenStatus = "Accepted"
                        Log.w("Token", RequestApi.tokenStatus)
                    }
                    catch (cause: Exception) {
                        Log.w("Big Warning", "Hello World")
                        tokenStatus = "Not found"
                    }

                    return
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.w("Error here", "Catch Token")
                    tokenStatus = "Error"
                    return
                }
            })
        }

        @JvmStatic
        fun Request(url: String, jsonObject: JSONObject, token: String, getObject: String, ) {
            var catchJsonObject : String = ""
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            val body: RequestBody = jsonObject.toString().toRequestBody(mediaType)
            val request =  Request.Builder().url(url).header("auth-token", token).post(body).build()
            Log.w("Warning", "Enter Request")
            val client = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.w("Warning", "Enter Json")
                    val strJSon = response.body?.string()

                    if (strJSon != null) {
                        Log.w("WARNING", strJSon)
                    }
//                            val parseJson: Parser = Parser.default()
                    try {
                        val jsonMap = JSONObject(strJSon)
                        tokenStatus = "Accepted"
                        JSONMap = jsonMap
                        Log.w("Token", RequestApi.tokenStatus)
                    }
                    catch (cause: Exception) {
                        Log.w("Cause", cause)
                        tokenStatus = "Not found"
                    }

                    return
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.w("Error here", "Catch Token")
                    tokenStatus = "Error"
                    return
                }
            })
        }

        @JvmStatic
        fun GetRequestApi(url: String, token: String, getObject: String) {
            var catchJsonObject : String = ""
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            val request =  Request.Builder().url(url).header("auth-token", token).get().build()
            Log.w("Warning", "Enter Request")
            val client = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.w("Warning", "Enter Json")
                    val strJSon = response.body?.string()

                    if (strJSon != null) {
//                        Log.w("WARNING", strJSon)
                    }
//                            val parseJson: Parser = Parser.default()
                    try {
                        JSONMap = JSONObject(strJSon)
                        tokenStatus = "Accepted"
                        Log.w("Token", RequestApi.tokenStatus)
                    }
                    catch (cause: Exception) {
                        Log.w("Cause", cause)
                        tokenStatus = "Not found"
                    }

                    return
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.w("Error here", "Catch Token")
                    tokenStatus = "Error"
                    return
                }
            })
        }
    }
}