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
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GameSearchListener {
    public static void main(String[] args) throws IOException {

        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress address = InetAddress.getByName("230.0.0.1");
        socket.joinGroup(address);

        DatagramPacket packet;

        byte[] buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);

        // String dString = "I_AM_HERE";
        // buf = dString.getBytes();
        //
        // // send it
        // InetAddress group = InetAddress.getByName("230.0.0.1");
        // packet = new DatagramPacket(buf, buf.length, group, 4446);
        // socket.send(packet);
        //

        // // get a few quotes
        // for (int i = 0; i < 5; i++) {
        //
        // byte[] buf = new byte[256];
        // packet = new DatagramPacket(buf, buf.length);
        // socket.receive(packet);
        //
        // String received = new String(packet.getData(), 0,
        // packet.getLength());
        // System.out.println("Quote of the Moment: " + received);
        //
        //
        // }

        socket.leaveGroup(address);
        socket.close();
    }
}
