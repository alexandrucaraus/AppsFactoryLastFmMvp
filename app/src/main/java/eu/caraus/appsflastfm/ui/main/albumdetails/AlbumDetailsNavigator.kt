package eu.caraus.appsflastfm.ui.main.albumdetails

import eu.caraus.appsflastfm.ui.main.MainActivityScreenFlow

class AlbumDetailsNavigator( val navigation : MainActivityScreenFlow)
        : AlbumDetailsContract.Navigator {

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}