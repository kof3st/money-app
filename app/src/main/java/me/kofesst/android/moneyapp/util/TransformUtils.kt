package me.kofesst.android.moneyapp.util

import android.graphics.Color
import android.view.View
import androidx.annotation.IntegerRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialElevationScale
import me.kofesst.android.moneyapp.R

fun Fragment.setPostpone(view: View) {
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }
}

fun Fragment.setEnterSharedTransition(@IntegerRes durationRes: Int) {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = R.id.fragment_container
        duration = resources.getInteger(durationRes).toLong()
        scrimColor = Color.TRANSPARENT
    }
}

fun Fragment.setExitSharedTransition(@IntegerRes durationRes: Int) {
    exitTransition = MaterialElevationScale(false).apply {
        duration = resources.getInteger(durationRes).toLong()
    }

    reenterTransition = MaterialElevationScale(true).apply {
        duration = resources.getInteger(durationRes).toLong()
    }
}

infix fun Int.include(view: View): FragmentNavigator.Extras {
    val transitionName = view.resources.getString(this)
    return FragmentNavigatorExtras(view to transitionName)
}