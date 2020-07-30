package com.sous.telegram.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sous.telegram.R
import com.sous.telegram.models.CommonModel
import com.sous.telegram.models.UserModel
import com.sous.telegram.utilits.APP_ACTIVITY
import com.sous.telegram.utilits.AppValueEventListener
import com.sous.telegram.utilits.TYPE_MESSAGE_IMAGE
import com.sous.telegram.utilits.showToast
import java.util.*

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: UserModel
lateinit var REF_STORAGE_ROOT: StorageReference

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"
const val NODE_MESSAGES = "messages"
const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_MESSAGE_IMAGE = "message_image"

const val TYPE_TEXT = "text"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"
const val CHILD_IMAGE_URL = "imageUrl"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    )
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    )  //Лезет в БД,чтобы скачать данные
        //Конкретно этот слушатель проверяет БД единожды(при запуске)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(UserModel::class.java)
                ?: UserModel()  //null гарантированно не будет,тк поля User инициализированы в конструкторе,но мало ли
            if (USER.username.isEmpty()) {
                USER.username =
                    CURRENT_UID
            }
            function()
        })
}


//Добавляет номер телефона с id в БД
fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(
            AppValueEventListener { dataSnapshot ->
                dataSnapshot.children.forEach { snapshot ->
                    arrayContacts.forEach { contact ->
                        if (snapshot.key == contact.phone) {
                            if (snapshot.key == contact.phone) {
                                REF_DATABASE_ROOT.child(
                                    NODE_PHONES_CONTACTS
                                ).child(CURRENT_UID)
                                    .child(snapshot.value.toString())
                                    .child(CHILD_ID)
                                    .setValue(snapshot.value.toString())
                                    .addOnFailureListener {
                                        showToast(
                                            it.message.toString()
                                        )
                                    }

                                REF_DATABASE_ROOT.child(
                                    NODE_PHONES_CONTACTS
                                ).child(CURRENT_UID)
                                    .child(snapshot.value.toString())
                                    .child(CHILD_FULLNAME)
                                    .setValue(contact.fullname)
                                    .addOnFailureListener {
                                        showToast(
                                            it.message.toString()
                                        )
                                    }
                            }
                        }
                    }
                }

            })
    }
}

//Преобразует данные из БД в модель CommonModel
fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = TYPE_TEXT
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener {
            function()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun updateCurrentUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    ).child(CHILD_USERNAME)
        .setValue(newUsername)
        .addOnSuccessListener {
            deleteOldUsername(newUsername)
            showToast(APP_ACTIVITY.getString(R.string.toast_data_updated))
        }
        .addOnFailureListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_error))
        }
}

fun deleteOldUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(
        USER.username
    ).removeValue()
        .addOnSuccessListener {
            USER.username = newUsername
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    ).child(CHILD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_updated))
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(
        CURRENT_UID
    ).child(CHILD_FULLNAME)
        .setValue(fullname).addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_updated))
            USER.fullname = fullname
            APP_ACTIVITY.mAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
}

fun sendMessageAsImage(receivingUserID: String, imageUrl: String, messageKey: String) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = TYPE_MESSAGE_IMAGE
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_IMAGE_URL] = imageUrl

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}
