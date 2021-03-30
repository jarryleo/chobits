package cn.leo.chobits.model

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.SP
import cn.leo.chobits.ext.TextContentWatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : 笔记编辑model
 */
@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class NoteViewModel @Inject constructor(var db: DB) : ViewModel() {
    //云端地址
    var url: String by SP("dav_url", "")

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

    //协程锁
    private val mutex = Mutex()

    //异步锁定
    private fun asyncAndLock(action: () -> Unit) {
        viewModelScope.launch(context = Dispatchers.IO) {
            mutex.withLock { action.invoke() }
        }
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


    /**
     * 从数据库删除笔记
     */
    private fun delNote() {
        asyncAndLock {
            db.runInTransaction {
                data?.let {
                    db.noteDao().del(it)
                }
                data = null
            }
        }
    }

    /**
     * 更新数据
     */
    private fun update(text: String?) {
        if (text.isNullOrEmpty()) {
            if (data == null) {
                return
            }
            if (url.isEmpty()) {
                //没有设置云同步，直接删除本地笔记
                delNote()
                return
            }
        }
        Log.e("update", "update: $text")
        saveNote(text ?: "")
    }

    /**
     * 创建或者保存笔记
     */
    private fun saveNote(text: String) {
        val sp = text.split("\n", limit = 2)
        val date = System.currentTimeMillis()
        if (data == null) {
            //创建新的笔记
            data = NoteEntity(
                version = 1L,
                title = sp[0],
                summary = if (sp.size >= 2) sp[1] else "",
                content = text,
                date = date
            )
            //插入数据库
            asyncAndLock {
                db.runInTransaction {
                    db.noteDao().insert(data!!)
                    data = db.noteDao().getNoteByDate(date) //获取带id的数据
                    Log.e("创建新笔记", "saveNote: $text")
                }
            }
        } else {
            if (text == data?.content) {
                return
            }
            //修改老的笔记
            data?.apply {
                this.version++
                this.title = sp[0]
                this.summary = if (sp.size >= 2) sp[1] else ""
                this.content = text
                this.date = date
            }
            //更新数据库
            asyncAndLock {
                db.runInTransaction {
                    data?.let {
                        db.noteDao().update(it)
                        Log.e("保存笔记", "saveNote: $text id = ${data?._id}")
                    }
                }
            }
        }

    }
}