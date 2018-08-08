package eu.caraus.appsflastfm.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Artist
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.data.local.lastFm.converters.ImageListTypeConverter
import eu.caraus.appsflastfm.data.local.lastFm.dao.AlbumsDao
import eu.caraus.appsflastfm.data.local.lastFm.dao.ArtistsDao
import eu.caraus.appsflastfm.data.local.lastFm.dao.TracksDao

@Database(

        entities = [
            Album::class,
            TrackItem::class,
            Artist::class
        ],

        version = 3,

        exportSchema = false

)
@TypeConverters(
        ( ImageListTypeConverter::class )
)
abstract class Database : RoomDatabase() {

    companion object {

        const val DATABASE_NAME = "last_fm_db"

    }

    abstract fun albumsDao()  : AlbumsDao

    abstract fun tracksDao()  : TracksDao

    abstract fun artistsDao() : ArtistsDao

}