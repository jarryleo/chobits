package cn.leo.chobits.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.leo.chobits.R
import cn.leo.chobits.databinding.ActivityMainBinding
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.binding
import cn.leo.chobits.holder.NoteItemHolder
import cn.leo.chobits.holder.TitleHolder
import cn.leo.chobits.model.NoteListViewModel
import cn.leo.chobits.webdav.WebDavManager
import cn.leo.paging_ktx.simple.SimplePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val model: NoteListViewModel by viewModels()
    private val adapter = SimplePagingAdapter(NoteItemHolder(), TitleHolder())
    private val webDav by lazy {
        WebDavManager.getInstance("lingluo511@gmail.com", "aq9he4arf3ttmve7")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = adapter
        binding.model = model
        putTest()
    }

    private fun getList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = webDav.fileList("https://dav.jianguoyun.com/dav/davTest")
            Log.d("WebDavManager", "getList: + $list")
        }
    }

    private fun putTest() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            val item = adapter.getData(0) ?: return@launch
            val data = (item as? NoteEntity) ?: return@launch
            val byteArray = data.content?.encodeToByteArray() ?: return@launch
            val result = webDav.put("https://dav.jianguoyun.com/dav/davTest/test.txt", byteArray)
            if (result) {
                getList()
            }
            Log.d("WebDavManager", "putTest: = $result")
        }
    }
}