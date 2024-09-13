package com.deepak.calendly.domain.usecase

import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.utils.Result
import com.deepak.calendly.data.repository.CalendlyTaskRepository
import javax.inject.Inject

class DeleteTask @Inject constructor(
    private val repository: CalendlyTaskRepository
) {
    suspend operator fun invoke(userId: Int, taskId: Int): Result<Boolean> {
        return repository.deleteCalenderTask(userId, taskId)
    }
}