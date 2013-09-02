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
 * Chariot(JU) of the Chinese Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class Chariot extends Stone implements Cloneable {

    public Chariot(Role owner, Location loc, int id, Game game) {

        super(owner, loc, id, game);
        // Initializing
        setOwner(owner);
        alive = true;

        if (owner.isRed()) {

            // the image name of red stone
            imageName = "res/images/chariot_red.gif";
        } else {
            // the image name of black stone
            imageName = "res/images/chariot_black.gif";
        }

    }

    protected boolean isGameRuleMove(Location loc) {
        boolean result = true;
        if (!this.loc.isInLine(loc))
            result = false;
        if (game.haveStones(this.getLoc(), loc))
            result = false;

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

    public Chariot clone() {
        return new Chariot(this.getOwner(), this.getLoc(), this.id, this.game);
    }
}