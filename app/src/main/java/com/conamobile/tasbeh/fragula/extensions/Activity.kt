package com.conamobile.tasbeh.fragula.extensions

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.conamobile.tasbeh.fragula.Navigator
import com.conamobile.tasbeh.fragula.common.BundleBuilder

inline fun <reified T : Fragment> Activity.addFragment(
    @IdRes navigatorId: Int? = null,
    noinline bundleBuilder: (BundleBuilder.() -> Unit)? = null
) {

    val fragment = T::class.java.newInstance()
    val navigator = this.findNavigatorView(navigatorId)

    navigator.addFragment(
        fragment = fragment,
        builder = bundleBuilder
    )
}

inline fun <reified T : Fragment> Activity.replaceFragment(
    position: Int? = null,
    @IdRes navigatorId: Int? = null,
    noinline bundleBuilder: (BundleBuilder.() -> Unit)? = null
) {

    val fragment = T::class.java.newInstance()
    val navigator = findNavigatorView(navigatorId)

    navigator.replaceFragment(
        fragment = fragment,
        position = position,
        builder = bundleBuilder
    )
}

fun Activity.findNavigatorView(@IdRes navigatorId: Int? = null): Navigator {
    val decorView = (window.decorView as ViewGroup)

    val visited: MutableList<View> = mutableListOf()
    val unvisited: MutableList<View> = mutableListOf()
    unvisited.add(decorView)

    while (unvisited.isNotEmpty()) {
        val child = unvisited.removeAt(0)
        if (child is Navigator && (navigatorId == null || navigatorId == child.id)) return child
        visited.add(child)
        if (child !is ViewGroup) continue
        val childCount = child.childCount
        for (i in 0 until childCount) unvisited.add(child.getChildAt(i))
    }

    throw NullPointerException("Activity doesn't have a Navigator. Add a Navigator view to the xml layout of your Activity.")
}