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
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named


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
                                         scheduler  : SchedulerProvider,
                                         @Named("albumsList.disposable") disposable: CompositeDisposable)
            : AlbumsContract.Presenter = AlbumsPresenter( interactor, navigator , scheduler, disposable )

    @Provides
    @MainActivityScope
    internal fun provideAlbumsInteractor( database: Database,
                                          scheduler: SchedulerProvider,
                                          @Named("albumsList.disposable") disposable: CompositeDisposable)
            : AlbumsContract.Interactor = AlbumsInteractor( database, scheduler, disposable )

    @Provides
    @MainActivityScope
    internal fun provideAlbumsNavigator( navigation: MainActivityScreenLoader)
            : AlbumsContract.Navigator = AlbumsNavigator( navigation )

    @Provides
    @Named("albumsList.disposable")
    @MainActivityScope
    fun provideAlbumsDisposable() = CompositeDisposable()

    // Album Details

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsPresenter( interactor : AlbumDetailsContract.Interactor,
                                               navigator  : AlbumDetailsContract.Navigator,
                                               scheduler  : SchedulerProvider,
                                               rxBus      : RxBus,
                                               @Named("albumDetails.disposable")
                                               disposable : CompositeDisposable)
            : AlbumDetailsContract.Presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsInteractor( database: Database ,
                                                scheduler: SchedulerProvider,
                                                @Named("albumDetails.disposable") disposable : CompositeDisposable)
            : AlbumDetailsContract.Interactor = AlbumDetailsInteractor( database, scheduler, disposable )

    @Provides
    @MainActivityScope
    internal fun provideAlbumDetailsNavigator( navigation: MainActivityScreenLoader)
            : AlbumDetailsContract.Navigator = AlbumDetailsNavigator( navigation )

    @Provides
    @Named("albumDetails.disposable")
    @MainActivityScope
    fun provideAlbumDetailsDisposable() = CompositeDisposable()

}
