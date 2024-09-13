package com.deepak.calendly.data.remote.client

import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.model.TaskResponse
import com.deepak.calendly.data.remote.response.TaskModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendlyClient {

    @POST("/api/storeCalendarTask")
    suspend fun storeCalendarTask(@Body request: TaskRequest.StoreTaskRequest): Response<Any>

    @POST("/api/deleteCalendarTask")
    suspend fun deleteCalendarTask(@Body request: TaskRequest.DeleteTaskRequest): Response<Any>

    @POST("/api/getCalendarTaskList")
    suspend fun getCalendarTasks(@Body request: TaskRequest.GetTasksRequest): Response<TaskResponse>
}