package com.conamobile.tasbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition


class ListFragment : Fragment() {

    lateinit var sliderInterface: SlidrInterface
    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        textView = view.findViewById(R.id.textView)

        textView.setOnClickListener {
            view.findNavController().popBackStack()
        }
//        Navigation.findNavController(view).popBackStack()

        sliderInterface = Slidr.attach(requireActivity())

        return view
    }

    override fun onResume() {
        super.onResume()


        Slidr.attach(requireActivity())
//
//        if (sliderInterface == null)
//            sliderInterface = Slidr.replace(
//            requireView().findViewById(R.id.home_fragment),
//                SlidrConfig.Builder().position(SlidrPosition.LEFT).build()
//        )

    }


}