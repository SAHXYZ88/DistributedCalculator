import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server side of the Distributed Calculator.
 * It accepts multiple clients and processes each client on a separate thread.
 */
public class CalculatorServer {
    private static final int PORT = 6000;

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("     DISTRIBUTED CALCULATOR SERVER");
        System.out.println("====================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: "
                        + clientSocket.getInetAddress()
                        + ":" + clientSocket.getPort());

                Thread clientThread = new Thread(
                        () -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (Socket client = socket;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             PrintWriter output = new PrintWriter(
                     client.getOutputStream(), true)) {

            output.println("Connected to Distributed Calculator Server.");

            String request;
            while ((request = input.readLine()) != null) {
                if (request.equalsIgnoreCase("EXIT")) {
                    output.println("Goodbye!");
                    break;
                }

                System.out.println("Request from "
                        + client.getInetAddress() + ": " + request);

                String response = calculate(request);
                output.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client connection error: " + e.getMessage());
        }

        System.out.println("Client disconnected.");
    }

    /**
     * Request format: OPERATION|NUMBER1|NUMBER2
     * Example: ADD|12|5
     */
    private static String calculate(String request) {
        String[] parts = request.split("\\|");

        if (parts.length != 3) {
            return "ERROR: Invalid request format.";
        }

        String operation = parts[0].trim().toUpperCase();

        try {
            double first = Double.parseDouble(parts[1].trim());
            double second = Double.parseDouble(parts[2].trim());
            double result;

            switch (operation) {
                case "ADD":
                    result = first + second;
                    break;
                case "SUBTRACT":
                    result = first - second;
                    break;
                case "MULTIPLY":
                    result = first * second;
                    break;
                case "DIVIDE":
                    if (second == 0) {
                        return "ERROR: Division by zero is not allowed.";
                    }
                    result = first / second;
                    break;
                case "MODULO":
                    if (second == 0) {
                        return "ERROR: Modulo by zero is not allowed.";
                    }
                    result = first % second;
                    break;
                default:
                    return "ERROR: Unsupported operation.";
            }

            if (!Double.isFinite(result)) {
                return "ERROR: Result is outside the supported numeric range.";
            }

            return "RESULT: " + result;
        } catch (NumberFormatException e) {
            return "ERROR: Please provide valid numeric values.";
        }
    }
}
