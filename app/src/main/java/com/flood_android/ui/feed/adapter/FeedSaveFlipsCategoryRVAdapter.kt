package com.flood_android.ui.feed.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flood_android.R
import com.flood_android.ui.feed.FeedFragment
import com.flood_android.ui.feed.data.BookmarkData
import com.flood_android.ui.main.MainActivity
import com.flood_android.util.OnSingleClickListener

class FeedSaveFlipsCategoryRVAdapter(
    private val ctx: Context, var dataList: ArrayList<BookmarkData>
) : RecyclerView.Adapter<FeedSaveFlipsCategoryRVAdapter.Holder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view: View = LayoutInflater.from(ctx)
            .inflate(R.layout.rv_item_feed_save_flips_category, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        dataList[position].let { item ->
            Glide.with(ctx)
                .load(item.thumb)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(holder.categoryImg)
            holder.categoryName.text = item.categoryName

            holder.categoryBtn.setOnClickListener {
                (object : OnSingleClickListener() {
                    override fun onSingleClick(v: View) {
                        (ctx as MainActivity).dismissFlipDialog()   // dialog 끄기
                        (ctx as FeedFragment).makeToast(item.thumb, item.categoryName)
                        /**
                         *  여기다가 북마크 추가했다는 서버 통신도 하긔
                         */
                    }
                })
            }
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImg =
            itemView.findViewById(R.id.iv_rv_item_feed_save_flips_category) as ImageView
        var categoryName =
            itemView.findViewById(R.id.tv_rv_item_feed_save_flips_category) as TextView
        var categoryBtn =
            itemView.findViewById(R.id.cl_rv_item_feed_save_flips_category) as ConstraintLayout
    }
}