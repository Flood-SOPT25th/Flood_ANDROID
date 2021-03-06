package com.flood_android.ui.companydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.flood_android.R
import com.flood_android.network.ApplicationController
import com.flood_android.util.SharedPreferenceController
import com.flood_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_company_detail.*
import kotlinx.android.synthetic.main.fragment_company_detail_category.*

class CompanyDetailActivity : AppCompatActivity() {

        private var feedFragments = listOf<Fragment>()
        set(value) {
            if (value.isEmpty()) return
            active = null
            value.forEach {
                addFragment(it)
            }
            showFragment(value.first())
            field = value
        }

    private lateinit var companyDetailCategoryRVAdapter: CompanyDetailCategoryRVAdapter
    private var active: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_detail)

        var groupCode = intent.getStringExtra("code")
        // GET company 1차 통신
        getCompanyDetail(groupCode.toString())

        iv_company_detail_back.setOnClickListener {
            finish()
        }

    }

    private fun initView(dataList: List<String>) {
        companyDetailCategoryRVAdapter = CompanyDetailCategoryRVAdapter(this@CompanyDetailActivity, dataList) { position ->
            showFragment(feedFragments[position])
        }

        rv_company_detail_category_list.apply {
            adapter = this@CompanyDetailActivity.companyDetailCategoryRVAdapter
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // GET company 1차 통신
    private fun getCompanyDetail(groupCode :String) {
        val getCompanyDetailResponse = ApplicationController.networkServiceCompany
            .getCompanyDetailResponse(SharedPreferenceController.getAuthorization(this@CompanyDetailActivity).toString(),
                groupCode)
        getCompanyDetailResponse.safeEnqueue {
            if (it.message == "그룹에 대한 정보") {
                // 기업 아이콘과 기업명 뷰
                setTopView(it.data.groupInfo)
                // 카테고리 리싸이클러뷰
                setCategoryRecyclerView(it.data.groupInfo.category)
                successGetCategory(groupCode, it.data.groupInfo)
                initView(it.data.groupInfo.category)
            }
        }
    }

    /**
      기업 아이콘과 기업명 뷰
     */
    private fun setTopView(data: GroupInfo) {
        Glide.with(this)
            .load(data.groupIcon)
            .transform(CenterCrop(), CircleCrop())
            .into(iv_company_detail_company_logo)
        tv_company_detail_company_name.text = data.name
    }

    /**
     카테고리 리싸이클러뷰
     */
    private fun setCategoryRecyclerView(dataList: List<String>) {
        companyDetailCategoryRVAdapter = CompanyDetailCategoryRVAdapter(this@CompanyDetailActivity, dataList)
        rv_company_detail_category_list.adapter = companyDetailCategoryRVAdapter
        rv_company_detail_category_list.layoutManager = LinearLayoutManager(this@CompanyDetailActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     *  피드 카테고리 서버 통신
     */
    private fun successGetCategory(groupCode: String, data: GroupInfo){
        feedFragments = data.category.filterNot { it == "Flood" }.map { CompanyDetailCategoryFragment(groupCode, it) }
        companyDetailCategoryRVAdapter.dataList = data.category
        companyDetailCategoryRVAdapter.notifyDataSetChanged()
    }

    fun addFragment(fragment: Fragment){
        supportFragmentManager?.let { fm ->
            val transaction = fm.beginTransaction()
            transaction.add(R.id.fl_company_detail_frag, fragment).hide(fragment)
            transaction.commit()
        }
    }

    fun showFragment(fragment: Fragment){
        supportFragmentManager?.let { fm ->
            if (active == null) active = fragment

            val transaction = fm.beginTransaction()
            transaction.hide(active!!).show(fragment)
            transaction.commit()
            active = fragment
        }
    }
    fun calculateTime(postTimeDate: String): String {

        var dateList: List<String> = postTimeDate.split("T")
        var date: String = dateList[0]
        var timeList: List<String> = dateList[1].split(".")
        var time: String = timeList[0]

        var formattedServerTime: String = date.plus(" ").plus(time)

        return formattedServerTime}
}
