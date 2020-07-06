package com.sous.telegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.sous.telegram.databinding.ActivityMainBinding
import com.sous.telegram.ui.fragments.ChatsFragment
import com.sous.telegram.ui.objects.AppDrawer
import com.sous.telegram.utilits.AUTH
import com.sous.telegram.utilits.initFirebase
import com.sous.telegram.utilits.replaceActivity
import com.sous.telegram.utilits.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AUTH = FirebaseAuth.getInstance()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {

        if (AUTH.currentUser!=null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(),false)
        } else {
            replaceActivity(RegisterActivity())
        }

    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(mainActivity = this, toolbar = mToolbar)
        initFirebase()
    }

}