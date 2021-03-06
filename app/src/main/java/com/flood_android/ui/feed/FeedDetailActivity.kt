package com.flood_android.ui.feed

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flood_android.R
import com.flood_android.network.ApplicationController
import com.flood_android.network.NetworkServiceFeed
import com.flood_android.ui.feed.adapter.FeedDetailCommentRVAdapter
import com.flood_android.ui.feed.data.CommentsData
import com.flood_android.ui.feed.data.GetFeedDetailResponse
import com.flood_android.ui.feed.data.PostCommentData
import com.flood_android.util.GlobalData
import com.flood_android.util.OnSingleClickListener
import com.flood_android.util.SharedPreferenceController
import com.flood_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_feed_detail.*
import kotlinx.android.synthetic.main.fragment_feed_flood.*


class FeedDetailActivity : AppCompatActivity() {

    var feed_idx : String = ""
    var commentListSize : Int = -1

    lateinit var commentDataList : ArrayList<CommentsData>

    var imgList = ArrayList<String>()
//    private val flipsSaveDialog by lazy{
//        FeedFlipsSaveDialog()
//    }

    lateinit var feedDetailCommentRVAdapter: FeedDetailCommentRVAdapter
    private val networkServiceFeed: NetworkServiceFeed by lazy {
        ApplicationController.networkServiceFeed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)

