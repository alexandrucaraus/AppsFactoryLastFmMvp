package eu.caraus.appsflastfm.ui.main.albumdetails

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import io.reactivex.disposables.Disposable

class AlbumDetailsPresenter( private val interactor: AlbumDetailsContract.Interactor,
                             private val navigator: AlbumDetailsContract.Navigator,
                             private val scheduler : SchedulerProvider) : AlbumDetailsContract.Presenter {

    private var view : AlbumDetailsContract.View? = null

    private var disposable : Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        disposable = interactor.getAlbumInfoOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> showAlbumInfo( it.data )
            }
        }
    }

    private fun showAlbumInfo( album : Album?) {
        view?.showAlbumInfo( album )
    }

    private fun hideLoading() {

    }

    private fun showLoading() {

    }

    private fun showError(error: Throwable) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable?.dispose()
    }


    override fun getAlbumInfo(artistName: String, albumName: String) {
        interactor.getAlbumInfo( artistName, albumName)
    }

    override fun goBack(): Boolean {
       return navigator.goBack()
    }

    override fun onViewAttached(view: AlbumDetailsContract.View) {
        this.view = view
    }

    override fun onViewDetached(detach: Boolean) {
        this.view = null
    }


}