package eu.caraus.appsflastfm.ui.search.albums.di

import android.support.v4.app.Fragment

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.search.albums.AlbumsFragment

@Module(subcomponents = [AlbumsComponent::class])
abstract class AlbumsBuilder {

    @Binds
    @IntoMap
    @FragmentKey(AlbumsFragment::class)
    internal abstract fun bindCourseList(builder: AlbumsComponent.Builder): AndroidInjector.Factory<out Fragment>


}
