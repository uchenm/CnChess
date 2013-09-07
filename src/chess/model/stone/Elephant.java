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
 * Elephant(XIANG) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Elephant extends Stone implements Cloneable {

    public Elephant(Role owner, Location loc, int id, Game game) {
        super(owner, loc, id, game);
        // Initializing
        setOwner(owner);
        alive = true;

        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/elephant_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/elephant_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {
        // default result value is false
        boolean result = false;
        int xdiff = loc.getX() - this.loc.getX();
        int ydiff = loc.getY() - this.loc.getY();
        // the stoneID of the "elephant's eye"
        Location tempid = new Location(this.loc.getX() + xdiff / 2,
                this.loc.getY() + ydiff / 2);

        // when the stone's home is in the bottom
        if (getOwner().isRed()) {
            if (loc.getY() >= 5 && Math.abs(ydiff) == 2 && Math.abs(xdiff) == 2
                    && !game.hasStone(tempid)) {
                result = true;
            }
        }
        // when the stone's home is on the top
        else {
            if (loc.getY() < 5 && Math.abs(ydiff) == 2 && Math.abs(xdiff) == 2
                    && !game.hasStone(tempid)) {
                result = true;
            }
        }

        return result;
    }

    public Vector<Location> getLegalMoves() {
        Vector<Location> v = new Vector<Location>();
        Location legalMove = null;
        // when the stone's home is in the bottom
        if (getOwner().isRed()) {
            // it has only seven elephant eyes.
            if (isLegalMove(legalMove = new Location(2, 9))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(6, 9))) {
                v.add(legalMove);
            }

            if (isLegalMove(legalMove = new Location(2, 5))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(6, 5))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(4, 7))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(0, 7))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(8, 7))) {
                v.add(legalMove);
            }

        }
        // when the stone's home is on the top
        else {
            if (isLegalMove(legalMove = new Location(2, 0))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(6, 0))) {
                v.add(legalMove);
            }

            if (isLegalMove(legalMove = new Location(2, 4))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(6, 4))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(4, 2))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(0, 2))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(8, 2))) {
                v.add(legalMove);
            }
        }

        return v;
    }

    public Elephant clone() {
        return new Elephant(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}