package cn.leo.chobits.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.leo.chobits.utils.toCommentDateToYMD

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
) {
    fun getDateFormat(): String = date.toCommentDateToYMD()
}