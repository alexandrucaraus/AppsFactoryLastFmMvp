package eu.caraus.appsflastfm.data.remote.lastFm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  Retrofit client class for LastFmApi service
 */

class LastFmApiClient {

    companion object{
        const val BASE_URL = "http://ws.audioscrobbler.com/"
    }

    val client : Retrofit

    init {

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
             //level = HttpLoggingInterceptor.Level.BODY
        }

        val apiKeyInterceptor = LastFmApiKeyInterceptor()

        val client = OkHttpClient.Builder()
                                    .addInterceptor( httpLoggingInterceptor )
                                    .addInterceptor( apiKeyInterceptor )
                                    .build()

        this.client = Retrofit.Builder().baseUrl( BASE_URL )
                              .addConverterFactory( GsonConverterFactory.create() )
                              .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                              .client( client )
                              .build()
    }

}
