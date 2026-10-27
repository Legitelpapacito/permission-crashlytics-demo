package dev.example.permissioncrashdemo

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.example.permissioncrashdemo.di.DemoContainer
import dev.example.permissioncrashdemo.ui.DemoRoute
import dev.example.permissioncrashdemo.ui.DemoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = DemoContainer.createViewModelFactory(applicationContext)
        setContent {
            val viewModel: DemoViewModel = viewModel(factory = factory)
            MaterialTheme {
                DemoRoute(
                    activity = this,
                    permission = Manifest.permission.READ_CONTACTS,
                    viewModel = viewModel,
                    firebaseEnabled = BuildConfig.FIREBASE_ENABLED,
                )
            }
        }
    }
}
