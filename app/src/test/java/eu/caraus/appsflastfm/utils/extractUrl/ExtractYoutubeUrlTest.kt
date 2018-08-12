package eu.caraus.appsflastfm.utils.extractUrl

import eu.caraus.appsflastfm.services.youtube.model.lastFm.ExtractYoutubeUrlFromLastFm
import junit.framework.Assert.assertEquals

import org.junit.Test


class ExtractYoutubeUrlTest {

    @Test
    fun getYoutubeUrl(  ){

        val lastFmUrl = "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"

        val youtubeUrl = ExtractYoutubeUrlFromLastFm().extract( lastFmUrl )!!

        assertEquals( "https://www.youtube.com/watch?v=PiZHNw1MtzI", youtubeUrl )

    }

    @Test
    fun getYoutubeObject() {

        val lastFmUrl = "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"

        val element = ExtractYoutubeUrlFromLastFm().extract( lastFmUrl )

        println( element.toString() )

    }

}