package com.example.karosuo.gyrocontrol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by karosuo on 1/08/16.
 */
public class UDPConnection {

    private static byte[] buf;
    private static int port; //Destination port
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static InetAddress serverAddress;
    private static String UDPlog;
    private static String ACK_fromServer;
    private static String ACK_toCompare;
    private static boolean ACK_response;

    public static String getUDPlog(){return UDPlog;}

    public static boolean getACKStatus(){return ACK_response;}

    public static void setup(String ipAddress, int newPort){
        UDPlog = String.format("");
        ACK_fromServer = String.format("");
        ACK_response = false;
        try{
            serverAddress = InetAddress.getByName(ipAddress);

        }catch (UnknownHostException e) {
            e.printStackTrace();
        }

        port = newPort;
    }

    public static void sendString(String message){
        buf = message.getBytes();

        new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new DatagramSocket();
                    if (!socket.getBroadcast()) socket.setBroadcast(true);
                    packet = new DatagramPacket(buf, buf.length,serverAddress,port);
                    socket.send(packet);
                    socket.close();
                } catch (final UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }//End catch
            }//End new Runnable
        }).start();
    }

    public static void askACK(String message, String ACK){
        buf = message.getBytes(); //message to send
        ACK_toCompare = ACK;
        ACK_response = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new DatagramSocket();
                    packet = new DatagramPacket(buf, buf.length, serverAddress, port);
                    socket.send(packet);
                    packet = new DatagramPacket(buf, buf.length);
                    socket.setSoTimeout(5000); //Wait for the ACK
                    socket.receive(packet);
                    socket.close();
                } catch(SocketTimeoutException ex){
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ACK_fromServer = new String(packet.getData(), 0, packet.getLength());
                if (ACK_fromServer.equals(ACK_toCompare)){
                    ACK_response = true;
                }else if(ACK_fromServer.equals("Blocked")){
                    UDPlog = ACK_fromServer;
                }
            }
        }).start();
    }

}
