package com.alexandr.safespend.di

import androidx.room.Room
import com.alexandr.safespend.data.datastore.SettingsDataStore
import com.alexandr.safespend.data.db.AppDatabase
import com.alexandr.safespend.data.db.DayEntryDao
import com.alexandr.safespend.data.db.ResilienceCheckDao
import com.alexandr.safespend.data.repository.DayRepository
import com.alexandr.safespend.ui.screens.addday.AddDayViewModel
import com.alexandr.safespend.ui.screens.analytics.AnalyticsViewModel
import com.alexandr.safespend.ui.screens.daydetail.DayDetailViewModel
import com.alexandr.safespend.ui.screens.history.HistoryViewModel
import com.alexandr.safespend.ui.screens.home.HomeViewModel
import com.alexandr.safespend.ui.screens.resilience.ResilienceCalculatorViewModel
import com.alexandr.safespend.ui.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun dataModule() = module {
    // DataStore
    single {
        SettingsDataStore(androidContext())
    }

    // Database
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "safespend_db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    // DAOs
    single<DayEntryDao> {
        get<AppDatabase>().dayEntryDao()
    }

    single<ResilienceCheckDao> {
        get<AppDatabase>().resilienceCheckDao()
    }

    // Repository
    single {
        DayRepository(get(), get())
    }
}

fun uiModule() = module {
    // ViewModels
    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        AddDayViewModel(get())
    }

    viewModel {
        HistoryViewModel(get())
    }

    viewModel {
        AnalyticsViewModel(get())
    }

    viewModel {
        ResilienceCalculatorViewModel(get())
    }

    viewModel {
        SettingsViewModel(
            context = androidContext(),
            dayRepository = get(),
            settingsDataStore = get()
        )
    }

    viewModel {
        DayDetailViewModel(get())
    }
}

fun appModules() = listOf(
    dataModule(),
    uiModule()
)
