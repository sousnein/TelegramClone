package com.sous.telegram.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.sous.telegram.R
import com.sous.telegram.activities.MainActivity
import com.sous.telegram.activities.RegisterActivity
import com.sous.telegram.databinding.FragmentChatsBinding
import com.sous.telegram.databinding.FragmentSettingsBinding
import com.sous.telegram.utilits.AUTH
import com.sous.telegram.utilits.replaceActivity

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_actions_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit->{
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
        }
        return true
    }
}