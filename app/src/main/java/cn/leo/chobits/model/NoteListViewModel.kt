package cn.leo.chobits.model

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.activity.NoteActivity
import cn.leo.chobits.binding.ClickHandler
import cn.leo.chobits.db.DB
import cn.leo.paging_ktx.simple.SimplePager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 列表 ViewModel
 */

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class NoteListViewModel @Inject constructor(var db: DB) : ViewModel(), ClickHandler {

    val pager = SimplePager(
        viewModelScope,
        pagingSource = { db.noteDao().getNoteListSource() }
    )

    override fun onClick(v: View) {
        NoteActivity.jumpActivity(v.context)
    }

}