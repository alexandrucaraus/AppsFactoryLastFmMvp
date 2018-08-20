package eu.caraus.appsflastfm.ui.search.artists.di

import android.support.v4.app.Fragment

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import eu.caraus.appsflastfm.ui.search.artists.ArtistsFragment


@Module(subcomponents = [ArtistsComponent::class])
abstract class ArtistsBuilder {

    @Binds
    @IntoMap
    @FragmentKey(ArtistsFragment::class)
    abstract fun bindCourseList(builder: ArtistsComponent.Builder): AndroidInjector.Factory<out Fragment>


}
