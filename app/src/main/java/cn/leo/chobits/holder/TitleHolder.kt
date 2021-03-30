package cn.leo.chobits.holder

import cn.leo.chobits.R
import cn.leo.chobits.bean.TitleBean
import cn.leo.chobits.databinding.ItemTitleBinding
import cn.leo.chobits.ext.binding
import cn.leo.paging_ktx.adapter.ItemHelper
import cn.leo.paging_ktx.simple.SimpleHolder

/**
 * @author : leo
 * @date : 2020/11/10
 * @description : 标题holder
 */
class TitleHolder : SimpleHolder<TitleBean>(R.layout.item_title) {
    override fun bindItem(
        item: ItemHelper,
        data: TitleBean,
        payloads: MutableList<Any>?
    ) {
        item.binding<ItemTitleBinding>()?.data = data
    }
}