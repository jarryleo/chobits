package cn.leo.chobits.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.leo.chobits.R
import cn.leo.chobits.databinding.ActivityMainBinding
import cn.leo.chobits.ext.binding
import cn.leo.chobits.holder.NoteItemHolder
import cn.leo.chobits.holder.TitleHolder
import cn.leo.chobits.model.NoteListViewModel
import cn.leo.chobits.model.SyncViewModel
import cn.leo.paging_ktx.simple.SimplePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.jetbrains.anko.toast

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val model: NoteListViewModel by viewModels()
    private val syncModel: SyncViewModel by viewModels()
    private val adapter = SimplePagingAdapter(NoteItemHolder(), TitleHolder())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = adapter
        binding.model = model
        binding.syncModel = syncModel
        syncModel.isInSync.observe(this, {
            if (it) {
                toast(R.string.app_start_sync)
            } else {
                toast(R.string.app_sync_complete)
            }
        })
    }

}