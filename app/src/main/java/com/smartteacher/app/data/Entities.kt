package com.smartteacher.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** الفصول الدراسية الأربعة، مطابقة للتسميات في النسخة الأصلية */
object Term {
    const val FIRST_HALF = "النصف الأول"
    const val SECOND_HALF = "النصف الثاني"
    const val MID_YEAR = "نصف السنة"
    const val END_YEAR = "نهاية السنة"
    val ALL = listOf(FIRST_HALF, SECOND_HALF, MID_YEAR, END_YEAR)
}

/** أنواع السجلات الفرعية لكل طالب (مشاركة / واجب / دفتر / سلوك) */
enum class RecordType { PARTICIPATION, HOMEWORK, NOTEBOOK, DISTURBANCE }

@Entity(tableName = "schools")
data class SchoolEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(
    tableName = "classes",
    foreignKeys = [ForeignKey(
        entity = SchoolEntity::class,
        parentColumns = ["id"],
        childColumns = ["schoolId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("schoolId")]
)
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val schoolId: Long,
    val name: String,
    val subject: String,
    val year: String = "2026-2027",
    val lastActiveTerm: String = Term.FIRST_HALF
)

@Entity(
    tableName = "students",
    foreignKeys = [ForeignKey(
        entity = ClassEntity::class,
        parentColumns = ["id"],
        childColumns = ["classId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("classId")]
)
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val classId: Long,
    val name: String,
    val note: String = ""
)

/** الحقول الإضافية المخصصة التي يضيفها المدرس لكل صف */
@Entity(
    tableName = "custom_fields",
    foreignKeys = [ForeignKey(
        entity = ClassEntity::class,
        parentColumns = ["id"],
        childColumns = ["classId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("classId")]
)
data class CustomFieldEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val classId: Long,
    val name: String
)

@Entity(
    tableName = "custom_field_records",
    foreignKeys = [
        ForeignKey(entity = CustomFieldEntity::class, parentColumns = ["id"], childColumns = ["fieldId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = StudentEntity::class, parentColumns = ["id"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fieldId"), Index("studentId")]
)
data class CustomFieldRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fieldId: Long,
    val studentId: Long,
    val value: String,
    val date: String,
    val term: String
)

/** سجل موحّد للمشاركة/الواجب/الدفتر/السلوك (type يحدد النوع) */
@Entity(
    tableName = "records",
    foreignKeys = [ForeignKey(entity = StudentEntity::class, parentColumns = ["id"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("studentId")]
)
data class RecordEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val type: String, // قيمة من RecordType.name
    val value: String,
    val date: String,
    val term: String
)

@Entity(
    tableName = "absences",
    foreignKeys = [ForeignKey(entity = StudentEntity::class, parentColumns = ["id"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("studentId")]
)
data class AbsenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val date: String, // yyyy-M-d
    val term: String
)

@Entity(
    tableName = "term_grades",
    foreignKeys = [ForeignKey(entity = StudentEntity::class, parentColumns = ["id"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["studentId", "term"], unique = true)]
)
data class TermGradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val term: String,
    val exam1: Double? = null,
    val exam2: Double? = null,
    val finalGrade: Double? = null
)

@Entity(
    tableName = "schedule_entries",
    foreignKeys = [ForeignKey(entity = SchoolEntity::class, parentColumns = ["id"], childColumns = ["schoolId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("schoolId")]
)
data class ScheduleEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val schoolId: Long,
    val dayOfWeek: Int, // 1=السبت ... 7=الجمعة
    val hour: Int,
    val minute: Int,
    val ampm: String,
    val sortKey: Int,
    val className: String,
    val subject: String
)
