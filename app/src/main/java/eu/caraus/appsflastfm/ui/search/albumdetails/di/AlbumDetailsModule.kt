package eu.caraus.appsflastfm.ui.search.albumdetails.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsContract
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsPresenter


@Module
class AlbumDetailsModule {

    @Provides
    internal fun provideAlbumDetailsPresenter(interactor : AlbumDetailsContract.Interactor,
                                              navigator  : AlbumDetailsContract.Navigator,
                                              scheduler  : SchedulerProvider,
                                              rxBus: RxBus)
            : AlbumDetailsContract.Presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus )

}