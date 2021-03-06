package com.flood_android.util

import com.flood_android.ui.bookmarkedit.post.CategoryObejct
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody

object GlobalData {
    // 이동이 어려운 데이터나, 여기저기 쓰이는 데이터를 담을 객체
    lateinit var categoryList:List<String>
    lateinit var selectedCategory: String
    lateinit var categoryDialogFalg: String
    lateinit var loginDialogMessage: String
    val addFlip: MutableList<String> = mutableListOf()
    val deleteFlip: MutableList<String> = mutableListOf()
    lateinit var updateFlipId: String
    lateinit var updateFlipName: String
    val updateFlips: MutableList<MutableList<String>> = mutableListOf()
    lateinit var editFolderName: String
    lateinit var categoryObejct: CategoryObejct
    var bottomSheetDialogFragment : BottomSheetDialogFragment? = null

    var recommentFlag : Boolean = false
    var commentId : String? = null

    lateinit var gCode : String
    var pfImage : MultipartBody.Part?=null
    lateinit var pName : RequestBody
    lateinit var pRank : RequestBody
}