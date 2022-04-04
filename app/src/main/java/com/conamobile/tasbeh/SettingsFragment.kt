package com.conamobile.tasbeh

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import com.conamobile.tasbeh.Objects.BackObject
import com.conamobile.tasbeh.fragula.extensions.addFragment
import com.conamobile.tasbeh.fragula.extensions.getCallback
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.delay


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    lateinit var button: Button

    override fun onStart() {
        super.onStart()
        BackObject.backAllowed = false
        BackObject.systemBar = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        status bar height
        getStatusBarHeight(view)

        val connectingAnimation =
            AnimationUtils.loadAnimation(context, R.anim.brand_anim)
        view.brand_text.startAnimation(connectingAnimation)

        card1.setOnClickListener {
            requireActivity().onBackPressed()
            HomeFragment.starter()
        }

        card2.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Cona+Mobile"))
            startActivity(browserIntent)
        }

        card3.setOnClickListener {
            addFragment<AboutFragment>()
            getCallback<ExampleCallback>().onSuccess()
        }

    }

    private fun getStatusBarHeight(view: View): Int {
        var statusBar = view.findViewById<View>(R.id.home_status_bar)

        val height: Int
        val myResources: Resources = resources
        val idStatusBarHeight: Int =
            myResources.getIdentifier("status_bar_height", "dimen", "android")
        if (idStatusBarHeight > 0) {
            height = resources.getDimensionPixelSize(idStatusBarHeight)
            statusBar.layoutParams.height = height
        } else {
            height = 0
        }
        return height
    }

}
