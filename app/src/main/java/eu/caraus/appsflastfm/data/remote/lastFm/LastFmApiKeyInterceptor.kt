package eu.caraus.appsflastfm.data.remote.lastFm

import okhttp3.Interceptor
import okhttp3.Response

class LastFmApiKeyInterceptor( private val apiKey : String) : Interceptor {

    companion object {
        const val LAST_FM_API_KEY = "ea925ddd5cc742fea4488fc786d21731"
    }

    override fun intercept( chain: Interceptor.Chain?) : Response {

        val httpUrl = chain?.request()?.url()?.newBuilder()?.
                            addQueryParameter("api_key", apiKey )?.
                            addQueryParameter("format","json")?.
                            build()

        val request = chain?.request()?.newBuilder()?.url( httpUrl!!)?.build()

        return chain?.proceed( request!!)!!
    }

}