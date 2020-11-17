package cn.leo.chobits.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author : leo
 * @date : 2020/11/17
 * 数据库版本
 */
@Database(entities = [NoteEntity::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}