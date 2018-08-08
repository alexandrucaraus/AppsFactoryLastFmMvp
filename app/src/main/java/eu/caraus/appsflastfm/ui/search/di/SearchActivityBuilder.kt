package eu.caraus.appsflastfm.ui.search.di

import android.app.Activity

import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.search.albums.di.ArtistsComponent
import eu.caraus.appsflastfm.ui.search.SearchActivity
import eu.caraus.appsflastfm.ui.search.albums.di.AlbumDetailsComponent
import eu.caraus.appsflastfm.ui.search.albums.di.AlbumsComponent


@Module( subcomponents = [

    ArtistsComponent::class,
    AlbumsComponent::class,
    AlbumDetailsComponent::class

])
abstract class SearchActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(SearchActivity::class)
    internal abstract fun bindMainActivity(builder: SearchActivityComponent.Builder): AndroidInjector.Factory<out Activity>

}