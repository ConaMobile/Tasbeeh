package com.conamobile.tasbeh.fragula.adapter.base


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.conamobile.tasbeh.fragula.extensions.log
import com.conamobile.tasbeh.fragula.extensions.simpleName
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

internal abstract class FragmentStatePagerAdapter

@JvmOverloads constructor(private val mFragmentManager: FragmentManager,
                          @param:Behavior private val mBehavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT
) : PagerAdapter() {
    private var mCurTransaction: FragmentTransaction? = null

    private val mSavedState = ArrayList<Fragment.SavedState?>()
    private val mFragments = ArrayList<Fragment?>()
    private var mCurrentPrimaryItem: Fragment? = null

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
        BEHAVIOR_SET_USER_VISIBLE_HINT,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )
    private annotation class Behavior

    abstract fun getItem(position: Int): Fragment

    override fun startUpdate(container: ViewGroup) {
        check(container.id != View.NO_ID) {
            ("ViewPager with adapter " + this
                    + " requires a view id")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        if (mFragments.size > position) {
            val f = mFragments[position]
            if (f != null) {
                return f
            }
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }

        val fragment = getItem(position)
        if (DEBUG) Log.v(
            TAG, "Adding item #$position: f=$fragment")
        if (mSavedState.size > position) {
            val fss = mSavedState[position]
            if (fss != null) {
                fragment.setInitialSavedState(fss)
            }
        }
        while (mFragments.size <= position) {
            mFragments.add(null)
        }
        fragment.setMenuVisibility(false)
        if (mBehavior == BEHAVIOR_SET_USER_VISIBLE_HINT) {
            fragment.userVisibleHint = false
        }

        mFragments[position] = fragment
        mCurTransaction!!.add(container.id, fragment, position.toString())

        if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            mCurTransaction!!.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        }

        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment = `object` as Fragment

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }

        mFragments.set(position, null)

        mCurTransaction!!.remove(fragment)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment = `object` as Fragment
        if (fragment !== mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem!!.setMenuVisibility(false)
                if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    if (mCurTransaction == null) {
                        mCurTransaction = mFragmentManager.beginTransaction()
                    }
                    mCurTransaction!!.setMaxLifecycle(mCurrentPrimaryItem!!, Lifecycle.State.STARTED)
                } else {
                    mCurrentPrimaryItem!!.userVisibleHint = false
                }
            }
            fragment.setMenuVisibility(true)
            if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                if (mCurTransaction == null) {
                    mCurTransaction = mFragmentManager.beginTransaction()
                }
                mCurTransaction!!.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            } else {
                fragment.userVisibleHint = true
            }

            mCurrentPrimaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        if (mCurTransaction != null) {
            mCurTransaction!!.commitNowAllowingStateLoss()
            mCurTransaction = null
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (`object` as Fragment).view === view
    }

    override fun saveState(): Parcelable? {
        var state: Bundle? = null
        if (mSavedState.size > 0) {
            state = Bundle()
            val fss = arrayOfNulls<Fragment.SavedState?>(mSavedState.size)
            mSavedState.toTypedArray<Fragment.SavedState?>()
            state.putParcelableArray("states", fss)
        }
        for (i in mFragments.indices) {
            val f = mFragments[i]
            if (f != null && f.isAdded) {
                if (state == null) {
                    state = Bundle()
                }
                val key = "f$i"
                mFragmentManager.putFragment(state, key, f)
            }
        }
        return state
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        log("RESTORED_FRAGMENTS___: state ${state} ")
        if (state != null) {
            val bundle = state as Bundle?
            bundle!!.classLoader = loader
            val fss = bundle.getParcelableArray("states")
            mSavedState.clear()
            mFragments.clear()
            if (fss != null) {
                for (i in fss.indices) {
                    mSavedState.add(fss[i] as Fragment.SavedState)
                }
            }
            val keys = bundle.keySet()
            log("RESTORED_FRAGMENTS___: keys ${keys} ")
            for (key in keys) {
                if (key.startsWith("f")) {
                    val index = Integer.parseInt(key.substring(1))
                    val f = mFragmentManager.getFragment(bundle, key)
                    log("RESTORED_FRAGMENTS___: ROOT ${f?.simpleName} ")
                    if (f != null) {
                        while (mFragments.size <= index) {
                            mFragments.add(null)
                        }
                        f.setMenuVisibility(false)
                        mFragments[index] = f
                    } else {
                        Log.w(TAG, "Bad fragment at key $key")
                    }
                }
            }
            onRestoredFragments(mFragments)
        }
    }

    open fun onRestoredFragments(fragments: ArrayList<Fragment?>?) { }

    companion object {
        private val TAG = "FragmentStatePagerAdapt"
        private val DEBUG = false


        @Deprecated("This behavior relies on the deprecated\n" +
                "      {@link Fragment#setUserVisibleHint(boolean)} API. Use\n" +
                "      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT} to switch to its replacement,\n" +
                "      {@link FragmentTransaction#setMaxLifecycle}.\n" +
                "      ")
        const val BEHAVIOR_SET_USER_VISIBLE_HINT = 0

        const val BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1
    }
}