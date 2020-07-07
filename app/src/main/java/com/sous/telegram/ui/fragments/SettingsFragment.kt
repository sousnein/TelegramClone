package com.sous.telegram.ui.fragments

import android.view.*
import com.sous.telegram.R
import com.sous.telegram.MainActivity
import com.sous.telegram.activities.RegisterActivity
import com.sous.telegram.utilits.AUTH
import com.sous.telegram.utilits.USER
import com.sous.telegram.utilits.replaceActivity
import com.sous.telegram.utilits.replaceFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_full_name.text = USER.fullname
        settings_phone_number.text = USER.phone
        settings_bio.text = USER.bio
        settings_username.text = USER.username
        settings_status.text = USER.status

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_actions_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_change_name->{
                replaceFragment(ChangeNameFragment())
            }
            R.id.settings_menu_exit->{
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
        }
        return true
    }
}