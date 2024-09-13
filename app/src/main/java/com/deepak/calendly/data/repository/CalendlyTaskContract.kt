package com.deepak.calendly.data.repository

import com.deepak.calendly.data.remote.model.Task
import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.model.TaskResponse
import com.deepak.calendly.data.remote.utils.Result

class CalendlyTaskContract {
    interface Repository {
        suspend fun storeCalenderTask(userId: Int, task: TaskRequest.TaskModel): Result<Boolean>
        suspend fun getCalenderTaskLists(taskDate: String, userId: Int): Result<List<Task>>
        suspend fun deleteCalenderTask(userId: Int, taskId: Int): Result<Any>
    }
}