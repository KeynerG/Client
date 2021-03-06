package com.cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Clase encargada de crear la conexión con el servidor por medio de Sockets.
 */
public class Client
{
    final static int ServerPort = 1234;

    /**
     *
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);

        InetAddress ip = InetAddress.getLocalHost();
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());



        // sendMessage thread
        /**
         * Hilo encargado de enviar mensajes.
         */
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {

                        // write on the output stream
                        dos.writeUTF(msg);
                        if (msg == "logout"){
                            s.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    dis.close();
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // readMessage thread
        /**
         *  Hilo encargado de enviar mensajes.
         */
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }
}
