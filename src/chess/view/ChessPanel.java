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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.UIManager;

import chess.control.GameController;
import chess.model.ModelLocator;
import chess.model.game.BoardModel;
import chess.model.game.Game;
import chess.model.game.Move;
import chess.model.stone.Location;
import chess.model.stone.Stone;

public class ChessPanel extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    // the images of the two players
    private Image[][]         images           = new Image[2][16];
    // the default toolkit of AWT
    private Toolkit           theKit;
    // the tool to help load images
    private MediaTracker      mt;

    public ChessPanel(int width, int height) throws IOException {

        setOpaque(false); // set the background transparent so that the chess
                          // board can be seen
        // this.add(new ChessButtonPanel(), BorderLayout.SOUTH);
        this.setSize(width, height);
        // initialize the toolkit
        theKit = getToolkit();
        mt = new MediaTracker(this);
        // System.out.println("width=" + this.getWidth() + ",height=" +
        // this.getHeight());

        this.addMouseListener(GameController.getInstance());
        this.setVisible(true);
    }

    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            // name = (String) arg;
            // System.out.println("NameObserver: Name changed to " + name);
        }
        updateUI();
    }

    public Game getCurrentGame() {
        return ModelLocator.getInstance().getCurrentGame();
    }

    protected void loadStone(Graphics g, Game game, Stone stone, int i, int j) {

        // images of the stone
        if (images[i][j] == null) {
            images[i][j] = theKit.getImage(stone.getImageName());
            // wait until the image is really loaded
            mt.addImage(images[i][j], 0);
            try {
                mt.waitForID(0);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        BoardModel board = ModelLocator.getInstance().getBoard();

        Location viewLoc = ViewHelper.getViewLoc(stone.getLoc());

        g.drawImage(images[i][j], board.getCoordinate(viewLoc).getX(), board
                .getCoordinate(viewLoc).getY(), board.getGridWidth(), board
                .getGridWidth(), this);

        if (stone.isSelected())
            drawSelectionLabel(g, stone, game);

    }

    protected void drawSelectionLabel(Graphics g, Stone stone, Game game) {
        Graphics2D g2d = (Graphics2D) g;
        drawSelectionLines(g2d, ViewHelper.getViewLoc(stone.getLoc()),
                Color.GREEN);
    }

    private void drawSelectionLines(Graphics2D g2d, Location loc, Color color) {
        final BoardModel board = ModelLocator.getInstance().getBoard();
        final Location coord = board.getCoordinate(loc);
        final int smallDelt = 10;
        final int smallMove = 5;
        final int deltX = board.getGridWidth();
        final int deltY = board.getGridHeight();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));

        drawTopLeftCorner(
                g2d,
                new Location(coord.getX() + smallMove, coord.getY() + smallMove),
                color, smallDelt);
        drawTopRightCorner(g2d, new Location(coord.getX() + deltX - smallMove,
                coord.getY() + smallMove), color, smallDelt);
        drawBotLeftCorner(g2d,
                new Location(coord.getX() + smallMove, coord.getY() + deltY
                        - smallMove), color, smallDelt);
        drawBotRightCorner(g2d, new Location(coord.getX() + deltX - smallMove,
                coord.getY() + deltY - smallMove), color, smallDelt);
    }

    private void drawTopLeftCorner(Graphics2D g2d, Location loc, Color color,
            final int smallDelt) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(loc.getX(), loc.getY() + smallDelt, loc.getX(), loc.getY());
        g2d.drawLine(loc.getX(), loc.getY(), loc.getX() + smallDelt, loc.getY());
    }

    private void drawTopRightCorner(Graphics2D g2d, Location loc, Color color,
            final int smallDelt) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(loc.getX(), loc.getY() + smallDelt, loc.getX(), loc.getY());
        g2d.drawLine(loc.getX(), loc.getY(), loc.getX() - smallDelt, loc.getY());
    }

    private void drawBotLeftCorner(Graphics2D g2d, Location loc, Color color,
            final int smallDelt) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(loc.getX(), loc.getY() - smallDelt, loc.getX(), loc.getY());
        g2d.drawLine(loc.getX(), loc.getY(), loc.getX() + smallDelt, loc.getY());
    }

    private void drawBotRightCorner(Graphics2D g2d, Location loc, Color color,
            final int smallDelt) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(loc.getX(), loc.getY() - smallDelt, loc.getX(), loc.getY());
        g2d.drawLine(loc.getX(), loc.getY(), loc.getX() - smallDelt, loc.getY());
    }

    protected void drawLastMoveLabel(Graphics g, Location from, Location to,
            Game game) {
        Graphics2D g2d = (Graphics2D) g;
        drawSelectionLines(g2d, ViewHelper.getViewLoc(from), Color.BLUE);
        drawSelectionLines(g2d, ViewHelper.getViewLoc(to), Color.BLUE);
    }

    public void paint(Graphics g) {

        // draw the images of stones
        Game currentGame = getCurrentGame();
        if (currentGame != null) {
            Stone[][] stones = currentGame.getStones();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 16; j++) {
                    if (stones[i][j].isAlive())
                        loadStone(g, currentGame, stones[i][j], i, j);
                }
            }
            Move move = currentGame.getLastMove();
            if (move != null)
                drawLastMoveLabel(g, move.getFrom(), move.getTo(), currentGame);
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
