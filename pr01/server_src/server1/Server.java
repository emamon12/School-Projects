package server1;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 20 February 2019
 * @since 1.0, 20 February 2019 Filename: Server.java
 *
 * <p>
 * Sets up a multi-threaded UDP server with some calculation functionality.</p>
 *
 * @copyright Elijah Joshua Mamon Developed on NetBeans IDE 8.2
 */
public class Server {

    public static void main(String[] args) throws IOException {
        // listens to server on given port.
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        System.out.println("Server Info: " + serverSocket);

        //get client requests
        while (true) {
            Socket client = null;

            try {
                // socket client to receive incoming client requests 
                client = serverSocket.accept();

                System.out.println("User: " + client + " is connecting...");

                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());

                System.out.println("User: " + client + " connected. Settting up a thread to handle client...");

                Thread newClient = new ClientHandler(client, input, output);
                System.out.println("User: " + client + " handler successfully created.");

                newClient.start();

            } catch (IOException e) {
                client.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {

    final DataInputStream input;
    final DataOutputStream output;
    final Socket socket;

    final String MENU = "HELO <server-hostname> -   First comamnd to be issued by client\n"
            + "    HELP                   -   Shows the command list\n"
            + "    CALC                   -   This command must be issued before POWER/CUBE/FACT\n"
            + "    POWER <x> <e>          -   Calculates x^e\n"
            + "    CUBE <x>               -   Calculates the cubic square root of x\n"
            + "    FACT <x>               -   Calculates the factorial value of x\n"
            + "    BYE <server-hostname>  -   Closes connection";

    public ClientHandler(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        String received;
        String toClient;
        boolean noHello = true;
        boolean calcFunc = false;

        try {
            output.writeUTF("You have connected to: " + this.socket.getLocalAddress());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Command responses are seperated in sectors depending on what the client had typed
        while (true) {
            try {

                output.writeUTF("");

                received = input.readUTF();

                if (received.equalsIgnoreCase("bye")) {
                    System.out.println("User " + this.socket + " has disconnected");
                    System.out.println("Now closing connection");
                    this.socket.close();
                    System.out.println("Successfully closed connection");
                    break;
                }

                if (received.length() > 3) {
                    if (received.substring(0, 3).equalsIgnoreCase("bye")) {
                        System.out.println("User " + this.socket + " has disconnected");
                        System.out.println("Now closing connection");
                        this.socket.close();
                        System.out.println("Successfully closed connection");
                        break;
                    }
                }
                //when calc is typed
                if (calcFunc) {

                    if (received.length() <= 4
                            || (received.equalsIgnoreCase("power") && received.length() <= 5)) {

                        switch (received.toLowerCase()) {
                            case "helo":
                                toClient = "503 Hello again" + this.socket.getRemoteSocketAddress().toString();
                                output.writeUTF(toClient);
                                break;

                            case "help":
                                output.writeUTF("200 " + MENU);
                                break;

                            case "cube":
                            case "power":
                            case "fact":
                                toClient = "501 Parameters required";
                                output.writeUTF(toClient);
                                break;

                            case "calc":
                                toClient = "503 Calc already ready";
                                output.writeUTF(toClient);
                                break;

                            default:
                                output.writeUTF("500 Unrecognized Command. Type help for a list of commands");
                                break;

                        }
                    } else if (received.length() >= 5) {

                        if (received.substring(0, 5).equalsIgnoreCase("power")) {
                            try {
                                String[] stringArray;
                                int result = 1;

                                stringArray = received.split(" ");

                                int base = Integer.parseInt(stringArray[1]);
                                int exponent = Integer.parseInt(stringArray[2]);

                                for (int j = 0; j < exponent; j++) {
                                    result = result * base;
                                }

                                output.writeUTF("250 " + Integer.toString(result));

                            } catch (Exception nfe) {
                                output.writeUTF("501 ERROR: Not a valid number");
                            }
                        } else {
                            switch (received.toLowerCase().substring(0, 4)) {

                                case "helo":
                                    toClient = "503 Hello again" + this.socket.getRemoteSocketAddress().toString();
                                    output.writeUTF(toClient);
                                    break;

                                case "help":
                                    output.writeUTF("200 " + MENU);
                                    break;
                                //get the cube root of an integer
                                case "cube":
                                    try {
                                        double cubed = Integer.parseInt(received.trim().substring(5, received.trim().length()));
                                        output.writeUTF("250 " + Double.toString(Math.cbrt(cubed)));
                                        break;

                                    } catch (Exception nfe) {
                                        output.writeUTF("501 ERROR: Not a valid number");
                                        break;
                                    }
                                //get the factorial of an integer
                                case "fact":
                                    try {
                                        double factorial = Integer.parseInt(received.trim().substring(5, received.trim().length()));
                                        double findFactorial = 1;

                                        //just too lazy to write a more optimal solution
                                        for (int j = 2; j <= Math.abs(factorial); j++) {
                                            findFactorial *= j;
                                        }

                                        if (factorial < 0) {
                                            findFactorial = 0 - findFactorial;
                                        }

                                        output.writeUTF("250 " + Double.toString(findFactorial));

                                        break;

                                    } catch (Exception nfe) {
                                        output.writeUTF("501 ERROR: Not a valid number");
                                        break;
                                    }

                                default:
                                    output.writeUTF("500 Unrecognized Command. Type help for a list of commands");
                                    break;

                            }
                        }

                    }
                }
                //if typed helo, but haven't typed calc
                if (!noHello && !calcFunc) {

                    if (received.length() > 4) {
                        if (received.equalsIgnoreCase("power")) {
                            output.writeUTF("503 Specify function first. Type CALC");
                        } else if (received.substring(0, 4).equalsIgnoreCase("helo")) {
                            toClient = "503 Hello again " + socket.getRemoteSocketAddress().toString()
                                    + "(UDP)";
                            output.writeUTF(toClient);
                            noHello = false;
                        } else {
                            output.writeUTF("500 Unrecognized Command. Type help for a list of commands");
                        }
                    } else {
                        switch (received.toLowerCase()) {

                            case "cube":
                            case "power":
                            case "fact":
                                toClient = "503 Specify function first. Type CALC";
                                output.writeUTF(toClient);
                                break;

                            case "helo":
                                toClient = "503 Hello again " + this.socket.getRemoteSocketAddress().toString();
                                output.writeUTF(toClient);
                                break;

                            case "help":
                                output.writeUTF("200 " + MENU);
                                break;

                            case "calc":
                                toClient = "200 Calc Ready";
                                output.writeUTF(toClient);
                                calcFunc = true;
                                break;

                            default:
                                output.writeUTF("500 Unrecognized Command. Type help for a list of commands");
                                break;
                        }
                    }

                }
                //if haven't typed HELO first
                if (noHello) {

                    if (received.length() > 4) {
                        if (received.equalsIgnoreCase("power")) {
                            output.writeUTF("503 Why Don't Say HELO First :)");
                        } else if (received.substring(0, 4).equalsIgnoreCase("helo")) {
                            toClient = "200 Hello " + this.socket.getRemoteSocketAddress().toString()
                                    + "(UDP)";
                            output.writeUTF(toClient);
                            noHello = false;
                        } else {
                            output.writeUTF("500 Unrecognized Command. Type help for a list of commands");
                        }
                    } else {

                        switch (received.toLowerCase()) {

                            case "calc":
                            case "cube":
                            case "power":
                            case "fact":
                                toClient = "503 Why Don't Say HELO First :)";
                                output.writeUTF(toClient);
                                break;

                            case "helo":
                                toClient = "200 Hello " + this.socket.getRemoteSocketAddress().toString()
                                        + "(UDP)";
                                output.writeUTF(toClient);
                                noHello = false;
                                break;

                            case "help":
                                output.writeUTF("200 " + MENU);
                                break;

                            default:
                                output.writeUTF("503 Unrecognized Command. Type help for a list of commands");
                                break;
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.input.close();

            this.output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
