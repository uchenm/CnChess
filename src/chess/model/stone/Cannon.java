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
 * Cannon(PAO) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Cannon extends Stone implements Cloneable {

    public Cannon(Role owner, Location loc, int id, Game game) {

        super(owner, loc, id, game);
        setOwner(owner);
        alive = true;

        if (owner.isRed()) {
            // the image name of red stone
            imageName = "res/images/cannon_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/cannon_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {
        boolean result = true;

        int firstloc, secondloc, stonecount;
        Location tempid;

        // the current location and the location not in the same line is illegal
        if (this.loc.equals(loc)
                || (this.loc.getX() != loc.getX() && this.loc.getY() != loc
                        .getY())) {
            result = false;
        }
        // when in the same VARTICAL line
        else if (this.loc.getX() == loc.getX()) {
            firstloc = (this.loc.getY() < loc.getY() ? this.loc.getY() : loc
                    .getY());
            secondloc = (this.loc.getY() > loc.getY() ? this.loc.getY() : loc
                    .getY());
            stonecount = 0;

            for (int i = firstloc + 1; i < secondloc; i++) {
                tempid = new Location(loc.getX(), i);
                if (game.hasStone(tempid)) {
                    stonecount++;
                }
            }
            // Cannon can't jump over 2 stones or jump 1 stone without "eating"
            if (stonecount >= 2 || (stonecount == 1 && !game.hasStone(loc))) {
                result = false;
            }
            // Cannon can't "eat" other stones directly
            if (stonecount == 0 && game.hasStone(loc)) {
                result = false;
            }
        }
        // when in the same HORIZANTAL line
        else if (this.loc.getY() == loc.getY()) {
            firstloc = (this.loc.getX() < loc.getX() ? this.loc.getX() : loc
                    .getX());
            secondloc = (this.loc.getX() > loc.getX() ? this.loc.getX() : loc
                    .getX());
            stonecount = 0;

            for (int i = firstloc + 1; i < secondloc; i++) {
                tempid = new Location(i, loc.getY());
                if (game.hasStone(tempid)) {
                    stonecount++;
                }
            }
            // Cannon can't jump over 2 stones or jump 1 stone without "eating"
            if (stonecount >= 2 || (stonecount == 1 && !game.hasStone(loc))) {
                result = false;
            }
            // Cannon can't "eat" other stones directly
            if (stonecount == 0 && game.hasStone(loc)) {
                result = false;
            }
        }

        return result;
    }

    public Vector<Location> getLegalMoves() {
        Vector<Location> v = new Vector<Location>();

        // check horizontally
        for (int j = 0; j < 10; j++) {
            Location legalMove = new Location(getLoc().getX(), j);
            if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                v.add(legalMove);
            }
        }
        // }
        // check vertically
        for (int i = 0; i < 9; i++) {
            Location legalMove = new Location(i, getLoc().getY());
            if (!this.loc.equals(legalMove) && isLegalMove(legalMove)) {
                v.add(legalMove);
            }
        }
        return v;
    }

    public Cannon clone() {
        return new Cannon(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}