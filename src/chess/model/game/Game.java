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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import chess.model.game.GameConstants.GameState;
import chess.model.player.BlackRole;
import chess.model.player.Player;
import chess.model.player.RedRole;
import chess.model.player.Role;
import chess.model.stone.Advisor;
import chess.model.stone.Cannon;
import chess.model.stone.Chariot;
import chess.model.stone.Elephant;
import chess.model.stone.General;
import chess.model.stone.Horse;
import chess.model.stone.Location;
import chess.model.stone.Soldier;
import chess.model.stone.Stone;
import chess.utils.StringUtils;

public class Game extends Observable implements Serializable {

    private static final long serialVersionUID  = 1L;

    private Vector<Player>    players           = new Vector<Player>();

    // the stones of the two players
    private Stone[][]         stones            = new Stone[2][16];

    private GameState         gameState;

    private Stone             selectedStone;

    private Role              currentRoleToPlay = new BlackRole();

    private Player            currentPlayer;

    private Queue<String>     messageQueue;

    private Stack<Move>       gameStack;
    // 0--single machine player, 1--local net, 2--
    // chess.net
    private int               gameType          = 0;

    private Move              replayMove;

    private Player            blackPlayer;

    private Player            redPlayer;

    private GameResult        gameResult;

    private String            statusMessage;

    /**
     * Constructor
     */
    public Game() {

        createStones();
        this.gameStack = new Stack<Move>();
        this.gameState = GameState.GAME_ON;
    }

    public void endGame(Role loosingSole) {

        // GameResult result=new GameResult();

        this.setGameState(GameState.GAME_OVER);
    }

    public void resetStones() {

        // createStones();
    }

    /**
     * @Method : createStones
     * @Funct : create all the stones -- totaly 32 pcs
     */
    private void createStones() {
        Role red = new RedRole();
        Role black = new BlackRole();

        stones[0][0] = new Soldier(red, new Location(0, 6), 0, this);
        stones[0][1] = new Soldier(red, new Location(2, 6), 1, this);
        stones[0][2] = new Soldier(red, new Location(4, 6), 2, this);
        stones[0][3] = new Soldier(red, new Location(6, 6), 3, this);
        stones[0][4] = new Soldier(red, new Location(8, 6), 4, this);
        // initializing cannons
        stones[0][5] = new Cannon(red, new Location(1, 7), 5, this);
        stones[0][6] = new Cannon(red, new Location(7, 7), 6, this);
        // initializing trucks
        stones[0][7] = new Chariot(red, new Location(0, 9), 7, this);
        stones[0][8] = new Chariot(red, new Location(8, 9), 8, this);
        // initializing horses
        stones[0][9] = new Horse(red, new Location(1, 9), 9, this);
        stones[0][10] = new Horse(red, new Location(7, 9), 10, this);
        // initializing elephants
        stones[0][11] = new Elephant(red, new Location(2, 9), 11, this);
        stones[0][12] = new Elephant(red, new Location(6, 9), 12, this);
        // initializing servant
        stones[0][13] = new Advisor(red, new Location(3, 9), 13, this);
        stones[0][14] = new Advisor(red, new Location(5, 9), 14, this);
        // initializing general
        stones[0][15] = new General(red, new Location(4, 9), 15, this);

        // initializing solders of BLACK
        stones[1][0] = new Soldier(black, new Location(0, 3), 16, this);
        stones[1][1] = new Soldier(black, new Location(2, 3), 17, this);
        stones[1][2] = new Soldier(black, new Location(4, 3), 18, this);
        stones[1][3] = new Soldier(black, new Location(6, 3), 19, this);
        stones[1][4] = new Soldier(black, new Location(8, 3), 20, this);
        // initializing cannons
        stones[1][5] = new Cannon(black, new Location(1, 2), 21, this);
        stones[1][6] = new Cannon(black, new Location(7, 2), 22, this);
        // initializing trucks
        stones[1][7] = new Chariot(black, new Location(0, 0), 23, this);
        stones[1][8] = new Chariot(black, new Location(8, 0), 24, this);
        // initializing horses
        stones[1][9] = new Horse(black, new Location(1, 0), 25, this);
        stones[1][10] = new Horse(black, new Location(7, 0), 26, this);
        // initializing elephants
        stones[1][11] = new Elephant(black, new Location(2, 0), 27, this);
        stones[1][12] = new Elephant(black, new Location(6, 0), 28, this);
        // initializing servant
        stones[1][13] = new Advisor(black, new Location(3, 0), 29, this);
        stones[1][14] = new Advisor(black, new Location(5, 0), 30, this);
        // initializing general
        stones[1][15] = new General(black, new Location(4, 0), 31, this);

    }

