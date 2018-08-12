package eu.caraus.appsflastfm.ui.main.albumdetails

import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.main.MainActivityScreenLoader

class AlbumDetailsNavigator( val navigation : MainActivityScreenLoader)
        : AlbumDetailsContract.Navigator {

    override fun playSong( youtubeUrl: YouTubeVideo) {
        navigation.sendMusicIntent( youtubeUrl )
    }
    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}