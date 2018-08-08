package eu.caraus.appsflastfm.data.local.lastFm.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.ImageItem

class ImageListTypeConverter {

    @TypeConverter
    fun toFlags(  imageItemListAsString : String? ) : List<ImageItem?> {

        if( imageItemListAsString == null){
            emptyList<List<ImageItem?>>()
        }

        return Gson().fromJson<List<ImageItem>>( imageItemListAsString!! )
    }

    @TypeConverter
    fun fromFlags( flags : List<ImageItem> ) : String? {

        return Gson().toJson( flags )
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

}