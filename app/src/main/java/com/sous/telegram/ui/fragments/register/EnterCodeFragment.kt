package com.sous.telegram.ui.fragments.register

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.sous.telegram.R
import com.sous.telegram.database.*
import com.sous.telegram.utilits.APP_ACTIVITY
import com.sous.telegram.utilits.AppTextWatcher
import com.sous.telegram.utilits.restartActivity
import com.sous.telegram.utilits.showToast
import kotlinx.android.synthetic.main.fragment_enter_code.*


class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        AUTH = FirebaseAuth.getInstance()
        register_input_code.requestFocus();

        //Рефактор для сокращения кода
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(
            id,
            code
        )  //Получаем данные для входа по credential(смс код)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()  //Получаем уникальный ID
                val dataMap = mutableMapOf<String, Any>() //Мапа для хранения данных для БД

                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME] = uid

                //Сохранение данных в БД
                REF_DATABASE_ROOT.child(
                    NODE_PHONES
                ).child(phoneNumber).setValue(uid)
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(
                            NODE_USERS
                        ).child(uid).updateChildren(dataMap)
                            .addOnSuccessListener {
                                showToast("Добро пожаловать")
                                restartActivity()
                            }
                            .addOnFailureListener {
                                showToast(it.message.toString())
                            }
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }

            } else {
                showToast(task.exception?.message.toString())
            }
        }
    }
}
