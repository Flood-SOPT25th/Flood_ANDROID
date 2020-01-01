package com.flood_android.ui.signup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.flood_android.R
import kotlinx.android.synthetic.main.activity_signup.*

class GroupcodeMismatchDialog(context : Context, private val gokListener : View.OnClickListener?) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    private val clickedState = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.55f
        window!!.attributes = lpWindow

        setContentView(R.layout.dialog_signup_groupcode_inconsistency)

        if(gokListener != null){
            btn_signup_next.setOnClickListener(gokListener)
        }
    }

}