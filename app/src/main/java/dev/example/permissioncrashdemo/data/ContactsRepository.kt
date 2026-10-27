package dev.example.permissioncrashdemo.data

import dev.example.permissioncrashdemo.reporting.CrashReporter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(
    private val dataSource: ContactsDataSource,
    private val crashReporter: CrashReporter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun countContacts(): ContactCountResult = withContext(dispatcher) {
        ContactCountResult.Success(dataSource.countContacts())
    }
}
