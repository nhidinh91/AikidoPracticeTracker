package app.aikidopracticetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class PracticeSessionTest {
    private PracticeSession practiceSession;

    @BeforeEach
    public void setUp() {
        practiceSession = new PracticeSession(LocalDate.of(2025, 2, 26), 60);
    }

    @Test
    public void testGetDate() {
        assertEquals(LocalDate.of(2025, 2, 26), practiceSession.getDate());
    }

    @Test
    public void testGetDuration() {
        assertEquals(60, practiceSession.getDuration());
    }

    @Test
    public void testToString() {
        assertEquals("26-02-2025 - 60 mins", practiceSession.toString());
    }
}