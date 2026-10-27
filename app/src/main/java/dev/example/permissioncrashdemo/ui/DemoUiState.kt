package dev.example.permissioncrashdemo.ui

data class DemoUiState(
    val status: DemoStatus = DemoStatus.Ready,
    val contactCount: Int = 0,
)

enum class DemoStatus {
    Ready,
    Loading,
    Success,
    Denied,
    Blocked,
    PermissionRequired,
    Unavailable,
}
