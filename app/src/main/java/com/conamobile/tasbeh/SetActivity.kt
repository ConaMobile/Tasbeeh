package com.conamobile.tasbeh

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.conamobile.tasbeh.Objects.BackObject
import kotlinx.android.synthetic.main.activity_set.*


class SetActivity : AppCompatActivity(), ExampleCallback {

    private var activity: FragmentActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        if (savedInstanceState == null) {
            navigator.addFragment(HomeFragment())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (BackObject.systemBar)
                hideSystemBars()
            else
                showSystemBars()

        } else {
            if (BackObject.systemBar)
                hideSystemToLowVersion()
            else
                showSystemBars()
        }

    }

    private fun hideSystemToLowVersion() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun showSystemBars() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

//    override fun onBackPressed() {
//        if (BackObject.backAllowed)
//            showAlertDialog()
//        else
//            super.onBackPressed()
//    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        val view = LayoutInflater.from(this)
            .inflate(R.layout.customview_dialog, findViewById(R.id.layoutDialog))
        builder.setView(view)
        view.findViewById<TextView>(R.id.textTitle).text = getString(R.string.exit)
        view.findViewById<TextView>(R.id.textMessage).text = getString(R.string.message)
        view.findViewById<Button>(R.id.buttonCancelAction).text = getString(R.string.cancel)
        view.findViewById<Button>(R.id.buttonExitAction).text = getString(R.string.yes)

        val alertDialog: AlertDialog = builder.create()

        view.findViewById<Button>(R.id.buttonCancelAction).setOnClickListener {
            //
            alertDialog.dismiss()
        }

        view.findViewById<Button>(R.id.buttonExitAction).setOnClickListener {
            alertDialog.dismiss()
//
            finish()
//            exitProcess(-2)
//            exitProcess(2)

//            finishAndRemoveTask()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.setCancelable(true)
        alertDialog.show()

    }

    private fun hideSystemBars() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_navigation).navigateUp()
    }

    // Intercept and block touch event when the new fragment is opening
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (navigator.isBlockTouchEvent)
            true
        else
            super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (navigator.fragmentCount > 1) {
            navigator.goToPreviousFragmentAndRemoveLast()
        } else {
            showAlertDialog()
//            super.onBackPressed()
        }
    }

    override fun onSuccess() {
    }

}

interface ExampleCallback {
    fun onSuccess()
}

private const val TAG = "MainActivity"
