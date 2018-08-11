package eu.caraus.appsflastfm.ui.search.albumdetails

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenLoader

class AlbumDetailsNavigator( val navigation : SearchActivityScreenLoader)
        : AlbumDetailsContract.Navigator {

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}