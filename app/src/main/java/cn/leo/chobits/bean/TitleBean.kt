package cn.leo.chobits.bean

import cn.leo.paging_ktx.DifferData

/**
 * @author : leo
 * @date : 2020/11/10
 * @description : 标题分组
 */
data class TitleBean(
    val title: String
) : DifferData {
    override fun areItemsTheSame(d: DifferData): Boolean {
        return (d as? TitleBean)?.title == title
    }
}