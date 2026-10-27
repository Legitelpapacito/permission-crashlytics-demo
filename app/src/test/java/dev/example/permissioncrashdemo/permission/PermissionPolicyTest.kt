package dev.example.permissioncrashdemo.permission

import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionPolicyTest {
    @Test
    fun firstRequestIsNotMistakenForPermanentDenial() {
        assertEquals(PermissionDecision.Request, PermissionPolicy.beforeRequest(false, false))
        assertEquals(PermissionDecision.Blocked, PermissionPolicy.afterResult(false, false))
    }

    @Test
    fun deniedPermissionWithRationaleOffersExplanation() {
        assertEquals(PermissionDecision.Rationale, PermissionPolicy.beforeRequest(false, true))
        assertEquals(PermissionDecision.Rationale, PermissionPolicy.afterResult(false, true))
    }

    @Test
    fun existingGrantSkipsDialog() {
        assertEquals(PermissionDecision.Granted, PermissionPolicy.beforeRequest(true, false))
    }
}
