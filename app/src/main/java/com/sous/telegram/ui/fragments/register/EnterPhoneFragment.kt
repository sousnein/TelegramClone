package com.sous.telegram.ui.fragments.register

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.sous.telegram.R
import com.sous.telegram.database.AUTH
import com.sous.telegram.utilits.*
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit


class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        AUTH = FirebaseAuth.getInstance()
        showKeyboard()
        register_input_phone_number.requestFocus();
        //Вход по номеру телефона
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Добро пожаловать")
                        restartActivity()
                    } else {
                        showToast(it.exception?.message.toString())
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(message = p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(
                    EnterCodeFragment(
                        phoneNumber = mPhoneNumber,
                        id = id
                    )
                )
            }
        }

        register_btn_next.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
            replaceFragment(
                EnterCodeFragment(
                    mPhoneNumber,
                    id.toString()
                )
            )
        }
    }

    private fun authUser() {
        mPhoneNumber = register_input_phone_number.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber, 60, TimeUnit.SECONDS, APP_ACTIVITY, mCallback
        )

    }
}