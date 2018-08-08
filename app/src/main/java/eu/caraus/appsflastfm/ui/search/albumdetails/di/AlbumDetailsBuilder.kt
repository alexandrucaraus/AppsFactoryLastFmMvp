package eu.caraus.appsflastfm.ui.search.albums.di

import android.support.v4.app.Fragment

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsFragment

@Module(subcomponents = [AlbumDetailsComponent::class])
abstract class AlbumDetailsBuilder {

    @Binds
    @IntoMap
    @FragmentKey(AlbumDetailsFragment::class)
    internal abstract fun bindCourseList(builder: AlbumDetailsComponent.Builder): AndroidInjector.Factory<out Fragment>


}
