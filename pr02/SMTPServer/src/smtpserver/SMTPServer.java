package smtpserver;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 3 April 2019
 * @since 1.0, 3 April 2019 Filename: SMTPServer.java
 *
 * <p>
 * The server that handles both UDP and TCP connections of the client.</p>
 *
 * @copyright Elijah Joshua Mamon Developed on NetBeans IDE 8.2
 */
public class SMTPServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        File db = new File("db");
        //creaetes folder whenever startup
        if (!db.exists()) {
            db.mkdir();
        }

        System.out.println("Server Info: " + serverSocket);
        //creates a thread for UDP, when it ends, the server just closes.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(args[0]))) {
                    String username = null;
                    String password;

                    int numMail;

                    byte[] receiveData = new byte[8192];
                    byte[] sendData = new byte[8192];

                    DatagramPacket receivePacket = null;
                    DatagramPacket sendPacket = null;

                    boolean notAuthenticated = true;
                    boolean notDone = true;
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    InetAddress address = receivePacket.getAddress();

                    System.out.println(stringify(receiveData));

                    sendData = ("200 Hello" + address.toString()).getBytes();

                    try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                        toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "200 Hello.");
                        toFile.newLine();
                    }

                    sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                    serverSocket.send(sendPacket);
                    receiveData = new byte[8192];

                    while (notAuthenticated) {
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        System.out.println(stringify(receiveData));

                        receiveData = new byte[8192];
                        sendData = ("200 auth... \n334 Please input username".getBytes());
                        sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                        serverSocket.send(sendPacket);

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "334 Username Input.");
                            toFile.newLine();
                        }

                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        username = (stringify(receiveData).toString());
                        receiveData = new byte[8192];

                        sendData = ("334 please input password".getBytes());
                        sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                        serverSocket.send(sendPacket);

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "334 Username Input.");
                            toFile.newLine();
                        }

                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        password = (stringify(receiveData).toString());
                        receiveData = new byte[8192];

                        File ussFile = new File("./db/" + username);
                        if (ussFile.exists()) {
                            try (BufferedReader fileIn = new BufferedReader(new FileReader("./db/" + username + "/.user_pass.txt"))) {
                                String passwordCheck = fileIn.readLine();
                                if (passwordCheck.equals(password)) {
                                    notAuthenticated = false;

                                    sendData = ("200 authentication good.".getBytes());
                                    sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                                    serverSocket.send(sendPacket);

                                    try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                        toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "200 authentication good.");
                                        toFile.newLine();
                                    }

                                } else {
                                    sendData = ("250 invalid username or password.".getBytes());
                                    sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                                    serverSocket.send(sendPacket);

                                    try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                        toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "250 invalid username or password.");
                                        toFile.newLine();
                                    }
                                }
                            }
                        } else {
                            sendData = ("204 invalid username or password".getBytes());
                            sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                            serverSocket.send(sendPacket);

                            try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "250 invalid username or password.");
                                toFile.newLine();
                            }

                        }
                    }

                    int numGet = 0;
                    numMail = 0;
                    int emailCounter = 1;
                    File emailFile = new File("./db/" + username + "/" + emailCounter + ".email");
                    while (emailFile.exists()) {
                        numMail++;
                        emailCounter++;
                        emailFile = new File("./db/" + username + "/" + emailCounter + ".email");
                    }

                    sendData = (("250 You have " + numMail + " emails in the database\nType the number of Emails to download").getBytes());
                    sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                    serverSocket.send(sendPacket);

                    sendData = (Integer.toString(numMail).getBytes());
                    sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                    serverSocket.send(sendPacket);

                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    numGet = (Integer.parseInt(stringify(receiveData).toString()));
                    receiveData = new byte[8192];

                    if (numGet > numMail) {
                        numGet = numMail;
                    }

                    for (int i = 1; i <= numGet; i++) {
                        StringBuilder result = new StringBuilder();
                        SimpleDateFormat formatt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                        File file = new File("./db/" + username + "/" + i + ".email");

                        result.append("HTTP/1.1 200 OK\n");
                        result.append("Server: ").append(InetAddress.getLocalHost().getHostName()).append("\n");
                        result.append("Last Modified: ").append(formatt.format(file.lastModified())).append("\n");
                        result.append("Count: ").append(numGet).append("\n");
                        result.append("Content-Type: text/plain").append("\n");
                        result.append("Message: ").append(i).append("\n");

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + InetAddress.getLocalHost() + " To: " + username + "400 URL found");
                            toFile.newLine();
                        }

                        String path = file.getAbsolutePath();
                        URL url = new URL("file:///" + path);
                        URLConnection conn = url.openConnection();
                        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                            String line;
                            while ((line = rd.readLine()) != null) {
                                result.append("\n");
                                result.append(line);
                            }
                            sendData = (result.toString().getBytes());
                            sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(args[0]) - 1);
                            serverSocket.send(sendPacket);
                        }

                    }
                    System.out.println("Closing udp socket.");
                    serverSocket.close();

                } catch (IOException ioe) {
                    System.err.println("Cannot open the port.");
                    ioe.printStackTrace();
                } finally {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SMTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Closing UDP.");
                }
            }
        }
        ).start();

        //get client requests
        while (true) {
            Socket client = null;

            try {
                // serverSocket client to receive incoming client requests 
                client = serverSocket.accept();

                System.out.println("User: " + client + " is connecting...");

                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());

                System.out.println("User: " + client + " connected. Settting up a thread to handle client...");

                Thread newClientTCP = new ClientHandler(client, input, output);
                System.out.println("User: " + client + " handler successfully created.");

                newClientTCP.start();

            } catch (IOException e) {
                client.close();
                e.printStackTrace();
            }
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
//Client Handler handles the TCP connection. This is a linear execution starting from the method from the bottom.
//Uses flags to determine the state of the server and the client.

