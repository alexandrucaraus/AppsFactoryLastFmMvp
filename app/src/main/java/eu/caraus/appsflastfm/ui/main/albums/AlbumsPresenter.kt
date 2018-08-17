package eu.caraus.appsflastfm.ui.main.albums

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.widget.ImageView
import eu.caraus.appsflastfm.common.extensions.addTo
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import io.reactivex.disposables.CompositeDisposable


class AlbumsPresenter( private val interactor : AlbumsContract.Interactor  ,
                       private val navigator  : AlbumsContract.Navigator   ,
                       private val scheduler  : SchedulerProvider,
                       private val disposable : CompositeDisposable ) : AlbumsContract.Presenter {

    private var view : AlbumsContract.View? = null


    private var data = listOf<Album?>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        interactor.getAlbumsOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> {
                    data = it.data
                    showFoundAlbums(it.data)
                }
            }
        }.addTo( disposable )

        interactor.deleteAlbumOutcome().subOnIoObsOnUi(scheduler).subscribe{
            when(it){
                is Outcome.Success -> view?.deleteSuccess()
                is Outcome.Failure -> view?.deleteFailed()
            }
        }.addTo( disposable )

        interactor.getAlbums()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        data?.let {
            showFoundAlbums(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable.dispose()
    }

    override fun showSearchResultScreen( searchTerm: String ) {
        navigator.showSearchResultScreen( searchTerm )
    }

    override fun deleteAlbum( album : Album) {
        interactor.deleteAlbum( album )
    }

    override fun showAlbumDetails( mbid: String ) {
        navigator.showAlbumDetails( mbid )
    }

    override fun showAlbumDetails( mbid: String, imageUrl: String, view: ImageView) {
       navigator.showAlbumDetails( mbid, imageUrl, view)
    }

    private fun showFoundAlbums( data : List<Album?> ) {
        if( data.isNotEmpty()) {
            view?.hidePlaceholder()
            view?.updateAlbums(data)
            view?.hideLoading()
            view?.showList()
        } else {
            view?.showPlaceholder()
            view?.updateAlbums(data)
            view?.hideLoading()
            view?.hideList()
        }
    }

    private fun hideLoading() {
        view?.hideLoading()
    }

    private fun showLoading() {
        view?.showLoading()
    }

    private fun showError( error : Throwable ) {
        view?.showError( error )
    }

    override fun goBack(): Boolean {
       return navigator.goBack()
    }

    override fun onViewAttached( view: AlbumsContract.View) {
       this.view = view
    }

    override fun onViewDetached( detach: Boolean) {
       this.view = null
    }

}