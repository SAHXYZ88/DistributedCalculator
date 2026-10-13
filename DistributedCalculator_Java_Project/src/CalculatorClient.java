import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client side of the Distributed Calculator.
 * The client sends calculation requests to the server over TCP.
 */
public class CalculatorClient {
    private static final String HOST = "localhost";
    private static final int PORT = 6000;

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("     DISTRIBUTED CALCULATOR CLIENT");
        System.out.println("====================================");

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(input.readLine());
            boolean running = true;

            while (running) {
                System.out.println();
                System.out.println("Choose an operation:");
                System.out.println("1. Addition (+)");
                System.out.println("2. Subtraction (-)");
                System.out.println("3. Multiplication (*)");
                System.out.println("4. Division (/)");
                System.out.println("5. Modulo (%)");
                System.out.println("0. Exit");
                System.out.print("Your choice: ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    output.println("EXIT");
                    System.out.println("Server: " + input.readLine());
                    running = false;
                    continue;
                }

                String operation = getOperation(choice);
                if (operation == null) {
                    System.out.println("Invalid choice. Please select 0-5.");
                    continue;
                }

                try {
                    System.out.print("Enter first number: ");
                    double first = Double.parseDouble(scanner.nextLine().trim());

                    System.out.print("Enter second number: ");
                    double second = Double.parseDouble(scanner.nextLine().trim());

                    if (!Double.isFinite(first) || !Double.isFinite(second)) {
                        System.out.println("Please enter finite numeric values.");
                        continue;
                    }

                    String request = operation + "|" + first + "|" + second;
                    output.println(request);

                    String response = input.readLine();
                    System.out.println("Server response: " + response);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter numeric values.");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not connect to the server.");
            System.out.println("Make sure CalculatorServer is running on port "
                    + PORT + ".");
            System.out.println("Details: " + e.getMessage());
        }
    }

    private static String getOperation(String choice) {
        switch (choice) {
            case "1":
                return "ADD";
            case "2":
                return "SUBTRACT";
            case "3":
                return "MULTIPLY";
            case "4":
                return "DIVIDE";
            case "5":
                return "MODULO";
            default:
                return null;
        }
    }
}
