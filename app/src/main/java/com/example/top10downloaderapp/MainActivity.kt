package com.example.top10downloaderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    lateinit var getButton: Button
    lateinit var adapter: RVTopApp
    lateinit var rvMain: RecyclerView

    var topApps = mutableListOf<TopApp>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getButton = findViewById(R.id.btnGet)
        rvMain = findViewById(R.id.rvMain)

        getButton.setOnClickListener { getFeeds() }

    }

    private fun getFeeds(){
        CoroutineScope(Dispatchers.IO).launch{
            async {
                fetchData()
            }.await()
            withContext(Dispatchers.Main){
                adapter = RVTopApp(topApps)
                rvMain.adapter = adapter
                rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    private fun fetchData(){
        val parser = XMLParser()
        val url = URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        val urlConnection = url.openConnection() as HttpURLConnection
        topApps = urlConnection.inputStream?.let {
            parser.parse(it)
        } as MutableList<TopApp>

    }
}