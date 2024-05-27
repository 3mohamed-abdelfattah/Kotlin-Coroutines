package com.example.coroutines

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
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


        // This Work on main thread
        GlobalScope.launch(Dispatchers.IO) {
            fakeAPI()
            Log.d("TAG", "GlobalScope Thread: ${Thread.currentThread().name} ")

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

    // Switching Between Threads to Coroutine
    suspend fun fakeAPI() {
        delay(5500)
        Log.d("TAG", "Before Thread : ${Thread.currentThread().name} ")
        withContext(Dispatchers.Main) {
            binding.textView.text = "Fake Request"
            Log.d("TAG", "After Thread: ${Thread.currentThread().name} ")
        }
    }


}