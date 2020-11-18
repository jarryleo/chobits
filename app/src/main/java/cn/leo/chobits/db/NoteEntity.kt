package cn.leo.chobits.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.leo.chobits.utils.toCommentDateToYMD
import cn.leo.paging_ktx.DifferData

/**
 * @author : leo
 * @date : 2020/11/17
 * @description : 笔记数据库实体类
 */
@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Long? = null,
    val version: Long = 0,
    val title: String? = "",
    val summary: String? = "",
    val content: String? = "",
    val date: Long = 0L,
) : DifferData {
    fun getDateFormat(): String = date.toCommentDateToYMD()

    override fun areItemsTheSame(d: DifferData): Boolean {
        return (d as? NoteEntity)?._id == _id
    }

    override fun areContentsTheSame(d: DifferData): Boolean {
        val data = (d as? NoteEntity) ?: return false
        return data.title == title &&
                data.summary == summary &&
                data.date == date
    }
}