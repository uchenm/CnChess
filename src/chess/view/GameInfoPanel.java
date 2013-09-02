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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chess.model.game.Game;
import chess.model.player.BlackRole;
import chess.model.player.Player;
import chess.model.player.RedRole;
import chess.model.player.Role;

@SuppressWarnings("serial")
public class GameInfoPanel extends JPanel implements Observer {

    private PlayerInfoPanel redPlayerPanel;
    private PlayerInfoPanel blackPlayerPanel;
    private JLabel gameStatus;

    public void update(Observable obj, Object arg) {

        if (obj instanceof Game) {
            Game game = (Game) obj;
            Role r = game.getCurrentRoleToPlay();
            gameStatus.setText(r.getName() + "'s Turn!");
            Vector<Player> players = game.getPlayers();

            if (players != null && !players.isEmpty()) {
                Enumeration<?> e = players.elements();
                while (e.hasMoreElements()) {
                    Object o = e.nextElement();
                    if (o instanceof Player) {
                        Player p = (Player) o;
                        if (p.getRole().isRed())
                            redPlayerPanel.setPlayer(p);
                        if (p.getRole().isBlack())
                            blackPlayerPanel.setPlayer(p);
                    }
                }
            }
        }
        updateUI();
    }

    public GameInfoPanel() {
        this.setPreferredSize(new Dimension(200, 600));
        this.setBounds(20, 20, 200, 600);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new GridLayout(5, 1, 200, 50));

        redPlayerPanel = new PlayerInfoPanel();
        this.add(redPlayerPanel);

        this.add(new JLabel(""));

        gameStatus = new JLabel();
        this.add(gameStatus);

        this.add(new JLabel());

        blackPlayerPanel = new PlayerInfoPanel();
        this.add(blackPlayerPanel);
    }

    public static void main(String[] args) {
        GameInfoPanel panel = new GameInfoPanel();
        panel.blackPlayerPanel.setPlayer(new Player("Ming", new BlackRole()));
        panel.redPlayerPanel.setPlayer(new Player("Allen", new RedRole()));
        panel.setSize(1000, 600);

        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    // public void updateUI() {
    // this.blackPlayerPanel.setPlayer(new Player("Ming", new BlackRole()));
    // }

    /**
     * ========================================================================#
     * PlayerInfoPanel
     * 
     * @author root
     * 
     */
    public class PlayerInfoPanel extends JPanel implements Observer {

        private Player player;
        private JLabel nameLabel;
        private JLabel roleLabel;

        public void update(Observable obj, Object arg) {
            if (arg instanceof Player) {
                player = (Player) arg;
                // System.out.println("NameObserver: Name changed to " + name);
                this.nameLabel.setText(player.getName());
                this.roleLabel.setText(player.getRole().isRed() ? "Red Player" : "Black Player");

            }
        }

        public PlayerInfoPanel() {
            this.setBackground(Color.LIGHT_GRAY);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            nameLabel = new JLabel();
            nameLabel.setFont(new Font("Serif", Font.PLAIN, 24));
            roleLabel = new JLabel();

            this.setLayout(new BorderLayout(20, 20));
            this.add(nameLabel, BorderLayout.CENTER);
            this.add(roleLabel, BorderLayout.SOUTH);
        }

        public Player getPlayer() {
            return player;

        }

        public void setPlayer(Player player) {
            this.player = player;
            this.nameLabel.setText(player.getName());
            this.roleLabel.setText(player.getRole().isRed() ? "Red Player" : "Black Player");
        }

        // public static void main(String[] args) {
        // PlayerInfoPanel panel = new PlayerInfoPanel();
        // panel.setPlayer(new Player("Ming", new BlackRole()));
        // panel.setSize(1000, 600);
        //
        // JFrame frame = new JFrame();
        // frame.getContentPane().add(panel);
        // frame.pack();
        // frame.setVisible(true);
        //
        // }

    }

}
