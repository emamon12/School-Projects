package smtpclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Base64;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 3 April 2019
 * @since 1.0, 3 April 2019 Filename: SMTPClient.java
 *
 * <p>
 * This runs both UDP and TCP Sets up a client program to connect to a server.</p>
 *
 * @copyright Elijah Joshua Mamon Developed on NetBeans IDE 8.2
 */
public class SMTPClient {

    public static void main(String[] args) throws IOException {

        boolean authFlag = false;

        String choice;

        File db = new File("./db/");
        if (!db.exists()) {
            db.mkdir();
        }

        System.out.println(
                "Would you like to send or receive emails..."
                + "type 1 to send, type anything else to receive");

        Scanner choiceScanner = new Scanner(System.in);
        choice = choiceScanner.nextLine();

        if (choice.equalsIgnoreCase(
                "1")) {
            try {
                DataInputStream inputToClient = null;
                DataOutputStream outputToServer = null;
                boolean firstLogin = true;

                try (Scanner inputScanner = new Scanner(System.in)) {

                    InetAddress serverIpAddress = InetAddress.getByName(args[0]);
                    Socket clientSide = new Socket(serverIpAddress, Integer.parseInt(args[1]));

                    inputToClient = new DataInputStream(clientSide.getInputStream());
                    outputToServer = new DataOutputStream(clientSide.getOutputStream());

                    System.out.println(inputToClient.readUTF());
                    String sendToServer = "helo";
                    outputToServer.writeUTF(sendToServer);
                    String received = inputToClient.readUTF();
                    System.out.print(received);

                    while (true) {

                        System.out.println(inputToClient.readUTF());
                        sendToServer = "auth";
                        outputToServer.writeUTF(sendToServer);
                        received = inputToClient.readUTF();
                        System.out.print(received);

                        System.out.println(inputToClient.readUTF());

                        if (firstLogin) {
                            while (firstLogin) {
                                sendToServer = inputScanner.nextLine();
                                if (sendToServer.endsWith("@cs447.edu")) {
                                    firstLogin = false;
                                    break;
                                }
                                System.out.println("invalid username, must contain a @cs447.edu suffix");
                            }
                        } else {
                            sendToServer = inputScanner.nextLine();
                        }

                        sendToServer = (Base64.getEncoder().encodeToString(sendToServer.getBytes()));
                        outputToServer.writeUTF(sendToServer);
                        received = inputToClient.readUTF();
                        System.out.print(received);

                        System.out.println(inputToClient.readUTF());
                        sendToServer = inputScanner.nextLine();
                        sendToServer = (Base64.getEncoder().encodeToString(sendToServer.getBytes()));
                        outputToServer.writeUTF(sendToServer);
                        received = inputToClient.readUTF();
                        System.out.print(received);

                        if (inputToClient.readUTF().equals("200 user authenticated. Type rcpt to add recipients.")) {
                            break;
                        }

                    }

                    while (true) {

                        System.out.println(inputToClient.readUTF());
                        sendToServer = inputScanner.nextLine();
                        outputToServer.writeUTF(sendToServer);

                        if (sendToServer.toLowerCase().equals("quit")) {
                            System.out.println("200 BYE " + clientSide.getLocalAddress() + "(TCP)");
                            System.out.println("Disconnecting to server: " + clientSide.getRemoteSocketAddress());
                            clientSide.close();
                            System.out.println("Successfully Disconnected");
                            break;
                        }

                        received = inputToClient.readUTF();
                        System.out.print(received);

                    }
                } catch (IOException err) {
                    System.err.println("200 Closing Connection");
                    inputToClient.close();
                    outputToServer.close();
                }
                inputToClient.close();
                outputToServer.close();
            } catch (IOException | NumberFormatException err) {
                err.printStackTrace();
            }
        } else {
            String userIn;
            boolean running = true;
            boolean notDone = true;
            String username = null;
            
            //the datagram takes the port given - 1 to prevent binding conflicts with TCP
            //nuke verything wwhen the UDP is done
            DatagramSocket cSocket = new DatagramSocket(Integer.parseInt(args[1]) - 1);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            byte[] sendData = new byte[8192];
            byte[] receiveData = new byte[8192];

            boolean notAuthenticated = true;
            InetAddress ip = InetAddress.getByName(args[0]);
            sendData = "helo".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
            cSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            cSocket.receive(receivePacket);

            System.out.println(stringify(receiveData));
            receiveData = new byte[8192];

            sendData = "auth".getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
            cSocket.send(sendPacket);

            do {

                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                cSocket.receive(receivePacket);
                System.out.println(stringify(receiveData));
                receiveData = new byte[8192];

                userIn = input.readLine();
                username = userIn;
                sendData = userIn.getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
                cSocket.send(sendPacket);

                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                cSocket.receive(receivePacket);
                System.out.println(stringify(receiveData));
                receiveData = new byte[8192];

                userIn = input.readLine();
                sendData = userIn.getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
                cSocket.send(sendPacket);

                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                cSocket.receive(receivePacket);
                System.out.println(stringify(receiveData));

                String checkAuth = stringify(receiveData).toString();
                receiveData = new byte[8192];

                if (!checkAuth.substring(0, 3).equals("250")) {
                    while (notDone) {
                        String response;
                        int numEmail;
                        int numGet;

                        File userFile = new File("./db/" + username);
                        if (!userFile.exists()) {
                            userFile.mkdir();
                        }
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        cSocket.receive(receivePacket);
                        System.out.println(stringify(receiveData));
                        receiveData = new byte[8192];

                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        cSocket.receive(receivePacket);
                        numEmail = Integer.parseInt(stringify(receiveData).toString());
                        receiveData = new byte[8192];

                        numGet = Integer.parseInt(input.readLine());
                        sendData = Integer.toString(numGet).getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
                        cSocket.send(sendPacket);

                        if (numGet > numEmail) {
                            numGet = numEmail;
                        }

                        for (int i = 1; i <= numGet; i++) {
                            receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            cSocket.receive(receivePacket);

                            try (BufferedWriter emailWriter = new BufferedWriter(new FileWriter("./db/" + username + "/" + i + ".txt"))) {
                                emailWriter.newLine();
                                emailWriter.write(stringify(receiveData).toString());
                            }
                            receiveData = new byte[8192];
                        }
                        notDone = false;
                        break;
                    }
                }
                if (!notDone) {
                    break;
                }
                sendData = "auth".getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, ip, Integer.parseInt(args[1]));
                cSocket.send(sendPacket);

            } while (notAuthenticated);

            System.out.println("221 closing connection...");
            input.close();
            cSocket.close();
        }

    }

    public static StringBuilder stringify(byte[] a) {

        if (a == null) {
            return null;
        }

        StringBuilder newString = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            newString.append((char) a[i]);
            i++;
        }
        return newString;
    }

}
