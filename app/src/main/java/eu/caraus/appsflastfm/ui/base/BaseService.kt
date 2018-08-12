package eu.caraus.appsflastfm.ui.base

import android.app.Service
import dagger.android.AndroidInjection

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

abstract class BaseService : Service() {

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

}