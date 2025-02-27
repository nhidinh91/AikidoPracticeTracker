package app.aikidopracticetracker;

import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AikidoPracticeTrackerTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final List<PracticeSession> sessions = AikidoPracticeTracker.getSessions();
    private static final String FILE_NAME = "sessions.txt";
    private final InputStream originalIn = System.in;
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        sessions.clear();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);   // Restore System.in
        System.setOut(originalOut); // Restore System.out
    }

    @Test
    void testAddPracticeSession_ValidInput() {
        String simulatedInput = "20-02-2025\n90\n"; // Mock user input
        Scanner scanner = new Scanner(simulatedInput);
        AikidoPracticeTracker.addPracticeSession(scanner);

        assertEquals(1, sessions.size());
        assertEquals(LocalDate.of(2025, 2, 20), sessions.getFirst().getDate());
        assertEquals(90, sessions.getFirst().getDuration());
    }

    @Test
    void testAddPracticeSession_InvalidDate() {
        String simulatedInput = "INVALID-DATE\n90\n"; // Mock invalid input
        Scanner scanner = new Scanner(simulatedInput);
        AikidoPracticeTracker.addPracticeSession(scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input"), "Expected error message for invalid date");
    }

    @Test
    void testViewTotalPracticeTime() {
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 20), 90));
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 21), 120));

        AikidoPracticeTracker.viewTotalPracticeTime();
        String output = outputStream.toString();

        assertTrue(output.contains("Total Training Time: 3 hours 30 minutes"));

        sessions.clear();
        AikidoPracticeTracker.viewTotalPracticeTime();
        output = outputStream.toString();
        assertTrue(output.contains("Total Training Time: 0 hours 0 minutes"));
    }

    @Test
    void testCheckGraduationEligibility_NotEligible() {
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 20), 600)); // 10 hours

        AikidoPracticeTracker.checkGraduationEligibility();
        String output = outputStream.toString();

        assertTrue(output.contains("You need"), "Should indicate missing hours for eligibility");
    }

    @Test
    void testCheckGraduationEligibility_Eligible() {
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 20), 600)); // 10 hours
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 21), 600)); // 10 hours

        AikidoPracticeTracker.checkGraduationEligibility();
        String output = outputStream.toString();

        assertTrue(output.contains("Congratulations! You are eligible for Kyu graduation."));
    }

    @Test
    void testSaveAndLoadSessions() throws IOException {
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 20), 90));
        sessions.add(new PracticeSession(LocalDate.of(2025, 2, 21), 120));

        AikidoPracticeTracker.saveSessions(FILE_NAME);

        sessions.clear();
        AikidoPracticeTracker.loadSessions(FILE_NAME);

        assertEquals(2, sessions.size());
        assertEquals(90, sessions.get(0).getDuration());
        assertEquals(120, sessions.get(1).getDuration());
    }

    @Test
    void testSaveSessions_WithException() throws IOException {
        boolean result;
        result = AikidoPracticeTracker.saveSessions(null);

        assertFalse(result);
    }

    @Test
    void testLoadSessions_FileNotFound() {
        AikidoPracticeTracker.loadSessions("non-existent-file.txt");
        assertEquals(0, sessions.size());
    }

    @Test
    void testLoadSessions_WithException() {
        AikidoPracticeTracker.loadSessions(null);
        assertEquals(0, sessions.size());
    }

    @Test
    void testGetSessions() {
        assertNotNull(sessions);
    }

    @Test
    void testMainMenuOption1_AddPracticeSession() {
        String simulatedInput = "1\n60\n4\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        AikidoPracticeTracker.main(new String[]{});

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("===== Aikido Practice Tracker ====="));
        assertTrue(consoleOutput.contains("Add Practice Session"));
        assertTrue(consoleOutput.contains("Exiting. See you in the next practice!"));
    }

    @Test
    void testMainMenuOption2_ViewTotalPracticeTime() {
        String simulatedInput = "2\n4\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        AikidoPracticeTracker.main(new String[]{});

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("View Total Practice Time"));
        assertTrue(consoleOutput.contains("Exiting. See you in the next practice!"));
    }

}
