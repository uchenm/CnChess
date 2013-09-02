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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import chess.control.ChessException;
import chess.control.command.Command;
import chess.control.command.JoinGameCommand;
import chess.model.game.Game;

public class GameServer extends Thread {
    public static final int MAX_CLIENTS = 100;
    private int port = 9090;
    private ServerSocket serverSocket;
    private boolean isGameOn = true;
    // private MulticastSender sender;

    public Set<ClientThread> players;// = new Vector<ClientThread>();

    private static Game game;

    public GameServer() {
        try {
            // serverSocket = new ServerSocket(this.port, MAX_CLIENTS,
            // InetAddress.getLocalHost());
            serverSocket = new ServerSocket(this.port);
            players = Collections.synchronizedSet(new HashSet<ClientThread>());
            // sender = new MulticastSender();

            game = new Game();
            game.setGameType(1);

        } catch (IOException e) {
            System.out.println("Could not listen on port " + this.port);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public boolean isGameOn() {
        return isGameOn;
    }

    public void setGameOn(boolean isGameOn) {
        this.isGameOn = isGameOn;
    }

    @Override
    public void run() {
        while (isGameOn) {
            try {
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
                players.add(client);

                new Thread(client).start();
            } catch (IOException e) {
            }
        }
        // game is over, so close the socket
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ClientThread inner class
     */
    public class ClientThread implements Runnable {

        private Socket socket;
        private BufferedInputStream in = null;
        private BufferedOutputStream out = null;
        private ObjectOutputStream oo = null;
        private ObjectInputStream oi = null;

        public ClientThread(Socket sock) {
            socket = sock;
            try {
                out = new BufferedOutputStream(socket.getOutputStream());
                in = new BufferedInputStream(socket.getInputStream());

            } catch (IOException e) {
                // Log.add("error connecting to player!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (isGameOn) {
                try {
                    Object obj = read();
                    if (obj != null) {

                        if (obj instanceof Command) {
                            Command command = (Command) obj;
                            try {
                                command.execute(game);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (obj instanceof JoinGameCommand) {
                            boardcast(game);
                        } else {
                            boardcast(obj);
                        }
                    }
                } catch (ChessException e) {
                    // e.printStackTrace();
                    System.out.println(e.getMessage());
                    players.remove(this);
                    break;
                }
            }

            try {
                close();
            } catch (Exception ex) {
                ex.printStackTrace();
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

        public Object read() throws ChessException {
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

        public void send(Object obj) throws ChessException {
            try {
                // System.out.println(obj + " got on server!");
                if (oo == null)
                    oo = new ObjectOutputStream(out);
                // if (obj instanceof Game) {
                // Game g = (Game) obj;
                // System.out.println("Players in send()=" + g.getPlayers() +
                // " got on server!");
                // }
                oo.reset();
                oo.writeObject(obj);
                oo.flush();

            } catch (Exception ex) {
                // ex.printStackTrace();
                System.out.println(ex.getMessage());
                throw new ChessException(ex.getMessage(), ex);
            }
        }

        /* Send to all players on the server */
        public void boardcast(Object obj) throws ChessException {
            // System.out.println(obj + " got on server!");
            synchronized (players) {
                for (ClientThread c : players) {
                    c.send(obj);
                    if (obj instanceof Game) {
                        Game g = (Game) obj;
                        System.out.println("Players=" + g.getPlayers() + " got on server!");
                    }
                }
            }
        }
    }

    // public static void main(String[] args) {
    // new GameServer().start();
    //
    // }
}
