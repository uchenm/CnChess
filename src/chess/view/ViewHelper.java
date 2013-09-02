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
package chess.view;

import chess.model.ModelLocator;
import chess.model.stone.Location;

public class ViewHelper {
    public static Location getViewLoc(Location loc) {
        Location tempLoc;
        // if
        // (ModelLocator.getInstance().getCurrentGame().getCurrentPlayer().getRole().isRed())
        // {
        if (ModelLocator.getInstance().isRedOnBottom()) {
            tempLoc = new Location(loc.getX(), loc.getY());
        } else {
            tempLoc = new Location(8 - loc.getX(), 9 - loc.getY());
        }
        return tempLoc;
    }
}
