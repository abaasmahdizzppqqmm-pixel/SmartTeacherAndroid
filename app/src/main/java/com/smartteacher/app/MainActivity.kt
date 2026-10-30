package com.smartteacher.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.smartteacher.app.data.AppDatabase
import com.smartteacher.app.data.TeacherRepository
import com.smartteacher.app.ui.navigation.AppNavHost
import com.smartteacher.app.ui.theme.SmartTeacherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = TeacherRepository(AppDatabase.getInstance(applicationContext))

        setContent {
            SmartTeacherTheme {
                AppNavHost(repository = repository)
            }
        }
    }
}
