package cn.leo.chobits.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

/**
 * @author : leo
 * @date : 2020/11/17
 * @description : 笔记数据库操作类
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY date DESC")
    fun getNoteListSource(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM note ORDER BY date DESC")
    fun getNoteList(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE date = :date")
    fun getNoteByDate(date: Long): NoteEntity

    @Query("SELECT * FROM note WHERE _id = :id")
    fun getContentById(id: Long): LiveData<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg note: NoteEntity)

    @Update
    fun update(vararg note: NoteEntity): Int

    @Delete
    fun del(vararg note: NoteEntity): Int

    @Query("DELETE FROM note")
    fun delAll()
}