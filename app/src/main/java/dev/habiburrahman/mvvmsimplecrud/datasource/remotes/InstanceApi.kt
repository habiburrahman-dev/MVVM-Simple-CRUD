package dev.habiburrahman.mvvmsimplecrud.datasource.remotes

import dev.habiburrahman.mvvmsimplecrud.BuildConfig
import dev.habiburrahman.mvvmsimplecrud.datasource.local.AppSharedPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class InstanceApi {

    companion object {

        private val provideHttpLoggingInterceptor by lazy {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val provideCacheInterceptor by lazy {
            run {
                Interceptor { chain ->
                    val response = chain.proceed(chain.request())
                    val maxAge = 60

                    response.newBuilder().header("Cache-Control", "public, max-age=$maxAge")
                        .removeHeader("Pragma").build()
                }
            }
        }

        private val client by lazy {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(provideHttpLoggingInterceptor)
                .addInterceptor(provideCacheInterceptor)
                .callTimeout(6, TimeUnit.MINUTES)
                .writeTimeout(6, TimeUnit.MINUTES)
                .pingInterval(30, TimeUnit.SECONDS)
                .readTimeout(6, TimeUnit.MINUTES)
                .connectTimeout(6, TimeUnit.MINUTES)
                .build()
        }

        private val stockRetrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.SOURCE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val restaurantAPI: SourceApi by lazy {
            stockRetrofit.create(SourceApi::class.java)
        }

    }

}