package eu.caraus.appsflastfm.testUtils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import eu.caraus.appsflastfm.App
import eu.caraus.appsflastfm.ui.base.BaseActivity


class TestApplication : App(), Application.ActivityLifecycleCallbacks {

    var currentActivity: BaseActivity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated( activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity as BaseActivity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity as BaseActivity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity as BaseActivity
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}