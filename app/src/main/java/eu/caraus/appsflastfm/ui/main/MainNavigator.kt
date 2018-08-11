package eu.caraus.appsflastfm.ui.main


/**
 *  MainNavigator - a class to hold all possible flow directions of the MainActivity,
 *  @param - navigation [MainActivityScreenLoader]
 */

class MainNavigator( private val navigation : MainActivityScreenLoader) : MainContract.Navigator {

    override fun showSavedAlbumsScreen() {
        navigation.navigateToSavedAlbums()
    }

    /**
     *  this method navigates to search result
     */
    override fun showSearchResultScreen( searchTerm : String) {
        navigation.navigateToSearchResult( searchTerm )
    }


    /**
     *  this method is used for back navigation in MainActivity
     */
    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}