package eu.caraus.appsflastfm.ui.main.di

import android.app.Activity

import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.main.MainActivity
import eu.caraus.appsflastfm.ui.main.albums.di.AlbumDetailsComponent
import eu.caraus.appsflastfm.ui.main.albums.di.AlbumsComponent

@Module( subcomponents = [

        AlbumDetailsComponent::class,
        AlbumsComponent::class

])
abstract class MainActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivity(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>

}