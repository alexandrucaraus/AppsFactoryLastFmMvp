package eu.caraus.appsflastfm.ui.search.albums

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.widget.ImageView
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import io.reactivex.disposables.Disposable

class AlbumsPresenter( private val interactor : AlbumsContract.Interactor  ,
                       private val navigator  : AlbumsContract.Navigator   ,
                       private val scheduler  : SchedulerProvider) : AlbumsContract.Presenter {


    private var view : AlbumsContract.View? = null

    private var disposableList : Disposable? = null

    private var disposableSave : Disposable? = null

    private var data : List<AlbumItem?>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        disposableList = interactor.getAlbumsOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) if( data == null) showLoading()  else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> {
                    data = it.data
                    showFoundAlbums( it.data )
                }
            }
        }

        disposableSave = interactor.getAlbumSaveOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Success -> showMessage( "\u2661 ${it.data.name}")
                is Outcome.Failure -> showError(it.error)
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        data?.let {
            showFoundAlbums(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposableList?.dispose()
        disposableSave?.dispose()
    }

    override fun showAlbumDetails( artistName: String, albumName : String ) {
        navigator.showAlbumDetails( artistName,  albumName )
    }

    override fun showAlbumDetails(artistName: String, albumName: String, sharedElement: ImageView) {

    }

    override fun showAlbumDetails(artistName: String, albumName: String, sharedPicUrl: String, sharedElement: ImageView) {
        navigator.showAlbumDetails( artistName, albumName, sharedPicUrl, sharedElement)
    }

    override fun saveAlbumDetails( artistName: String, albumName: String ) {
        interactor.saveAlbum( artistName, albumName )
    }

    override fun getAlbums( artistId : String) {
        interactor.getAlbums( artistId )
    }

    private fun showFoundAlbums( data : List<AlbumItem?> ) {
        if( data.isNotEmpty()) {
            view?.hidePlaceholder()
            view?.showFoundAlbums(data)
            view?.hideLoading()
            view?.showList()
        } else {
            view?.showPlaceholder()
            view?.showFoundAlbums(data)
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

    private fun showMessage( msg : String ){
        view?.showMessage( msg )
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