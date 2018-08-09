package eu.caraus.appsflastfm.ui.util.metrics

import android.content.res.Resources

import android.util.DisplayMetrics

fun DisplayMetrics.dpToPx( dp : Float ) : Int
        = (dp * ( this.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT )).toInt()

fun DisplayMetrics.pxToDp( px : Float ) : Int
        = (px / ( this.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT )).toInt()

fun Resources.dpToPx( resId : Int ) : Int
        = ( this.getDimension(resId) * this.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT ).toInt()
