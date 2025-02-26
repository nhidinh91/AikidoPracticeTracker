package app.aikidopracticetracker;

import java.io.*;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AikidoPracticeTracker {
    private static final String FILE_NAME = "sessions.txt";
    private static final int KYU_ELIGIBILITY_MINUTES = 1200;
    private static final List<PracticeSession> sessions = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static void main(String[] args) {
        loadSessions(FILE_NAME); // Load saved sessions from file
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Aikido Practice Tracker =====");
            System.out.println("1. Add Practice Session");
            System.out.println("2. View Total Practice Time");
            System.out.println("3. Check Graduation Eligibility");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addPracticeSession(scanner);
                    break;
                case 2:
                    viewTotalPracticeTime();
                    break;
                case 3:
                    checkGraduationEligibility();
                    break;
                case 4:
                    saveSessions(FILE_NAME);
                    System.out.println("Exiting. See you in the next practice!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    public static void addPracticeSession(Scanner scanner) {
        try {
            System.out.print("Enter date (DD-MM-YYYY): ");
            String dateInput = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateInput, FORMATTER);

            System.out.print("Enter duration in minutes: ");
            int duration = scanner.nextInt();
            scanner.nextLine();

            sessions.add(new PracticeSession(date, duration));
            System.out.println("Session added successfully.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    public static void viewTotalPracticeTime() {
        int totalMinutes = sessions.stream().mapToInt(PracticeSession::getDuration).sum();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        System.out.println("Total Training Time: " + hours + " hours " + minutes + " minutes");
        if (!sessions.isEmpty()) {
            System.out.println("Practice Log:");
            sessions.forEach(System.out::println);
        }
    }

    public static void checkGraduationEligibility() {
        int totalMinutes = sessions.stream().mapToInt(PracticeSession::getDuration).sum();
        if (totalMinutes >= KYU_ELIGIBILITY_MINUTES) {
            System.out.println("Congratulations! You are eligible for Kyu graduation.");
        } else {
            int totalRemainingMinutes = KYU_ELIGIBILITY_MINUTES - totalMinutes;
            int remainingHours = totalRemainingMinutes / 60;
            int remainingMinutes = totalRemainingMinutes - remainingHours * 60;
            System.out.println("You need " + remainingHours + " hours" + " and " + remainingMinutes + " minutes" + " more to be eligible.");
        }
    }

    public static boolean saveSessions(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (PracticeSession session : sessions) {
                writer.write(session.getDate().format(FORMATTER) + "," + session.getDuration());
                writer.newLine();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error saving sessions.");
            return false;
        }
    }

    public static void loadSessions(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                sessions.add(new PracticeSession(LocalDate.parse(parts[0], FORMATTER), Integer.parseInt(parts[1])));
            }
        } catch (Exception e) {
            System.out.println("Error loading sessions.");
        }
    }

    public static List<PracticeSession> getSessions() {
        return sessions;
    }

}
