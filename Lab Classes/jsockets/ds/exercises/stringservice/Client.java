package ds.exercises.stringservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner scanner;

    private Client(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    private void start() throws IOException {
        String command;
        String result;

        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        while (true) {
            System.out.print("$ ");
            command = scanner.nextLine();

            if (command.equals("quit")) {
                break;
            }

            out.println(command);
            out.flush();

            result = in.readLine();
            System.out.printf("Result: %s\n", result);
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        System.out.printf("\r\nConnected to server: %s\n", client.socket.getInetAddress());
        client.start();
    }

    
}
