package eu.caraus.appsflastfm

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import eu.caraus.appsflastfm.common.schedulers.AppSchedulerProvider
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApiClient
import eu.caraus.appsflastfm.ui.main.di.MainActivityComponent
import android.arch.persistence.room.Room
import android.content.Context
import eu.caraus.appsflastfm.data.local.Database
import eu.caraus.appsflastfm.ui.search.di.SearchActivityComponent


@Module( subcomponents = [

    MainActivityComponent::class,
    SearchActivityComponent::class

])
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext( app : App ) :  Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideHome24Api() : LastFmApi = LastFmApiClient().client.create( LastFmApi::class.java )

    @Provides
    @Singleton
    fun providesScheduler() : SchedulerProvider = AppSchedulerProvider()

    @Provides
    @Singleton
    fun providesDatabase(context: Context): Database {
        return Room.databaseBuilder( context,
                                     Database::class.java,
                                     Database.DATABASE_NAME)
                                    .fallbackToDestructiveMigration()
                                    .build()

    }


}
