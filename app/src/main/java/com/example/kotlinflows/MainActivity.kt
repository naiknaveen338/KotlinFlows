package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val job1 = GlobalScope.launch {
            val data: Flow<Int> = producer()
            data.collect {
                Log.e("naveen", "First consumer $it")
            }
        }

        //AFter 3500 , Flow will be cancelled , as coroutine is cancelled
        /*  GlobalScope.launch {
            delay(3500)
            job1.cancel()
        }*/

        //Multiple consumer case
        GlobalScope.launch {
            val data: Flow<Int> = producer()
            //Since it is cold stream , even if we start collecting
            //data after delay , still we get all the datas which are emittted
            delay(3000)
            data.collect {
                Log.e("naveen", "Second COnsumer $it")
            }
        }
    }
    fun producer() = flow<Int> {
        val list = listOf(1,2,3,4,5,6,7,8,9)
        list.forEach {
            delay(1000)
            emit(it)
        }
    }
}