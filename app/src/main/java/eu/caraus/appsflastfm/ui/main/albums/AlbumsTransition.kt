package eu.caraus.appsflastfm.ui.main.albums

import android.content.Context
import android.support.transition.ChangeBounds
import android.support.transition.ChangeClipBounds
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet


class AlbumsTransition : TransitionSet {

    constructor() {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {

        interpolator = FastOutSlowInInterpolator()

        ordering = ORDERING_TOGETHER

        duration = 250

        addTransition( ChangeClipBounds() )
                .addTransition( ChangeTransform())
                .addTransition( ChangeBounds())

    }

}