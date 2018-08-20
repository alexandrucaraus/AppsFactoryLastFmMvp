package eu.caraus.appsflastfm.ui.search.artists.di

import dagger.Subcomponent
import dagger.android.AndroidInjector

import eu.caraus.appsflastfm.ui.search.artists.ArtistsFragment

@Subcomponent(modules = [ArtistsModule::class])
interface ArtistsComponent : AndroidInjector<ArtistsFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ArtistsFragment>()
}
