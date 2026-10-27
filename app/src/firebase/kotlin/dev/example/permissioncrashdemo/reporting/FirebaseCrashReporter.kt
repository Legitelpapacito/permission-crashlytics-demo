package dev.example.permissioncrashdemo.reporting

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashReporter : CrashReporter {
    private val crashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun reportNonFatal(error: Exception, attributes: Map<String, String>) {
        attributes.forEach { (key, value) -> crashlytics.setCustomKey(key, value) }
        crashlytics.log("Contact operation failed; returning a recoverable state")
        crashlytics.recordException(error)
    }
}
