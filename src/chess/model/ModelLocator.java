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
package chess.model;

import chess.model.game.BoardModel;
import chess.model.game.Game;
import chess.model.player.Player;

public class ModelLocator {
    private static ModelLocator instance;

    public static ModelLocator getInstance() {
        if (instance == null) {
            synchronized (ModelLocator.class) {
                if (instance == null) {
                    instance = new ModelLocator();
                }
            }
        }
        return instance;
    }

    private ModelLocator() {
        System.out.println("constructor ModelLocator() called ");
    }

    // =========================================================================================#
    private Game       currentGame;

    // private int width;
    // private int height;

    private BoardModel board;

    private boolean    bgmusic;

    private boolean    soundEffect;

    private Player     player;

    private boolean    redOnBottom = false;

    public boolean isRedOnBottom() {
        return redOnBottom;
    }

    public void setRedOnBottom(boolean redOnBottom) {
        this.redOnBottom = redOnBottom;
    }

    public BoardModel getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.setRedOnBottom(this.player.getRole().isRed());
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }

    public boolean isSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(boolean soundEffect) {
        this.soundEffect = soundEffect;
    }

    public boolean isBgmusic() {
        return bgmusic;
    }

    public void setBgmusic(boolean bgmusic) {
        this.bgmusic = bgmusic;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

}
