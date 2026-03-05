package com.mac.cozyfarm

import android.app.Application
import androidx.room.Room
import com.mac.cozyfarm.data.local.FarmDatabase
import com.mac.cozyfarm.data.repository.FarmRepositoryImpl
import com.mac.cozyfarm.domain.repository.FarmRepository

class FarmApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this,
            FarmDatabase::class.java,
            FarmDatabase.DATABASE_NAME
        ).build()
    }

    val repository: FarmRepository by lazy {
        FarmRepositoryImpl(database.farmDao())
    }
}
