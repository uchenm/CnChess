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
package chess.model.stone;

import java.util.Vector;

import chess.model.game.Game;
import chess.model.player.Role;

/*******************************************************
 * 
 * General(JIANG or SHUAI) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class General extends Stone implements Cloneable {

    public General(Role owner, Location loc, int id, Game game) {
        super(owner, loc, id, game);
        setOwner(owner);
        alive = true;

        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/general_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/general_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {

        // default result value is false
        boolean result = false;

        int xdiff = loc.getX() - this.loc.getX();
        int ydiff = loc.getY() - this.loc.getY();

        // when the stone's home is in the bottom
        if (getOwner().isRed()) {
            if (loc.getX() >= 3
                    && loc.getX() <= 5
                    && loc.getY() >= 7
                    && loc.getY() <= 9
                    && ((Math.abs(xdiff) == 0 && Math.abs(ydiff) == 1) || (Math
                            .abs(xdiff) == 1 && Math.abs(ydiff) == 0))) {
                result = true;
            }

            // one general could kill another if that one comes into its face.
            Stone s = game.getStone(loc);
            if (this.getLoc().getX() == loc.getX() && s != null
                    && !s.getOwner().isRed()
                    && s.getClass().getName().equals("chess.stone.General")
                    && !game.haveStones(this.getLoc(), loc)) {
                result = true;
            }

        }

        // when the stone's home is on the top
        else {
            if (loc.getX() >= 3
                    && loc.getX() <= 5
                    && loc.getY() >= 0
                    && loc.getY() <= 2
                    && ((Math.abs(xdiff) == 0 && Math.abs(ydiff) == 1) || (Math
                            .abs(xdiff) == 1 && Math.abs(ydiff) == 0))) {
                result = true;
            }
            // one general could kill another if that one comes into its face.
            Stone s = game.getStone(loc);
            if (this.getLoc().getX() == loc.getX() && s != null
                    && s.getOwner().isRed()
                    && s.getClass().getName().equals("chess.stone.General")
                    && !game.haveStones(this.getLoc(), loc)) {
                result = true;
            }
        }

        return result;
    }

    public Vector<Location> getLegalMoves() {
        Vector<Location> v = new Vector<Location>();

        // check horizontally

        for (int i = 3; i < 6; i++) {
            Location legalMove = new Location(i, getLoc().getY());
            if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                v.add(legalMove);
            }
        }
        // check vertically
        for (int j = 7; j < 10; j++) {
            Location legalMove = new Location(getLoc().getX(), j);
            if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                v.add(legalMove);
            }
        }
        for (int j = 0; j < 3; j++) {
            Location legalMove = new Location(getLoc().getX(), j);
            if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                v.add(legalMove);
            }
        }

        return v;
    }

    public General clone() {
        return new General(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}