package com.conamobile.tasbeh

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.conamobile.tasbeh.Objects.BackObject
import com.conamobile.tasbeh.fragula.extensions.addFragment
import com.conamobile.tasbeh.fragula.extensions.dp
import com.conamobile.tasbeh.fragula.extensions.getCallback
import com.conamobile.tasbeh.fragula.listener.OnFragmentNavigatorListener
import com.conamobile.tasbeh.memory.MySharedPrefarance
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import kotlin.math.roundToInt

private lateinit var drawerLayout: DrawerLayout

class HomeFragment : Fragment(), SensorEventListener, OnFragmentNavigatorListener {

    lateinit var progressBar: ProgressBar
    lateinit var textViewProgress: TextView
    lateinit var count_salovat_all: TextView
    lateinit var restartBtn: CircularRevealCardView
    lateinit var voiceBtn: CircularRevealCardView
    lateinit var animBtn: CircularRevealCardView
    lateinit var listBtn: CircularRevealCardView
    lateinit var settingsBtn: CircularRevealCardView
    lateinit var soundImg: ImageView
    lateinit var animationImg: ImageView
    lateinit var salovatlarName: TextView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var mediaPlayer2: MediaPlayer
    private var progress_count: Double = 0.0
    lateinit var zoomAnim: Animation
    lateinit var alphaAnim: Animation

    private var salovat_bar_count = 0
    private var all_count = 0
    private var sound_master = 0
    private var animation_master = 0
    private var salovatlar_master = 0
    lateinit var imageView: ImageView
    private lateinit var sensorManager: SensorManager

    override fun onPause() {
        super.onPause()
        MySharedPrefarance(requireContext()).isSavedJami(all_count)

        MySharedPrefarance(requireContext()).isSaved33(salovat_bar_count)

        MySharedPrefarance(requireContext()).isSavedProgress(progress_count.toDouble().toString())

        //
    }

    override fun onStart() {
        super.onStart()

        when (MySharedPrefarance(requireContext()).getSavedBackImage()) {

            1 -> {
                imageView.setImageResource(R.drawable.splash_image2)
            }

            2 -> {
                imageView.setImageResource(R.drawable.splash_image1)
            }

            3 -> {
                imageView.setImageResource(R.drawable.splash_image3)
            }

            4 -> {
                imageView.setImageResource(R.drawable.splash_image4)
            }
            5 -> {

                imageView.setImageResource(R.drawable.splash_images)
            }

        }

        BackObject.backAllowed = true
        BackObject.systemBar = true

        metricsIn()
        setUpSensorStuff()

        when (MySharedPrefarance(requireContext()).getSavedVoice()) {

            0 -> {
                soundImg.setImageResource(R.drawable.vibration_img)
                sound_master = 1
            }

            1 -> {
                soundImg.setImageResource(R.drawable.audio_off)
                sound_master = 2
            }

            2 -> {
                soundImg.setImageResource(R.drawable.audio_on)
                sound_master = 0
            }

        }

        when {
            !MySharedPrefarance(requireContext()).getSavedAnim() -> {
                animationImg.setImageResource(R.drawable.animation_off)
                sensorManager.unregisterListener(this)
                animation_master = 1
            }

            MySharedPrefarance(requireContext()).getSavedAnim() -> {
                animationImg.setImageResource(R.drawable.animation_on)
                sound_master = 0
            }
        }

//        if (BackObject.navBar) {
//            drawerLayout.open()
//            BackObject.navBar = false
//        }
    }

