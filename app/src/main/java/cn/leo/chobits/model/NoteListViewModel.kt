package cn.leo.chobits.model

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.R
import cn.leo.chobits.activity.NoteActivity
import cn.leo.chobits.activity.SyncActivity
import cn.leo.chobits.binding.ClickHandler
import cn.leo.chobits.binding.LongClickHandler
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
class NoteListViewModel @Inject constructor(var db: DB) : ViewModel(),
    ClickHandler, LongClickHandler {

    val pager =
        SimplePager(viewModelScope, pagingSource = { db.noteDao().getNoteList() })

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_add -> NoteActivity.jumpActivity(v.context)
            R.id.iv_sync -> sync(v.context)
        }
    }

    override fun onLongClick(v: View) {
        SyncActivity.jumpActivity(v.context)
    }

    private fun sync(context: Context) {
        //1.检查同步服务器配置，没有配置跳转到配置页。
        //2.有配置，直接开始同步逻辑
        SyncActivity.jumpActivity(context)
    }
}