package com.deepak.calendly.data.di

import android.annotation.SuppressLint
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.deepak.calendly.BuildConfig
import com.deepak.calendly.data.remote.client.CalendlyClient
import com.deepak.calendly.data.remote.interceptors.NetworkReachabilityInterceptor
import com.deepak.calendly.data.remote.interceptors.NetworkStateChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttp3Client(networkStateChecker: NetworkStateChecker, @ApplicationContext context: Context): OkHttpClient{
        val timeOutInSeconds = 120L
        val builder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(timeOutInSeconds, TimeUnit.SECONDS)
            .readTimeout(timeOutInSeconds, TimeUnit.SECONDS)
            .addInterceptor(NetworkReachabilityInterceptor(networkStateChecker))


        if (BuildConfig.DEBUG){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            builder.addInterceptor(ChuckerInterceptor(context))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCalendlyApi(retrofit: Retrofit) = retrofit.create(CalendlyClient::class.java)
}