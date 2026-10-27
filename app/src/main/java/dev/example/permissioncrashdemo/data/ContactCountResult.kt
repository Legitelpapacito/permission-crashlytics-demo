package dev.example.permissioncrashdemo.data

sealed interface ContactCountResult {
    data class Success(val count: Int) : ContactCountResult

    data object PermissionRequired : ContactCountResult

    data object Unavailable : ContactCountResult
}
