package cn.leo.chobits.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.leo.chobits.R
import cn.leo.chobits.databinding.ActivityNoteBinding
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.binding
import cn.leo.chobits.ext.toDateyyyyMMddHHmm
import cn.leo.chobits.model.NoteViewModel
import org.jetbrains.anko.startActivity

class NoteActivity : AppCompatActivity() {

    private val binding: ActivityNoteBinding by binding(R.layout.activity_note)

    private val model: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model
        val data = intent.getParcelableExtra<NoteEntity>(KEY_LIST_DATA)
        title = data?.getDateFormat() ?: System.currentTimeMillis().toDateyyyyMMddHHmm()
        model.data = data
    }

    companion object {
        private const val KEY_LIST_DATA = "listData"
        fun jumpActivity(context: Context, data: NoteEntity? = null) {
            context.startActivity<NoteActivity>(KEY_LIST_DATA to data)
        }
    }
}