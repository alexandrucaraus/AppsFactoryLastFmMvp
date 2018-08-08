package eu.caraus.appsflastfm.ui.search.albumdetails

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenFlow

class AlbumDetailsNavigator( val navigation : SearchActivityScreenFlow)
        : AlbumDetailsContract.Navigator {

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}