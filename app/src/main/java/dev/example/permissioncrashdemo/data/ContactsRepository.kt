package dev.example.permissioncrashdemo.data

import dev.example.permissioncrashdemo.reporting.CrashReporter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(
    private val dataSource: ContactsDataSource,
    private val crashReporter: CrashReporter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun countContacts(): ContactCountResult = withContext(dispatcher) {
        var permissionGranted = false
        try {
            permissionGranted = dataSource.hasReadPermission()
            if (!permissionGranted) {
                throw SecurityException("READ_CONTACTS is not granted")
            }
            ContactCountResult.Success(dataSource.countContacts())
        } catch (error: CancellationException) {
            throw error
        } catch (error: SecurityException) {
            reportFailure(error, permissionGranted)
            ContactCountResult.PermissionRequired
        } catch (error: Exception) {
            reportFailure(error, permissionGranted)
            ContactCountResult.Unavailable
        }
    }

    private fun reportFailure(error: Exception, permissionGranted: Boolean) {
        crashReporter.reportNonFatal(
            error,
            mapOf(
                "operation" to "count_contacts",
                "permission" to "READ_CONTACTS",
                "permission_granted_at_check" to permissionGranted.toString(),
            ),
        )
    }
}
