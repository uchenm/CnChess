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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chess.control.ChessException;
import chess.control.command.Command;
import chess.model.ModelLocator;
import chess.model.game.Game;

public class GameClient extends Thread {

    private Socket socket = null;
    private BufferedInputStream in = null;
    private BufferedOutputStream out = null;
    private ObjectOutputStream oo = null;
    private ObjectInputStream oi = null;
    private String serverip;
    private int port;

    public GameClient(String serverIp, int port) {
        this.serverip = serverIp;
        this.port = port;
        try {
            socket = new Socket(serverip, this.port);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (Exception e2) {
        }

    }

    public void run() {
        while (true) {
            try {
                Object obj = readData();
                if (obj != null) {
                    System.out.println(obj + "====");

                    if (obj instanceof Game) {
                        Game serverGame = (Game) obj;

                        System.out.println("Received players=" + serverGame.getPlayers());
                        // do synchronize with the server
                        // ModelLocator.getInstance().setCurrentGame(game);
                        ModelLocator.getInstance().getCurrentGame().syncWithServer(serverGame);
                        // ModelLocator.getInstance().getCurrentGame().
                        // GameController.getInstance().fireChessEvent(new
                        // ChessMessage("update UI"));
                    }
                    if (obj instanceof Command) {
                        Command command = (Command) obj;
                        command.execute(ModelLocator.getInstance().getCurrentGame());
                    }
                }
            } catch (ChessException e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
                break;
            }

        }
        close();
    }

    public void writeData(Object obj) throws ChessException {
        try {
            if (oo == null)
                oo = new ObjectOutputStream(out);
            oo.writeObject(obj);
            oo.flush();
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
            throw new ChessException(e.getMessage(), e);
        }
    }

    public Object readData() throws ChessException {
        Object obj = null;
        try {
            if (oi == null)
                oi = new ObjectInputStream(in);
            obj = oi.readObject();

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
            throw new ChessException(e.getMessage(), e);
        }
        return obj;
    }

    // public static void main(String[] args) throws Exception {
    //
    // GameClient client = new GameClient("192.168.1.104", 9090);
    // client.writeData(new String("test=="));
    // System.out.println(client.readData() + "===");
    // }

}
