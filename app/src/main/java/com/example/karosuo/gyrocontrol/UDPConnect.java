package com.example.karosuo.gyrocontrol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by karosuo on 1/08/16.
 */
public class UDPConnect {

    private static byte[] buf;
    private static String ipAddress;
    private static int port;

    public static void setup(String ipAdd, int newPort){
        ipAddress = ipAdd;
        port = newPort;
    }

    public static void sendString(String message){
        buf = message.getBytes();

        new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName(ipAddress);
                    DatagramSocket socket = new DatagramSocket();
                    if (!socket.getBroadcast()) socket.setBroadcast(true);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddress,port);
                    socket.send(packet);
                    socket.close();
                } catch (final UnknownHostException e) {
                    e.printStackTrace();
                }catch (final SocketException e) {
                    e.printStackTrace();
                } catch (final IOException e) {
                    e.printStackTrace();
                }//End try catch
            }//End new Runnable
        }).start();
    }

}
