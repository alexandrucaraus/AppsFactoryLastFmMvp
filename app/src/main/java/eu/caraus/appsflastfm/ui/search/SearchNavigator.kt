package eu.caraus.appsflastfm.ui.search

class SearchNavigator( val navigation: SearchActivityScreenFlow ) : SearchContract.Navigator {

    override fun showArtists( string : String ) {
        navigation.navigateToArtists( string )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}