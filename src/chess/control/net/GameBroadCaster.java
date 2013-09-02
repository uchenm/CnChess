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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GameBroadCaster extends Thread {

    public GameBroadCaster() throws IOException {
        this("GameBroadCaster");
    }

    public GameBroadCaster(String name) throws IOException {
        super(name);
        socket = new MulticastSocket(8888);
    }

    private long FIVE_SECONDS = 1000;
    private boolean founded = false;
    protected MulticastSocket socket = null;
    protected BufferedReader in = null;

    public void run() {
        while (!founded) {
            try {
                byte[] buf = new byte[256];

                // construct quote
                String dString = "WHERE_IS_GAME_CREATOR";
                buf = dString.getBytes();

                // send it
                InetAddress group = InetAddress.getByName("224.2.2.3");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 8888);
                socket.send(packet);

                // sleep for a while
                try {
                    sleep((long) (Math.random() * FIVE_SECONDS));
                } catch (InterruptedException e) {
                }

            } catch (IOException e) {
                e.printStackTrace();
                founded = false;
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        new GameBroadCaster().start();
//    }
}
