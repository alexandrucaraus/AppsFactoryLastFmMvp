package eu.caraus.appsflastfm.ui.main.di


import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.local.Database
import eu.caraus.appsflastfm.ui.main.MainActivity
import eu.caraus.appsflastfm.ui.main.MainContract
import eu.caraus.appsflastfm.ui.main.MainNavigator
import eu.caraus.appsflastfm.ui.main.MainPresenter
import eu.caraus.appsflastfm.ui.main.MainActivityScreenLoader
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsContract
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsInteractor
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsNavigator
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsPresenter
import eu.caraus.appsflastfm.ui.main.albums.AlbumsContract
import eu.caraus.appsflastfm.ui.main.albums.AlbumsInteractor
import eu.caraus.appsflastfm.ui.main.albums.AlbumsNavigator
import eu.caraus.appsflastfm.ui.main.albums.AlbumsPresenter


@Module
class MainActivityModule {

    // All Main

    @Provides
    @MainActivityScope
    internal fun provideMainNavigation(activityView: MainActivity)
            : MainActivityScreenLoader = MainActivityScreenLoader(activityView, R.id.main_fragment_container )

    // Main

    @Provides
    @MainActivityScope
    internal fun provideMainPresenter( navigator: MainContract.Navigator)
            : MainContract.Presenter = MainPresenter( navigator )

    @Provides
    @MainActivityScope
    internal fun provideMainNavigator( navigation: MainActivityScreenLoader)
            : MainContract.Navigator = MainNavigator(navigation)

    // Top Albums

    @Provides
    @MainActivityScope
    internal fun provideAlbumsPresenter( interactor : AlbumsContract.Interactor,
                                         navigator  : AlbumsContract.Navigator,
                                         scheduler  : SchedulerProvider)
            : AlbumsContract.Presenter = AlbumsPresenter( interactor, navigator , scheduler )

    @Provides
    @MainActivityScope
    internal fun provideAlbumsInteractor(database: Database, scheduler: SchedulerProvider)
            : AlbumsContract.Interactor = AlbumsInteractor( database, scheduler )

    @Provides
    @MainActivityScope
    internal fun provideAlbumsNavigator( navigation: MainActivityScreenLoader)
            : AlbumsContract.Navigator = AlbumsNavigator( navigation )

    // Album Details

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsPresenter( interactor : AlbumDetailsContract.Interactor,
                                               navigator  : AlbumDetailsContract.Navigator,
                                               scheduler  : SchedulerProvider,
                                               rxBus: RxBus )
            : AlbumDetailsContract.Presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus )

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsInteractor( database: Database , scheduler: SchedulerProvider)
            : AlbumDetailsContract.Interactor = AlbumDetailsInteractor( database, scheduler )

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsNavigator( navigation: MainActivityScreenLoader)
            : AlbumDetailsContract.Navigator = AlbumDetailsNavigator( navigation )


}
