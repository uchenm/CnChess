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

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import chess.control.command.JoinGameCommand;
import chess.control.net.ClientProxy;
import chess.control.net.GameClient;
import chess.model.ModelLocator;
import chess.model.player.Player;
import chess.model.player.RedRole;

/*******************************************************
 * 
 * The Login Frame of Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class ChessLogin extends JFrame {

    // the width of the board frame
    private int       frameWidth  = 450;

    // the height of the board frame
    private int       frameHeight = 520;

    // size of window
    private Dimension wndSize;

    // the default toolkit of AWT
    private Toolkit   theKit;

    // private List serverlist;
    //
    // private Map<String, ServerInfoVO> servers = new Hashtable<String,
    // ServerInfoVO>();

    // private ChessCreator chessCreator;

    // --------- the controllers showed in the frame ------------- //
    Label             l_pname     = new Label("Player Name:");
    TextField         s_textname  = new TextField(20);
    Label             l_serverip  = new Label("Server IP:");
    TextField         s_Serverip  = new TextField(20);

    // List namelist = new List(10, true);
    // Label listTitle=new
    // Label(" No.          Creator Name                 IP Address");
    Button            bt_create   = new Button("Create");
    Button            bt_join     = new Button(" Join ");
    Button            bt_end      = new Button(" End ");

    // --------- the controllers showed in the frame ------------- //
    // private String s_NameList;

    private Player    player;

    public ChessLogin(Player player) {
        this.player = player;

        // the default container to display the AWT components
        Container display = getContentPane();

        // The layout of the "display"
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        display.setLayout(gbl);

        // ------ settel the controllers into the frame ------ //
        // label pname
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 20, 0);
        display.add(l_pname, gbc);
        // textname
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 20, 20, 0);
        display.add(s_textname, gbc);

        // label pname
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 20, 0);
        display.add(l_serverip, gbc);
        // textname
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 20, 20, 0);
        display.add(s_Serverip, gbc);

        // serverlist = new List(20, true);
        // gbc.anchor = GridBagConstraints.NORTH;
        // gbc.gridwidth = GridBagConstraints.REMAINDER;
        // gbc.insets = new Insets(0, 20, 20, 0);
        // display.add(serverlist, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
        display.add(bt_create, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 0, 0);
        display.add(bt_join, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 0, 0);
        display.add(bt_end, gbc);
        // ------ settel the controllers into the frame ------ //
        ButtonListener bl = new ButtonListener();
        bt_create.addActionListener(bl);
        bt_join.addActionListener(bl);
        bt_end.addActionListener(bl);

        // set the background color to white
        display.setBackground(Color.white);

        // the default toolkit
        theKit = getToolkit();

        // window size
        wndSize = theKit.getScreenSize();

        // determine the start point and the size of the board frame
        setBounds((wndSize.width - frameWidth) / 2,
                (wndSize.height - frameHeight) / 2, frameWidth, frameHeight);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // fix the size of the frame

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setBounds((wndSize.width - frameWidth) / 2,
                        (wndSize.height - frameHeight) / 2, frameWidth,
                        frameHeight);

            }
        });

        // the title of the frame
        setTitle("Chinese Chess -- Login");

    }

    class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bt_create) {
                System.out.println("Create button is pressed");

                setVisible(false);
                dispose();

            } else if (e.getSource() == bt_join) {

                if ("".equals(s_textname.getText().trim())) {
                    s_textname.setBackground(Color.RED);
                    return;
                }
                if ("".equals(l_serverip.getText().trim())) {
                    l_serverip.setBackground(Color.RED);
                    return;
                }

                Player p = new Player(s_textname.getText().trim());
                p.setRole(new RedRole());

                String ip = s_Serverip.getText().toString().trim();
                System.out.println(ip);

                ClientProxy.getInstance().setClient(new GameClient(ip, 9090));

                ModelLocator.getInstance().setPlayer(player);

                ClientProxy.getInstance().startListening();
                // ClientProxy.getInstance().initGame();
                ClientProxy.getInstance().executeRemoteCommand(
                        new JoinGameCommand(player));

                setVisible(false);
                dispose();

            } else if (e.getSource() == bt_end) {
                dispose();
            }
        }
    }

    public class ServerInfoVO {

        public ServerInfoVO(String serverIp, int port, String name) {
            this.serverIp = serverIp;
            this.port = port;
            this.name = name;
        }

        private String serverIp;
        private int    port;
        private String name;

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static void main(String[] args) {
        String sCreator = "Allen";
        Player player = new Player(sCreator);
        player.setRole(new RedRole());
        ChessLogin login = new ChessLogin(player);
        login.setVisible(true);
        // login.getServers();
    }
}