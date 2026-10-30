package com.smartteacher.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SchoolEntity::class, ClassEntity::class, StudentEntity::class,
        CustomFieldEntity::class, CustomFieldRecordEntity::class,
        RecordEntryEntity::class, AbsenceEntity::class,
        TermGradeEntity::class, ScheduleEntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun classDao(): ClassDao
    abstract fun studentDao(): StudentDao
    abstract fun customFieldDao(): CustomFieldDao
    abstract fun customFieldRecordDao(): CustomFieldRecordDao
    abstract fun recordDao(): RecordDao
    abstract fun absenceDao(): AbsenceDao
    abstract fun termGradeDao(): TermGradeDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_teacher.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
