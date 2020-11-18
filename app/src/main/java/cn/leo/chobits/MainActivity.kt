package cn.leo.chobits

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.leo.chobits.databinding.ActivityMainBinding
import cn.leo.chobits.holder.NoteItemHolder
import cn.leo.chobits.model.NoteListViewModel
import cn.leo.chobits.utils.binding
import cn.leo.paging_ktx.SimplePagingAdapter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    private val model: NoteListViewModel by viewModels()

    private val adapter = SimplePagingAdapter(NoteItemHolder())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = adapter
        adapter.setPager(model.pager)
    }

}