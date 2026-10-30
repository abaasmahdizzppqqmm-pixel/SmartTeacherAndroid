package com.smartteacher.app.data

import kotlinx.coroutines.flow.Flow

class TeacherRepository(private val db: AppDatabase) {

    // ===== المدارس =====
    fun observeSchools(): Flow<List<SchoolEntity>> = db.schoolDao().observeAll()
    suspend fun addSchool(name: String) = db.schoolDao().insert(SchoolEntity(name = name))
    suspend fun deleteSchool(school: SchoolEntity) = db.schoolDao().delete(school)

    // ===== الصفوف =====
    fun observeClasses(schoolId: Long): Flow<List<ClassEntity>> = db.classDao().observeBySchool(schoolId)
    suspend fun addClass(schoolId: Long, name: String, subject: String) =
        db.classDao().insert(ClassEntity(schoolId = schoolId, name = name, subject = subject))
    suspend fun deleteClass(classEntity: ClassEntity) = db.classDao().delete(classEntity)
    fun observeStudentCount(classId: Long): Flow<Int> = db.classDao().observeStudentCount(classId)

    // ===== الطلاب =====
    fun observeStudents(classId: Long): Flow<List<StudentEntity>> = db.studentDao().observeByClass(classId)
    suspend fun addStudent(classId: Long, name: String) =
        db.studentDao().insert(StudentEntity(classId = classId, name = name))
    suspend fun deleteStudent(student: StudentEntity) = db.studentDao().delete(student)
    suspend fun updateStudent(student: StudentEntity) = db.studentDao().update(student)

    // ===== الحقول المخصصة =====
    fun observeCustomFields(classId: Long): Flow<List<CustomFieldEntity>> = db.customFieldDao().observeByClass(classId)
    suspend fun addCustomField(classId: Long, name: String) =
        db.customFieldDao().insert(CustomFieldEntity(classId = classId, name = name))
    suspend fun deleteCustomField(field: CustomFieldEntity) = db.customFieldDao().delete(field)

    // ===== السجلات (مشاركة/واجب/دفتر/سلوك) =====
    fun observeRecords(studentId: Long, type: RecordType, term: String): Flow<List<RecordEntryEntity>> =
        db.recordDao().observe(studentId, type.name, term)
    suspend fun addRecord(studentId: Long, type: RecordType, value: String, date: String, term: String) =
        db.recordDao().insert(RecordEntryEntity(studentId = studentId, type = type.name, value = value, date = date, term = term))
    suspend fun deleteRecord(record: RecordEntryEntity) = db.recordDao().delete(record)

    // ===== الحضور والغياب =====
    fun observeAbsences(studentId: Long): Flow<List<AbsenceEntity>> = db.absenceDao().observeAll(studentId)
    suspend fun addAbsence(studentId: Long, date: String, term: String) =
        db.absenceDao().insert(AbsenceEntity(studentId = studentId, date = date, term = term))
    suspend fun removeAbsence(studentId: Long, date: String, term: String) =
        db.absenceDao().deleteByDate(studentId, date, term)

    // ===== الدرجات =====
    fun observeGrade(studentId: Long, term: String): Flow<TermGradeEntity?> = db.termGradeDao().observe(studentId, term)
    suspend fun saveGrade(grade: TermGradeEntity) = db.termGradeDao().upsert(grade)
}
