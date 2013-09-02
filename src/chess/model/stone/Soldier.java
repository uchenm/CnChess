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
 * Solder(BING) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Soldier extends Stone implements Cloneable {

    public Soldier(Role owner, Location loc, int id, Game game) {
        super(owner, loc, id, game);
        // Initializing
        setOwner(owner);
        alive = true;

        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/solder_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/solder_black.gif";
        }
    }

    protected boolean isGameRuleMove(Location loc) {

        // default result value is false
        boolean result = false;
        int xdiff = loc.getX() - this.loc.getX();
        int ydiff = loc.getY() - this.loc.getY();

        // when the stone's home is in the bottom
        if (owner.isRed()) {
            if (this.loc.getY() >= 5 && ydiff == -1 && xdiff == 0) {
                result = true;
            }
            if (this.loc.getY() < 5
                    && ((ydiff == -1 && xdiff == 0) || (ydiff == 0 && Math.abs(xdiff) == 1))) {
                result = true;
            }
        }
        // when the stone's home is on the top
        else {
            if (this.loc.getY() < 5 && ydiff == 1 && xdiff == 0) {
                result = true;
            }
            if (this.loc.getY() >= 5
                    && ((ydiff == 1 && xdiff == 0) || (ydiff == 0 && Math.abs(xdiff) == 1))) {
                result = true;
            }
        }

        return result;
    }

    public Vector<Location> getLegalMoves() {
        Vector<Location> v = new Vector<Location>();

        Location legalMove = null;
        // when the stone's home is in the bottom
        if (owner.isRed()) {
            // the soldier can always move one step forward if it has not
            // reached the BOTTOM LINE
            if (this.loc.getY() > 0) {
                // it has only one possible move, that is forward one step.
                legalMove = new Location(getLoc().getX(), getLoc().getY() - 1);
                if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                    v.add(legalMove);
                }
            }
            // the soldier has passed across the river, it can also move
            // horizontally
            if (this.loc.getY() < 5) {
                if (getLoc().getX() > 0) {
                    legalMove = new Location(getLoc().getX() - 1, getLoc().getY());
                    if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                        v.add(legalMove);
                    }
                }
                if (getLoc().getX() < 8) {
                    legalMove = new Location(getLoc().getX() + 1, getLoc().getY());
                    if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                        v.add(legalMove);
                    }
                }
            }
        }
        // when the stone's home is on the top
        else {
            // the soldier can always move one step forward if it has not
            // reached the BOTTOM LINE
            if (this.loc.getY() < 9) {
                // it has only one possible move, that is forward one step.
                legalMove = new Location(getLoc().getX(), getLoc().getY() + 1);
                if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                    v.add(legalMove);
                }
            }
            // the soldier has passed across the river, it can also move
            // horizontally
            if (this.loc.getY() < 5) {
                if (getLoc().getX() > 0) {
                    legalMove = new Location(getLoc().getX() - 1, getLoc().getY());
                    if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                        v.add(legalMove);
                    }
                }
                if (getLoc().getX() < 8) {
                    legalMove = new Location(getLoc().getX() + 1, getLoc().getY());
                    if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                        v.add(legalMove);
                    }
                }
            }

        }
        return v;
    }

    public Soldier clone() {
        return new Soldier(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}