package me.spighetto.mypoop.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PoopRulesServiceTest {

    private final PoopRulesService svc = new PoopRulesService();

    @Test
    @DisplayName("shouldTrigger: true when accumulated >= trigger")
    void shouldTrigger_true() {
        assertTrue(svc.shouldTrigger(10, 10));
        assertTrue(svc.shouldTrigger(11, 10));
    }

    @Test
    @DisplayName("shouldTrigger: false when accumulated < trigger")
    void shouldTrigger_false() {
        assertFalse(svc.shouldTrigger(9, 10));
    }

    @Test
    @DisplayName("isAtLimit: true when accumulated >= limit")
    void isAtLimit_true() {
        assertTrue(svc.isAtLimit(5, 5));
        assertTrue(svc.isAtLimit(6, 5));
    }

    @Test
    @DisplayName("isAtLimit: false when accumulated < limit")
    void isAtLimit_false() {
        assertFalse(svc.isAtLimit(4, 5));
    }

    @Test
    @DisplayName("accumulateWithinLimit: saturates at limit and handles overflow")
    void accumulateWithinLimit_saturatesAndOverflow() {
        // saturates to limit
        assertEquals(100, svc.accumulateWithinLimit(95, 10, 100));
        assertEquals(100, svc.accumulateWithinLimit(100, 1, 100));
        // normal sum below limit
        assertEquals(15, svc.accumulateWithinLimit(10, 5, 100));
        // overflow safety (extreme, just ensure no crash and returns min int)
        assertEquals(Integer.MIN_VALUE, svc.accumulateWithinLimit(Integer.MIN_VALUE, -1, 100));
    }
}

