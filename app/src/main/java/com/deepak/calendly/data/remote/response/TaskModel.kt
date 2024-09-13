package com.deepak.calendly.data.remote.response

import java.util.Date

data class TaskModel(
    val taskId: Int,
    val taskTitle: String,
    val taskDescription: String,
    val createdAt: Date,
    val updatedAt: Date
)
