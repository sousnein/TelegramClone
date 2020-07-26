package com.sous.telegram.ui.fragments

import ChangeUsernameFragment
import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.sous.telegram.R
import com.sous.telegram.database.*
import com.sous.telegram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Настройки"
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_full_name.text = USER.fullname
        settings_phone_number.text = USER.phone
        settings_bio.text = USER.bio
        settings_username.text = USER.username
        settings_status.text = USER.state
        settings_btn_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_change_photo.setOnClickListener { changePhotoUser() }
        settings_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)        // Кропер будет пропорционален 1 к 1
            .setRequestedSize(600, 600)      //Картинка размером 600*600
            .setCropShape(CropImageView.CropShape.OVAL)        //Овальная картинка
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri         //Получаем uri
            val path = REF_STORAGE_ROOT.child(
                FOLDER_PROFILE_IMAGE
            )         //Путь
                .child(CURRENT_UID)

            putImageToStorage(
                uri,
                path
            ) {          //Загружаем картинку
                getUrlFromStorage(path) {           //Получение URL картинки
                    putUrlToDatabase(it) {          //Записываем URL в БД
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_updated))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_actions_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
        }
        return true
    }
}