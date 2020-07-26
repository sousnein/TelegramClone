package com.sous.telegram.ui.fragments

import androidx.fragment.app.Fragment
import com.sous.telegram.R
import com.sous.telegram.utilits.APP_ACTIVITY
import com.sous.telegram.utilits.hideKeyboard

class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Чаты"
        APP_ACTIVITY.mAppDrawer.enableDrawer() //Включаем Drawer,при нажатии кнопки назад
        hideKeyboard()
    }
}