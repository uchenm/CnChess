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

import chess.model.game.GameConstants.ResultType;
import chess.model.player.Player;

@SuppressWarnings("serial")
public class GameResult implements Serializable {

    private ResultType resultType;
    private Player     redPlayer;
    private Player     blackPlayer;

    public GameResult() {

    }

    public GameResult(ResultType resultType, Player redPlayer,
            Player blackPlayer) {
        this.resultType = resultType;
        this.redPlayer = redPlayer;
        this.blackPlayer = blackPlayer;
    }

    public Player getWinner() {
        if (resultType.equals(ResultType.BLACKWIN))
            return blackPlayer;
        if (resultType.equals(ResultType.REDWIN))
            return redPlayer;
        return null;
    }

    public Player getLooser() {
        if (resultType.equals(ResultType.BLACKWIN))
            return redPlayer;
        if (resultType.equals(ResultType.REDWIN))
            return blackPlayer;
        return null;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

}
