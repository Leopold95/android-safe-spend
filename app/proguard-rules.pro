# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep source file names and line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations
-keepattributes *Annotation*

# ==================== Room Database ====================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keepclassmembers class * {
    @androidx.room.* *;
}

# ==================== Kotlin Serialization ====================
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-dontwarn kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keep class * implements kotlinx.serialization.Serializable { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# ==================== Ktor Client ====================
-keep class io.ktor.** { *; }
-keep class io.ktor.client.** { *; }
-keepclassmembers class io.ktor.client.** { *; }
-dontwarn io.ktor.**

# ==================== Koin Dependency Injection ====================
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-keepclassmembers class org.koin.** { *; }
-dontwarn org.koin.**

# ==================== Jetpack/AndroidX ====================
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keepclassmembers class androidx.** { *; }
-keepattributes *AndroidRuntimeVisible*
-dontwarn androidx.**

# ==================== Compose ====================
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ==================== Data Classes ====================
-keep class com.alexandr.safespend.data.model.** { *; }
-keep class com.alexandr.safespend.ui.**.** extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * {
    *** get*();
    void set*(***);
}

# ==================== Reflection Prevention ====================
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontnote com.android.internal.util.**

# ==================== Keep Model Classes ====================
-keep class com.alexandr.safespend.** { *; }
-keepclassmembers class com.alexandr.safespend.** { *; }

# ==================== Verbose ====================
-verbose
