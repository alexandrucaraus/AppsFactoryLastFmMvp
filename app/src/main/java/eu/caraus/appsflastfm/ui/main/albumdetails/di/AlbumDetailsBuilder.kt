package eu.caraus.appsflastfm.ui.main.albumdetails.di

import android.support.v4.app.Fragment

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsFragment

@Module(subcomponents = [AlbumDetailsComponent::class])
abstract class AlbumDetailsBuilder {

    @Binds
    @IntoMap
    @FragmentKey(AlbumDetailsFragment::class)
    abstract fun bindCourseList(builder: AlbumDetailsComponent.Builder): AndroidInjector.Factory<out Fragment>


}
