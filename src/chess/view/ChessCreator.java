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
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
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
import chess.control.net.ServerProxy;
import chess.model.ModelLocator;
import chess.model.player.Player;

/*******************************************************
 * 
 * The Create/Join Frame of Chess
 * 
 *******************************************************/

@SuppressWarnings("serial")
public class ChessCreator extends JFrame {

    // the width of the board frame
    private int       frameWidth  = 400;

    // the height of the board frame
    private int       frameHeight = 300;

    // size of window
    private Dimension wndSize;

    // the default toolkit of AWT
    private Toolkit   theKit;

    private TextField creatorName;
    private TextField joinorName;

    // --------- the controllers showed in the frame ------------- //
    Panel             selpanel    = new Panel();
    Label             creatornm   = new Label("Creator : ");
    Label             joinernm    = new Label("Joiner  : ");
    Choice            creatorch   = new Choice();
    Choice            joinorch    = new Choice();
    List              chatlist    = new List(20, true);
    // JList chat list = new JList();
    TextField         chatinput   = new TextField(80);

    Button            bt_start    = new Button(" Start ");
    // Button bt_send = new Button(" Send ");
    Button            bt_Cancel   = new Button(" Cancel ");
    private String    sCreator;
    private String    sJoinor;

    public ChessCreator(Player creator) {

        sCreator = creator.getName();

        // the default container to display the AWT components
        Container display = getContentPane();

        // The layout of the "display"
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        display.setLayout(gbl);

        // ------ settle the controllers into the frame ------ //
        creatorch.add("BLACK");
        creatorch.add("RED");

        joinorch.add("RED");
        joinorch.add("BLACK");

        creatorName = new TextField(sCreator);
        creatorName.setEditable(false);
        joinorName = new TextField(sJoinor);
        joinorName.setEditable(false);

        selpanel.setLayout(new GridLayout(2, 2, 10, 10));

        // joinorName.setSize(1,10);
        selpanel.add(creatornm);
        selpanel.add(creatorName);
        selpanel.add(creatorch);
        selpanel.add(joinernm);
        selpanel.add(joinorName);
        selpanel.add(joinorch);
        //
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 10);
        display.add(selpanel, gbc);
        // // chat list
        // chatlist.add("                                                  ");
        // gbc.anchor = GridBagConstraints.NORTH;
        // gbc.gridwidth = GridBagConstraints.REMAINDER;
        // gbc.insets = new Insets(0, 0, 0, 0);
        // display.add(chatlist, gbc);
        // // chat input text area
        // gbc.anchor = GridBagConstraints.NORTH;
        // gbc.gridwidth = GridBagConstraints.REMAINDER;
        // gbc.insets = new Insets(20, 0, 20, 0);
        // display.add(chatinput, gbc);
        // button OK
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        display.add(bt_start, gbc);
        // button drop
        // gbc.anchor = GridBagConstraints.NORTH;
        // gbc.gridwidth = 1;
        // gbc.insets = new Insets(0, 30, 0, 0);
        // display.add(bt_send, gbc);
        // button Cancel
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 0, 0);
        display.add(bt_Cancel, gbc);
        // ------ settel the controllers into the frame ------ //

        // button listener is added
        // ButtonListener buttonListener=new ButtonListener(this);
        bt_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        // bt_send.addActionListener(new ActionListener(){
        // public void actionPerformed(ActionEvent e){
        //
        // }
        // });
        bt_Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

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
        setTitle("Chinese Chess -- Create/Join");

        // new Thread(new GameServer()).start();

        ServerProxy.getInstance().startServer();
        // MulticastSender.sent(creator.toString());

        // MulticastSender.sent(creator.getName());
        GameClient client = new GameClient("127.0.0.1", 9090);

        ClientProxy.getInstance().setClient(client);
        ClientProxy.getInstance().startListening();
        // ClientProxy.getInstance().initGame();
        ClientProxy.getInstance().executeRemoteCommand(
                new JoinGameCommand(creator));
        ModelLocator.getInstance().setPlayer(creator);
    }

    public static void main(String[] args) {
        // Creator name
        String sCreator = "Ming Chen";
        Player player = new Player(sCreator);

        ChessCreator creator = new ChessCreator(player);
        creator.setSize(800, 600);
        creator.setVisible(true);
    }

}