package eu.caraus.appsflastfm

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import eu.caraus.appsflastfm.services.youtube.YoutubePlayerService
import eu.caraus.appsflastfm.services.youtube.YoutubeServiceBuilder
import eu.caraus.appsflastfm.ui.main.di.MainActivityBuilder
import eu.caraus.appsflastfm.ui.search.di.SearchActivityBuilder

@Singleton
@Component( modules = [

        AndroidSupportInjectionModule::class,
        AppModule::class,

        MainActivityBuilder::class,
        SearchActivityBuilder::class,
        YoutubeServiceBuilder::class

])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): AppComponent.Builder

        fun build(): AppComponent

    }

    override fun inject(app: App)

}
