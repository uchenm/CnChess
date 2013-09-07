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

/*******************************************************
 * 
 * The location of stones
 * 
 *******************************************************/

@SuppressWarnings("serial")
public final class Location implements Serializable {

    private final int x; // range: 0 ~ 8
    private final int y; // range: 0 ~ 9

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
     * public void setX(int val) { x = val; }
     * 
     * public void setY(int val) { y = val; }
     */
    public boolean equals(Location loc) {
        if (x == loc.x && y == loc.y)
            return true;
        else
            return false;
    }

    public boolean isInLine(Location loc) {
        return x == loc.getX() && y != loc.getY() || x != loc.getX()
                && y == loc.getY();
    }

    public String toString() {
        return "[x=" + x + ",y=" + y + "]";
    }
}