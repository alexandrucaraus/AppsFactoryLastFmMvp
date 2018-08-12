package eu.caraus.appsflastfm.services.youtube

import android.app.Service


import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap


@Module(subcomponents = [YoutubeServiceComponent::class])
abstract class YoutubeServiceBuilder {

    @Binds
    @IntoMap
    @ServiceKey(YoutubePlayerService::class)
    internal abstract fun bindService(builder: YoutubeServiceComponent.Builder): AndroidInjector.Factory<out Service>


}