    public Stone getStoneWithId(int id) {
        return stones[id / 16][id % 16];
    }

    public Stone getStone(Location loc) {
        if (loc == null)
            return null;

        Stone stone = null;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (stones[i][j].isAlive() && stones[i][j].getLoc().equals(loc))
                    stone = stones[i][j];
            }
        }
        return stone;
    }

    public Stone[][] getStones() {
        return stones;
    }

    public void setStones(Stone[][] stones) {
        this.stones = stones;
    }

    /**
     * check whether there are stones in between two location, given that the
     * two location is on the same line
     * 
     * @param a
     * @param b
     * @return
     */
    public boolean haveStones(Location a, Location b) {
        boolean result = false;
        int start, end;
        if (a.isInLine(b)) {
            if (a.getX() == b.getX()) {
                start = (a.getY() < b.getY() ? a.getY() : b.getY());
                end = (a.getY() > b.getY() ? a.getY() : b.getY());
                for (int i = start + 1; i < end; i++) {
                    Location loc = new Location(a.getX(), i);
                    if (hasStone(loc))
                        return true;
                }
            }
            if (a.getY() == b.getY()) {
                start = (a.getX() < b.getX() ? a.getX() : b.getX());
                end = (a.getX() > b.getX() ? a.getX() : b.getX());
                for (int i = start + 1; i < end; i++) {
                    Location loc = new Location(i, a.getY());
                    if (hasStone(loc))
                        return true;
                }
            }
        }
        return result;
    }

    /**
     * check whether there is a stone on the given location
     * 
     * @param loc
     * @return
     */
    public boolean hasStone(Location loc) {
        boolean result = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (stones[i][j].isAlive() && stones[i][j].getLoc().equals(loc)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void switchRole() {
        if (this.getCurrentRoleToPlay().isRed())
            setCurrentRoleToPlay(new BlackRole());
        else
            setCurrentRoleToPlay(new RedRole());

        // this.getSelectedStone().setSelected(false);
        this.setSelectedStone(null);

    }

    public Player getCurrentPlayer() {
        for (int index = 0; index < players.size(); index++) {
            Player p = players.get(index);
            if (getCurrentRoleToPlay().equals(p.getRole())) {
                setCurrentPlayer(p);
            }
        }
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Move getLastMove() {
        if (this.getGameStack() != null && !this.getGameStack().isEmpty())
            return this.getGameStack().lastElement();
        else
            return null;
    }

    public boolean isLoosingGame(Role role) {
        boolean result = true;
        int index = stones[0][15].getOwner().equals(role) ? 0 : 1;
        for (int i = 0; i < 16; i++) {
            if (stones[index][i].isAlive()) {
                Vector<Location> v = stones[index][i].getLegalMoves();
                if (v != null && !v.isEmpty()) {
                    Enumeration<Location> e = v.elements();
                    while (e.hasMoreElements()) {
                        Location loc = (Location) e.nextElement();
                        if (isSafeMove(stones[index][i], loc)) {
                            return false;
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean isInTrouble(Role role) {
        return isInTrouble(role, stones);
    }

    private boolean isInTrouble(Role role, Stone[][] s) {
        boolean result = false;

        Stone general;
        int oppIndex;
        if (s[0][15].getOwner().equals(role)) {
            general = s[0][15];
            oppIndex = 1;
        } else {
            general = s[1][15];
            oppIndex = 0;
        }

        for (int i = 0; i < 16; i++) {
            if (s[oppIndex][i].isAlive()
                    && s[oppIndex][i].isLegalMove(general.getLoc())) {
                return true;
            }
        }

        return result;
    }

    public Object deepClone(Object obj) {

        Object returnObj = null;

        try {
            // write to stream
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            // read from stream
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            returnObj = oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public Location getStoneIndexes(Stone s) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (stones[i][j].equals(s) && stones[i][j] == s)
                    return new Location(i, j);
            }
        }
        return new Location(-1, -1);
    }

    // public boolean isSafeMove(Stone s, Location loc) {
    // Location index = getStoneIndexes(s);
    // Stone[][] stoneCopy;
    // // stoneCopy = (Stone[][]) deepClone(stones);
    // stoneCopy = (Stone[][]) DeepClone.clone(stones);
    // // stoneCopy = cloneStones(stones);
    // // simulate the move on copies
    //
    // // if an enemy is on target location, kill it.
    // // note: 1-index.getX() is for enemy's stones
    // for (int j = 0; j < 16; j++) {
    // if (stoneCopy[1 - index.getX()][j].getLoc().getX() == loc.getX()
    // && stoneCopy[1 - index.getX()][j].getLoc().getY() == loc.getY())
    // stoneCopy[1 - index.getX()][j].setAlive(false);
    // }
    //
    // stoneCopy[index.getX()][index.getY()].setLoc(loc);
    // // System.out.println("save move "+s.toString()+" to "+loc);
    // return !isInTrouble(s.getOwner(), stoneCopy);
    //
    // }

    public boolean isSafeMove(Stone s, Location loc) {
        if (!s.isLegalMove(loc))
            return false;

        Location fromLoc = s.getLoc();
        Stone target = getStone(loc);

        // try to do move now.
        if (target != null)
            target.setAlive(false);
        s.setLoc(loc);

        boolean result = !isInTrouble(s.getOwner());

        // set it back
        if (target != null)
            target.setAlive(true);
        s.setLoc(fromLoc);

        return result;
    }

    public Stone[][] cloneStones(Stone[][] stones) {
        Stone[][] s = new Stone[2][16];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                s[i][j] = stones[i][j].clone();
            }
        }
        return s;
    }

    public void saveGame() {
        Player blackPlayer = null;
        Player redPlayer = null;

        for (int index = 0; index < players.size(); index++) {
            Player p = players.get(index);
            if (new BlackRole().equals(p.getRole())) {
                blackPlayer = p;
            }
            if (new RedRole().equals(p.getRole())) {
                redPlayer = p;
            }
        }

        String fileName = blackPlayer.getName() + "_vs_" + redPlayer.getName()
                + "_" + StringUtils.getDate() + ".cnchess";
        ObjectOutputStream oo = null;
        try {
            OutputStream out = new FileOutputStream(new File(fileName));
            oo = new ObjectOutputStream(new BufferedOutputStream(out));
            oo.writeObject(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (oo != null)
                    oo.close();
            } catch (IOException e) {
            }
        }
    }

    public void syncWithServer(Game game) {
        this.setPlayers(game.getPlayers());
        this.setCurrentRoleToPlay(game.getCurrentRoleToPlay());
        this.setGameStack(game.getGameStack());
        this.setGameType(game.getGameType());
        this.setGameState(game.getGameState());
        this.setStones(game.getStones());
        setChanged();
        notifyObservers();

    }

    public void updateObsevers() {
        setChanged();
        notifyObservers();
    }

    /**
     * ========================================================================#
     * replay related methods #
     * ========================================================================#
     */

    /**
     * ========================================================================#
     * Setters & getters #
     * ========================================================================#
     */
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Stone getSelectedStone() {
        return selectedStone;
    }

    public void setSelectedStone(Stone selectedStone) {
        this.selectedStone = selectedStone;
    }

    public Role getCurrentRoleToPlay() {
        return currentRoleToPlay;
    }

    public void setCurrentRoleToPlay(Role currentRoleToPlay) {
        this.currentRoleToPlay = currentRoleToPlay;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Vector<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        // if(player.isCreator()){
        // if(player.getRole().isBlack()) this.setBlackPlayer(player);
        // if(player.getRole().isRed()) this.setRedPlayer(player);
        // }else{
        // //creator already exists, the second joiner is the opponent
        // if(this.getBlackPlayer()==null) {
        // player.setRole(new BlackRole());
        // this.setBlackPlayer(player);
        // }
        // if(this.getRedPlayer()==null) {
        // player.setRole(new RedRole());
        // this.setRedPlayer(player);
        // }
        // }

        players.add(player);
        setChanged();
        notifyObservers(players);
    }

    public Queue<String> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(Queue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public Stack<Move> getGameStack() {
        return gameStack;
    }

    public void setGameStack(Stack<Move> gameStack) {
        this.gameStack = gameStack;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public Player getOwner(Role role) {
        System.out.println("Players=" + players);
        for (int index = 0; index < players.size(); index++) {
            Player p = players.get(index);
            if (role.equals(p.getRole())) {
                return p;
            }
        }
        return null;
    }

    public Move getReplayMove() {
        return replayMove;
    }

    public void setReplayMove(Move replayMove) {
        this.replayMove = replayMove;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
