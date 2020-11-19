package cn.leo.chobits.model

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.DbModelProperty
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
class NoteViewModel : ViewModel() {

    var data: NoteEntity? by Delegates.observable(null) { _, _, new ->
        new?.content?.let {
            content.set(it)
        }
    }

    val content = ObservableField<String>()

    private val flow = MutableStateFlow("")

    private val db by DbModelProperty(DB::class.java)

    val textWatcher = TextContentWatcher {
        collect()
        flow.value = it
    }

    private fun collect() {
        viewModelScope.launch {
            flow.debounce(200)
                .collectLatest {
                    update(it)
                }
        }
    }

    override fun onCleared() {
        update(content.get())
        super.onCleared()
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
                date = System.currentTimeMillis()
            )
            viewModelScope.launch(Dispatchers.IO) {
                db.runInTransaction {
                    db.noteDao().insert(data!!)
                }
            }
        } else {
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
                date = System.currentTimeMillis()
            }
            viewModelScope.launch(Dispatchers.IO) {
                db.runInTransaction {
                    db.noteDao().update(data!!)
                }
            }
        }

    }
}