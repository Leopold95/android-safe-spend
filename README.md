# SafeSpend - Financial Discipline Tracker

Android application for tracking spending discipline through a streak-based system.

## 📱 Features

- **Streak System**: Track consecutive safe days without impulse purchases
- **Daily Logging**: Mark each day as safe or overspend with notes
- **Resilience Calculator**: Evaluate financial resilience (0-100 score)
- **History & Search**: Filter by period (week/month/all) and search entries
- **Analytics**: View current streak, longest streak, and spending patterns
- **Onboarding**: Two-screen introduction to app concepts

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM with Repository pattern
- **DI**: Koin
- **Database**: Room
- **Storage**: DataStore (Preferences)
- **Navigation**: Navigation Compose
- **Min SDK**: 24
- **Target SDK**: 36

## 📦 Project Structure

```
app/src/main/java/com/alexandr/safespend/
├── data/
│   ├── model/         # Data models
│   ├── db/            # Room database
│   ├── repository/    # Data repositories
│   └── datastore/     # Preferences storage
├── ui/
│   ├── splash/        # Splash screen
│   ├── onboarding/    # Onboarding screens
│   ├── home/          # Main screen
│   ├── addday/        # Add/Edit day screen
│   ├── history/       # History with filters
│   ├── daydetail/     # Day details
│   ├── analytics/     # Statistics & charts
│   ├── resilience/    # Resilience calculator
│   ├── settings/      # App settings
│   ├── components/    # Reusable UI components
│   ├── theme/         # Design tokens & theme
│   └── navigation/    # Navigation graph
├── di/                # Koin modules
└── utils/             # Utility functions
```

## 🎨 Design System

- **Primary Color**: Green (#2EBE4A) - success, progress
- **Secondary Color**: Yellow (#FFD600) - warnings, accents
- **Theme**: Light mode
- **Typography**: Material 3 type scale
- **Spacing**: 4/8/16/24/32dp tokens

## 🚀 Build & Run

1. Clone the repository:
```bash
git clone <repository-url>
cd android-safe-spend
```

2. Open project in Android Studio

3. Sync Gradle files

4. Run on device/emulator:
```bash
./gradlew installDebug
```

## 📦 Build Types

### Debug
```bash
./gradlew assembleDebug
```

### Release
```bash
./gradlew assembleRelease
```

Release build includes:
- ProGuard minification
- Resource shrinking
- Code optimization

## 📄 License

This project is provided as-is for educational purposes.

## 🔐 Security

- All sensitive strings in `strings.xml`
- No hardcoded API keys
- ProGuard rules included for Room, Koin, Ktor

---

**Application ID**: `com.alexandr.safespend`  
**Version**: 1.0  
**Build Date**: March 2026

