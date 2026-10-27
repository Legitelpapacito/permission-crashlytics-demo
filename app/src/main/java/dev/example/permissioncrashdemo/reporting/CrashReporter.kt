package dev.example.permissioncrashdemo.reporting

interface CrashReporter {
    fun reportNonFatal(error: Exception, attributes: Map<String, String>)
}
