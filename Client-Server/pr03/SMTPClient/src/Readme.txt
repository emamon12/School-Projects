Developed on Netbeans IDE 8.2
Java version 8 update 191 (build 1.8.0_191-b12)

To build just type make in the smtpclient directory. In Windows, type make outside the directory.

To run type: java smtpclient.SMTPClient <host-name> <port-number>

Make sure the port-number is the same as the server.

The data management happens in the current directory.

If you're going to import a key to the trust store make sure you also do the same for the server keystore.

Truststore Name: SMTPClient Truststore
Truststore Password: 1234566

To patch make sure you add the --binary option since there is whitespace.

Additional info: For extra credit, instead of using base64 encoding I used SHA-256 hashing.
