package eu.caraus.appsflastfm.data.local.lastFm.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Artist
import io.reactivex.Flowable

@Dao
interface ArtistsDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    fun insert( artist : Artist )

    @Query("SELECT * FROM artists_table")
    fun getAll() : Flowable<List<Artist>>

}