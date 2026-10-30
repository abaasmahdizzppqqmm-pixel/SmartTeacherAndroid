package com.smartteacher.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    @Query("SELECT * FROM schools ORDER BY id DESC")
    fun observeAll(): Flow<List<SchoolEntity>>

    @Insert
    suspend fun insert(school: SchoolEntity): Long

    @Delete
    suspend fun delete(school: SchoolEntity)
}

@Dao
interface ClassDao {
    @Query("SELECT * FROM classes WHERE schoolId = :schoolId ORDER BY id DESC")
    fun observeBySchool(schoolId: Long): Flow<List<ClassEntity>>

    @Insert
    suspend fun insert(classEntity: ClassEntity): Long

    @Update
    suspend fun update(classEntity: ClassEntity)

    @Delete
    suspend fun delete(classEntity: ClassEntity)

    @Query("SELECT COUNT(*) FROM students WHERE classId = :classId")
    fun observeStudentCount(classId: Long): Flow<Int>
}

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE classId = :classId ORDER BY id ASC")
    fun observeByClass(classId: Long): Flow<List<StudentEntity>>

    @Insert
    suspend fun insert(student: StudentEntity): Long

    @Update
    suspend fun update(student: StudentEntity)

    @Delete
    suspend fun delete(student: StudentEntity)
}

@Dao
interface CustomFieldDao {
    @Query("SELECT * FROM custom_fields WHERE classId = :classId ORDER BY id ASC")
    fun observeByClass(classId: Long): Flow<List<CustomFieldEntity>>

    @Insert
    suspend fun insert(field: CustomFieldEntity): Long

    @Delete
    suspend fun delete(field: CustomFieldEntity)
}

@Dao
interface CustomFieldRecordDao {
    @Query("SELECT * FROM custom_field_records WHERE studentId = :studentId AND fieldId = :fieldId ORDER BY id DESC")
    fun observe(studentId: Long, fieldId: Long): Flow<List<CustomFieldRecordEntity>>

    @Insert
    suspend fun insert(record: CustomFieldRecordEntity): Long

    @Delete
    suspend fun delete(record: CustomFieldRecordEntity)
}

@Dao
interface RecordDao {
    @Query("SELECT * FROM records WHERE studentId = :studentId AND type = :type AND term = :term ORDER BY id DESC")
    fun observe(studentId: Long, type: String, term: String): Flow<List<RecordEntryEntity>>

    @Insert
    suspend fun insert(record: RecordEntryEntity): Long

    @Delete
    suspend fun delete(record: RecordEntryEntity)
}

@Dao
interface AbsenceDao {
    @Query("SELECT * FROM absences WHERE studentId = :studentId")
    fun observeAll(studentId: Long): Flow<List<AbsenceEntity>>

    @Insert
    suspend fun insert(absence: AbsenceEntity): Long

    @Query("DELETE FROM absences WHERE studentId = :studentId AND date = :date AND term = :term")
    suspend fun deleteByDate(studentId: Long, date: String, term: String)
}

@Dao
interface TermGradeDao {
    @Query("SELECT * FROM term_grades WHERE studentId = :studentId AND term = :term LIMIT 1")
    fun observe(studentId: Long, term: String): Flow<TermGradeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(grade: TermGradeEntity): Long
}

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule_entries WHERE schoolId = :schoolId AND dayOfWeek = :day ORDER BY sortKey ASC")
    fun observe(schoolId: Long, day: Int): Flow<List<ScheduleEntryEntity>>

    @Insert
    suspend fun insert(entry: ScheduleEntryEntity): Long

    @Delete
    suspend fun delete(entry: ScheduleEntryEntity)
}
