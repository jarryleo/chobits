package cn.leo.chobits.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.leo.chobits.R
import cn.leo.chobits.databinding.ActivitySyncBinding
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.binding
import cn.leo.chobits.model.SyncViewModel
import cn.leo.chobits.webdav.WebDavManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity


class SyncActivity : AppCompatActivity() {
    private val binding: ActivitySyncBinding by binding(R.layout.activity_sync)
    private val model: SyncViewModel by viewModels()
    private val webDav by lazy {
        WebDavManager.getInstance("lingluo511@gmail.com", "aq9he4arf3ttmve7")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model
        title = "同步设置"
    }

    companion object {
        fun jumpActivity(context: Context) {
            context.startActivity<SyncActivity>()
        }
    }

    private fun getList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = webDav.fileList("https://dav.jianguoyun.com/dav/davTest")
            Log.d("WebDavManager", "getList: + $list")
        }
    }

    private fun putTest() {
        /*lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            val item = adapter.getData(0) ?: return@launch
            val data = (item as? NoteEntity) ?: return@launch
            val byteArray = data.content?.encodeToByteArray() ?: return@launch
            val result = webDav.put("https://dav.jianguoyun.com/dav/davTest/test.txt", byteArray)
            if (result) {
                getList()
            }
            Log.d("WebDavManager", "putTest: = $result")
        }*/
    }
}