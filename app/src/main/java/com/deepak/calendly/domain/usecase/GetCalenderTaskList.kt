package com.deepak.calendly.domain.usecase

import com.deepak.calendly.data.remote.model.Task
import com.deepak.calendly.data.remote.model.TaskResponse
import com.deepak.calendly.data.remote.utils.Result
import com.deepak.calendly.data.repository.CalendlyTaskRepository
import javax.inject.Inject

class GetCalenderTaskList @Inject constructor(
    private val repository: CalendlyTaskRepository
) {
    suspend operator fun invoke(taskDate: String, userId: Int): Result<List<Task>> {
        return repository.getCalenderTaskLists(taskDate, userId)
    }
}