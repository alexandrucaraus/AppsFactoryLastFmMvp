package eu.caraus.appsflastfm.ui.main.albums.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import eu.caraus.appsflastfm.ui.main.albums.AlbumsFragment

@Subcomponent(modules = [AlbumsModule::class])
interface AlbumsComponent : AndroidInjector<AlbumsFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AlbumsFragment>()
}
