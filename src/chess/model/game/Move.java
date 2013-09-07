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
package chess.model.game;

import java.io.Serializable;

import chess.model.game.GameConstants.GameState;
import chess.model.game.GameConstants.ResultType;
import chess.model.player.Role;
import chess.model.stone.Location;
import chess.model.stone.Stone;

public class Move implements Serializable {

    private static final long serialVersionUID = 1L;

    private Stone             stone;
    private Location          from;
    private Location          to;
    private Stone             target;

    public Move(Stone stone, Location from, Location to, Stone target) {
        this.stone = stone;
        this.from = from;
        this.to = to;
        this.target = target;
    }

    public Stone getTarget() {
        return target;
    }

    public Stone getStone() {
        return stone;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Role getMovingRole() {
        return stone.getOwner();
    }

    public void doMove(Game game) {
        // do real things
        Stone moveStone = game.getStoneWithId(stone.getId());
        moveStone.setLoc(to);

        if (target != null) {
            Stone targetStone = game.getStoneWithId(target.getId());
            targetStone.setAlive(false);
        }
        // fire move event and killing event
        game.setStatusMessage(game.getOwner(stone.getOwner()).getName()
                + ":"
                + stone.getName()
                + " moved from "
                + from
                + " to "
                + to
                + (target != null ? "killing " + target.getOwner().getName()
                        + "'s " + target.getName() : ""));

        // play move sound
        // MusicUtil.playSound("eat");
        // write move history to stack

        game.getGameStack().push(this);

        // switch role
        game.switchRole();

        moveStone.setSelected(false);
        // judge if game is over and if General is in Danger
        if (game.isInTrouble(game.getCurrentRoleToPlay())) {
            if (game.isLoosingGame(game.getCurrentRoleToPlay())) {
                game.setStatusMessage(game
                        .getOwner(game.getCurrentRoleToPlay()).getName()
                        + " lost the game!!");
                ResultType resultType = game.getCurrentRoleToPlay().isRed() ? ResultType.BLACKWIN
                        : ResultType.REDWIN;
                GameResult gameResult = new GameResult();
                gameResult.setResultType(resultType);
                gameResult.setBlackPlayer(game.getBlackPlayer());
                gameResult.setRedPlayer(game.getRedPlayer());

                game.setGameResult(gameResult);

                game.setGameState(GameState.GAME_OVER);

                game.saveGame();
            } else {
                game.setStatusMessage(game
                        .getOwner(game.getCurrentRoleToPlay()).getName()
                        + "'s General is in trouble!!!, please take action!!!");
            }
        }

        game.updateObsevers();

    }

    public void unDoMove(Game game) {
        Stone movingStone = game.getStoneWithId(stone.getId());

        movingStone.setLoc(from);
        if (target != null) {
            Stone targetStone = game.getStoneWithId(target.getId());
            targetStone.setAlive(true);
        }

        // play move sound
        // MusicUtil.playSound("eat");
        // switch role back
        game.switchRole();

        game.getGameStack().pop();

        // judge if game is over and if General is in Danger
        if (game.isInTrouble(game.getCurrentRoleToPlay())) {
            if (game.isLoosingGame(game.getCurrentRoleToPlay())) {
                game.setStatusMessage(game
                        .getOwner(game.getCurrentRoleToPlay()).getName()
                        + " lost the game!!");
            } else {
                game.setStatusMessage(game
                        .getOwner(game.getCurrentRoleToPlay()).getName()
                        + "'s General is in trouble!!!, please take action!!!");
            }
        }

        movingStone.setSelected(false);
        game.updateObsevers();
    }

}
