package eu.caraus.appsflastfm.ui.search.albums.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.ui.search.albums.AlbumsContract
import eu.caraus.appsflastfm.ui.search.albums.AlbumsPresenter

@Module
class AlbumsModule {

    @Provides
    fun provideAlbumsPresenter(interactor : AlbumsContract.Interactor,
                                        navigator  : AlbumsContract.Navigator,
                                        scheduler  : SchedulerProvider)
            : AlbumsContract.Presenter = AlbumsPresenter( interactor, navigator , scheduler )

}