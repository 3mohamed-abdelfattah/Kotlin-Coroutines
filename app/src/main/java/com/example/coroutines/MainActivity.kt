package com.example.coroutines

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //    val customScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var firstRes: String? = null
        var secondRes: String? = null


        // This Work on main thread
//        GlobalScope.launch(Dispatchers.IO) {
////            fakeAPI()
//            repeatLogs()
//            Log.d("TAG", "GlobalScope Thread: ${Thread.currentThread().name} ")
//
//        }

//        //lifecycleScope
//        lifecycleScope.launch(Dispatchers.IO) {
//            repeatLogs()
//        }


        //CoroutineScope
        // Multiple Jobs
        val jobParent = lifecycleScope.launch {
            val job1 = launch { firstRes = repeatLogs() }
            val job2 = launch { secondRes = repeatLogs2() }

            //Coroutines join jobs
            job1.join()
            job2.join()
            Log.d(TAG, firstRes.toString())
            Log.d(TAG, secondRes.toString())
        }


        // TO cancel request after clicking button
        binding.btnCancel.setOnClickListener {
            jobParent.cancel()
        }

//        // This Work on any  CoroutineDefaultDispatcher thread
//        GlobalScope.launch{
//            fakeAPI()
//            Log.d("TAG", "After Coroutine ${Thread.currentThread().name}")
//        }


//        // For example only to call suspend function
//        runBlocking {
//            fakeAPI()
//        }

    }


    suspend fun repeatLogs(): String {


        while (true) {
            delay(5000)
            Log.d("TAG", "Hi, Still Running response 1!!")
            return "Hi,Response 1"
        }


//        delay(2500)
//        withContext(Dispatchers.Main) {
//            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
//            finish()
//        }

    }


    suspend fun repeatLogs2(): String {
        delay(3000)
        Log.d("TAG", "Hi, Still Running response 2 !!")
        return "Hi,Response 2"
    }


    // Switching Between Threads to Coroutine
    suspend fun fakeAPI() {
        delay(5500)
        Log.d("TAG", "Before Thread : ${Thread.currentThread().name} ")
        withContext(Dispatchers.Main) {
            binding.textView.text = "Fake Request"
            Log.d("TAG", "After Thread: ${Thread.currentThread().name} ")
        }
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        customScope.cancel()
//    }

}