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

import java.io.Serializable;

import chess.control.ChessException;
import chess.model.game.Game;
import chess.model.game.Move;
import chess.model.player.Player;

@SuppressWarnings("serial")
public class TakebackCommand implements Command, Serializable {

    private Player player;

    public TakebackCommand(Player p) {
        this.player = p;
    }

    public void execute(Game game) throws ChessException {
        //

        Move move = game.getGameStack().lastElement();
        move.unDoMove(game);
        if (!player.getRole().equals(move.getMovingRole())) {
            move = game.getGameStack().lastElement();
            move.unDoMove(game);
        }
    }

}
