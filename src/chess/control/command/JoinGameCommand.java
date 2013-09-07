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
import java.util.Vector;

import chess.model.game.Game;
import chess.model.player.BlackRole;
import chess.model.player.ObseverRole;
import chess.model.player.Player;
import chess.model.player.RedRole;

@SuppressWarnings("serial")
public class JoinGameCommand implements Command, Serializable {

    private Player player;

    public JoinGameCommand(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void execute(Game game) {
        Vector<Player> players = game.getPlayers();
        // creator
        if (players == null || players.isEmpty()) {
            game.addPlayer(player);
        }
        // second joiner will be the opponent
        // third and others will be observers.
        if (players != null && !players.isEmpty()) {
            if (players.size() == 1 && !player.isCreator()) {
                player.setRole(players.get(0).getRole().isBlack() ? new RedRole()
                        : new BlackRole());
                game.addPlayer(player);
            } else if (players.size() >= 2) {
                player.setRole(new ObseverRole());
                game.addPlayer(player);
            }
        }

    }

}
