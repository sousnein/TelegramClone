package com.sous.telegram.utilits

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACTS = android.Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200

    //Если android >= 23 и разрешение ещё не дано, то открывается окно и инициализируются контакты
    //Иначе
    //Всё разрешено и без запроса
fun checkPermissions(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true

}