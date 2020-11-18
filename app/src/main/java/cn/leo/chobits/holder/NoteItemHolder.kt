package cn.leo.chobits.holder

import cn.leo.chobits.R
import cn.leo.chobits.databinding.ItemNoteListBinding
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.utils.binding
import cn.leo.paging_ktx.PagingDataAdapterKtx
import cn.leo.paging_ktx.SimpleHolder

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 笔记条目holder
 */
class NoteItemHolder : SimpleHolder<NoteEntity>(R.layout.item_note_list) {
    override fun bindItem(
        helper: PagingDataAdapterKtx.ItemHelper,
        data: NoteEntity,
        payloads: MutableList<Any>?
    ) {
        helper.binding<ItemNoteListBinding>()?.data = data
    }
}