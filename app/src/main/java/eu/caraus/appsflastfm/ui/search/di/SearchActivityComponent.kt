package eu.caraus.appsflastfm.ui.search.di

import dagger.Subcomponent
import dagger.android.AndroidInjector

import eu.caraus.appsflastfm.ui.search.SearchActivity
import eu.caraus.appsflastfm.ui.search.albumdetails.di.AlbumDetailsBuilder
import eu.caraus.appsflastfm.ui.search.albums.di.AlbumsBuilder
import eu.caraus.appsflastfm.ui.search.artists.di.ArtistsBuilder

@SearchActivityScope
@Subcomponent( modules = [

        SearchActivityModule::class,

        ArtistsBuilder::class,
        AlbumsBuilder::class,
        AlbumDetailsBuilder::class

])
interface SearchActivityComponent : AndroidInjector<SearchActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SearchActivity>()

}
