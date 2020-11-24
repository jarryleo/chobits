package cn.leo.chobits.model

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.activity.NoteActivity
import cn.leo.chobits.binding.ClickHandler
import cn.leo.chobits.db.DB
import cn.leo.paging_ktx.SimplePager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 列表 ViewModel
 */

@ExperimentalCoroutinesApi
@FlowPreview
class NoteListViewModel @ViewModelInject constructor(var db: DB) : ViewModel(), ClickHandler {

    val pager =
        SimplePager(viewModelScope, pagingSource = { db.noteDao().getNoteList() })

    override fun onClick(v: View) {
        NoteActivity.jumpActivity(v.context)
    }
}