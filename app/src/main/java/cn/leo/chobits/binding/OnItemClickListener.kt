package cn.leo.chobits.binding

import android.view.View
import cn.leo.paging_ktx.simple.SimplePagingAdapter


interface OnItemClickListener {
    fun onItemClick(adapter: SimplePagingAdapter, v: View, position: Int)
}