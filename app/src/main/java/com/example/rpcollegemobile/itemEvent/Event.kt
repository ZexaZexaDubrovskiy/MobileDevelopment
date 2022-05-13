package com.example.rpcollegemobile.itemEvent

import java.io.Serializable
import java.util.*

data class Event(
    val id: Int,
    val activityType: String,
    val name: String,
    val subdivision: String,
    val local: Int,
    val contWorId: Int,
    val contWorName: String,
    val planDate: String,
    val place: String,
    val execDate: String,
    val responsibleWorkers: String,
    val signed: Int,
    val canceled: Int,
    val planStartTime: String,
    val planEndTime: String,
    val planDateTimeStr: String
    ) : Serializable
