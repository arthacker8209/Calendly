package com.deepak.calendly.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class CoroutineDispatcherProvider(
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher
) {
    @Inject
    constructor(): this(Dispatchers.IO, Dispatchers.Main, Dispatchers.Default)

}