package com.sous.telegram.utilits

import com.sous.telegram.database.*

enum class AppStates(val state: String) {
    ONLINE("В сети"),
    OFFLINE("Был недавно"),
    TYPING("Печатает");

    companion object {
        fun updateState(appStates: AppStates) {
            if (AUTH.currentUser!=null) {
                REF_DATABASE_ROOT.child(
                    NODE_USERS
                ).child(CURRENT_UID).child(
                    CHILD_STATE
                )
                    .setValue(appStates.state)          //Обновляем статус
                    .addOnSuccessListener {
                        USER.state = appStates.state        //Показываем изменения пользователю
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
        }
    }

}