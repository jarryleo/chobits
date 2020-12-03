package cn.leo.chobits.holder

import cn.leo.chobits.R
import cn.leo.chobits.databinding.ItemNoteListBinding
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.binding
import cn.leo.paging_ktx.adapter.ItemHelper
import cn.leo.paging_ktx.simple.SimpleHolder

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 笔记条目holder
 */
class NoteItemHolder : SimpleHolder<NoteEntity>(R.layout.item_note_list) {
    override fun bindItem(
        item: ItemHelper,
        data: NoteEntity,
        payloads: MutableList<Any>?
    ) {
        item.binding<ItemNoteListBinding>()?.data = data
    }
}