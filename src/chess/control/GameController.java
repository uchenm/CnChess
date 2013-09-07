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

package chess.control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import chess.control.command.CommandProxy;
import chess.control.command.GiveupCommand;
import chess.control.command.MoveCommand;
import chess.control.command.RequestCommand;
import chess.control.command.SeekPeaceCommand;
import chess.control.command.TakebackCommand;
import chess.control.event.ChessEvent;
import chess.control.event.ChessEventListener;
import chess.control.event.ChessMessage;
import chess.model.ModelLocator;
import chess.model.game.Game;
import chess.model.game.GameConstants.GameState;
import chess.model.game.Move;
import chess.model.player.Role;
import chess.model.stone.Location;
import chess.model.stone.Stone;
import chess.view.ViewHelper;

public class GameController extends MouseAdapter {

    private Vector<ChessEventListener> listeners = new Vector<ChessEventListener>();

    private static GameController      instance;

    public static GameController getInstance() {

        if (instance == null) {
            synchronized (GameController.class) {
                if (instance == null) {
                    instance = new GameController();
                }
            }
        }

        return instance;
    }

    private GameController() {

    }

    public void addChessEventListener(ChessEventListener listener) {
        listeners.add(listener);
    }

    public void fireChessEvent(ChessMessage message) {
        Enumeration<ChessEventListener> e = listeners.elements();
        while (e.hasMoreElements()) {
            ChessEventListener el = (ChessEventListener) e.nextElement();
            el.onChessEvent(new ChessEvent(message));
        }
    }

    public void newGame() {
        try {
            // new NewGameCommand().execute();
            Game currentGame = new Game();
            ModelLocator.getInstance().setCurrentGame(currentGame);
            // Player ming=new Player("Ming",new RedRole());
            // Player allen=new Player("Allen",new BlackRole());

            // currentGame.addPlayer(ming);
            // currentGame.addPlayer(allen);
            // ModelLocator.getInstance().setGameType(0);

            // ModelLocator.getInstance().setPlayer(ming);
            // GameController.getInstance().getChessPanel().updateUI();
            // GameController.getInstance().fireChessEvent("updateUI");

            // GameController.getInstance().fireChessEvent(new
            // ChessMessage("update UI"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent e) {
        Game cgame = ModelLocator.getInstance().getCurrentGame();
        if (cgame != null
                && cgame.getGameState().equals(GameState.GAME_ON)
                && (cgame.getGameType() == 0 || cgame.getCurrentRoleToPlay()
                        .equals(ModelLocator.getInstance().getPlayer()
                                .getRole()))) {
            System.out.println("x=" + e.getX() + ",y=" + e.getY());

            Location clickLoc = ModelLocator.getInstance().getBoard()
                    .getLoc(e.getX(), e.getY());
            Location realLoc = ViewHelper.getViewLoc(clickLoc);

            System.out.println(realLoc.toString());
            Stone clickedStone = null;

            clickedStone = cgame.getStone(realLoc);

            System.out.println(clickedStone + "");

            Role role = cgame.getCurrentRoleToPlay();

            if (clickedStone != null && clickedStone.getOwner().equals(role)) {
                selectStone(clickedStone);
            }

            if (cgame.getSelectedStone() != null && realLoc != null) {
                if (clickedStone == null || clickedStone != null
                        && !clickedStone.getOwner().equals(role)) {
                    if (cgame.getSelectedStone().isLegalMove(realLoc)
                            && cgame.isSafeMove(cgame.getSelectedStone(),
                                    realLoc)) {
                        moveStone(realLoc);
                    } else {

                        this.fireChessEvent(new ChessMessage(
                                ChessMessage.GENERAL_IN_DANGER,
                                "This is not the safe move, our opponent is going to win!!!"));
                        // pop up a message to remind the use.
                        // System.out.println("This is not the safe move, our opponent is going to win!!!");
                    }
                }

            }
            this.fireChessEvent(new ChessMessage("mouse Clicked"));
            // chessPanel.updateUI();
        }

    }

    public void selectStone(Stone stone) {
        Game g = ModelLocator.getInstance().getCurrentGame();
        Stone s = g.getSelectedStone();
        if (s != null) {
            if (s.equals(stone)) {
                stone.setSelected(false);
                g.setSelectedStone(null);
            } else {
                s.setSelected(false);
                stone.setSelected(true);
                g.setSelectedStone(stone);
            }
        } else {
            stone.setSelected(true);
            g.setSelectedStone(stone);
        }
        // if(MusicUtil.isGameMusic()){
        // MusicUtil.playSound("go");
        // }
    }

    public void moveStone(Location loc) {

        try {
            Game g = ModelLocator.getInstance().getCurrentGame();
            Move move = new Move(g.getSelectedStone(), g.getSelectedStone()
                    .getLoc(), loc, g.getStone(loc));

            new CommandProxy(new MoveCommand(move)).execute(g);
        } catch (ChessException ex) {
            ex.printStackTrace();
        }
    }

    public void takeBackRequest() {

        try {
            Game g = ModelLocator.getInstance().getCurrentGame();
            if (g != null && g.getGameState().equals(GameState.GAME_ON)) {
                if (!g.getGameStack().isEmpty()) {
                    // Move move = g.getGameStack().pop();
                    // Move move = g.getGameStack().lastElement();
                    // new CommandProxy(new
                    // TakebackRequestCommand(ModelLocator.getInstance().getPlayer())).execute(g);
                    new CommandProxy(new RequestCommand(ModelLocator
                            .getInstance().getPlayer(), "take back",
                            new TakebackCommand(ModelLocator.getInstance()
                                    .getPlayer()))).execute(g);
                }
            }
            // new MoveCommand(g.getSelectedStone(), loc, g).execute();
        } catch (ChessException ex) {
            ex.printStackTrace();
        }
    }

    public void seekPeace() {
        try {
            Game g = ModelLocator.getInstance().getCurrentGame();
            RequestCommand request = new RequestCommand(ModelLocator
                    .getInstance().getPlayer(), "peace", new SeekPeaceCommand());
            new CommandProxy(request).execute(g);
        } catch (ChessException ex) {
            ex.printStackTrace();
        }
    }

    public void giveUp() {
        try {
            Game g = ModelLocator.getInstance().getCurrentGame();
            new CommandProxy(new GiveupCommand(ModelLocator.getInstance()
                    .getPlayer())).execute(g);
        } catch (ChessException ex) {
            ex.printStackTrace();
        }
    }
}
