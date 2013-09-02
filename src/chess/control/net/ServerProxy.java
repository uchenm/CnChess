/**
 * Copyright 2013  Ming Chen<uchenm@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package chess.control.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerProxy {

    private static ServerProxy instance;

    private GameServer server;

    public static ServerProxy getInstance() {
        if (instance == null) {
            synchronized (ServerProxy.class) {
                if (instance == null) {
                    instance = new ServerProxy();
                }
            }
        }
        return instance;
    }

    private ServerProxy() {
        server = new GameServer();
    }

    // ======================================================================#
    public void startServer() {
        server.setGameOn(true);
        server.start();

        new MulticastSender().start();
    }

    public void stopServer() {
        server.setGameOn(false);
    }

    public class MulticastSender extends Thread {

        private DatagramSocket dcpSocket = null;
        private DatagramPacket outPacket = null;
        private InetAddress address;
        private byte[] outBuf;
        private final int PORT = 8888;

        public MulticastSender() {
            try {
                dcpSocket = new DatagramSocket();
                address = InetAddress.getByName("224.2.2.3");
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        /**
         * // Send msg to multicast IP address and port
         * 
         * @param msg
         */

        public void run() {
            String msg = "I am here";
            try {
                dcpSocket = new DatagramSocket();
                while (server.isGameOn()) {
                    outBuf = msg.getBytes();
                    outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
                    dcpSocket.send(outPacket);
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
}
