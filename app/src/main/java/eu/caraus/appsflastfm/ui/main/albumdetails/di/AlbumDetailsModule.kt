package eu.caraus.appsflastfm.ui.main.albumdetails.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsContract
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsPresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

@Module
class AlbumDetailsModule {

    @Provides
    fun provideAlbumDetailsPresenter(interactor : AlbumDetailsContract.Interactor,
                                              navigator  : AlbumDetailsContract.Navigator,
                                              scheduler  : SchedulerProvider,
                                              rxBus      : RxBus,
                                              @Named("albumDetails.disposable")
                                              disposable : CompositeDisposable)
            : AlbumDetailsContract.Presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

}