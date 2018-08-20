package eu.caraus.appsflastfm.ui.search.artists

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Artist
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import io.reactivex.disposables.Disposable

class ArtistsPresenter( val interactor : ArtistsContract.Interactor ,
                        val navigator  : ArtistsContract.Navigator  ,
                        val scheduler  : SchedulerProvider) : ArtistsContract.Presenter {

    private var view : ArtistsContract.View? = null

    private var disposable : Disposable? = null

    private var data : List<ArtistItem?>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        disposable = interactor.getArtistsOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading )
                         if( data == null) showLoading()
                    else hideLoading()

                is Outcome.Failure  ->
                    showError( it.error )

                is Outcome.Success  -> {
                    data = it.data
                    showFoundArtists(it.data)
                }

            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        data?.let {
            showFoundArtists(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable?.dispose()
    }

    override fun searchArtist( artistName : String ) {
        interactor.getArtists( artistName )
    }

    override fun showTopAlbums( artistId: String ) {
        navigator.showTopAlbums( artistId )
    }

    private fun showFoundArtists( artists : List<ArtistItem?> ){
        if( artists.isNotEmpty() ) {
            view?.hidePlaceholder()
            view?.showFoundArtists(artists)
            view?.hideLoading()
            view?.showList()
        } else {
            view?.showPlaceholder()
            view?.showFoundArtists(artists)
            view?.hideLoading()
            view?.showList()
        }
    }

    private fun showLoading(){
        view?.showLoading()
    }

    private fun hideLoading(){
        view?.hideLoading()
    }

    private fun showError( error : Throwable ){
        view?.showError( error )
    }

    override fun onViewAttached(view: ArtistsContract.View) {
        this.view = view
    }

    override fun onViewDetached(detach: Boolean) {
        this.view = null
    }

    override fun goBack(): Boolean {
        return navigator.goBack()
    }

}