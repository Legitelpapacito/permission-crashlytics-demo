package dev.example.permissioncrashdemo.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactsRepositoryTest {
    @Test
    fun grantedPermissionReturnsCountWithoutReporting() = runTest {
        val source = FakeContactsDataSource()
        val reporter = RecordingCrashReporter()
        val repository = ContactsRepository(source, reporter, StandardTestDispatcher(testScheduler))

        assertEquals(ContactCountResult.Success(7), repository.countContacts())
        assertEquals(1, source.queryCount)
        assertTrue(reporter.errors.isEmpty())
    }

    @Test
    fun deniedPermissionAvoidsProviderAndReportsNonFatal() = runTest {
        val source = FakeContactsDataSource(granted = false)
        val reporter = RecordingCrashReporter()
        val repository = ContactsRepository(source, reporter, StandardTestDispatcher(testScheduler))

        assertEquals(ContactCountResult.PermissionRequired, repository.countContacts())
        assertEquals(0, source.queryCount)
        assertTrue(reporter.errors.single() is SecurityException)
        assertEquals("false", reporter.attributes["permission_granted_at_check"])
    }

    @Test
    fun revocationBetweenCheckAndQueryIsRecoverable() = runTest {
        val failure = SecurityException("Permission revoked during query")
        val source = FakeContactsDataSource(failure = failure)
        val reporter = RecordingCrashReporter()
        val repository = ContactsRepository(source, reporter, StandardTestDispatcher(testScheduler))

        assertEquals(ContactCountResult.PermissionRequired, repository.countContacts())
        assertSame(failure, reporter.errors.single())
        assertEquals("true", reporter.attributes["permission_granted_at_check"])
    }

    @Test
    fun unavailableProviderDoesNotCloseApp() = runTest {
        val source = FakeContactsDataSource(failure = IllegalStateException("Provider unavailable"))
        val reporter = RecordingCrashReporter()
        val repository = ContactsRepository(source, reporter, StandardTestDispatcher(testScheduler))

        assertEquals(ContactCountResult.Unavailable, repository.countContacts())
        assertEquals("count_contacts", reporter.attributes["operation"])
    }

    @Test
    fun cancellationPropagatesWithoutReporting() = runTest {
        val cancellation = CancellationException("Screen closed")
        val source = FakeContactsDataSource(failure = cancellation)
        val reporter = RecordingCrashReporter()
        val repository = ContactsRepository(source, reporter, StandardTestDispatcher(testScheduler))

        val failure = runCatching { repository.countContacts() }.exceptionOrNull()

        assertTrue(failure is CancellationException)
        assertEquals(cancellation.message, failure?.message)
        assertTrue(reporter.errors.isEmpty())
    }
}
