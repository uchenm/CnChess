package chess.control.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSender {
    public static void main(String[] args) {
        MulticastSender.sent("MingChen");
    }

    public static void sent(String msg) {
        DatagramSocket socket = null;
        DatagramPacket outPacket = null;
        byte[] outBuf;
        final int PORT = 8888;

        try {
            socket = new DatagramSocket();
            // long counter = 0;

            while (true) {
                // msg = "This is multicast! " + counter;
                // counter++;
                outBuf = msg.getBytes();

                // Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                outPacket = new DatagramPacket(outBuf, outBuf.length, address,
                        PORT);

                socket.send(outPacket);

                // System.out.println("Server sends : " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
