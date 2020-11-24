package cn.leo.chobits.model

import android.util.Log
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.TextContentWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 笔记编辑model
 */
@FlowPreview
@ExperimentalCoroutinesApi
class NoteViewModel @ViewModelInject constructor(var db: DB) : ViewModel() {

    var data: NoteEntity? by Delegates.observable(null) { _, _, new ->
        new?.content?.let {
            content.set(it)
            flow.value = it
        }
    }

    val content = ObservableField<String>()

    private val flow = MutableStateFlow("")

    val textWatcher = TextContentWatcher {
        flow.value = it
    }

    init {
        viewModelScope.launch {
            flow.debounce(200)
                .collectLatest {
                    update(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        data = null
    }

    private fun delNote() {
        viewModelScope.launch(Dispatchers.IO) {
            db.runInTransaction {
                db.noteDao().del(data!!)
                data = null
            }
        }
    }

    private fun update(text: String?) {
        Log.e("update", "update: $text")
        if (text.isNullOrEmpty()) {
            if (data == null) {
                return
            } else {
                delNote()
            }
        }
        saveNote(text!!)
    }

    private fun saveNote(text: String) {
        val sp = text.split("\n", limit = 2)
        val date = System.currentTimeMillis()
        if (data == null) {
            //创建新的笔记
            data = NoteEntity(
                version = 1L,
                title = sp[0],
                summary = if (sp.size >= 2) {
                    sp[1]
                } else {
                    ""
                },
                content = text,
                date = date
            )
            viewModelScope.launch(Dispatchers.IO) {
                db.runInTransaction {
                    db.noteDao().insert(data!!)
                    data = db.noteDao().getNoteByDate(date)
                    Log.e("创建新笔记", "saveNote: $text")
                }
            }
        } else {
            if (text == data?.content) {
                return
            }
            //修改老的笔记
            data?.apply {
                version++
                title = sp[0]
                summary = if (sp.size >= 2) {
                    sp[1]
                } else {
                    ""
                }
                content = text
                this.date = date
            }
            viewModelScope.launch(Dispatchers.IO) {
                db.runInTransaction {
                    data?.let {
                        db.noteDao().update(it)
                        Log.e("保存笔记", "saveNote: $text  id = ${data?._id}")
                    }
                }
            }
        }

    }
}