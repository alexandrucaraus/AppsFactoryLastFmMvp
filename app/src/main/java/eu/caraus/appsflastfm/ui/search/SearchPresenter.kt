package eu.caraus.appsflastfm.ui.search

class SearchPresenter( private val navigator : SearchContract.Navigator ) : SearchContract.Presenter {

    override fun searchArtist( artist: String ) {
        navigator.showArtists( artist )
    }

    override fun goBack(): Boolean {
        return navigator.goBack()
    }

    override fun onViewAttached(view: SearchContract.View) {

    }

    override fun onViewDetached(detach: Boolean) {

    }

}