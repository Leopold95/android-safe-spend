package com.alexandr.safespend.di

import androidx.room.Room
import com.alexandr.safespend.data.datastore.SettingsDataStore
import com.alexandr.safespend.data.db.AppDatabase
import com.alexandr.safespend.data.db.DayEntryDao
import com.alexandr.safespend.data.repository.DayRepository
import com.alexandr.safespend.ui.addday.AddDayViewModel
import com.alexandr.safespend.ui.analytics.AnalyticsViewModel
import com.alexandr.safespend.ui.daydetail.DayDetailViewModel
import com.alexandr.safespend.ui.history.HistoryViewModel
import com.alexandr.safespend.ui.home.HomeViewModel
import com.alexandr.safespend.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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
        ).build()
    }

    // DAOs
    single<DayEntryDao> {
        get<AppDatabase>().dayEntryDao()
    }

    // Repository
    single {
        DayRepository(get())
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
