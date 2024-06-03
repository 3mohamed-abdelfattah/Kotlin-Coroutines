package com.example.coroutines

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class FlowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        playFlow()
//        playFlowMap()
        playFlowError()
    }


    // Use Filter And Map in flow
    private fun playFlowMap() {
        val flow = flow<Int> {
            for (i in 1..100) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Default)

        lifecycleScope.launch {
            flow.map { it }.filter { it % 2 == 1 }.collect {
                Log.d(TAG, "playFlowMap: ${it.toString()}")
            }
        }
    }

    //Handling errors in kotlin flow
    private fun playFlowError() {
        val flow = flow<Int> {
            for (i in 1..8) {
                if (i == 5) {
                    throw Exception("playFlowError")
                }
                emit(i)
                delay(1000)
            }
        }

        lifecycleScope.launch {
            flow.onCompletion {
                Log.d(TAG, "Complete")
            }.catch {
                Log.e(TAG, it.message.toString())
            }.collect {
                Log.d(
                    TAG,
                    "playFlowError:$it "
                )
            }
        }
    }


    //Normal flow example
    private fun playFlow() {
        val flow = flow {
            for (i in 1..20) {
                emit("Hello")
                emit("World!!  $i")
                delay(1000)
            }
        }

        lifecycleScope.launch {
            flow.collect {
                Log.d(TAG, "playFlow: $it ")
            }
        }
    }


}