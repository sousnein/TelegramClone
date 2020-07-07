package com.sous.telegram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sous.telegram.MainActivity


open class BaseFragment(private val layout: Int) : Fragment() {
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
        (activity as MainActivity).mAppDrawer.disableDrawer() //Выключаем Drawer,когда мы в одном из фрагментов Drawer
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).mAppDrawer.enableDrawer() //Включаем Drawer,при нажатии кнопки назад
    }
}