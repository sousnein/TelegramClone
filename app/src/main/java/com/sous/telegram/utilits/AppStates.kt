package com.sous.telegram.utilits

enum class AppStates(val state: String) {
    ONLINE("В сети"),
    OFFLINE("Был недавно"),
    TYPING("Печатает");

    companion object {
        fun updateState(appStates: AppStates) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
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