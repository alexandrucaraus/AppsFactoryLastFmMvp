package eu.caraus.appsflastfm.data.remote


import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApiClient
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApiKeyInterceptor
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test

class LastFmApiTest {

    private val lastFmApi : LastFmApi? =  LastFmApiClient(LastFmApiKeyInterceptor.LAST_FM_API_KEY).client.create( LastFmApi::class.java )

    @Test
    fun searchArtists() {

        lastFmApi?.searchArtists("Metallica")
                 ?.subscribe({
                    assertThat( it.results?.artistmatches?.artist?.size, `is`( not( 0)))
                 },{
                     assertEquals("isSuccessful","isFailed ${it.message}")
                 })

    }

    @Test
    fun getArtistsTopAlbums(){

        lastFmApi?.getArtistTopAlbums("Metallica")
                ?.subscribe({
                    assertThat( it.topalbums?.album?.size, `is`( not( 0)))
                },{
                    assertEquals("isSuccessful","isFailed ${it.message}")
                })

    }

    @Test
    fun getArtistAlbumInfo(){

        lastFmApi?.getArtistAlbumInfo("Metallica", "Metallica")
                ?.subscribe({
                    assertThat( it.album?.tracks?.track?.size, `is`( not( 0)))
                },{
                    assertEquals("isSuccessful","isFailed ${it.message}")
                })

    }

}