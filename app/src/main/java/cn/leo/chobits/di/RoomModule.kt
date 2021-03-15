package cn.leo.chobits.di

import android.app.Application
import cn.leo.chobits.db.DB
import cn.leo.chobits.ext.RoomHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author : ling luo
 * @date : 2020/11/24
 * @description : 数据库注入
 */

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    fun provideRoomDatabase(context: Application): DB {
        return RoomHelper.getDb(context, DB::class.java)
    }
}