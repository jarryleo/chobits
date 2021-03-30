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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.jetbrains.anko.startActivity

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {

    private val binding: ActivityNoteBinding by binding(R.layout.activity_note)
    private val model: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.data = intent.getParcelableExtra(KEY_NOTE_DATA)
        binding.model = model
        title = model.data?.getDateFormat() ?: System.currentTimeMillis().toDateyyyyMMddHHmm()
    }

    companion object {
        private const val KEY_NOTE_DATA = "note_data"
        fun jumpActivity(context: Context, data: NoteEntity? = null) {
            context.startActivity<NoteActivity>(KEY_NOTE_DATA to data)
        }
    }
}