package eu.caraus.appsflastfm.ui.search.albumdetails.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsFragment

@Subcomponent(modules = [AlbumDetailsModule::class])
interface AlbumDetailsComponent : AndroidInjector<AlbumDetailsFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AlbumDetailsFragment>()
}
