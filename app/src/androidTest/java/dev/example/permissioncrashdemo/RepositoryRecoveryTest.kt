package dev.example.permissioncrashdemo

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.example.permissioncrashdemo.data.ContactCountResult
import dev.example.permissioncrashdemo.di.DemoContainer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryRecoveryTest {
    @Test
    fun revokedPermissionReturnsRecoverableState(): Unit = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(
            "Revoke READ_CONTACTS before running this test",
            PackageManager.PERMISSION_DENIED,
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS),
        )
        val repository = DemoContainer.createRepository(context)

        assertEquals(ContactCountResult.PermissionRequired, repository.countContacts())
    }
}
