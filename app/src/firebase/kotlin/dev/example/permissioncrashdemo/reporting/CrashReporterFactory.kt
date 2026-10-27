package dev.example.permissioncrashdemo.reporting

object CrashReporterFactory {
    fun create(): CrashReporter = FirebaseCrashReporter()
}
