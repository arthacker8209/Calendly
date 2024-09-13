package com.deepak.calendly.domain.di

import com.deepak.calendly.data.repository.CalendlyTaskRepository
import com.deepak.calendly.domain.usecase.DeleteTask
import com.deepak.calendly.domain.usecase.GetCalenderTaskList
import com.deepak.calendly.domain.usecase.StoreCalenderTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetCalenderTaskList(repository: CalendlyTaskRepository): StoreCalenderTask {
        return StoreCalenderTask(repository)
    }

    @Provides
    @Singleton
    fun provideStoreCalenderTask(repository: CalendlyTaskRepository): GetCalenderTaskList {
        return GetCalenderTaskList(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTask(repository: CalendlyTaskRepository): DeleteTask {
        return DeleteTask(repository)
    }
}