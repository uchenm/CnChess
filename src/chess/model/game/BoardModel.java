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
package chess.model.game;

import chess.model.stone.Location;

public class BoardModel {
    private int          width;
    private int          height;

    private int          gridWidth;
    private int          gridHeight;
    // the physical location of left_up grid.
    private Location     leftUpLoc;
    // the coordination of each point
    private Location[][] coords = new Location[9][10];

    public BoardModel(final int width, final int height) {
        this.width = width;
        this.height = height;
        initlizeCoords(width, height);
    }

    private void initlizeCoords(int width, int height) {
        gridWidth = width / 10;
        gridHeight = height / 11;

        final int menuBarHeight = 0;

        leftUpLoc = new Location(gridWidth / 2, gridHeight / 2 + menuBarHeight);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                coords[i][j] = new Location(leftUpLoc.getX() + i * gridWidth,
                        leftUpLoc.getY() + j * gridHeight);
            }
        }

    }

    public Location getCoordinate(Location loc) {
        return coords[loc.getX()][loc.getY()];
    }

    public Location getLoc(int x, int y) {
        Location loc = new Location(-1, -1);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if (coords[i][j].getX() < x
                        && x < coords[i][j].getX() + gridWidth
                        && coords[i][j].getY() < y
                        && y < coords[i][j].getY() + gridHeight) {
                    loc = new Location(i, j);
                }
            }
        }
        return loc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Location getLeftUpLoc() {
        return leftUpLoc;
    }

    public Location[][] getCoords() {
        return coords;
    }
}
