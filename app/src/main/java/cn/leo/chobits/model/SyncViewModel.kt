package cn.leo.chobits.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.leo.chobits.R
import cn.leo.chobits.activity.SyncActivity
import cn.leo.chobits.binding.ClickHandler
import cn.leo.chobits.binding.LongClickHandler
import cn.leo.chobits.db.DB
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.SP
import cn.leo.chobits.ext.jsonToBean
import cn.leo.chobits.ext.toJson
import cn.leo.chobits.webdav.WebDavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(var db: DB) :
    ViewModel(), ClickHandler, LongClickHandler {

    var url: String by SP("dav_url", "")
    var username: String by SP("username", "")
    var password: String by SP("password", "")

    private val webDav by lazy {
        WebDavManager.getInstance(username, password)
    }

    //协程锁
    private val mutex = Mutex()

    //异步锁定
    private fun asyncAndLock(action: () -> Unit) {
        viewModelScope.launch(context = Dispatchers.IO) {
            mutex.withLock { action.invoke() }
        }
    }

    //同步状态
    val isInSync = MutableLiveData<Boolean>()

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_sync -> sync(v.context)
            R.id.tv_tutorials -> jumpToTutorials(v)
        }
    }

    override fun onLongClick(v: View) {
        SyncActivity.jumpActivity(v.context)
    }

    private fun sync(context: Context) {
        //1.检查同步服务器配置，没有配置跳转到配置页。
        if (url.isEmpty()) {
            SyncActivity.jumpActivity(context)
            return
        }
        if (isInSync.value == true) {
            Log.e("SyncViewModel", "sync: 正在同步中，跳过事件")
            return
        }
        //2.有配置，直接开始同步逻辑
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("SyncViewModel", "sync: 开始同步")
            syncNote()
            Log.e("SyncViewModel", "sync: 同步完成")
        }
    }

    /**
     * 跳转到 WebDav 设置教程
     */
    private fun jumpToTutorials(v: View) {
        val url = "https://juejin.cn/post/6894182448872030216"
        val context = v.context
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    /**
     * 获取云端笔记列表
     */
    private suspend fun getNoteListFromWeb(): List<NoteEntity> {
        val list = webDav.fileList(url)
        val fileList = mutableListOf<NoteEntity>()
        list.forEach {
            val filePath = it.href.rawPath
            Log.d("getNoteListFromWeb", "$filePath")
            if (!it.isDirectory) {
                val json = webDav.get(filePath)
                val file = json.jsonToBean(NoteEntity::class.java)
                file?.let {
                    fileList.add(file)
                }
            }
        }
        return fileList
    }

    /**
     * 把单个笔记文件写入云端 webDav
     */
    private suspend fun updateNoteToWeb(note: NoteEntity): Boolean {
        val byteArray = note.toJson().encodeToByteArray()
        val fileUrl = note.getFileUrl()
        Log.d("updateNoteToWeb", fileUrl)
        return webDav.put(fileUrl, byteArray)
    }

    /**
     * 删除笔记
     */
    private suspend fun deleteFromWeb(fileUrl: String) {
        webDav.delete(fileUrl)
    }

    /**
     * 获取笔记文件在webDav的url
     */
    private fun NoteEntity.getFileUrl(): String {
        return url + "/note_${_id}.txt"
    }


    /**
     * 同步笔记流程
     * 1.拉取云端笔记列表
     * 2.拉取本地数据库笔记列表
     * 3.对比两个列表（笔记的 _id 唯一标志），云端有，本地不存在的笔记，写入数据库
     * 4.本地有，云端没有的，上传到云端
     * 5.云端有，本地也有的，对比 version ，云端版本高的更新本地，本地版本高的更新云端
     * 6.本地删除的笔记，先保留数据库，content 为空，version++ ，
     *  同步的时候比对云端，比云端高则删除云端，然后删除本地，比云端低，则把云端写入本地， 云端没有则直接删除
     */
    private suspend fun syncNote() {
        isInSync.postValue(true)
        val noteListFromWeb = getNoteListFromWeb()
        val noteListLocal = db.noteDao().getNoteList()
        //对比云端已有笔记
        noteListFromWeb.forEach { webNote ->
            val localNote = noteListLocal.find { it._id == webNote._id }
            if (localNote == null) {
                //如果本地不存在,把云端存入数据库
                saveNoteToDb(webNote)
            } else {
                //云端和本地都有，比较version
                if (localNote.version > webNote.version) {
                    //本地版本高，且内容为空，则删除云端和本地
                    if (localNote.content.isNullOrEmpty()) {
                        deleteFromWeb(webNote.getFileUrl())
                        delNoteFromDb(localNote)
                    } else {
                        updateNoteToWeb(localNote) //本地版本高，覆盖云端
                    }
                } else {
                    updateNoteToDb(webNote) //云端版本高，覆盖本地
                }
            }
        }
        //对比本地已有笔记
        noteListLocal.forEach { localNote ->
            val webNote = noteListFromWeb.find { it._id == localNote._id }
            //本地有，云端没有的笔记，上传云端
            if (webNote == null) {
                updateNoteToWeb(localNote)
            }
        }
        isInSync.postValue(false)
    }

    /**
     * 新笔记存入数据库
     */
    private suspend fun saveNoteToDb(note: NoteEntity) {
        asyncAndLock {
            db.runInTransaction { db.noteDao().insert(note) }
        }
    }

    /**
     * 更新笔记到数据库
     */
    private suspend fun updateNoteToDb(note: NoteEntity) {
        asyncAndLock {
            db.runInTransaction { db.noteDao().update(note) }
        }
    }

    /**
     * 从数据库删除笔记
     */
    private suspend fun delNoteFromDb(note: NoteEntity) {
        asyncAndLock {
            db.runInTransaction { db.noteDao().del(note) }
        }
    }
}