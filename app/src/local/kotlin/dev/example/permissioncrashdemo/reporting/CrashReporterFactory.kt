package dev.example.permissioncrashdemo.reporting

object CrashReporterFactory {
    fun create(): CrashReporter = object : CrashReporter {
        override fun reportNonFatal(error: Exception, attributes: Map<String, String>) = Unit
    }
}
