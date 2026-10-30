package com.smartteacher.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// نفس الألوان المستخدمة في النسخة الأصلية (متغيرات CSS :root)
val PrimaryColor = Color(0xFF00695C)
val PrimaryLight = Color(0xFFB2DFDB)
val AccentOrange = Color(0xFFF57C00)
val AccentRed = Color(0xFFD32F2F)
val AccentGreen = Color(0xFF388E3C)
val AccentBlue = Color(0xFF1976D2)
val BgColor = Color(0xFFF4F7F9)
val CardBg = Color(0xFFFFFFFF)
val TextDark = Color(0xFF263238)
val TextLight = Color(0xFF546E7A)

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    primaryContainer = PrimaryLight,
    secondary = AccentOrange,
    error = AccentRed,
    background = BgColor,
    surface = CardBg,
    onPrimary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun SmartTeacherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
