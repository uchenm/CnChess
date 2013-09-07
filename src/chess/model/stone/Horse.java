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
 * Horse(MA) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Horse extends Stone implements Cloneable {

    public Horse(Role owner, Location loc, int id, Game game) {

        super(owner, loc, id, game);

        setOwner(owner);
        alive = true;

        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/horse_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/horse_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {
        // default result value is false
        boolean result = false;
        int xdiff = loc.getX() - this.loc.getX();
        int ydiff = loc.getY() - this.loc.getY();
        // the tempid of "horse's foot"
        Location tempid;

        // if the shap is "RI" and the horse's foot does not exist, then legal
        if (Math.abs(ydiff) == 2 && Math.abs(xdiff) == 1) {
            tempid = new Location(this.loc.getX(), this.loc.getY() + ydiff
                    / Math.abs(ydiff));
            if (!game.hasStone(tempid))
                result = true;
        } else if (Math.abs(xdiff) == 2 && Math.abs(ydiff) == 1) {
            tempid = new Location(this.loc.getX() + xdiff / Math.abs(xdiff),
                    this.loc.getY());
            if (!game.hasStone(tempid))
                result = true;
        }

        return result;
    }

    public Vector<Location> getLegalMoves() {
        // it has at most 8 possible move places at a given point. check them
        Vector<Location> v = new Vector<Location>();
        Location legalMove = null;

        if (getLoc().getX() - 1 >= 0 && getLoc().getY() - 2 >= 0) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() - 1,
                    getLoc().getY() - 2))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() + 1 <= 8 && getLoc().getY() - 2 >= 0) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() + 1,
                    getLoc().getY() - 2))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() - 1 >= 0 && getLoc().getY() + 2 <= 9) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() - 1,
                    getLoc().getY() + 2))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() + 1 <= 8 && getLoc().getY() + 2 <= 9) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() + 1,
                    getLoc().getY() + 2))) {
                v.add(legalMove);
            }
        }
        // ------------------------------
        if (getLoc().getX() - 2 >= 0 && getLoc().getY() - 1 >= 0) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() - 2,
                    getLoc().getY() - 1))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() + 2 <= 8 && getLoc().getY() - 1 >= 0) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() + 2,
                    getLoc().getY() - 1))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() - 2 >= 0 && getLoc().getY() + 1 <= 9) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() - 2,
                    getLoc().getY() + 1))) {
                v.add(legalMove);
            }
        }
        if (getLoc().getX() + 2 <= 8 && getLoc().getY() + 1 <= 9) {
            if (isLegalMove(legalMove = new Location(getLoc().getX() + 2,
                    getLoc().getY() + 1))) {
                v.add(legalMove);
            }
        }

        return v;
    }

    public Horse clone() {
        return new Horse(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}