package cn.leo.chobits.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.leo.chobits.R
import cn.leo.chobits.databinding.ActivitySyncBinding
import cn.leo.chobits.ext.binding
import cn.leo.chobits.model.SyncViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class SyncActivity : AppCompatActivity() {
    private val binding: ActivitySyncBinding by binding(R.layout.activity_sync)
    private val model: SyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model
        title = getString(R.string.app_sync_title)
    }

    companion object {
        fun jumpActivity(context: Context) {
            context.startActivity<SyncActivity>()
        }
    }

}