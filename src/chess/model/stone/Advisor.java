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
 * Servant(SHI) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Advisor extends Stone implements Cloneable {

    public Advisor(Role owner, Location loc, int id, Game game) {
        super(owner, loc, id, game);
        setOwner(owner);
        alive = true;
        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/advisor_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/advisor_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {
        // default result value is false
        boolean result = false;
        int xdiff = loc.getX() - this.loc.getX();
        int ydiff = loc.getY() - this.loc.getY();

        // when the stone's home is in the bottom
        if (getOwner().isRed()) {
            if (loc.getX() >= 3 && loc.getX() <= 5 && loc.getY() >= 7
                    && loc.getY() <= 9 && Math.abs(xdiff) == 1
                    && Math.abs(ydiff) == 1) {
                result = true;
            }
        }
        // when the stone's home is on the top
        else {
            if (loc.getX() >= 3 && loc.getX() <= 5 && loc.getY() >= 0
                    && loc.getY() <= 2 && Math.abs(xdiff) == 1
                    && Math.abs(ydiff) == 1) {
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
            // it has only five possible places, so lets check them all.
            if (isLegalMove(legalMove = new Location(3, 7))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(3, 9))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(5, 9))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(5, 9))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(4, 8))) {
                v.add(legalMove);
            }

        }
        // when the stone's home is on the top
        else {

            // it has only five possible places, so lets check them all.
            if (isLegalMove(legalMove = new Location(3, 2))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(3, 0))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(5, 2))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(5, 0))) {
                v.add(legalMove);
            }
            if (isLegalMove(legalMove = new Location(4, 1))) {
                v.add(legalMove);
            }
        }
        return v;
    }

    public Advisor clone() {
        return new Advisor(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}