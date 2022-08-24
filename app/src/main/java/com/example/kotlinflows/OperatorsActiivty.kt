package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class OperatorsActiivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators_actiivty)

        val job1 = GlobalScope.launch(Dispatchers.Main) {

            //Non terminal operators
            /*  val data = producer().first()
              Log.e("naveen", "$data")

              val data1 = producer().toList()
              Log.e("naveen", "$data1")*/

            //Terminal operartors
            /*  val data: Flow<Int> = producer()
                  .map {
                      it * 2
                  }
                  .filter {
                      it < 8
                  }
              data.collect {
                  Log.e("naveen", "Consumer $it")
              }*/


            //Real time example of operators
            /*  getNotes()
                  .map {
                      //We need Title with Capital case and of type FOrmatted data
                      //so we use map to convert Note to FormattedNote
                      FormattedNote(it.isActive,it.title.uppercase(),it.description)
                  }
                  .filter {
                      //We require only data with isActive true
                      it.isActive
                  }
                  .collect {
                      Log.e("naveen","Collected val : $it")
                  }
  */

            //Buffer , Produceer fast consumer slow
/*
            val time = measureTimeMillis {
                val data: Flow<Int> = producer()
                    //without buffer it takes 12 sec,
                    //with buffer takes 8 sec
                    .buffer(3)
                data.collect {

                    //Add deley more thean producer
                    delay(1500)
                    Log.e("naveen", "Consumer $it")
                }

            }
            Log.e("naveen", "time $time")
*/

            //Run produce on Io , consumer on Main using flowOn
            val time = measureTimeMillis {
                val data: Flow<Int> =
                    producer()
                        .map {
                            //This runs on IO
                            it * 2
                        }
                        .flowOn(Dispatchers.IO)
                        .filter {
                            //This runs on main
                            it < 8
                        }
                        .flowOn(Dispatchers.Main)
                data.collect {

                    //Add deley more thean producer
                    delay(1500)
                    Log.e("naveen", "Consumer $it Thread : ${Thread.currentThread().name}")
                }
            }


            //First commented
            /*   val data: Flow<Int> = producer()
                   .onStart {
                       //We can do manual  emit our value also
                       emit(-1)
                       Log.e("naveen", "Starting out")
                   }
                   .onCompletion {
                       Log.e("naveen", "Completed")
                   }
                   .onEach {
                       Log.e("naveen", "About to emit $it")
                   }
               data.collect {
                   Log.e("naveen", "Consumer $it")
               }*/

        }
    }

    fun producer() = flow<Int> {
        Log.e("naveen", "Producer Thread is ${Thread.currentThread().name}")
        val list = listOf(1, 2, 3, 4, 5)
        list.forEach {
            delay(1000)
            emit(it)
        }

    }
        //this isException handling for produceer only,
        //we can use try catch for produceer and consumer  above
        //for handling both
        .catch {

        }

    private fun getNotes(): Flow<Note> {
        val list = listOf(
            Note(1, true, "First", "First description"),
            Note(2, false, "Second", "Secomd description"),
            Note(3, true, "Third", "Third description"),
            Note(4, false, "Fourth", "Fourth description")
        )
        return list.asFlow()
    }

    data class Note(val id: Int, val isActive: Boolean, val title: String, val description: String)
    data class FormattedNote(val isActive: Boolean, val title: String, val description: String)
}