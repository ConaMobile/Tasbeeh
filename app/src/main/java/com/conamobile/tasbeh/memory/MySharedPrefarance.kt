package com.conamobile.tasbeh.memory

import android.content.Context
import androidx.core.content.edit
import java.util.function.DoubleToIntFunction
import java.util.function.LongToDoubleFunction

class MySharedPrefarance(context: Context) {
    private val pref = context.getSharedPreferences("nimadir", Context.MODE_PRIVATE)

    //qisqa yo'li

//    var isSaved:Boolean
//        get() = pref.getBoolean("isSaved",false)
//        set(value) = pref.edit { this.putBoolean("isSaved",value) }
//

    //uzun yo'li

    fun isSavedVoice(isSavedVoice: Int){
        val editor = pref.edit()
        editor.putInt("isSavedVoice", isSavedVoice)
        editor.apply()
    }

    fun getSavedVoice():Int{
        return pref.getInt("isSavedVoice",0)
    }

////////////////////////////

        fun isSavedAnim(isSavedAnim: Boolean){
        val editor = pref.edit()
        editor.putBoolean("isSavedAnim", isSavedAnim)
        editor.apply()
    }

    fun getSavedAnim():Boolean{
        return pref.getBoolean("isSavedAnim", false)
    }

    ///////////////////////////////

    fun isSavedName(isSavedName: Int){
        val editor = pref.edit()
        editor.putInt("isSavedName", isSavedName)
        editor.apply()
    }

    fun getSavedName():Int{
        return pref.getInt("isSavedName",0)
    }

    ///////////////////////////

    fun isSavedJami(isSavedJami: Int){
        val editor = pref.edit()
        editor.putInt("isSavedJami", isSavedJami)
        editor.apply()
    }

    fun getSavedJami():Int?{
        return pref.getInt("isSavedJami",0)
    }

    /////////////////////////////

    fun isSaved33(isSaved33: Int){
        val editor = pref.edit()
        editor.putInt("isSaved33", isSaved33)
        editor.apply()
    }

    fun getSaved33():Int?{
        return pref.getInt("isSaved33",0)
    }

    /////////////////////////////

    fun isSavedProgress(isSavedProgress: String){
        val editor = pref.edit()
        editor.putString("isSavedProgress", isSavedProgress)
        editor.apply()
    }

    fun getSavedProgress(): String? {
        return pref.getString("isSavedProgress", "0")
    }

    /////////////////////////////

    fun isSavedBackImage(isSavedBackImage: Int){
        val editor = pref.edit()
        editor.putInt("isSavedBackImage", isSavedBackImage)
        editor.apply()
    }

    fun getSavedBackImage():Int{
        return pref.getInt("isSavedBackImage",1)
    }


}