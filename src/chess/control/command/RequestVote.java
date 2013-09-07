package chess.control.command;

import java.io.Serializable;
import java.util.UUID;

import chess.model.player.Player;

@SuppressWarnings("serial")
public class RequestVote implements Serializable {

    private UUID   requestId;

    private Player player;

    private int    result;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
