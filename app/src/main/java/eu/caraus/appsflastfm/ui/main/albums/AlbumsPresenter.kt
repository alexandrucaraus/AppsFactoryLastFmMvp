package eu.caraus.appsflastfm.ui.main.albums

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.view.View
import android.widget.ImageView
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import io.reactivex.disposables.Disposable

class AlbumsPresenter( private val interactor : AlbumsContract.Interactor  ,
                       private val navigator  : AlbumsContract.Navigator   ,
                       private val scheduler  : SchedulerProvider) : AlbumsContract.Presenter {

    private var view : AlbumsContract.View? = null

    private var disposable1 : Disposable? = null
    private var disposable2 : Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        disposable1 = interactor.getAlbumsOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> showFoundAlbums( it.data )
                    //if( it.data.isEmpty() ) /*showFoundNothing()*/ else
            }
        }

        disposable2 = interactor.deleteAlbumOutcum().subOnIoObsOnUi(scheduler).subscribe{
            when(it){
                is Outcome.Success -> view?.deleteSuccess()
                is Outcome.Failure -> view?.deleteFailed()
            }
        }

        interactor.getAlbums()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        interactor.getAlbums()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable1?.dispose()
        disposable2?.dispose()
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

    override fun showAlbumDetails( mbid: String, view : ImageView) {

    }

    override fun showAlbumDetails( mbid: String, imageUrl: String, view: ImageView) {
       navigator.showAlbumDetails( mbid, imageUrl, view)
    }

    private fun showFoundAlbums( data : List<Album?> ) {
        view?.hideLoading()
        view?.updateAlbums( data)
    }

    private fun showFoundNothing() {
        view?.showFoundNothing()
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