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

import javax.swing.JOptionPane;

import chess.control.ChessException;
import chess.model.game.Game;
import chess.model.player.Player;

@SuppressWarnings("serial")
public class GiveupCommand implements Command, Serializable {
    private Player player;

    public GiveupCommand(Player player) {
        this.player = player;
    }

    public void execute(Game game) throws ChessException {
        // move.doMove(game);
        JOptionPane.showMessageDialog(null, player.getName() + " has given up the game!",
                "Game over", JOptionPane.INFORMATION_MESSAGE);
        // (null, player.getName()
        // + " is seeking for peace, agree?", "Seeking Peace",
        // JOptionPane.YES_NO_OPTION);

    }

}
