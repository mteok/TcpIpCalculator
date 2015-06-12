package com.teo.tcpipcalculator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        {
            String received;
            ServerSocket serverSocket = new ServerSocket(10004); //Initialize Socket at port 10002

            boolean quit = false;               //Flag for stop service
            while (!quit) {
                Socket socket = serverSocket.accept();
                System.out.println("Got connection from " + socket.getRemoteSocketAddress());
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

                while (socket.isConnected() && !socket.isClosed()) {
                    try {
                        outToClient.writeChars("> ");
                        outToClient.flush();

                        received = inFromClient.readLine();

                        //Check if string received not null
                        if (received != null) {
                            //Receving command 'quit' will disconnect client
                            if (received.contains("quit")) {
                                outToClient.writeChars("Goodbye \n");
                                socket.close();
                                break;
                            }//Receving command 'Quit' will disconnect client and stop the service
                            else if (received.contains("Quit")) {
                                outToClient.writeChars("Goodbye \n");
                                socket.close();
                                serverSocket.close();
                                quit = true;
                                break;
                            }

                            //Regular expression for matching numbers and operator
                            Pattern p = Pattern.compile("(\\d+\\.?\\d*)\\s*(\\+|\\*|\\-|/)\\s*(\\d+\\.?\\d*)");
                            Matcher m = p.matcher(received);
                            boolean b = m.matches();
                            if (b) {
                                outToClient.writeChars(result(m));
                                outToClient.flush();
                            } else {
                                outToClient.writeChars("wrong expression \n");
                                outToClient.flush();
                            }
                        }
                    } catch (SocketException e) {
                        break;
                    }
                }
            }
        }
    }

    //Method for calculate result from input string
    private static String result(Matcher m) {

        float x = Float.parseFloat(m.group(1));
        float y = Float.parseFloat(m.group(3));
        String op = m.group(2);
        String result = "";
        if (op.equals("+"))
            result = String.valueOf(x + y) + "\n";
        else if (op.equals("-"))
            result = String.valueOf(x - y) + "\n";
        else if (op.equals("*"))
            result = String.valueOf(x * y) + "\n";
        else if (op.equals("/"))
            result = String.valueOf(x / y) + "\n";
        return result;
    }

}
