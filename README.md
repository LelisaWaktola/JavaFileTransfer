File Transfer Application
This is a simple yet robust file transfer application that allows a client to send files to a server over a network. The server receives the file, organizes it into a designated directory, and ensures a smooth transfer process. The client interface is equipped with a graphical user interface (GUI) for easy file selection and transfer.

Features
Server:
Receives files from the client over a socket connection.
Saves files in a designated directory (ReceivedFiles).
Automatically creates the directory if it does not exist.
Displays real-time file transfer status through a graphical user interface (GUI).
Handles multiple files with unique naming conventions to avoid overwriting.
Client:
Provides a user-friendly GUI for selecting and sending files.
Supports sending any file type to the server.
Ensures reliable file transfer with clear success/failure feedback.
How It Works
1. Server:
Listens for incoming file transfer requests from the client on a specified port.
Receives file metadata (file name and size) and the file content itself.
Saves the received file in the ReceivedFiles directory, prefixing the file name with received_ to avoid overwriting.
2. Client:
Allows users to select a file using a file chooser dialog.
Establishes a connection with the server and sends the selected file.
Provides real-time feedback on the file transfer process.
Technologies Used
Programming Language: Java
Socket Programming: For network communication between client and server.
Swing: For the graphical user interface.
File I/O: To handle file transfer and saving.
