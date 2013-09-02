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

import chess.control.ChessException;
import chess.control.command.Command;

public class ClientProxy {
    private static ClientProxy instance;

    public static ClientProxy getInstance() {
        if (instance == null) {
            synchronized (ClientProxy.class) {
                if (instance == null) {
                    instance = new ClientProxy();
                }
            }
        }
        return instance;
    }

    private ClientProxy() {
    }

    // ===========================================================================#

    private GameClient client;

    public GameClient getClient() {
        return client;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    // public void initGame() {
    // this.client.writeData("init game");
    // }

    public void executeRemoteCommand(Command command) {
        try {
            this.client.writeData(command);
        } catch (ChessException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        this.client.start();
    }

    // public void makeMove(Move move) {
    // this.client.writeData(move);
    // }
}
