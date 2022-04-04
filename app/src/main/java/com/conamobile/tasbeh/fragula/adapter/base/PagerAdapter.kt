package com.conamobile.tasbeh.fragula.adapter.base

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup

abstract class PagerAdapter {
    private val mObservable = DataSetObservable()
    private var mViewPagerObserver: DataSetObserver? = null

    abstract val count: Int

    open fun startUpdate(container: ViewGroup) {
        startUpdate(container as View)
    }

    open fun instantiateItem(container: ViewGroup, position: Int): Any {
        return instantiateItem(container as View, position)
    }

    open fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        destroyItem(container as View, position, `object`)
    }

    open fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        setPrimaryItem(container as View, position, `object`)
    }

    open fun finishUpdate(container: ViewGroup) {
        finishUpdate(container as View)
    }

    @Deprecated("Use {@link #startUpdate(ViewGroup)}")
    fun startUpdate(container: View) {
    }

    @Deprecated("Use {@link #instantiateItem(ViewGroup, int)}")
    fun instantiateItem(container: View, position: Int): Any {
        throw UnsupportedOperationException(
                "Required method instantiateItem was not overridden")
    }

    @Deprecated("Use {@link #destroyItem(ViewGroup, int, Object)}")
    fun destroyItem(container: View, position: Int, `object`: Any) {
        throw UnsupportedOperationException("Required method destroyItem was not overridden")
    }

    @Deprecated("Use {@link #setPrimaryItem(ViewGroup, int, Object)}")
    fun setPrimaryItem(container: View, position: Int, `object`: Any) {
    }

    @Deprecated("Use {@link #finishUpdate(ViewGroup)}")
    fun finishUpdate(container: View) {
    }

    abstract fun isViewFromObject(view: View, `object`: Any): Boolean

    open fun saveState(): Parcelable? {
        return null
    }

    open fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    open fun getItemPosition(`object`: Any): Int {
        return POSITION_UNCHANGED
    }

    fun notifyDataSetChanged() {
        synchronized(this) {
            if (mViewPagerObserver != null) {
                mViewPagerObserver!!.onChanged()
            }
        }
        mObservable.notifyChanged()
    }

    fun registerDataSetObserver(observer: DataSetObserver) {
        mObservable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        mObservable.unregisterObserver(observer)
    }

    fun setViewPagerObserver(observer: DataSetObserver?) {
        synchronized(this) {
            mViewPagerObserver = observer
        }
    }

    fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    fun getPageWidth(position: Int): Float {
        return 1f
    }

    companion object {
        const val POSITION_UNCHANGED = -1
        const val POSITION_NONE = -2
    }
}

