package com.deepak.calendly.data.remote.utils

import com.deepak.calendly.data.remote.interceptors.NetworkStateChecker
import com.deepak.calendly.data.remote.interceptors.NetworkStateCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {

    @Binds
    fun bindNetworkStateChecker(networkStateCheckerImpl: NetworkStateCheckerImpl): NetworkStateChecker
}