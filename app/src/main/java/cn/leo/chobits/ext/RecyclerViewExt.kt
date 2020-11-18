package cn.leo.chobits.ext

import androidx.recyclerview.widget.RecyclerView
import cn.leo.paging_ktx.SimplePagingAdapter

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : RecyclerView 拓展
 */

/**
 * 获取简易adapter
 */
fun RecyclerView.getSimpleAdapter(): SimplePagingAdapter? = adapter as? SimplePagingAdapter