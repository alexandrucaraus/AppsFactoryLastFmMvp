package eu.caraus.appsflastfm.data.local.lastFm.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import io.reactivex.Flowable

@Dao
interface TracksDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    fun insert( track : TrackItem )

    @Query("SELECT * FROM tracks_table")
    fun getAll() : Flowable<List<TrackItem>>

}