    companion object {
        fun starter() {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        drawerLayout = view.findViewById(R.id.drawer_layout)
        imageView = view.findViewById(R.id.imageView)
        progressBar = view.findViewById(R.id.progress_bar)
        textViewProgress = view.findViewById(R.id.text_view_progress)
        count_salovat_all = view.findViewById(R.id.count_salovat)
        restartBtn = view.findViewById(R.id.restart_btn)
        voiceBtn = view.findViewById(R.id.voice_btn)
        animBtn = view.findViewById(R.id.anim_btn)
        listBtn = view.findViewById(R.id.list_btn)
        settingsBtn = view.findViewById(R.id.settings_btn)
        soundImg = view.findViewById(R.id.sound_img)
        animationImg = view.findViewById(R.id.animation_img)
        salovatlarName = view.findViewById(R.id.salovatlar_name)
        mediaPlayer = MediaPlayer.create(context, R.raw.touch_sound4)
        mediaPlayer2 = MediaPlayer.create(context, R.raw.touch_sound2)
//        navigationView = view.findViewById(R.id.navigation_view)

        zoomAnim = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        alphaAnim = AnimationUtils.loadAnimation(context, R.anim.alpha)

        salovatlarName.animation = zoomAnim


        val width2 = progressBar.layoutParams.width
        progressBar.layoutParams.height = width2

        updateProgressBar()

        salovatlar_master = MySharedPrefarance(requireContext()).getSavedName()

        all_count = MySharedPrefarance(requireContext()).getSavedJami()!!.toInt()

        salovat_bar_count = MySharedPrefarance(requireContext()).getSaved33()!!.toInt()

        val integerDouble: Double =
            MySharedPrefarance(requireContext()).getSavedProgress()!!.toDouble()
        val integerInt: Int = integerDouble.roundToInt()

        progress_count = integerInt.toDouble()

        initHeader(view)

        return view
    }

    private fun initHeader(view: View) {
        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)

        val iconItem1 = headerView.findViewById<View>(R.id.icon_item1) as ShapeableImageView
        val iconItem2 = headerView.findViewById<View>(R.id.icon_item2) as ShapeableImageView
        val iconItem3 = headerView.findViewById<View>(R.id.icon_item3) as ShapeableImageView
        val iconItem4 = headerView.findViewById<View>(R.id.icon_item4) as ShapeableImageView
        val iconItem5 = headerView.findViewById<View>(R.id.icon_item5) as ShapeableImageView

        val selectorItem1 = headerView.findViewById<View>(R.id.selectorImg1) as ShapeableImageView
        val selectorItem2 = headerView.findViewById<View>(R.id.selectorImg2) as ShapeableImageView
        val selectorItem3 = headerView.findViewById<View>(R.id.selectorImg3) as ShapeableImageView
        val selectorItem4 = headerView.findViewById<View>(R.id.selectorImg4) as ShapeableImageView
        val selectorItem5 = headerView.findViewById<View>(R.id.selectorImg5) as ShapeableImageView

        when (MySharedPrefarance(requireContext()).getSavedBackImage()) {

            1 -> {
                iconItem1.strokeWidth = 5.dp.toFloat()
                iconItem2.strokeWidth = 0.dp.toFloat()
                iconItem3.strokeWidth = 0.dp.toFloat()
                iconItem4.strokeWidth = 0.dp.toFloat()
                iconItem5.strokeWidth = 0.dp.toFloat()

                selectorItem1.isVisible = true
                selectorItem2.isVisible = false
                selectorItem3.isVisible = false
                selectorItem4.isVisible = false
                selectorItem5.isVisible = false
            }

            2 -> {
                iconItem1.strokeWidth = 0.dp.toFloat()
                iconItem2.strokeWidth = 5.dp.toFloat()
                iconItem3.strokeWidth = 0.dp.toFloat()
                iconItem4.strokeWidth = 0.dp.toFloat()
                iconItem5.strokeWidth = 0.dp.toFloat()

                selectorItem1.isVisible = false
                selectorItem2.isVisible = true
                selectorItem3.isVisible = false
                selectorItem4.isVisible = false
                selectorItem5.isVisible = false
            }

            3 -> {
                iconItem1.strokeWidth = 0.dp.toFloat()
                iconItem2.strokeWidth = 0.dp.toFloat()
                iconItem3.strokeWidth = 5.dp.toFloat()
                iconItem4.strokeWidth = 0.dp.toFloat()
                iconItem5.strokeWidth = 0.dp.toFloat()

                selectorItem1.isVisible = false
                selectorItem2.isVisible = false
                selectorItem3.isVisible = true
                selectorItem4.isVisible = false
                selectorItem5.isVisible = false
            }

            4 -> {
                iconItem1.strokeWidth = 0.dp.toFloat()
                iconItem2.strokeWidth = 0.dp.toFloat()
                iconItem3.strokeWidth = 0.dp.toFloat()
                iconItem4.strokeWidth = 5.dp.toFloat()
                iconItem5.strokeWidth = 0.dp.toFloat()

                selectorItem1.isVisible = false
                selectorItem2.isVisible = false
                selectorItem3.isVisible = false
                selectorItem4.isVisible = true
                selectorItem5.isVisible = false
            }

            5 -> {
                iconItem1.strokeWidth = 0.dp.toFloat()
                iconItem2.strokeWidth = 0.dp.toFloat()
                iconItem3.strokeWidth = 0.dp.toFloat()
                iconItem4.strokeWidth = 0.dp.toFloat()
                iconItem5.strokeWidth = 5.dp.toFloat()

                selectorItem1.isVisible = false
                selectorItem2.isVisible = false
                selectorItem3.isVisible = false
                selectorItem4.isVisible = false
                selectorItem5.isVisible = true
            }

        }

        iconItem1.setOnClickListener {
            iconItem1.strokeWidth = 5.dp.toFloat()
            iconItem2.strokeWidth = 0.dp.toFloat()
            iconItem3.strokeWidth = 0.dp.toFloat()
            iconItem4.strokeWidth = 0.dp.toFloat()
            iconItem5.strokeWidth = 0.dp.toFloat()

            selectorItem1.isVisible = true
            selectorItem2.isVisible = false
            selectorItem3.isVisible = false
            selectorItem4.isVisible = false
            selectorItem5.isVisible = false

            imageView.setImageResource(R.drawable.splash_image2)
            MySharedPrefarance(requireContext()).isSavedBackImage(1)
        }

        iconItem2.setOnClickListener {
            iconItem1.strokeWidth = 0.dp.toFloat()
            iconItem2.strokeWidth = 5.dp.toFloat()
            iconItem3.strokeWidth = 0.dp.toFloat()
            iconItem4.strokeWidth = 0.dp.toFloat()
            iconItem5.strokeWidth = 0.dp.toFloat()

            selectorItem1.isVisible = false
            selectorItem2.isVisible = true
            selectorItem3.isVisible = false
            selectorItem4.isVisible = false
            selectorItem5.isVisible = false

            imageView.setImageResource(R.drawable.splash_image1)
            MySharedPrefarance(requireContext()).isSavedBackImage(2)
        }

        iconItem3.setOnClickListener {
            iconItem1.strokeWidth = 0.dp.toFloat()
            iconItem2.strokeWidth = 0.dp.toFloat()
            iconItem3.strokeWidth = 5.dp.toFloat()
            iconItem4.strokeWidth = 0.dp.toFloat()
            iconItem5.strokeWidth = 0.dp.toFloat()

            selectorItem1.isVisible = false
            selectorItem2.isVisible = false
            selectorItem3.isVisible = true
            selectorItem4.isVisible = false
            selectorItem5.isVisible = false

            imageView.setImageResource(R.drawable.splash_image3)
            MySharedPrefarance(requireContext()).isSavedBackImage(3)
        }

        iconItem4.setOnClickListener {
            iconItem1.strokeWidth = 0.dp.toFloat()
            iconItem2.strokeWidth = 0.dp.toFloat()
            iconItem3.strokeWidth = 0.dp.toFloat()
            iconItem4.strokeWidth = 5.dp.toFloat()
            iconItem5.strokeWidth = 0.dp.toFloat()

            selectorItem1.isVisible = false
            selectorItem2.isVisible = false
            selectorItem3.isVisible = false
            selectorItem4.isVisible = true
            selectorItem5.isVisible = false

            imageView.setImageResource(R.drawable.splash_image4)
            MySharedPrefarance(requireContext()).isSavedBackImage(4)
        }

        iconItem5.setOnClickListener {
            iconItem1.strokeWidth = 0.dp.toFloat()
            iconItem2.strokeWidth = 0.dp.toFloat()
            iconItem3.strokeWidth = 0.dp.toFloat()
            iconItem4.strokeWidth = 0.dp.toFloat()
            iconItem5.strokeWidth = 5.dp.toFloat()

            selectorItem1.isVisible = false
            selectorItem2.isVisible = false
            selectorItem3.isVisible = false
            selectorItem4.isVisible = false
            selectorItem5.isVisible = true

            imageView.setImageResource(R.drawable.splash_images)
            MySharedPrefarance(requireContext()).isSavedBackImage(5)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()


        progressBar.setOnClickListener {

            if (sound_master == 0) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    mediaPlayer.start()
                } else
                    mediaPlayer.start()
            } else if (sound_master == 1) {

                val vibe: Vibrator =
                    activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibe.vibrate(60)

            }

            all_count++
            count_salovat_all.text = "Жами: $all_count"

            progress_count += 3.0303030303
//            progress_count += 3.05

            updateProgressBar()

            if (salovat_bar_count == 33) {
                salovat_bar_count = 1

                progress_count = 3.05
                updateProgressBar()
                textViewProgress.text = "$salovat_bar_count"

            } else {
                salovat_bar_count++
                textViewProgress.text = salovat_bar_count.toString()

                if (salovat_bar_count == 33) {
                    salovatlar_master++
                    mediaPlayer2.start()
                    val vibe: Vibrator =
                        activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibe.vibrate(500)
                }
            }


            when (salovatlar_master) {

                0 -> {
                    salovatlarName.textSize = 45F
                    salovatlarName.text = "Субҳаналлоҳ"
                    MySharedPrefarance(requireContext()).isSavedName(0)
                }
                1 -> {
                    salovatlarName.text = "Алҳамдулиллаҳ"
                    MySharedPrefarance(requireContext()).isSavedName(1)
                }
                2 -> {
                    salovatlarName.text = "Аллоҳу Акбар"
                    MySharedPrefarance(requireContext()).isSavedName(2)
                }
                3 -> {
                    salovatlarName.text = "Астағфируллоҳ"
                    MySharedPrefarance(requireContext()).isSavedName(3)
                }
                4 -> {
                    salovatlarName.text = "Лаа Илаҳа Илаллаҳ"
                    MySharedPrefarance(requireContext()).isSavedName(4)
                }
                5 -> {
                    salovatlarName.text = "Астағфируллаҳу ва атубу илайҳ"
                    MySharedPrefarance(requireContext()).isSavedName(5)
                }
                6 -> {
                    salovatlarName.text = "Субҳаналлоҳи ва биҳамдиҳир"
                    MySharedPrefarance(requireContext()).isSavedName(6)
                }
                7 -> {
                    salovatlarName.text = "Субҳаналлоҳил азийм"
                    MySharedPrefarance(requireContext()).isSavedName(7)
                }
                8 -> {
                    salovatlarName.text = "Лаа ҳавла ва лаа қуввата иллаа биллааҳ"
                    MySharedPrefarance(requireContext()).isSavedName(8)
                }
                9 -> {
                    salovatlarName.textSize = 35F
                    salovatlarName.text = "Йа муқаллибал қулуб саббит қолбий ъала дийник"
                    MySharedPrefarance(requireContext()).isSavedName(9)
                }
                10 -> {
                    salovatlarName.text = "Ла илаҳа илла анта субҳанака инний кунту миназ золимийн"
                    MySharedPrefarance(requireContext()).isSavedName(10)
                }
                11 -> {
                    salovatlarName.textSize = 30F
                    salovatlarName.text =
                        "Астағфируллоҳаллазий ла илаҳа илла ҳувал ҳаййул қаййум ва атубу илайҳи"
                    MySharedPrefarance(requireContext()).isSavedName(11)
                }
                12 -> {
                    salovatlar_master = 0
                }

            }


            if (salovat_bar_count == 33) {
//                salovatlarName.visibility = View.GONE
//                zoomAnim.start()
            }

        }

