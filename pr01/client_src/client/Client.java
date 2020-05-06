package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 20 February 2019
 * @since 1.0, 20 February 2019 
 * Filename: Client.java
 * 
 * <p>
 * Sets up a client program to connect to a server.</p>
 *
 * @copyright Elijah Joshua Mamon 
 * Developed on NetBeans IDE 8.2
 */
public class Client {

    public static void main(String[] args) throws IOException {

        try {
            DataInputStream inputToClient = null;
            DataOutputStream outputToServer = null;

            try (Scanner inputScanner = new Scanner(System.in)) {

                InetAddress serverIpAddress = InetAddress.getByName(args[0]);
                Socket clientSide = new Socket(serverIpAddress, Integer.parseInt(args[1]));

                inputToClient = new DataInputStream(clientSide.getInputStream());
                outputToServer = new DataOutputStream(clientSide.getOutputStream());
                //performs the client-server communication
                while (true) {
                    System.out.println(inputToClient.readUTF());
                    String sendToServer = inputScanner.nextLine();
                    outputToServer.writeUTF(sendToServer);

                    String received = inputToClient.readUTF();
                    System.out.print(received);

                    if (sendToServer.toLowerCase().equals("bye")) {
                        System.out.println("BYE " + clientSide.getLocalAddress() + "(UDP)");
                        System.out.println("Disconnecting to server: " + clientSide.getRemoteSocketAddress());
                        clientSide.close();
                        System.out.println("Successfully Disconnected");
                        break;
                    }

                    if (sendToServer.length() > 3 && sendToServer.substring(0, 3).equalsIgnoreCase("bye")) {
                        System.out.println("BYE " + clientSide.getLocalAddress() + "(UDP)");
                        System.out.println("Disconnecting to server: " + clientSide.getRemoteSocketAddress());
                        clientSide.close();
                        System.out.println("Successfully Disconnected");
                        break;
                    }

                }
            }catch(IOException err){
                System.err.println("Invalid Parameters");
            }
            inputToClient.close();
            outputToServer.close();
        } catch (IOException | NumberFormatException err) {
            err.printStackTrace();
        }
    }
}
