package com.sous.telegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.sous.telegram.R
import com.sous.telegram.databinding.ActivityRegisterBinding
import com.sous.telegram.ui.fragments.EnterPhoneFragment
import com.sous.telegram.utilits.initFirebase
import com.sous.telegram.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(fragment = EnterPhoneFragment(),addStack = false)
    }
}