        restartBtn.setOnClickListener {
            showAlertDialog()
        }

        settingsBtn.setOnClickListener {
            addFragment<SettingsFragment>()
            getCallback<ExampleCallback>().onSuccess()
//            Navigation.findNavController(requireView())
//                .navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        voiceBtn.setOnClickListener {

            when (sound_master) {

                0 -> {
                    MySharedPrefarance(requireContext()).isSavedVoice(0)
                    soundImg.setImageResource(R.drawable.vibration_img)
                    sound_master = 1
                }
                1 -> {
                    MySharedPrefarance(requireContext()).isSavedVoice(1)
                    soundImg.setImageResource(R.drawable.audio_off)
                    sound_master = 2
                }
                2 -> {
                    MySharedPrefarance(requireContext()).isSavedVoice(2)
                    soundImg.setImageResource(R.drawable.audio_on)
                    sound_master = 0
                }

            }
        }

        animBtn.setOnClickListener {
            if (animation_master == 0) {
                animation_master = 1
                animationImg.setImageResource(R.drawable.animation_off)
                sensorManager.unregisterListener(this)
                MySharedPrefarance(requireContext()).isSavedAnim(false)
            } else {
                animation_master = 0
                animationImg.setImageResource(R.drawable.animation_on)
                sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                MySharedPrefarance(requireContext()).isSavedAnim(true)
            }
        }

