package dev.example.permissioncrashdemo.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.example.permissioncrashdemo.data.AndroidContactsDataSource
import dev.example.permissioncrashdemo.data.ContactsRepository
import dev.example.permissioncrashdemo.reporting.CrashReporterFactory
import dev.example.permissioncrashdemo.ui.DemoViewModel

object DemoContainer {
    fun createRepository(context: Context): ContactsRepository = ContactsRepository(
        dataSource = AndroidContactsDataSource(context),
        crashReporter = CrashReporterFactory.create(),
    )

    fun createViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = createRepository(context.applicationContext)
        return viewModelFactory { initializer { DemoViewModel(repository) } }
    }
}
