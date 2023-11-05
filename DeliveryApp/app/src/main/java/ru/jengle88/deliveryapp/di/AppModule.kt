package ru.jengle88.deliveryapp.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.jengle88.deliveryapp.data.remote.FoodApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFoodApi(retrofitBuilder: Retrofit.Builder): FoodApi {
        return retrofitBuilder
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .build()
            .create(FoodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}