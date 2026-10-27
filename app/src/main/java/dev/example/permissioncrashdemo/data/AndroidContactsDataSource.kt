package dev.example.permissioncrashdemo.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat

class AndroidContactsDataSource(context: Context) : ContactsDataSource {
    private val applicationContext = context.applicationContext

    override fun hasReadPermission(): Boolean = ContextCompat.checkSelfPermission(
        applicationContext,
        Manifest.permission.READ_CONTACTS,
    ) == PackageManager.PERMISSION_GRANTED

    override fun countContacts(): Int {
        val cursor = applicationContext.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(ContactsContract.Contacts._ID),
            null,
            null,
            null,
        ) ?: throw IllegalStateException("Contacts provider returned no cursor")
        return cursor.use { it.count }
    }
}
