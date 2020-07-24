package com.sous.telegram.ui.fragments

import android.view.View
import com.google.firebase.database.DatabaseReference
import com.sous.telegram.R
import com.sous.telegram.models.CommonModel
import com.sous.telegram.models.UserModel
import com.sous.telegram.utilits.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.*


class SingleChatFragment(private val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar:AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToobarInfo:View
    private lateinit var mRefUser:DatabaseReference

    override fun onResume() {
        super.onResume()
        mToobarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToobarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)

    }

    private fun initInfoToolbar() {
        mToobarInfo.toolbar_chat_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToobarInfo.toolbar_chat_fullname.text = mReceivingUser.fullname
        mToobarInfo.toolbar_chat_status.text = mReceivingUser.state

    }

    override fun onPause() {
        super.onPause()
        mToobarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }
}