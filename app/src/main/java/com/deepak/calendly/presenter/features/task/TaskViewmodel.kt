package com.deepak.calendly.presenter.features.task

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.calendly.common.dispatchers.CoroutineDispatcherProvider
import com.deepak.calendly.data.remote.model.Task
import com.deepak.calendly.data.remote.model.TaskRequest.TaskModel
import com.deepak.calendly.data.remote.model.TaskResponse
import com.deepak.calendly.data.remote.utils.Result
import com.deepak.calendly.data.repository.CalendlyTaskRepository
import com.deepak.calendly.domain.usecase.DeleteTask
import com.deepak.calendly.domain.usecase.GetCalenderTaskList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskViewmodel @Inject constructor(
    private val deleteTask: DeleteTask,
    private val getCalenderTaskList: GetCalenderTaskList,
    private val dispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {


    private val _tasks = mutableStateOf<List<Task>?>(listOf())
    val tasks: State<List<Task>?> get() = _tasks

    private val _taskDelete = mutableStateOf<Boolean?>(false)
    val taskDelete: State<Boolean?> get() = _taskDelete

    var selectedDate by mutableStateOf<Date?>(null)
        private set

    fun updateSelectedDate(date: Date) {
        selectedDate = date
    }

    fun getCalenderTaskLists(taskDate: String, userId: Int){
        viewModelScope.launch(dispatcherProvider.io) {
            when(val result = getCalenderTaskList.invoke(taskDate,userId)){
                is Result.Error -> {}
                is Result.Success -> {
                    withContext(dispatcherProvider.main){
                        _tasks.value = result.data
                    }
                }
            }
        }
    }

    fun deleteTask(date: String, userId: Int, taskId: Int){
        viewModelScope.launch(dispatcherProvider.io) {
            when(deleteTask.invoke(userId, taskId)){
                is Result.Error -> {
                    _taskDelete.value = false
                }
                is Result.Success -> {
                    withContext(dispatcherProvider.main){
                        _taskDelete.value = true
                        getCalenderTaskLists(date.toString(), userId)
                    }
                }
            }
        }
    }
}