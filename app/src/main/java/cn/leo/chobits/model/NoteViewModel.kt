package cn.leo.chobits.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.DbModelProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 笔记编辑model
 */
class NoteViewModel : ViewModel() {

    var data: NoteEntity? = null

    private val db by DbModelProperty(DB::class.java)

    fun update(text: String?) {
        if (text.isNullOrEmpty()) {
            if (data == null) {
                return
            } else {
                delNote()
            }
        }
        execute(text!!)
    }

    private fun delNote() {
        viewModelScope.launch(Dispatchers.IO) {
            db.runInTransaction {
                db.noteDao().del(data!!)
            }
        }
    }

    private fun execute(text: String) {
        val sp = text.split("\n", limit = 2)
        if (data == null) {
            data = NoteEntity(
                version = 1L,
                title = sp[0],
                summary = if (sp.size >= 2) {
                    sp[1]
                } else {
                    ""
                },
                content = text,
                date = System.currentTimeMillis()
            )
        } else {
            data?.apply {
                version++
                title = sp[0]
                summary = if (sp.size >= 2) {
                    sp[1]
                } else {
                    ""
                }
                content = text
                date = System.currentTimeMillis()
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            db.runInTransaction {
                db.noteDao().insert(data!!)
            }
        }
    }
}