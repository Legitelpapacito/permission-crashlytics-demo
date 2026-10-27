package dev.example.permissioncrashdemo.data

interface ContactsDataSource {
    fun hasReadPermission(): Boolean

    fun countContacts(): Int
}
