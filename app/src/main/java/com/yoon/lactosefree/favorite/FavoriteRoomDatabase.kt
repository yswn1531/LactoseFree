package com.yoon.lactosefree.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yoon.lactosefree.common.FAVORITE_DATABASE_NAME

@Database(entities = [(Favorite::class)], exportSchema = false, version = 1)
@TypeConverters(UriConverters::class)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private lateinit var INSTANCE: FavoriteRoomDatabase
        internal fun getDatabase(context: Context): FavoriteRoomDatabase {
            if (!this::INSTANCE.isInitialized) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDatabase::class.java,
                        FAVORITE_DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}