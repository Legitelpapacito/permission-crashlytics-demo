package dev.example.permissioncrashdemo.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionGate(
    activity: ComponentActivity,
    permission: String,
    rationale: String,
    onGranted: () -> Unit,
    onDenied: (Boolean) -> Unit,
    content: @Composable (requestPermission: () -> Unit, requestInFlight: Boolean) -> Unit,
) {
    var decisionName by rememberSaveable(permission) { mutableStateOf<String?>(null) }
    var requestInFlight by rememberSaveable(permission) { mutableStateOf(false) }
    val currentOnGranted by rememberUpdatedState(onGranted)
    val currentOnDenied by rememberUpdatedState(onDenied)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        requestInFlight = false
        val decision = PermissionPolicy.afterResult(granted, activity.shouldExplain(permission))
        decisionName = if (granted) null else decision.name
        if (granted) currentOnGranted() else currentOnDenied(decision == PermissionDecision.Blocked)
    }
    val launchRequest: () -> Unit = {
        if (!requestInFlight) {
            decisionName = null
            requestInFlight = true
            launcher.launch(permission)
        }
    }
    val requestPermission: () -> Unit = {
        if (!requestInFlight) {
            when (val decision = PermissionPolicy.beforeRequest(
                activity.isGranted(permission),
                activity.shouldExplain(permission),
            )) {
                PermissionDecision.Granted -> currentOnGranted()
                PermissionDecision.Request -> launchRequest()
                else -> decisionName = decision.name
            }
        }
    }
    PermissionDialogs(
        decision = decisionName?.let(PermissionDecision::valueOf),
        rationale = rationale,
        onRequest = launchRequest,
        onOpenSettings = {
            decisionName = null
            activity.openAppSettings()
        },
        onDismiss = { decisionName = null },
    )
    content(requestPermission, requestInFlight)
}

private fun ComponentActivity.isGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

private fun ComponentActivity.shouldExplain(permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

private fun ComponentActivity.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")))
}