class ClientHandler extends Thread {

    final DataInputStream input;
    final DataOutputStream output;
    final Socket socket;

    final String MENU = "HELO <server-hostname> -   First comamnd to be issued by client\n"
            + "    HELP                   -   Shows the command list\n"
            + "    AUTH                   -   Used to authenticate user\n"
            + "    MAIL                   -   Configures the sender of the email\n"
            + "    RCPT                   -   Configures the recepients of the email\n"
            + "    DATA                   -   Configures Data of the email\n"
            + "    QUIT                   -   Closes connection";

    // Constructor 
    public ClientHandler(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        String received;
        String toClient;
        String subject;
        String body;

        boolean noHello = true;
        boolean noAuth = true;
        boolean invalidProf = true;
        boolean usernameFound = false;
        boolean userAuthenticated = false;
        boolean rcptFlag = false;
        boolean rcptDone = false;
        boolean readyForData = false;

        ArrayList<String> rcptArry = new ArrayList<>();

        String username = "";
        String password;

        try {
            output.writeUTF("You have connected to: " + this.socket.getLocalAddress());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {

                output.writeUTF("");

                received = input.readUTF();

                if (received.equalsIgnoreCase("quit")) {
                    System.out.println("User " + this.socket + " has disconnected.");
                    System.out.println("Now closing connection");
                    this.socket.close();
                    System.out.println("Successfully closed connection.");
                    break;
                }

                if (readyForData) {
                    subject = received;
                    toClient = "200 input body below.";
                    output.writeUTF(toClient);

                    received = input.readUTF();
                    body = received;

                    int emailCounter = 1;
                    boolean emailExist;

                    for (String rcptArry1 : rcptArry) {
                        emailExist = false;
                        File ussFile = new File("./db/" + rcptArry1);
                        if (ussFile.exists()) {
                            System.out.println("250 file username found.");

                            try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + "250 file username found.");
                                toFile.newLine();
                            }

                            File emailFile = new File("./db/" + rcptArry1 + "/" + emailCounter + ".email");
                            while (emailFile.exists()) {
                                System.out.println(emailCounter);
                                emailCounter = emailCounter + 1;
                                emailFile = new File("./db/" + rcptArry1 + "/" + emailCounter + ".email");
                            }
                            try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                toFile.write(LocalDateTime.now() + " From: " + username + " To: " + rcptArry1 + "201 created" + emailCounter + ".email");
                                toFile.newLine();
                            }
                            try (BufferedWriter emailWriter = new BufferedWriter(new FileWriter("./db/" + rcptArry1 + "/" + emailCounter + ".email"))) {
                                SimpleDateFormat formatt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                                emailWriter.write("Date:    " + formatt.format(new Date()));
                                emailWriter.newLine();
                                emailWriter.write("From:    " + username);
                                emailWriter.newLine();
                                emailWriter.write("To:    " + rcptArry1);
                                emailWriter.newLine();
                                emailWriter.write("Subject: " + subject);
                                emailWriter.newLine();
                                emailWriter.newLine();
                                emailWriter.write(body);
                            }
                            emailCounter = 1;
                        }
                    }
                    rcptFlag = false;
                    rcptDone = false;
                    readyForData = false;
                    rcptArry.clear();
                    output.writeUTF("");

                    output.writeUTF("200 Type rcpt again to send more mail or type quit to end session.");
                    received = input.readUTF();

                    if (received.equalsIgnoreCase("quit")) {
                        System.out.println("User " + this.socket + " has disconnected.");
                        System.out.println("Now closing connection.");
                        this.socket.close();
                        System.out.println("200 Successfully closed connection.");
                        break;
                    }

                }
                if (rcptDone && !readyForData) {
                    if (received.length() < 4) {
                        output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                    } else {
                        switch (received.substring(0, 4)) {
                            case "helo":
                                toClient = "503 Hello again " + this.socket.getRemoteSocketAddress().toString() + ".";
                                output.writeUTF(toClient);
                                break;
                            case "auth":
                                toClient = "503 User already authenticated.";
                                output.writeUTF(toClient);
                                break;
                            case "mail":
                                toClient = "503 Illegal action, user can only send emails from the authenticated user.";
                                output.writeUTF(toClient);
                                break;
                            case "rcpt":
                                toClient = "503 Recepients already specified";
                                output.writeUTF(toClient);
                                break;
                            case "data":
                                toClient = "200 Enter Data below Starting with the Subject.";
                                output.writeUTF(toClient);
                                readyForData = true;
                                break;
                            case "help":
                                output.writeUTF("214 " + MENU);
                                break;
                            default:
                                output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                                break;
                        }

                    }
                }
                if (rcptFlag && !rcptDone) {
                    if (received.length() < 4) {
                        toClient = "200 type in the users to receive the mail below (can accept multiple recipients, type 'done' when done).";
                        output.writeUTF(toClient);
                        rcptArry.add(received);
                    } else {
                        switch (received.substring(0, 4)) {
                            case "done":
                                rcptDone = true;
                                toClient = "200 rcpt OK... Type data to begin creating email.";
                                output.writeUTF(toClient);

                                try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                    toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + toClient);
                                    toFile.newLine();
                                }

                                break;
                            default:
                                toClient = "200 type in the users to receive the mail below (can accept multiple recipients, type 'done' when done).";
                                output.writeUTF(toClient);
                                rcptArry.add(received);
                                break;
                        }

                    }
                }

                if (userAuthenticated && !rcptFlag) {
                    if (received.length() < 4) {
                        output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                    } else {
                        switch (received.substring(0, 4)) {
                            case "helo":
                                toClient = "503 Hello again " + this.socket.getRemoteSocketAddress().toString();
                                output.writeUTF(toClient);
                                break;
                            case "auth":
                                toClient = "503 User already authenticated.";
                                output.writeUTF(toClient);

                                try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                    toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + toClient);
                                    toFile.newLine();
                                }

                                break;
                            case "mail":
                                toClient = "503 Illegal action, user can only send emails from the authenticated user.";
                                output.writeUTF(toClient);

                                try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                    toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + toClient);
                                    toFile.newLine();
                                }

                                break;
                            case "rcpt":
                                toClient = "200 type in the users to receive the mail below (can accept multiple recipients, type 'done' when done). ";
                                output.writeUTF(toClient);
                                rcptFlag = true;
                                break;
                            case "help":
                                output.writeUTF("214 " + MENU);
                                break;
                            default:
                                output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                                break;
                        }
                    }

                }

                if (usernameFound && !userAuthenticated) {
                    String passwordCheck;
                    byte[] decoded = Base64.getDecoder().decode(received);
                    password = (new String(decoded, "UTF-8"));
                    System.out.println(password);
                    try (BufferedReader fileIn = new BufferedReader(new FileReader("./db/" + username + "/.user_pass.txt"))) {
                        passwordCheck = fileIn.readLine();
                    }
                    System.out.println(passwordCheck);
                    if (password.equals(passwordCheck)) {
                        userAuthenticated = true;
                        output.writeUTF("200 user authenticated. Type rcpt to add recipients.");
                        output.writeUTF("200 user authenticated. Type rcpt to add recipients.");

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + "200 user authenticated.");
                            toFile.newLine();
                        }
                    } else {
                        noAuth = true;
                        invalidProf = true;
                        usernameFound = false;

                        output.writeUTF("204 invalid username or password. Please try again.");
                        output.writeUTF("204 invalid username or password. Please try again.");

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + "204 invalid username or password. Please try again.");
                            toFile.newLine();
                        }

                        received = input.readUTF();
                        output.writeUTF("");

                        if (received.equalsIgnoreCase("quit")) {
                            System.out.println("User " + this.socket + " has disconnected.");
                            System.out.println("Now closing connection.");
                            this.socket.close();
                            System.out.println("Successfully closed connection.");
                            break;
                        }
                    }

                }
                if (!noAuth && !userAuthenticated) {
                    byte[] decoded = Base64.getDecoder().decode(received);
                    File userFile = new File("./db/" + (new String(decoded, "UTF-8")));
                    if (!userFile.exists()) {
                        userFile.mkdir();
                        decoded = Base64.getDecoder().decode(received);
                        username = (new String(decoded, "UTF-8"));
                        int randPass = (int) (Math.random() * 99998 + 1);

                        String passEncoded = (Integer.toString(randPass));
                        passEncoded = (Base64.getEncoder().encodeToString(passEncoded.getBytes()));

                        toClient = "330 " + passEncoded;
                        output.writeUTF(toClient + "    (User " + username + " registed. Now closing connection, please reconnect with appropriate info)\n"
                                + "\nPress enter to continue...");

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                            toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + "330 (User " + username + " registed. Now closing connection, please reconnect with appropriate info)\n"
                                    + "\nPress enter to continue...");
                            toFile.newLine();
                        }

                        randPass = randPass + 447;
                        passEncoded = (Integer.toString(randPass));
                        passEncoded = (Base64.getEncoder().encodeToString(passEncoded.getBytes()));

                        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("./db/" + (new String(decoded, "UTF-8")) + "/.user_pass.txt"))) {
                            toFile.write(passEncoded);
                            toFile.newLine();
                        }
                        System.out.println("User " + this.socket + " has disconnected/");
                        System.out.println("Now closing connection/");
                        this.socket.close();
                        System.out.println("Successfully closed connection/");
                        break;
                    }
                    decoded = Base64.getDecoder().decode(received);
                    username = (new String(decoded, "UTF-8"));
                    String passHold = "password";
                    passHold = (Base64.getEncoder().encodeToString(passHold.getBytes()));
                    toClient = "334 " + passHold + " Type in Password.";
                    output.writeUTF(toClient);
                    usernameFound = true;
                }

                if (!noHello && noAuth && invalidProf && !userAuthenticated) {
                    switch (received.toLowerCase().substring(0, 4)) {
                        case "helo":
                            toClient = "503 Hello again " + this.socket.getRemoteSocketAddress().toString();
                            output.writeUTF(toClient);
                            break;
                        case "auth":
                            String userEncoded = "username:";
                            userEncoded = (Base64.getEncoder().encodeToString(userEncoded.getBytes()));
                            toClient = "334 " + userEncoded + " Type in Username.";
                            output.writeUTF(toClient);
                            noAuth = false;
                            break;
                        case "mail":
                        case "rcpt":
                        case "data":
                            toClient = "503 user not authorized yet.";
                            output.writeUTF(toClient);
                            break;

                        case "help":
                            output.writeUTF("214 " + MENU);
                            break;
                        default:
                            output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                            break;
                    }
                }

                if (noHello && !userAuthenticated) {
                    if (received.length() >= 4) {
                        switch (received.toLowerCase().substring(0, 4)) {
                            case "auth":
                            case "mail":
                            case "rcpt":
                            case "data":
                                toClient = "503 Why Don't Say Hello First :).";
                                output.writeUTF(toClient);
                                break;

                            case "helo":
                                toClient = "200 Hello " + this.socket.getRemoteSocketAddress().toString()
                                        + "(TCP)";
                                output.writeUTF(toClient);
                                noHello = false;

                                try (BufferedWriter toFile = new BufferedWriter(new FileWriter(".server_log.txt", true))) {
                                    toFile.write(LocalDateTime.now() + " From: " + this.socket.getLocalAddress() + " To: " + this.socket.getLocalSocketAddress() + toClient);
                                    toFile.newLine();
                                }

                                break;

                            case "help":
                                output.writeUTF("214 " + MENU);
                                break;

                            default:
                                output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                                break;
                        }
                    } else {
                        output.writeUTF("502 Unrecognized Command. Type help for a list of commands.");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("221 Closing connection.");
            this.input.close();

            this.output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
