package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wankerbank.StaticRvAdapter
import com.example.wankerbank.DynamicRvModel
import com.example.wankerbank.DynamicRvAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.wankerbank.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wankerbank.DVRInterface.LoadMore
import android.widget.Toast
import com.beust.klaxon.Json
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_transfer.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class History : AppCompatActivity() {

    companion object {
        @JvmStatic
        var isAllowLoadingView: Boolean = true
        var transactionJSON: JSONObject? = null
        var amountOfNode: Int = 0
    }

    private val recyclerView: RecyclerView? = null
    private val staticRvAdapter: StaticRvAdapter? = null
    var items: MutableList<DynamicRvModel?> = ArrayList<DynamicRvModel?>()
    var dynamicRvAdapter: DynamicRvAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        getTransactionList()

        Back_Btn.setOnClickListener {
            val intent = Intent(this@History, Dashboard::class.java )

            startActivity(intent)
        }

    }

    fun getTransactionList() {
        if (!RequestApi.isOnline(this)) {
            Log.w("Internet Connection is ", RequestApi.isOnline(this).toString())
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        } else {
            Log.w("Internet Connection is: ", RequestApi.isOnline(this).toString())
        }

        val es: ExecutorService = Executors.newCachedThreadPool()
        es.execute {
            validateApi()
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
                        if (isAllowLoadingView) {
                            Handler(Looper.myLooper()!!).post{
                                Toast.makeText(this, "Data completed", Toast.LENGTH_LONG).show()

                                val intent = Intent(this@History, History::class.java )

                                startActivity(intent)

                                finish()
                            }
                            isAllowLoadingView = false;
                        }

                        break
                    }
                }
            }

            transactionJSON = RequestApi.JSONMap

//            Log.w("Hello0--------------", transactionJSON.toString())

            val transactionDateDSC: JSONArray = transactionJSON!!.getJSONArray("dateDSC")

            amountOfNode = transactionDateDSC?.length()

            Log.w("WARNING NO", "-----------------------------------------------------------" + amountOfNode)

            Log.w("WARNING NO", "-----------------------------------------------------------")

            for (transactionIndex in 0 until amountOfNode)
            {
                val transaction: JSONObject = transactionDateDSC.get(transactionIndex) as JSONObject

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                var transactionDateString: String
                var transactionDate = Date()

                try {
                    transactionDate = formatter.parse(transaction.get("date") as String)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")

                transactionDateString = dateFormat.format(transactionDate)
                items.add(DynamicRvModel(transaction.get("type").toString(), transactionDateString, transaction.get("fund").toString()))
            }

            val drv = findViewById<RecyclerView>(R.id.Transaction_History)
            drv.layoutManager = LinearLayoutManager(this)
            dynamicRvAdapter = DynamicRvAdapter(drv, this, items)
            drv.adapter = dynamicRvAdapter
            dynamicRvAdapter!!.setLoadMore {
                Log.w("ITEM SIZE", items.size.toString())
                if (items.size <= 10) {
                    items.add(null)
                    dynamicRvAdapter!!.notifyItemInserted(items.size - 1)
                    Handler().postDelayed({
                        items.removeAt(items.size - 1)
                        dynamicRvAdapter!!.notifyItemRemoved(items.size)
                        val index = items.size
                        val end = index + 10

//                            for (int i = index; i < 2; i++) {
//                                String type = UUID.randomUUID().toString();
//                                DynamicRvModel item = new DynamicRvModel("Hello World");
//                                items.add(item);
//                            }
                        dynamicRvAdapter!!.notifyDataSetChanged()
                        dynamicRvAdapter!!.setLoaded()
                    }, 4000)
                } else {
                    Toast.makeText(this@History, "Data Completed", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    fun validateApi() {

        val url = "https://wankerapi.azurewebsites.net/api/transaction"

        RequestApi.GetRequestApi(url, RequestApi.tokenApi, "balance")

    }

}