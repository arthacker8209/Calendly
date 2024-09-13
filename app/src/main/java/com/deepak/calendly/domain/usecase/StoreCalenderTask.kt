package com.deepak.calendly.domain.usecase

import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.utils.Result
import com.deepak.calendly.data.repository.CalendlyTaskRepository
import javax.inject.Inject

class StoreCalenderTask @Inject constructor(
    private val repository: CalendlyTaskRepository
) {
    suspend operator fun invoke(userId: Int, task: TaskRequest.TaskModel): Result<Boolean> {
        return repository.storeCalenderTask(userId, task)
    }
}