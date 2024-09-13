package com.deepak.calendly.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskResponse(
    val tasks: List<Task>
): Parcelable

@Parcelize
data class TaskDetail(
    val description: String,
    val taskId: Int,
    val title: String,
    val date: String
): Parcelable

@Parcelize
data class Task(
    @SerializedName("task_detail")
    val taskDetail: TaskDetail,
    @SerializedName("task_id")
    val taskId: Int
): Parcelable