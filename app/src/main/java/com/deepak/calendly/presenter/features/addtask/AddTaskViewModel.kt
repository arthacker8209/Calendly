package com.deepak.calendly.presenter.features.addtask

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.calendly.common.dispatchers.CoroutineDispatcherProvider
import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.data.remote.utils.Result
import com.deepak.calendly.data.repository.CalendlyTaskRepository
import com.deepak.calendly.domain.usecase.StoreCalenderTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val storeCalenderTask: StoreCalenderTask
): ViewModel() {
    private val _taskSaved = mutableStateOf<Boolean?>(false)
    val taskSaved: State<Boolean?> get() = _taskSaved

    fun storeCalenderTask(userId: Int, task: TaskRequest.TaskModel){
        viewModelScope.launch(dispatcherProvider.io) {
            when(storeCalenderTask.invoke(userId, task)){
                is Result.Error -> {
                   _taskSaved.value = false
                }
                is Result.Success -> {
                   _taskSaved.value = true
                }
            }
        }
    }
}