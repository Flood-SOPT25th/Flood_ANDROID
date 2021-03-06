package com.flood_android.ui.firstlogin


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.flood_android.R
import kotlinx.android.synthetic.main.fragment_first_login_withgroupcode1.*

class SigninOrgFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_login_withgroupcode1, container, false)
    }

    override fun onStart() {
        super.onStart()
        cstlay_first_login_withgroupcode_3.setOnClickListener {
            var intent = Intent(context, GroupCreationActivity::class.java)
            startActivity(intent)
        }
        edtxt_first_login_withgroupcode.addTextChangedListener(txtWatcher)
    }

    fun toSignal(flag : Boolean){
        (activity as SigninOrgActivity).activateNextBtn(flag)
    }

    private val txtWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if((s?:"").isNotEmpty()){
                toSignal(true)
                toSignin1()
            }
        }
    }

    fun toSignin1() {
        (activity as SigninOrgActivity).groupcode = edtxt_first_login_withgroupcode.text.toString()
    }
}
