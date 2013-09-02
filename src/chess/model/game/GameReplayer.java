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

import javax.swing.DefaultListModel;

import chess.model.game.GameConstants.GameState;

public class GameReplayer {
    private Game game;

    private int currentStep = 0;

    private DefaultListModel listModel = new DefaultListModel();

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public GameReplayer(Game game) {
        this.game = game;
    }

    public void resetGame() {

        for (int currentStep = game.getGameStack().size() - 1; currentStep > 0; currentStep--) {
            Move move = game.getGameStack().get(currentStep);
            unDomove(move);

        }

        // game.resetStones();
    }

    public void gotoNextStep() {
        if (currentStep < game.getGameStack().size()) {
            Move move = game.getGameStack().get(currentStep++);
            doMove(move);
            game.setReplayMove(move);
            listModel.addElement(move.getStone().getOwner().getName() + ":"
                    + move.getStone().getName() + " " + move.getFrom() + "->" + move.getTo());
        }
    }

    public void gotoPrevousStep() {
        if (currentStep > 0) {
            currentStep--;
            unDomove(game.getGameStack().get(currentStep));
            Move move = game.getGameStack().get(currentStep);

            if (currentStep != 0) {
                game.setReplayMove(move);
            } else {
                game.setReplayMove(null);
            }
            listModel.removeElementAt(currentStep);
        }
    }

    public void gotoFirstStep() {
        while (currentStep > 0) {
            gotoPrevousStep();
        }
        game.setReplayMove(null);
        // Move move=game.getGameStack().get(0);
        // doMove(move);
        // game.setReplayMove(move);

    }

    public void gotoLastStep() {

        while (currentStep < game.getGameStack().size()) {
            gotoNextStep();
        }
        // Move move = game.getGameStack().get(game.getGameStack().size() - 1);
        // doMove(move);
        // game.setReplayMove(move);

    }

    public void unDomove(Move move) {
        game.getStoneWithId(move.getStone().getId()).setLoc(move.getFrom());
        if (move.getTarget() != null) {
            game.getStoneWithId(move.getTarget().getId()).setAlive(true);
        }
        game.switchRole();

        game.getStoneWithId(move.getStone().getId()).setSelected(false);

    }

    public void doMove(Move move) {
        game.getStoneWithId(move.getStone().getId()).setLoc(move.getTo());
        if (move.getTarget() != null)
            game.getStoneWithId(move.getTarget().getId()).setAlive(false);

        // fire move event and killing event
        // GameController.getInstance().fireChessEvent(
        // new ChessMessage(ChessMessage.STONE_MOVE, stone.getOwner().getName()
        // + "'s "
        // + stone.getName() + " moved from " + from + " to " + to));
        // if (targetStone != null)
        // GameController.getInstance().fireChessEvent(
        // new ChessMessage(ChessMessage.STONE_KILLED,
        // targetStone.getOwner().getName()
        // + "'s " + targetStone.getName() + " is killded"));
        // switch role
        game.switchRole();
        game.getStoneWithId(move.getStone().getId()).setSelected(false);

        // judge if game is over and if General is in Danger
        if (game.isInTrouble(game.getCurrentRoleToPlay())) {
            if (game.isLoosingGame(game.getCurrentRoleToPlay())) {
                game.setGameState(GameState.ENDED);
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
}
