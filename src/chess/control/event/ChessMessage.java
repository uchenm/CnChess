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
package chess.control.event;

public class ChessMessage {

    public static final int DEFAULT           = 0;

    public static final int STONE_SELECTED    = 1;

    public static final int STONE_MOVE        = 2;

    public static final int STONE_KILLED      = 3;

    public static final int GENERAL_IN_DANGER = 4;

    public static final int GAME_OVER         = 5;

    public static final int BG_MUSIC          = 6;

    private int             msgType;
    private String          msgContent;

    public ChessMessage(String msgContent) {
        this(ChessMessage.DEFAULT, msgContent);
    }

    public ChessMessage(int msgType, String msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

}
