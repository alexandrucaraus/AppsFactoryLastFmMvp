package eu.caraus.appsflastfm.services.youtube

import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = [YoutubeServiceModule::class])
interface YoutubeServiceComponent : AndroidInjector<YoutubePlayerService> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<YoutubePlayerService>()
}
