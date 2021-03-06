package com.flood_android.ui.feed.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.flood_android.R
import com.flood_android.ui.feed.FeedDetailActivity
import com.flood_android.ui.feed.data.CommentsData
import com.flood_android.util.GlobalData
import com.flood_android.util.OnSingleClickListener

class FeedDetailCommentRVAdapter(
    private val ctx: Context,
    var dataList: ArrayList<CommentsData>
) :
    RecyclerView.Adapter<FeedDetailCommentRVAdapter.Holder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view: View =
            LayoutInflater.from(ctx).inflate(R.layout.rv_item_feed_detail_comment, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        dataList[position].let { item ->
            Glide.with(ctx)
                .load(item.comment_user_img)
                .transform(CenterCrop(), CircleCrop())
                .into(holder.userImg)

            holder.userName.text = item.comment_user_name
            holder.time.text = item.comment_time //(ctx as MainActivity).calculateTime(item.comment_time)
            holder.commentContents.text = item.comment_content

            // 댓글에서 답글달기 클릭했을 때 처리하기
            holder.btnRecomment.setOnClickListener(object  : OnSingleClickListener(){
                override fun onSingleClick(v: View) {
                    (ctx as FeedDetailActivity).recomment(item.comment_user_name)
                    GlobalData.commentId = item.comment_id
                }
            })

            if ( item.subComment!=null){
                holder.recomments.apply {
                    adapter = FeedDetailRecommentRVAdapter(context!!, item.subComment, item.comment_id)
                    layoutManager =
                        LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
                }
            }
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userImg =
            itemView.findViewById(R.id.iv_rv_item_feed_detail_comment_user_img) as ImageView
        var userName =
            itemView.findViewById(R.id.tv_rv_item_feed_detail_comment_user_name) as TextView
        var time = itemView.findViewById(R.id.tv_rv_item_feed_detail_comment_time) as TextView
        var commentContents =
            itemView.findViewById(R.id.tv_rv_item_feed_detail_comment_contents) as TextView
        var btnRecomment =
            itemView.findViewById(R.id.btn_rv_item_feed_detail_comment_recomment) as ConstraintLayout
        var recomments =
            itemView.findViewById(R.id.rv_rv_item_feed_detail_comment_recomment) as RecyclerView
    }
}