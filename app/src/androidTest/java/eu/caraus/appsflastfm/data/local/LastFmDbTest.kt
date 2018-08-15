package eu.caraus.appsflastfm.data.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApiClient
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat

import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LastFmDbTest {

    private val lastFmApi : LastFmApi? =  LastFmApiClient().client.create( LastFmApi::class.java )

    private lateinit var database : Database

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder ( InstrumentationRegistry.getTargetContext(),
                                                  Database::class.java
                                              //, Database.DATABASE_NAME
                                                  )
                                                  .fallbackToDestructiveMigration()
                                                  .build()

    }

    @Test
    fun testInsertAlbums() {

        lastFmApi?.getArtistAlbumInfo("Metallica", "Metallica")
                ?.subscribe({

                    assertEquals("isSuccessful","isSuccessful")

                    database.albumsDao().insert( it.album!! )

                    database.albumsDao().selectAll().let{ list ->
                        assertThat( list.size , `is`(1) )
                    }

                },{
                    assertEquals("isSuccessful", "isFailed ${it.message}")
                })

    }

    @Test
    fun testSelectAlbums(){

        lastFmApi?.getArtistAlbumInfo("Metallica", "Metallica")
                ?.subscribe({

                    assertEquals("isSuccessful","isSuccessful")

                    database.albumsDao().insertWhole( it.album!! )

                    database.albumsDao().selectAll().let { list ->
                        assertThat( list.size , `is`(1) )
                    }

                    database.tracksDao().getAll().subscribe { list->
                        assertThat( list.size, `is`( not( 0)))
                    }

                    database.artistsDao().getAll().subscribe{ list->
                        assertThat( list.size, `is`( not( 0)))
                    }

                },{
                    assertEquals("isSuccessful", "isFailed ${it.message}")
                })

    }

}