package dev.example.permissioncrashdemo.permission

enum class PermissionDecision {
    Granted,
    Rationale,
    Request,
    Blocked,
}

object PermissionPolicy {
    fun beforeRequest(granted: Boolean, showRationale: Boolean): PermissionDecision = when {
        granted -> PermissionDecision.Granted
        showRationale -> PermissionDecision.Rationale
        else -> PermissionDecision.Request
    }

    fun afterResult(granted: Boolean, showRationale: Boolean): PermissionDecision = when {
        granted -> PermissionDecision.Granted
        showRationale -> PermissionDecision.Rationale
        else -> PermissionDecision.Blocked
    }
}
