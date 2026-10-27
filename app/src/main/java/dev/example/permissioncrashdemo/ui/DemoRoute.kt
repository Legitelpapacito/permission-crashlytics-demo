package dev.example.permissioncrashdemo.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.permissioncrashdemo.R
import dev.example.permissioncrashdemo.permission.PermissionGate

@Composable
fun DemoRoute(
    activity: ComponentActivity,
    permission: String,
    viewModel: DemoViewModel,
    firebaseEnabled: Boolean,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PermissionGate(
        activity = activity,
        permission = permission,
        rationale = stringResource(R.string.permission_rationale),
        onGranted = viewModel::onPermissionGranted,
        onDenied = viewModel::onPermissionDenied,
    ) { requestPermission, requestInFlight ->
        DemoScreen(
            state = state,
            requestInFlight = requestInFlight,
            firebaseEnabled = firebaseEnabled,
            onCountRequested = viewModel::onPermissionGranted,
        )
    }
}