        initView()
    }

    private fun initView() {
        val feedIdx: String = intent.getStringExtra("feed_id")
        feed_idx = feedIdx
        getFeedDetailResponse(feedIdx)
        setOnClickListener(feedIdx)
        activateComment()
    }

    /**
     * 게시물 조회 서버 통신 성공 함수
     */
    private val onGetSuccess: (GetFeedDetailResponse) -> Unit = { response ->

        response.data.pidArr.let {
            tv_feed_detail_flips_cnt.text = it.bookmark_cnt.toString()
            tv_feed_detail_comments_cnt.text = it.comment_cnt.toString()
            tv_feed_detail_time.text = calculateTime(it.time)
            tv_feed_detail_category.text = it.category
            tv_feed_detail_user_contents.text = it.post_content

            imgList = it.postImages

            // 사진이 있을 때 사진 나타내기
            var pic_num: Int? = it.postImages.size

            if (pic_num == 0) {
                imgList = it.postImages
            }

            when (pic_num!!) {
                0 -> setGone(cv_feed_detail_img)
                1 -> {
                    setVisible(iv_feed_detail_pic_1)
                    setInvisible(ll_feed_detail_pic_2)
                    setInvisible(cl_feed_detail_pic_3)

                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[0])
                        .centerCrop()
                        .into(iv_feed_detail_pic_1)
                }
                2 -> {
                    setInvisible(iv_feed_detail_pic_1)
                    setVisible(ll_feed_detail_pic_2)
                    setInvisible(cl_feed_detail_pic_3)

                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[0])
                        .centerCrop()
                        .into(iv_feed_detail_pic_2_1)
                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[1])
                        .centerCrop()
                        .into(iv_feed_detail_pic_2_2)
                }
                3 -> {
                    setInvisible(iv_feed_detail_pic_1)
                    setInvisible(ll_feed_detail_pic_2)
                    setVisible(cl_feed_detail_pic_3)

                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[0])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_1)
                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[1])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_2)
                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[2])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_3)
                }
                //4개 이상
                else -> {
                    var etc_num = pic_num - 3
                    tv_feed_detail_pic_3_3_etc.text = "+" + etc_num.toString()
                    setVisible(cl_feed_detail_pic_3_3_black)
                    setVisible(tv_feed_detail_pic_3_3_etc)

                    setInvisible(iv_feed_detail_pic_1)
                    setInvisible(ll_feed_detail_pic_2)
                    setVisible(cl_feed_detail_pic_3)

                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[0])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_1)
                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[1])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_2)
                    Glide.with(this@FeedDetailActivity)
                        .load(it.postImages[2])
                        .centerCrop()
                        .into(iv_feed_detail_pic_3_3)
                }
            }

            if (it.news_url == "" || it.news_url == null) {
                setGone(cl_feed_detail_news)
            } else {
                Glide.with(this@FeedDetailActivity)
                    .load(it.news_img)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(iv_feed_detail_news_img)
                tv_feed_detail_news_title.text = it.news_title

                if (it.news_content == "") {
                    setGone(tv_feed_detail_news_contents)
                } else {
                    tv_feed_detail_news_contents.text = it.news_content
                }
                tv_feed_detail_user_name.text = it.writer
            }

            Glide.with(this@FeedDetailActivity)
                .load(it.post_user_img)
                .transform(CenterCrop(), CircleCrop())
                .into(iv_feed_detail_user_img)

            iv_feed_detail_flips.isSelected = it.bookmarked

            cl_feed_detail_news.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(v: View) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.news_url))
                    this@FeedDetailActivity.startActivity(intent)
                }
            })
            if (it.comments != null) {
                commentDataList = it.comments
                commentListSize = it.comments.size
                setCommentRecyclerView()
            }
        }
    }

    /**
     * 게시물 상세 서버 통신
     */
    private fun getFeedDetailResponse(feed_idx: String) {
        var token = SharedPreferenceController.getAuthorization(this@FeedDetailActivity)!!
        networkServiceFeed.getFeedDetailResponse(token, feed_idx).safeEnqueue({}, onGetSuccess)
    }

    private fun setOnClickListener(feed_idx: String) {

        // 댓글 업로드 버튼이 활성화되어있을 때만 업로드하기
        btn_feed_detail_upload_comment.setOnClickListener(object  : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (btn_feed_detail_upload_comment.isSelected){
                    postComment(feed_idx, edt_feed_detail_comment_upload.text.toString())
                    edt_feed_detail_comment_upload.setText("")
                    nsv_feed_detail.fullScroll(NestedScrollView.FOCUS_DOWN)
                }
                else
                    Toast.makeText(
                        this@FeedDetailActivity,
                        "글자를 입력해주세요!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        })

        // 댓글창 클릭하면 키보드에 포커스
        cl_feed_detail_comment_upload.setOnClickListener {
            (object : OnSingleClickListener() {
                override fun onSingleClick(v: View) {
                    focusKeyboard()
                }
            })
        }
        // editText 클릭하면 키보드에 포커스
        cl_feed_detail_comment_upload.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                focusKeyboard()
            }
        })

        // 뉴스 기사 눌렀을 때 웹뷰로 이동
        cl_feed_detail_news.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                val intent = Intent(this@FeedDetailActivity, WebViewActivity::class.java)
                this@FeedDetailActivity.startActivity(intent)
            }
        })

        // 플립(북마크) 버튼 눌렀을 때 북마크 추가/취소
        btn_feed_detail_flips.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                setFlips(feed_idx)
            }
        })

        // 뒤로 가기 눌렀을 때
        btn_feed_detail_back.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })

        // 이미지 눌렀을 때
        cv_feed_detail_img.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                openPhotoZoomActivity()
            }
        })

        // 플래그 닫기 버튼 눌렀을 때
        btn_feed_detail_recomment_flag_x.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                edt_feed_detail_comment_upload.setText("")
                setGone(cl_feed_detail_recomment_flag)
                GlobalData.recommentFlag = false
                GlobalData.commentId = null
            }
        })
    }

    private fun focusKeyboard() {
        edt_feed_detail_comment_upload.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 포토줌 액티비티 열기
     */
    private fun openPhotoZoomActivity() {
        val intent = Intent(this@FeedDetailActivity, PhotoZoomActivity::class.java)
        intent.putStringArrayListExtra("imageList", imgList)
        this.startActivity(intent)
    }

    /**
     * 댓글 리사이클러뷰 설정
     */
    private fun setCommentRecyclerView() {
        rv_feed_detail_comments.isNestedScrollingEnabled = false
        rv_feed_detail_comments.setHasFixedSize(false)

        feedDetailCommentRVAdapter =
            FeedDetailCommentRVAdapter(this@FeedDetailActivity, commentDataList)
        rv_feed_detail_comments.apply {
            adapter = feedDetailCommentRVAdapter
            layoutManager =
                LinearLayoutManager(this@FeedDetailActivity, LinearLayoutManager.VERTICAL, false)
        }
        feedDetailCommentRVAdapter.notifyDataSetChanged()
    }

    /**
     *  글자를 하나라도 입력하면 댓글 창 활성화시키기
     */
    private fun activateComment() {
        edt_feed_detail_comment_upload.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 글자가 있을 때
                if (p0.toString().trim().isNotEmpty()) {
                    btn_feed_detail_upload_comment.isSelected = true
                } else {
                    btn_feed_detail_upload_comment.isSelected = false
                }
            }
        })
    }

    /**
     * 태그 하기  : @사람이름
     */
    fun recomment(username: String) {
        var tagUser: String = ("@").plus(username).plus(" ")
        edt_feed_detail_comment_upload.setText(tagUser)
        edt_feed_detail_comment_upload.setSelection(edt_feed_detail_comment_upload.length())

        // 키보드 올리기
        focusKeyboard()

        // flag 띄우기
        tv_feed_detail_recomment_flag_user_name.text = username
        setVisible(cl_feed_detail_recomment_flag)

        GlobalData.recommentFlag = true
    }

    /**
     * 댓글달기 서버 통신
     */
    private fun postComment(post_id: String, commentContent: String) {
        // 답글다는 중이 아닐 때
        if (!GlobalData.recommentFlag)
            GlobalData.commentId = null

        networkServiceFeed.postCommentRequest(
            SharedPreferenceController.getAuthorization(this@FeedDetailActivity)!!,
            PostCommentData(post_id, commentContent, GlobalData.commentId)).safeEnqueue(
          onSuccess={
              setGone(cl_feed_detail_recomment_flag)

              //키보드 내리기
              val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)  as InputMethodManager
              imm.hideSoftInputFromWindow(edt_feed_detail_comment_upload.windowToken, 0)

              networkServiceFeed.getFeedDetailResponse(SharedPreferenceController.getAuthorization(this@FeedDetailActivity)!!, feed_idx).safeEnqueue(
                  {},
                  {
                      feedDetailCommentRVAdapter.dataList = it.data.pidArr.comments!!
                      feedDetailCommentRVAdapter.notifyDataSetChanged()
                      nsv_feed_detail.fullScroll(NestedScrollView.FOCUS_DOWN)
                  }
              )
              //feedDetailCommentRVAdapter.notifyDataSetChanged()

              GlobalData.commentId = null
              GlobalData.recommentFlag = false
          }
        )
    }

    override fun onResume() {
        super.onResume()
        getFeedDetailResponse(feed_idx)
    }

    /**
     *  북마크 설정
     */
    private fun setFlips(feed_idx: String) {
        val ivFlips = findViewById<ImageView>(R.id.iv_feed_detail_flips)
        if (ivFlips.isSelected)      //북마크 취소
            ivFlips.isSelected = false
        else {   // 북마크하기
            val feedFlipsSaveDialog = FeedFlipsSaveDialog(feed_idx, iv_feed_detail_flips)
            GlobalData.bottomSheetDialogFragment = feedFlipsSaveDialog
            feedFlipsSaveDialog.show(this@FeedDetailActivity.supportFragmentManager, "")
        }
    }

    /**
     *  날짜 계산
     */
    fun calculateTime(postTimeDate: String): String {

        var dateList: List<String> = postTimeDate.split("T")
        var date: String = dateList[0]
        var timeList: List<String> = dateList[1].split(".")
        var time: String = timeList[0]

        var formattedServerTime: String = date.plus(" ").plus(time)

        return formattedServerTime
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setGone(view: View) {
        view.visibility = View.GONE
    }

    private fun setVisible(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun setInvisible(view: View) {
        view.visibility = View.INVISIBLE
    }
}