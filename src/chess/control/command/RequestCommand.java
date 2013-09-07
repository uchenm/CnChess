package chess.control.command;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.swing.JOptionPane;

import chess.control.ChessException;
import chess.control.net.ClientProxy;
import chess.model.ModelLocator;
import chess.model.game.Game;
import chess.model.player.Player;

@SuppressWarnings("serial")
public class RequestCommand implements Command, Serializable {

    public enum RequestState {
        INIT, VOTING, APPROVED, REJECTED
    }

    private UUID             requestId;

    private String           topic;
    private Player           requester;
    private RequestState     state;
    private Set<RequestVote> voteResult = new HashSet<RequestVote>();
    private int              VOTE_SIZE  = 2;
    private Command          target;

    public RequestCommand(Player p, String t, Command target) {
        this.state = RequestState.INIT;
        this.requester = p;
        this.topic = t;
        this.target = target;

    }

    public void execute(Game game) throws ChessException {

        if (state.equals(RequestState.INIT)) {
            // for the server, need to take voting result.
            // so change its state to VOTING
            state = RequestState.VOTING;
            requestId = UUID.randomUUID();
        } else if (state.equals(RequestState.VOTING)) {
            // for the client[player], need to make a vote.
            if (!ModelLocator.getInstance().getPlayer().getRole().isObserver()) {

                if (ModelLocator.getInstance().getPlayer().equals(requester)) {
                    // the requester will always approve
                    RequestVote vote = new RequestVote();
                    vote.setRequestId(requestId);
                    vote.setPlayer(ModelLocator.getInstance().getPlayer());
                    vote.setResult(0);
                    ClientProxy.getInstance().sendObj(vote);
                } else {
                    // others can either approve or reject, so show dialogue box
                    int result = JOptionPane.showConfirmDialog(null,
                            requester.getName() + " is asking for " + topic
                                    + " , agree?", topic,
                            JOptionPane.YES_NO_OPTION);

                    // 0 means yes, 1 or -1 means no.
                    System.out.println(result);

                    RequestVote vote = new RequestVote();
                    vote.setRequestId(requestId);
                    vote.setPlayer(ModelLocator.getInstance().getPlayer());
                    vote.setResult(result);
                    ClientProxy.getInstance().sendObj(vote);
                }
            }

        }

    }

    public void vote(RequestVote vote) {
        voteResult.add(vote);
        // wait until all the validate voters has voted
        if (voteResult.size() >= VOTE_SIZE) {

            // now we get all validate vote, so make a decision.
            // boolean decision = getVotingResult();
            this.state = getVotingResult() ? RequestState.APPROVED
                    : RequestState.REJECTED;
        }

    }

    public boolean getVotingResult() {
        boolean result = true;

        for (RequestVote v : voteResult) {
            // if one of the voters reject, it will be rejected.
            if (v.getResult() == 1) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * getters and setters
     * 
     * @return
     */

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Command getTarget() {
        return target;
    }

    public void setTarget(Command target) {
        this.target = target;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public Player getRequester() {
        return requester;
    }

    public void setRequester(Player requester) {
        this.requester = requester;
    }

    public Set<RequestVote> getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(Set<RequestVote> voteResult) {
        this.voteResult = voteResult;
    }
}