        listBtn.setOnClickListener {
            Navigation.findNavController(requireView()).apply {
                navigate(R.id.listFragment)
            }

        }

        when (salovatlar_master) {

            0 -> {
                salovatlarName.textSize = 45F
                salovatlarName.text = "Субҳаналлоҳ"
            }
            1 -> {
                salovatlarName.text = "Алҳамдулиллаҳ"
            }
            2 -> {
                salovatlarName.text = "Аллоҳу Акбар"
            }
            3 -> {
                salovatlarName.text = "Астағфируллоҳ"
            }
            4 -> {
                salovatlarName.text = "Лаа Илаҳа Илаллаҳ"
            }
            5 -> {
                salovatlarName.text = "Астағфируллаҳу ва атубу илайҳ"
            }
            6 -> {
                salovatlarName.text = "Субҳаналлоҳи ва биҳамдиҳир"
            }
            7 -> {
                salovatlarName.text = "Субҳаналлоҳил азийм"
            }
            8 -> {
                salovatlarName.text = "Лаа ҳавла ва лаа қуввата иллаа биллааҳ"
            }
            9 -> {
                salovatlarName.textSize = 35F
                salovatlarName.text = "Йа муқаллибал қулуб саббит қолбий ъала дийник"
            }
            10 -> {
                salovatlarName.text = "Ла илаҳа илла анта субҳанака инний кунту миназ золимийн"
            }
            11 -> {
                salovatlarName.textSize = 30F
                salovatlarName.text =
                    "Астағфируллоҳаллазий ла илаҳа илла ҳувал ҳаййул қаййум ва атубу илайҳи"
            }
            12 -> {
                salovatlar_master = 0
            }

        }

