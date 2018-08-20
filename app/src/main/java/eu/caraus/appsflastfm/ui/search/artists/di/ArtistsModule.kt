package eu.caraus.appsflastfm.ui.search.artists.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.ui.search.artists.ArtistsContract
import eu.caraus.appsflastfm.ui.search.artists.ArtistsPresenter


@Module
class ArtistsModule {

    @Provides
    fun provideArtistsPresenter(interactor : ArtistsContract.Interactor,
                                navigator  : ArtistsContract.Navigator,
                                scheduler  : SchedulerProvider)
            : ArtistsContract.Presenter = ArtistsPresenter( interactor, navigator , scheduler )

}