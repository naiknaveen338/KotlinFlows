package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class SharedFlow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_flow2)

        //Shared flow
      /*  GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            result.collect {
                Log.e("naveen","Comsumer 1 : $it")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            delay(2500)
            result.collect {
                Log.e("naveen","Comsumer 2 : $it")
            }
        }*/

        //State flow

        GlobalScope.launch(Dispatchers.Main) {
            val result = stateFlowPRoducer()
            delay(6000)
            result.collect {
                Log.e("naveen","Comsumer 2 : $it")
            }
        }

    }

    //Here returned value is Flow , which is immutable , SharedFlow
    private fun producer(): MutableSharedFlow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>(replay = 1)
        GlobalScope.launch {
            val list = listOf<Int>(1,2,3,4,5)
            list.forEach {
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    //State Flow
    private fun stateFlowPRoducer() : kotlinx.coroutines.flow.Flow<Int> {
        val mutableStateFlow = MutableStateFlow(10)
        GlobalScope.launch {
            delay(2000)
            mutableStateFlow.emit(20)
            delay(2000)
            mutableStateFlow.emit(30)
        }
        return mutableStateFlow
    }
}