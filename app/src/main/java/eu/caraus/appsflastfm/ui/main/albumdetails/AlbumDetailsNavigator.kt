package eu.caraus.appsflastfm.ui.main.albumdetails

import eu.caraus.appsflastfm.ui.main.MainActivityScreenLoader

class AlbumDetailsNavigator( val navigation : MainActivityScreenLoader)
        : AlbumDetailsContract.Navigator {

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}