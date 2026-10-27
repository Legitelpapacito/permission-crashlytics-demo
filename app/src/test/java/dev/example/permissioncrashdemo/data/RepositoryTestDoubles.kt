package dev.example.permissioncrashdemo.data

import dev.example.permissioncrashdemo.reporting.CrashReporter

class FakeContactsDataSource(
    var granted: Boolean = true,
    var count: Int = 7,
    var failure: Exception? = null,
) : ContactsDataSource {
    var queryCount = 0

    override fun hasReadPermission(): Boolean = granted

    override fun countContacts(): Int {
        queryCount++
        failure?.let { throw it }
        return count
    }
}

class RecordingCrashReporter : CrashReporter {
    val errors = mutableListOf<Exception>()
    var attributes = emptyMap<String, String>()

    override fun reportNonFatal(error: Exception, attributes: Map<String, String>) {
        errors += error
        this.attributes = attributes
    }
}
