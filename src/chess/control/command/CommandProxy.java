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
package chess.control.command;

import chess.control.ChessException;
import chess.control.net.ClientProxy;
import chess.model.game.Game;

public class CommandProxy implements Command {

    private Command command;

    public CommandProxy(Command command) {
        this.command = command;
    }

    public void execute(Game game) throws ChessException {
        System.out.println("executing move in MoveCommandProxy====");
        if (game.getGameType() == 0)
            command.execute(game);
        else if (game.getGameType() == 1)
            ClientProxy.getInstance().executeRemoteCommand(command);

    }

}
