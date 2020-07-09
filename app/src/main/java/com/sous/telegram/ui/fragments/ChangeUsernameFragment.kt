import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.sous.telegram.MainActivity
import com.sous.telegram.R
import com.sous.telegram.ui.fragments.BaseFragment
import com.sous.telegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername:String

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        settings_input_username.setText(USER.username)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change->change()
        }
        return true
    }

    private fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if(mNewUsername.isEmpty()){
            showToast(getString(R.string.toast_unpu_username_is_empty))
        }else{
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast(getString(R.string.toast_username_already_exist))
                    }else{
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    updateCurrentUsername()
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    USER.username = mNewUsername
                    fragmentManager?.popBackStack()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    deleteOldUsername()
                    showToast(getString(R.string.toast_data_updated))
                }else{
                    showToast(getString(R.string.toast_error))
                }
            }
    }
}

