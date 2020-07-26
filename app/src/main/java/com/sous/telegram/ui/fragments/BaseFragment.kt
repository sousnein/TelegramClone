package com.sous.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sous.telegram.utilits.APP_ACTIVITY
import com.sous.telegram.utilits.hideKeyboard


open class BaseFragment(private val layout: Int) : Fragment(layout) {
    private lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layout, container, false)
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer() //Выключаем Drawer,когда мы в одном из фрагментов Drawer
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}