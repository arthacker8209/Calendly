package com.deepak.calendly.data.repository

import com.deepak.calendly.data.remote.client.CalendlyClient
import com.deepak.calendly.data.remote.model.Task
import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.model.TaskRequest.TaskModel
import com.deepak.calendly.data.remote.model.TaskResponse
import com.deepak.calendly.data.remote.utils.ErrorType
import com.deepak.calendly.data.remote.utils.getResult
import com.deepak.calendly.data.remote.utils.Result
import javax.inject.Inject

class CalendlyTaskRepository @Inject constructor(
    private val client: CalendlyClient
) : CalendlyTaskContract.Repository {

    override suspend fun storeCalenderTask(userId: Int, task: TaskModel): Result<Boolean> {
        val result = getResult { client.storeCalendarTask(TaskRequest.StoreTaskRequest(userId, task)) }
        if (result is Result.Success) {
            return Result.Success(true)
        } else if (result is Result.Error && result.type == ErrorType.NetworkException) {
            return Result.Error("Something went wrong", ErrorType.NetworkException)
        }
        return Result.Error("Something went wrong", ErrorType.InvalidData)
    }

    override suspend fun getCalenderTaskLists(taskDate: String, userId: Int): Result<List<Task>> {
        val result = getResult { client.getCalendarTasks(TaskRequest.GetTasksRequest(userId)) }
        if (result is Result.Success) {
            val filteredList = result.data.tasks.filter {
                it.taskDetail.date == taskDate
            }
            return Result.Success(filteredList)
        } else if (result is Result.Error && result.type == ErrorType.NetworkException) {
            return Result.Error("Something went wrong", ErrorType.NetworkException)
        }
        return Result.Error("Something went wrong", ErrorType.InvalidData)
    }

    override suspend fun deleteCalenderTask(userId: Int, taskId: Int): Result<Boolean> {

        val result = getResult { client.deleteCalendarTask(TaskRequest.DeleteTaskRequest(userId, taskId)) }
        if (result is Result.Success) {
            return Result.Success(true)
        } else if (result is Result.Error && result.type == ErrorType.NetworkException) {
            return Result.Error("Something went wrong", ErrorType.NetworkException)
        }
        return Result.Error("Something went wrong", ErrorType.InvalidData)
    }
}