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

public class GameConstants {

    public static enum GameState {
        GAME_PRE, GAME_ON, GAME_OVER,
    }

    public static enum ResultType {
        PEACE, REDWIN, BLACKWIN
    }

    public static enum PlayerState {
        LOGGED_IN, // the player has logged in.
        LOGGED_OUT, // the player has logged out.
        IN_A_ROOM, // player has entered into a chess room but doesn't sit down.
        AT_A_TABLE, // player is at the chess table but is not playing the game
        IN_A_GAME// the player is playing the game.
    }

    public static enum ReplayState {
        REPLAY_READY, REPLAY_STARTED, REPLAY_PAUSED;
    }

}
