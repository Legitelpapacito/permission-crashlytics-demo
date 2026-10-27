package dev.example.permissioncrashdemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.example.permissioncrashdemo.R

@Composable
fun DemoScreen(
    state: DemoUiState,
    requestInFlight: Boolean,
    firebaseEnabled: Boolean,
    onCountRequested: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(stringResource(R.string.feature_title), style = MaterialTheme.typography.headlineMedium)
            Text(stringResource(R.string.feature_description))
            Button(
                onClick = onCountRequested,
                enabled = !requestInFlight && state.status != DemoStatus.Loading,
            ) { Text(stringResource(R.string.count_contacts)) }
            Text(statusText(state))
            Text(stringResource(if (firebaseEnabled) R.string.firebase_mode else R.string.local_mode))
        }
    }
}

@Composable
private fun statusText(state: DemoUiState): String = when (state.status) {
    DemoStatus.Ready -> stringResource(R.string.ready_status)
    DemoStatus.Loading -> stringResource(R.string.loading_status)
    DemoStatus.Success -> stringResource(R.string.count_status, state.contactCount)
    DemoStatus.Denied -> stringResource(R.string.denied_status)
    DemoStatus.Blocked -> stringResource(R.string.blocked_status)
    DemoStatus.PermissionRequired -> stringResource(R.string.revoked_status)
    DemoStatus.Unavailable -> stringResource(R.string.failure_status)
}
