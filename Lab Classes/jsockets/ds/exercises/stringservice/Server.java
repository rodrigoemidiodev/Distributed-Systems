package ds.exercises.stringservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;

    public Server(String ipAddress) throws Exception {
        this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
    }

    private void listen() throws Exception {
        while (true) {
            Socket client = this.server.accept();
            String clientAddress = client.getInetAddress().getHostAddress();
            System.out.printf("\r\nNew connection from %s\n", clientAddress);
            new Thread(new ConnectionHandler(client)).start();
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        Server app = new Server(args[0]);
        System.out.printf("\r\nRunning server: host=%s @ port=%d\n", app.getSocketAddress().getHostAddress(), app.getPort());
        app.listen();
    }
}

class ConnectionHandler implements Runnable {
    private Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                String command = in.readLine();

                if (command == null) break;

                System.out.printf("Command received: %s\n", command);

                String[] parts = command.split(":");
                String operation = parts[0];
                String result;

                switch (operation) {
                    case "length":
                        result = String.valueOf(parts[1].length());
                        break;
                    case "equal":
                        result = String.valueOf(parts[1].equals(parts[2]));
                        break;
                    case "cat":
                        result = parts[1] + parts[2];
                        break;
                    case "break":
                        char delimiter = parts[2].charAt(0);
                        String[] tokens = parts[1].split(String.valueOf(delimiter));
                        result = String.join(",", tokens);
                        break;
                    default:
                        result = "Unknown operation";
                }

                out.println(result);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
