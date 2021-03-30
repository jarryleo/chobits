package cn.leo.chobits.db

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.leo.chobits.ext.toDateyyyyMMddHHmm
import cn.leo.paging_ktx.adapter.DifferData
import kotlinx.android.parcel.Parcelize

/**
 * @author : leo
 * @date : 2020/11/17
 * @description : 笔记数据库实体类
 */
@Keep
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

    override fun areItemsTheSame(data: DifferData): Boolean {
        return (data as? NoteEntity)?._id == _id
    }

    override fun areContentsTheSame(data: DifferData): Boolean {
        val note = (data as? NoteEntity) ?: return false
        return note.title == title &&
                note.summary == summary &&
                note.date == date
    }

    override fun equals(other: Any?): Boolean {
        return (other as? NoteEntity)?._id == _id
    }

    override fun hashCode(): Int {
        var result = _id?.hashCode() ?: 0
        result = 31 * result + version.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + date.hashCode()
        return result
    }
}