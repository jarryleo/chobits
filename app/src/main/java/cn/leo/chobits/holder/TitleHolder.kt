package cn.leo.chobits.holder

import cn.leo.chobits.R
import cn.leo.chobits.bean.TitleBean
import cn.leo.chobits.databinding.ItemTitleBinding
import cn.leo.chobits.ext.binding
import cn.leo.paging_ktx.PagingDataAdapterKtx
import cn.leo.paging_ktx.SimpleHolder

/**
 * @author : leo
 * @date : 2020/11/10
 * @description : 标题holder
 */
class TitleHolder : SimpleHolder<TitleBean>(R.layout.item_title) {
    override fun bindItem(
        helper: PagingDataAdapterKtx.ItemHelper,
        data: TitleBean,
        payloads: MutableList<Any>?
    ) {
        helper.binding<ItemTitleBinding>()?.data = data
    }
}