package com.flood_android.ui.bookmarkedit

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.flood_android.R
import com.flood_android.util.GlobalData
import com.flood_android.util.toast
import kotlinx.android.synthetic.main.fragment_bookmark_edit_name_dialog.*

class BookmarkEditNameDialog : DialogFragment() {

    var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        position = arguments!!.getInt("position")
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_bookmark_edit_name_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        edt_bookmark_name_dialog.setText(GlobalData.editFolderName)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_bookmark_name_dialog_cancel.setOnClickListener {
            dismiss()
        }
        btn_bookmark_name_dialog_ok.setOnClickListener {
            if (edt_bookmark_name_dialog.text.length == 0){
                toast("카테고리 명이 입력되지 않았습니다")
            } else{
                GlobalData.updateFlipName = edt_bookmark_name_dialog.text.toString()

                GlobalData.updateFlips.add(mutableListOf(GlobalData.updateFlipId, GlobalData.updateFlipName))
                (context as BookmarkEditActivity).setUpdateFolderName(position, GlobalData.updateFlipName)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        edt_bookmark_name_dialog.setText("")
    }
}
