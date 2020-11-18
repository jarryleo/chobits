package cn.leo.chobits.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.leo.chobits.ext.toDateyyyyMMddHHmm
import cn.leo.paging_ktx.DifferData
import kotlinx.android.parcel.Parcelize

/**
 * @author : leo
 * @date : 2020/11/17
 * @description : 笔记数据库实体类
 */
@Parcelize
@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Long? = null,
    var version: Long = 0,
    var title: String? = "",
    var summary: String? = "",
    var content: String? = "",
    var date: Long = 0L,
) : DifferData, Parcelable {
    fun getDateFormat(): String = date.toDateyyyyMMddHHmm()

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