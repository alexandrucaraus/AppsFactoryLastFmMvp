package eu.caraus.appsflastfm.ui.idlingResources

import android.support.test.espresso.IdlingPolicies
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.ViewInteraction
import java.util.concurrent.TimeUnit

class IdlingResourceUtils {

    companion object {

        fun idlingPoliciesSetup ( timeoutSeconds : Long )  {
            IdlingPolicies.setMasterPolicyTimeout( timeoutSeconds, TimeUnit.SECONDS)
            IdlingPolicies.setIdlingResourceTimeout( timeoutSeconds, TimeUnit.SECONDS)
        }

        fun whenReady(idlingResource : IdlingResource, action : () -> ViewInteraction) {
            IdlingRegistry.getInstance().register(idlingResource)
            action()
            IdlingRegistry.getInstance().unregister(idlingResource)
        }

    }

}