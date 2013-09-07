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

import java.io.Serializable;
import java.util.Vector;

import chess.model.game.Game;
import chess.model.player.Role;

/*******************************************************
 * 
 * ABSTRACT class -> the parent of stones
 * 
 *******************************************************/

@SuppressWarnings("serial")
public abstract class Stone implements Serializable, Cloneable {

    public Stone(Role owner, Location loc, int id) {
        this.owner = owner;
        this.loc = loc;
        this.id = id;
    }

    public Stone(Role owner, Location loc, int id, Game game) {
        this.owner = owner;
        this.loc = loc;
        this.id = id;
        this.game = game;
    }

    protected Game     game;
    protected Role     owner;

    // (1)true -> alive, (2)false -> killed
    protected boolean  alive    = true;

    // the current location of the stone
    protected Location loc;

    // the image of the stone
    protected String   imageName;

    // whether the stone is being dragged by user
    protected boolean  selected = false;

    protected int      id;

    // protected ChessBoard frame;

    public int getId() {
        return id;
    }

    public void killed() {
        alive = false;
    }

    public Role getOwner() {
        return owner;
    }

    public void setOwner(Role owner) {
        this.owner = owner;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        System.out.println(this.getClass().getName() + " is selected ");
        this.selected = selected;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getImageName() {
        return imageName;
    }

    public Location getPhylLocation() {
        return new Location(loc.getX() * 75 + 70, loc.getY() * 70 + 50);
    }

    public boolean isLegalMove(Location loc) {
        // the target position can't have an ally
        if (game.getStone(loc) != null
                && game.getStone(loc).getOwner().equals(this.getOwner())) {
            return false;
        }
        return isGameRuleMove(loc);
    }

    protected abstract boolean isGameRuleMove(Location loc);

    public abstract Vector<Location> getLegalMoves();

    public boolean equals(Stone s) {
        if (s == null)
            return false;
        return (this.loc.equals(s.getLoc())
                && this.getOwner().equals(s.getOwner()) && this.getImageName()
                .equals(s.getImageName()));
    }

    public String toString() {

        return "ClassName=" + this.getClass().getName() + ",loc="
                + getLoc().toString() + ",owner=" + this.getOwner().toString()
                + ",isSelect=" + this.isSelected() + ",isaLive="
                + this.isAlive();
    }

    public String getName() {
        final String clazzFullName = this.getClass().getName();
        return clazzFullName.substring(clazzFullName.lastIndexOf(".") + 1);
    }

    public abstract Stone clone();
}