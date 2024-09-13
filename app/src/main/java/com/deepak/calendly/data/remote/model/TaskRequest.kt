package com.deepak.calendly.data.remote.model

import com.google.gson.annotations.SerializedName

class TaskRequest {
    data class TaskModel(
        val title: String,
        val description: String,
        val date: String
    )

    data class StoreTaskRequest(
        @SerializedName("user_id")
        val userId: Int,
        val task: TaskModel
    )

    data class DeleteTaskRequest(
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("task_id")
        val taskId: Int
    )

    data class GetTasksRequest(
        @SerializedName("user_id")
        val userId: Int
    )
}