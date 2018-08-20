package eu.caraus.appsflastfm.ui.main.albums.di

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.ui.main.albums.AlbumsContract
import eu.caraus.appsflastfm.ui.main.albums.AlbumsPresenter
import eu.caraus.appsflastfm.ui.main.di.MainActivityScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named


@Module
class AlbumsModule {

    @Provides
    fun provideAlbumsPresenter(interactor : AlbumsContract.Interactor,
                                        navigator  : AlbumsContract.Navigator,
                                        scheduler  : SchedulerProvider,
                                        @Named("albumsList.disposable") disposable: CompositeDisposable)
            : AlbumsContract.Presenter = AlbumsPresenter( interactor, navigator , scheduler, disposable )

}