        count_salovat_all.text = "Жами: $all_count"

        textViewProgress.text = "$salovat_bar_count"

        progressBar.progress = progress_count.toInt()

    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

        val view = LayoutInflater.from(context)
            .inflate(R.layout.customview_dialog, view?.findViewById(R.id.layoutDialog))
        builder.setView(view)
        view.findViewById<TextView>(R.id.textTitle).text = getString(R.string.restart)
        view.findViewById<TextView>(R.id.textMessage).text = getString(R.string.message2)
        view.findViewById<Button>(R.id.buttonCancelAction).text = getString(R.string.cancel)
        view.findViewById<Button>(R.id.buttonExitAction).text = getString(R.string.yes)

        val alertDialog: AlertDialog = builder.create()

        view.findViewById<Button>(R.id.buttonCancelAction).setOnClickListener {
            alertDialog.dismiss()
        }

        view.findViewById<Button>(R.id.buttonExitAction).setOnClickListener {
            alertDialog.dismiss()

            count_salovat_all.text = "Жами: 0"
            all_count = 0
            textViewProgress.text = "0"
            progress_count = 0.0
            updateProgressBar()
            progressBar.progress = 0
            salovat_bar_count = 0
            salovatlar_master = 0
            salovatlarName.text = "Субҳаналлоҳ"


        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.setCancelable(true)

        if (textViewProgress.text != "0") {
            alertDialog.show()
        }

    }

    private fun updateProgressBar() {
        if (progressBar.progress >= 100) {
            progressBar.progress = 0
            progress_count = 0.0
        } else {
            progressBar.progress = progress_count.toDouble().toInt()
        }
    }

    private fun metricsIn() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        imageView.layoutParams.width = width + 550
        imageView.layoutParams.height = height + 850
    }

    private fun setUpSensorStuff() {
        sensorManager =
            activity?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this, it,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = p0.values[0]
            val upDown = p0.values[1]

            imageView.apply {

                rotationX = upDown * 1.2f
                rotationY = sides * 1.2f

                translationX = sides * -1
                translationY = upDown * 1

                rotation = -sides
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onOpenedFragment() {

    }

    override fun onReturnedFragment() {
    }

}