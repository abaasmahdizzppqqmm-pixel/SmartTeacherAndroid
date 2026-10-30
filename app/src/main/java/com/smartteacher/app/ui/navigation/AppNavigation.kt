package com.smartteacher.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smartteacher.app.data.TeacherRepository
import com.smartteacher.app.ui.classes.ClassesScreen
import com.smartteacher.app.ui.schools.SchoolsScreen
import com.smartteacher.app.ui.students.StudentsScreen

sealed class Screen(val route: String, val label: String = "", val icon: ImageVector? = null) {
    object Schools : Screen("schools", "الصفوف", Icons.Default.Home)
    object Schedule : Screen("schedule", "الجدول", Icons.Default.DateRange)
    object Plans : Screen("plans", "الخطط", Icons.Default.Edit)
    object Stats : Screen("stats", "الإحصائيات", Icons.Default.Star)
    object Settings : Screen("settings", "الإعدادات", Icons.Default.Settings)

    object Classes : Screen("classes/{schoolId}/{schoolName}") {
        fun createRoute(schoolId: Long, schoolName: String) = "classes/$schoolId/$schoolName"
    }

    object Students : Screen("students/{classId}/{className}") {
        fun createRoute(classId: Long, className: String) = "students/$classId/$className"
    }
}

// نفس ترتيب أقسام شريط التنقل السفلي في النسخة الأصلية
private val bottomTabs = listOf(Screen.Settings, Screen.Stats, Screen.Plans, Screen.Schedule, Screen.Schools)

@Composable
fun AppNavHost(repository: TeacherRepository) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Schools.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Schools.route) { SchoolsScreen(repository, navController) }
            composable(Screen.Schedule.route) { PlaceholderScreen("الجدول") }
            composable(Screen.Plans.route) { PlaceholderScreen("الخطط") }
            composable(Screen.Stats.route) { PlaceholderScreen("الإحصائيات") }
            composable(Screen.Settings.route) { PlaceholderScreen("الإعدادات") }

            composable(
                route = Screen.Classes.route,
                arguments = listOf(
                    navArgument("schoolId") { type = NavType.LongType },
                    navArgument("schoolName") { type = NavType.StringType }
                )
            ) { entry ->
                val schoolId = entry.arguments?.getLong("schoolId") ?: 0L
                val schoolName = entry.arguments?.getString("schoolName") ?: ""
                ClassesScreen(schoolId, schoolName, repository, navController)
            }

            composable(
                route = Screen.Students.route,
                arguments = listOf(
                    navArgument("classId") { type = NavType.LongType },
                    navArgument("className") { type = NavType.StringType }
                )
            ) { entry ->
                val classId = entry.arguments?.getLong("classId") ?: 0L
                val className = entry.arguments?.getString("className") ?: ""
                StudentsScreen(classId, className, repository)
            }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        bottomTabs.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Schools.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { screen.icon?.let { Icon(it, contentDescription = screen.label) } },
                label = { Text(screen.label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(title: String) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text(
                "قسم \"$title\" قيد التطوير — يأتي في المرحلة القادمة 🚧",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(32.dp)
            )
        }
    }
}
