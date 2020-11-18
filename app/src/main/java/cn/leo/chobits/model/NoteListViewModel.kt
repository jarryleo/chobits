package cn.leo.chobits.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.utils.DbModelProperty
import cn.leo.paging_ktx.SimplePager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 列表 ViewModel
 */
class NoteListViewModel : ViewModel() {

    private val db by DbModelProperty(DB::class.java)

    val pager =
        SimplePager(viewModelScope,
            pagingSource = { db.noteDao().getNoteList() }
        )

    fun testInsert() {
        viewModelScope.launch(Dispatchers.IO) {
            db.runInTransaction {
                db.noteDao().insert(
                    NoteEntity(
                        title = "测试标题，欢迎来到chobits～～",
                        summary = "1234567",
                        date = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}