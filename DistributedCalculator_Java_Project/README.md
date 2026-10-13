# Distributed Calculator (Java)

## Project overview

The Distributed Calculator is a console-based client-server application written in Java. A client enters two numbers and chooses an arithmetic operation. The client sends the request to a separate server over a TCP socket. The server performs the calculation and sends the result back to the client.

The server can handle multiple clients by creating a separate thread for each connected client.

## Features

- Addition
- Subtraction
- Multiplication
- Division
- Modulo
- TCP client-server communication
- Multiple clients handled with threads
- Input validation and basic error responses
- Console-based interface

## Requirements

- Java JDK 8 or newer
- Two terminal windows (or more if testing multiple clients)
- No external libraries required

## Project structure

```text
DistributedCalculator/
├── src/
│   ├── CalculatorServer.java
│   └── CalculatorClient.java
└── README.md
```

## How to compile

Open a terminal in the project root directory and run:

```bash
javac -d out src/CalculatorServer.java src/CalculatorClient.java
```

This creates an `out` directory containing the compiled class files.

## How to run

### Terminal 1: Start the server

```bash
java -cp out CalculatorServer
```

Wait until the terminal displays that the server has started and is waiting for clients.

### Terminal 2: Start a client

```bash
java -cp out CalculatorClient
```

Choose an operation, enter two numbers, and read the result returned by the server.

You can start additional clients in other terminals. Each client is handled on a separate server thread.

## Example

Client input:

```text
Your choice: 1
Enter first number: 12
Enter second number: 5
```

Client output:

```text
Server response: RESULT: 17.0
```

Server output includes the client connection and the request received.

## Request protocol

The client sends one text line in this format:

```text
OPERATION|NUMBER1|NUMBER2
```

Examples:

```text
ADD|12.0|5.0
DIVIDE|20.0|4.0
MODULO|10.0|3.0
```

The server responds with either `RESULT: <value>` or an `ERROR: <message>` response. The special request `EXIT` ends that client's session.

## How it demonstrates distributed computing

1. The client and server are separate program processes.
2. They communicate using a network socket and TCP.
3. The client sends a request to the server.
4. The server performs the calculation remotely.
5. The server sends a response back to the client.
6. The server uses multithreading to serve multiple clients.

For a simple lab demonstration, both programs can run on the same computer using `localhost`. To run them on different computers, change `HOST` in `CalculatorClient.java` to the server computer's IP address and ensure port `6000` is reachable through the network and firewall.

## Limitations

- The program uses a console interface.
- The example is intended for learning and does not use encryption or authentication.
- It uses `double`, so decimal results may have normal floating-point precision limitations.
- The server runs continuously until it is stopped.
- The default client host is `localhost`.

## Troubleshooting

- If the client cannot connect, start the server first.
- If port 6000 is already in use, change `PORT` in both Java files to the same available port.
- If running across two computers, use the server's reachable IP address instead of `localhost`.
