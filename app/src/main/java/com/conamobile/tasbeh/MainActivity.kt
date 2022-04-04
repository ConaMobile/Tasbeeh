package com.conamobile.tasbeh

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import kotlin.random.Random
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    var homeFragment: HomeFragment? = null
    lateinit var topAnim:Animation
    lateinit var bottomAnim:Animation
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var home_frag: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            hideSystemBars()

        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        initViews()


//        navController = findNavController(R.id.fragment)

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

    private fun initViews() {

        homeFragment = HomeFragment()
        imageView = findViewById(R.id.image)
        textView = findViewById(R.id.text)
        home_frag = findViewById(R.id.home_fragment)

        when(Random.nextInt(0,4)){
            0->{
                imageView.setImageResource(R.drawable.splash_image1)
            }
            1->{
                imageView.setImageResource(R.drawable.splash_image2)
            }
            2->{
                imageView.setImageResource(R.drawable.splash_image3)
            }
            3->{
                imageView.setImageResource(R.drawable.splash_images)
            }
        }



        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        imageView.animation = topAnim

        topAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                imageView.visibility = View.GONE
                textView.visibility = View.VISIBLE
                textView.animation = bottomAnim
            }
        })

        bottomAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                textView.visibility = View.GONE
                imageView.visibility = View.GONE
                val intent = Intent(this@MainActivity, SetActivity::class.java)
                startActivity(intent)
                finish()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.home_fragment, homeFragment!!)
//                    .commit()
            }
        })

//        imageView.setOnClickListener {
//            topAnim.hasEnded()
//            bottomAnim.hasEnded()
//            topAnim.cancel()
//            bottomAnim.cancel()
//        }

//        textView.setOnClickListener {
//            topAnim.cancel()
//            bottomAnim.cancel()
////            topAnim.hasEnded()
////            bottomAnim.hasEnded()
//        }

    }

    override fun onResume() {
        super.onResume()

        home_frag.setOnClickListener {
            val intent = Intent(this@MainActivity, SetActivity::class.java)
            startActivity(intent)
            textView.visibility = View.GONE
            imageView.visibility = View.GONE
            finish()
        }

    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_navigation).navigateUp()
    }
}