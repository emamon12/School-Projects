Developed on Netbeans IDE 8.2
Java version 8 update 191 (build 1.8.0_191-b12)

To build just type make in the smtpserver directory. In Windows, type make outside the directory.

To run type: java smtpserver.SMTPServer <port-number>

Make sure the port-number is the same as the client.

Data management happens in the current directory.

If you're going to import a key to the key store make sure you also do the same for the client trust store.

Keystore Name: SMTPClientKeystore
Keystore Password: 123456

To patch make sure you add the --binary option since there is whitespace.

Additional info: For extra credit, instead of using base64 encoding I used SHA-256 hashing.
