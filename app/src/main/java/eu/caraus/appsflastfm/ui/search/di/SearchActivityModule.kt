package eu.caraus.appsflastfm.ui.search.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.local.Database
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import eu.caraus.appsflastfm.ui.search.*
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsContract
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsInteractor
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsNavigator
import eu.caraus.appsflastfm.ui.search.albums.AlbumsContract
import eu.caraus.appsflastfm.ui.search.albums.AlbumsInteractor
import eu.caraus.appsflastfm.ui.search.albums.AlbumsNavigator
import eu.caraus.appsflastfm.ui.search.artists.ArtistsContract
import eu.caraus.appsflastfm.ui.search.artists.ArtistsInteractor
import eu.caraus.appsflastfm.ui.search.artists.ArtistsNavigator



@Module
class SearchActivityModule {

    // All

    @Provides
    @SearchActivityScope
    fun provideSearchNavigation( activityView : SearchActivity )
            : SearchActivityScreenLoader = SearchActivityScreenLoader( activityView , R.id.search_fragment_container )

    // Main
    @Provides
    @SearchActivityScope
    fun provideSearchPresenter( navigator: SearchContract.Navigator)
            : SearchContract.Presenter = SearchPresenter( navigator )

    @Provides
    @SearchActivityScope
    fun provideSearchNavigator( navigation: SearchActivityScreenLoader)
            : SearchContract.Navigator = SearchNavigator( navigation )


    // Artists

    @Provides
    @SearchActivityScope
    fun provideAritstsInteractor( service: LastFmApi, scheduler: SchedulerProvider)
            : ArtistsContract.Interactor = ArtistsInteractor( service, scheduler )

    @Provides
    @SearchActivityScope
    fun provideAritstsNavigator( navigation: SearchActivityScreenLoader)
            : ArtistsContract.Navigator = ArtistsNavigator( navigation )


    // Top Albums



    @Provides
    @SearchActivityScope
    fun provideAlbumsInteractor( service: LastFmApi,
                                          database: Database,
                                          scheduler: SchedulerProvider)
            : AlbumsContract.Interactor = AlbumsInteractor( service, database, scheduler )

    @Provides
    @SearchActivityScope
    fun provideAlbumsNavigator( navigation: SearchActivityScreenLoader)
            : AlbumsContract.Navigator = AlbumsNavigator( navigation )


    // Album Details

    @Provides
    @SearchActivityScope
    fun provideAlbumDetailsInteractor( service: LastFmApi, scheduler: SchedulerProvider)
            : AlbumDetailsContract.Interactor = AlbumDetailsInteractor( service, scheduler )

    @Provides
    @SearchActivityScope
    fun provideAlbumDetailsNavigator( navigation: SearchActivityScreenLoader)
            : AlbumDetailsContract.Navigator = AlbumDetailsNavigator( navigation )


}
