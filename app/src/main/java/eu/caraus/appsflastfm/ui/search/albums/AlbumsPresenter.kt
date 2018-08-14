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

    private var disposable : Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        disposable = interactor.getAlbumsOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  ->
                    if( it.data.isEmpty()) showFoundAlbums( it.data )
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable?.dispose()
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
        view?.showFoundAlbums( data)
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