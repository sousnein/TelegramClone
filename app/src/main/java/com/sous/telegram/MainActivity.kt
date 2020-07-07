package com.sous.telegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.sous.telegram.activities.RegisterActivity
import com.sous.telegram.databinding.ActivityMainBinding
import com.sous.telegram.models.User
import com.sous.telegram.ui.fragments.ChatsFragment
import com.sous.telegram.ui.objects.AppDrawer
import com.sous.telegram.utilits.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
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

        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }

    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(mainActivity = this, toolbar = mToolbar)
        initFirebase()
        initUser()
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)  //Лезет в БД,чтобы скачать данные
            //Конкретно этот слушатель проверяет БД единожды(при запуске)
            .addListenerForSingleValueEvent(AppValueEventListener {

                USER = it.getValue(User::class.java)?: User()  //null гарантированно не будет,тк поля User инициализированы в конструкторе,но мало ли
            })
    }

}