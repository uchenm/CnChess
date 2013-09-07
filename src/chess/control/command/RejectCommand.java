package chess.control.command;

import java.io.Serializable;

import javax.swing.JOptionPane;

import chess.control.ChessException;
import chess.model.ModelLocator;
import chess.model.game.Game;
import chess.model.player.Player;

@SuppressWarnings("serial")
public class RejectCommand implements Command, Serializable {

    private String topic;
    private Player player;

    public RejectCommand(Player p, String t) {
        this.player = p;
        this.topic = t;
    }

    public void execute(Game game) throws ChessException {
        // move.doMove(game);
        if (ModelLocator.getInstance().getPlayer().equals(player)) {
            JOptionPane.showMessageDialog(null, player.getName() + " 's "
                    + topic + " request has been rejected", "Request Rejected",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }
}
