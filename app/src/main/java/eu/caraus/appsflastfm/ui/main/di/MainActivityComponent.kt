package eu.caraus.appsflastfm.ui.main.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import eu.caraus.appsflastfm.ui.main.MainActivity
import eu.caraus.appsflastfm.ui.main.albums.di.AlbumDetailsBuilder
import eu.caraus.appsflastfm.ui.main.albums.di.AlbumsBuilder


@MainActivityScope
@Subcomponent(modules = [

        MainActivityModule::class,

        AlbumDetailsBuilder::class,
        AlbumsBuilder::class

])
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()

}
