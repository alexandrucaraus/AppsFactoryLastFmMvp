package eu.caraus.appsflastfm.services.youtube.model.lastFm

import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import org.jsoup.Jsoup

class ExtractYoutubeUrlFromLastFm {

    @Throws(Throwable::class)
    fun extract( lastFmUrl : String ) : YouTubeVideo? {

        val video = YouTubeVideo()

            val document = Jsoup.connect(lastFmUrl).get()
            val elements = document.getElementsByClass("video-preview")



            elements.forEach {

                val id = it.getElementsByAttribute("data-youtube-id").attr("data-youtube-id")
                val name = it.getElementsByAttribute("data-youtube-id").attr("data-track-name")


                if (id.isNotEmpty() && id != null) {
                    video.id = id
                    video.title = name
                }
            }


        return video
    }

}