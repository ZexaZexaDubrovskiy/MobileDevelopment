package com.example.rpcollegemobile

import android.util.Log
import com.example.rpcollegemobile.itemEvent.Event
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.*


class Repository {
    fun loadEventSync(callback: (List<Event>) -> Unit) {
        Thread {
            try {
                val call = Network.getEventCall()
                val response = call.execute()
                if (response.isSuccessful) {
                    val responseBodyString = response.body?.string() as String
                    val event: List<Event> = parseResponse(responseBodyString)
                    callback(event)
                } else {
                    Log.e("Event Server", "Server error response")
                    callback(listOf())
                }
            } catch (e: IOException) {
                Log.e("Event Server", "${e.message}", e)
                callback(listOf())
            }
        }.start()
    }

    private lateinit var successfulCallback: (List<Event>) -> Unit
    private lateinit var failureCallback: (e: Throwable) -> Unit

    fun loadEventAsync(
        successfulCallback: (List<Event>) -> Unit,
        failureCallback: (e: Throwable) -> Unit
    ): Call {
        this.successfulCallback = successfulCallback
        this.failureCallback = failureCallback

        val call = Network.getEventCall()
        call.enqueue(asyncCallback)
        return call
    }

    private val asyncCallback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("Event Server", "${e.message}", e)
            failureCallback(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBodyString = response.body?.string() as String
                val event: List<Event> = parseResponse(responseBodyString)
                successfulCallback(event)
            } else {
                Log.e("Event Server", "Server error responses")
                failureCallback(Throwable("Server error responses"))
            }
        }

    }

    private fun parseResponse(response: String): List<Event> {
        try {
            val eventArray = JSONArray(response)

            return (0 until eventArray.length()).map { index ->
                eventArray.getJSONObject(index)
            }.map { eventObject ->
                val id = eventObject.getInt("eventId")
                val activityType = eventObject.getString("ACTIVITY_TYPE")
                val name = eventObject.getString("NAME")
                val subdivision = eventObject.getString("SUB_DIVISION")
                val local = eventObject.getInt("LOCAL")
                val contWorId = eventObject.getInt("cont_wor_id")
                val contWorName = eventObject.getString("cont_wor_name")
                val planDate = eventObject.getString("PLAN_DATE")

                val place = eventObject.getString("PLACE")


                val execDate = eventObject.getString("EXEC_DATE")


                val responsibleWorkers = eventObject.getString("RESPONSIBLE_WORKERS")
                val signed = eventObject.getInt("SIGNED")
                val canceled = eventObject.getInt("CANCELED")
                val planStartTime = eventObject.getString("PLAN_START_TIME")
                val planEndTime = eventObject.getString("PLAN_END_TIME")
                val planDateTimeStr = eventObject.getString("PLAN_DATE_TIME_STR")

                Log.e(id.toString(), activityType)
                Log.e(subdivision.toString(), subdivision)
                Log.e(contWorName.toString(), contWorName)
                Log.e(planDate.toString(), planDate)

                Event(
                    id, activityType, name, subdivision, local, contWorId, contWorName,
                    planDate, place, execDate, responsibleWorkers, signed, canceled,
                    planStartTime, planEndTime, planDateTimeStr
                )
            }

        } catch (e: JSONException) {
            return emptyList()
        }
    }


}