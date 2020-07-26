import com.sous.telegram.R
import com.sous.telegram.database.*
import com.sous.telegram.ui.fragments.BaseChangeFragment
import com.sous.telegram.utilits.AppValueEventListener
import com.sous.telegram.utilits.showToast
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    private lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)

    }


    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast(getString(R.string.toast_unpu_username_is_empty))
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast(getString(R.string.toast_username_already_exist))
                    } else {
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(
            CURRENT_UID
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(newUsername = mNewUsername)
                }
            }
    }


}

