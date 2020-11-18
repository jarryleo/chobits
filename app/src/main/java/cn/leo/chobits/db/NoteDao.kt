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

    @Query("SELECT * FROM note")
    fun getNoteList(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM note WHERE _id = :id")
    fun getContentById(id: Long): LiveData<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg userBeans: NoteEntity)

    @Update
    fun update(vararg userBeans: NoteEntity): Int

    @Delete
    fun del(vararg userBean: NoteEntity): Int

    @Query("DELETE FROM note")
    fun delAll()
}