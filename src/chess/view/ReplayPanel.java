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
package chess.view;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.UIManager;

import chess.control.GameController;
import chess.model.game.Game;
import chess.model.game.Move;
import chess.model.stone.Stone;

public class ReplayPanel extends ChessPanel {

    private static final long serialVersionUID = 1L;

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ReplayPanel(int width, int height, Game game) throws IOException {
        super(width, height);
        this.game = game;

        // replayer
        setOpaque(false); // set the background transparent so that the chess
                          // board can be seen
        // this.add(new ChessButtonPanel(), BorderLayout.SOUTH);
        this.setSize(width, height);
        this.addMouseListener(GameController.getInstance());
        this.setVisible(true);
    }

    public Game getCurrentGame() {
        return game;
    }

    public void paint(Graphics g) {

        // draw the images of stones
        // Game currentGame = ModelLocator.getInstance().getCurrentGame();
        if (game != null) {
            Stone[][] stones = game.getStones();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 16; j++) {
                    if (stones[i][j].isAlive())
                        loadStone(g, game, stones[i][j], i, j);
                }
            }
            Move move = game.getReplayMove();
            if (move != null)
                drawLastMoveLabel(g, move.getFrom(), move.getTo(), getCurrentGame());
        }
    }

    public static void main(String[] args) {
        try {
            // set look&feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // ChessPanel
            new ChessPanel(1024, 786);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}