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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import chess.control.GameController;
import chess.control.command.JoinGameCommand;
import chess.control.event.ChessEvent;
import chess.control.event.ChessEventListener;
import chess.control.event.ChessMessage;
import chess.control.net.ClientProxy;
import chess.control.net.GameClient;
import chess.control.net.ServerProxy;
import chess.model.ModelLocator;
import chess.model.game.BoardModel;
import chess.model.game.Game;
import chess.model.game.Move;
import chess.model.player.BlackRole;
import chess.model.player.ObseverRole;
import chess.model.player.Player;
import chess.model.player.RedRole;
import chess.model.player.Role;
import chess.utils.MusicPlayer;
import chess.utils.MusicUtil;

public class GameBoard extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ChessPanel        chessPanel;

    private ChessMenu         chessMenu;

    private StatusBar         statusBar;

    private MusicPlayer       bgMusicPlayer;

    private GameInfoPanel     inforPanel;
    // private JList stepList;

    private GameStackPanel    stackPanel;

    public GameBoard() throws IOException {
        final int width = 600, height = 660, boardWidth = 1024;
        // final boolean redOnbottom = false;

        // bgMusicPlayer = new MusicPlayer();

        ModelLocator.getInstance().setBoard(new BoardModel(width, height));
        // ModelLocator.getInstance().setHeight(height);
        // ModelLocator.getInstance().setRedOnbottom(redOnbottom);

        chessMenu = new ChessMenu();
        this.setJMenuBar(chessMenu.getChessMenu());

        this.setLayout(new BorderLayout(5, 5));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setTitle("Chinese Chess");

        this.setIconImage(ImageIO.read(new File("res/chess.jpg")));

        initChessPanel(width, height, boardWidth);

        inforPanel = new GameInfoPanel();

        // stepList = new JList();
        // stepList.setPreferredSize(new Dimension(200, 600));
        stackPanel = new GameStackPanel();
        getContentPane().add(stackPanel, BorderLayout.EAST);

        getContentPane().add(inforPanel, BorderLayout.WEST);

        statusBar = new StatusBar();
        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

        // this.setSize(boardWidth, height);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this.add(chessPanel,BorderLayout.CENTER);
        this.update(this.getGraphics());
        this.pack();
        // this.setResizable(true);

        this.setLocationRelativeTo(null);

        this.setVisible(true);
        // GameController.getInstance().setGameBoard(this);
        GameController.getInstance().addChessEventListener(
                new ChessEventListener() {
                    public void onChessEvent(ChessEvent e) {
                        // System.out.println(e.getSource()+" received!");
                        ChessMessage msg = (ChessMessage) e.getSource();
                        switch (msg.getMsgType()) {

                        case ChessMessage.GENERAL_IN_DANGER:
                            if (ModelLocator.getInstance().isSoundEffect())
                                MusicUtil.playSound("qq/danger");
                            statusBar.setMessage(msg.getMsgContent());
                            chessPanel.updateUI();
                            break;
                        case ChessMessage.STONE_MOVE:
                            if (ModelLocator.getInstance().isSoundEffect())
                                MusicUtil.playSound("qq/go");
                            statusBar.setMessage(msg.getMsgContent());
                            chessPanel.updateUI();
                            break;
                        case ChessMessage.STONE_KILLED:
                            if (ModelLocator.getInstance().isSoundEffect())
                                MusicUtil.playSound("qq/eat");
                            statusBar.setMessage(msg.getMsgContent());
                            chessPanel.updateUI();
                            break;
                        case ChessMessage.STONE_SELECTED:
                            if (ModelLocator.getInstance().isSoundEffect())
                                MusicUtil.playSound("qq/select");
                            statusBar.setMessage(msg.getMsgContent());
                            chessPanel.updateUI();
                            break;
                        case ChessMessage.GAME_OVER:
                            if (ModelLocator.getInstance().isSoundEffect())
                                MusicUtil.playSound("qq/Female_jueshagamewin");
                            statusBar.setMessage(msg.getMsgContent());
                            chessPanel.updateUI();
                            break;
                        case ChessMessage.BG_MUSIC:
                            if (ModelLocator.getInstance().isBgmusic())
                                bgMusicPlayer.playMusic();
                            else
                                bgMusicPlayer.closeMusic();
                            break;
                        case ChessMessage.DEFAULT:
                        default:
                            chessPanel.updateUI();
                        }

                    }
                });
    }

    private void initChessPanel(final int width, final int height,
            final int boardWidth) throws IOException {

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new LineBorder(Color.RED));

        JLayeredPane lPane = new JLayeredPane();
        lPane.setPreferredSize(new Dimension(width, height));

        // Chess Board as background image.
        Image backgroundImg = ImageIO
                .read(new File("res/images/ChessBoard.png"));
        ImagePanel background = new ImagePanel(backgroundImg, width, height);

        lPane.add(background, new Integer(0));

        chessPanel = new ChessPanel(width, height);
        lPane.add(chessPanel, new Integer(1));

        leftPanel.add(lPane, BorderLayout.CENTER);
        leftPanel.add(new ChessButtonPanel(width, 100), BorderLayout.SOUTH);

        getContentPane().add(leftPanel, BorderLayout.CENTER);

    }

    @SuppressWarnings("serial")
    public class ChessButtonPanel extends JPanel {

        public ChessButtonPanel(int width, int height) {

            // this.setBounds(200, 200, 600, 200);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.setLayout(new GridLayout(1, 4, 10, 10));
            this.setSize(width, height);

            JButton seekPeace = new JButton("Seek Peace");
            JButton giveUp = new JButton("Give up");
            JButton takeBack = new JButton("Take back");
            JButton dapu = new JButton("dapu");
            JButton updown = new JButton("Up-Down");

            this.add(seekPeace);

            this.add(giveUp);
            this.add(takeBack);
            this.add(dapu);
            this.add(updown);

            seekPeace.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JButton menu = (JButton) e.getSource();
                    // ModelLocator.getInstance().setBgmusic(menu.getState());
                    // GameController.getInstance().fireChessEvent(
                    // new ChessMessage(ChessMessage.BG_MUSIC,
                    // "background music settings changed!"));

                    GameController.getInstance().seekPeace();
                }
            });
            giveUp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JButton menu = (JButton) e.getSource();
                    GameController.getInstance().giveUp();
                }
            });
            takeBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JButton menu = (JButton) e.getSource();
                    GameController.getInstance().takeBackRequest();
                }
            });
            dapu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JButton menu = (JButton) e.getSource();
                }
            });
            updown.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ModelLocator.getInstance().setRedOnBottom(
                            !ModelLocator.getInstance().isRedOnBottom());
                    ModelLocator.getInstance().getCurrentGame()
                            .updateObsevers();
                }
            });
        }

    }

    @SuppressWarnings("serial")
    public class GameStackPanel extends JPanel implements Observer {

        private JList            stepList;
        private DefaultListModel listModel = new DefaultListModel();

        public GameStackPanel() {
            stepList = new JList(listModel);
            this.setPreferredSize(new Dimension(280, 600));
            add(stepList);
        }

        public void update(Observable obj, Object arg) {

            if (obj instanceof Game) {
                Game game = (Game) obj;
                // Role r = game.getCurrentRoleToPlay();
                // gameStatus.setText(r.getName() + "'s Turn!");
                Stack<Move> moves = game.getGameStack();
                listModel.removeAllElements();
                if (moves != null && !moves.isEmpty()) {
                    Enumeration<Move> e = moves.elements();
                    while (e.hasMoreElements()) {
                        Move move = e.nextElement();
                        listModel.addElement(move.getStone().getOwner()
                                .getName()
                                + ":"
                                + move.getStone().getName()
                                + " "
                                + move.getFrom() + "->" + move.getTo());
                    }
                }
            }
            updateUI();
        }
    }

    /**
     * ================================================================#
     * ChessMenu
     * ================================================================#
     * 
     * @author root
     */
    public class ChessMenu {

        public JMenuBar getChessMenu() {

            // Chinese Chess
            JMenuBar menuBar = new JMenuBar();
            // Main menu
            JMenu gameMenu = new JMenu("Game");
            JMenu helpMenu = new JMenu("Help");
            JMenu soundItem = new JMenu("Sound");
            JMenu localNetItem = new JMenu("Local Net");

            JMenuItem createGameItem = new JMenuItem("Create Game");
            JMenuItem joinItem = new JMenuItem("Join Game");

            localNetItem.add(createGameItem);
            localNetItem.add(joinItem);

            // the Game menu
            JMenuItem startItem = new JMenuItem("New Game");
            JMenuItem openItem = new JMenuItem("Open Game");// load game
            JMenuItem saveItem = new JMenuItem("Save Game");

            JCheckBoxMenuItem bgmusic = new JCheckBoxMenuItem(
                    "Background Music");
            JCheckBoxMenuItem soundEffect = new JCheckBoxMenuItem(
                    "Game Sound effect");

            JMenuItem exitItem = new JMenuItem("Exit");
            JMenuItem aboutItem = new JMenuItem("About");

            /**
             * =========================================================#
             * menuItem actionListeners
             * =========================================================#
             */

            startItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // create multiple result JOptionPane
                    JTextField name1 = new JTextField(10);
                    Role[] roles1 = { new BlackRole(), new RedRole(),
                            new ObseverRole() };
                    JComboBox role1 = new JComboBox(roles1);
                    JTextField name2 = new JTextField(10);
                    Role[] roles2 = { new RedRole(), new BlackRole(),
                            new ObseverRole() };
                    JComboBox role2 = new JComboBox(roles2);

                    JPanel myPanel = new JPanel();

                    myPanel.setLayout(new GridLayout(4, 2));
                    myPanel.add(new JLabel("Name:"));
                    myPanel.add(name1);
                    // myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Role"));
                    myPanel.add(role1);

                    myPanel.add(new JLabel("Name:"));
                    myPanel.add(name2);
                    // myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Role"));
                    myPanel.add(role2);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                            "Please Enter Your Name and Role",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String playerName1 = name1.getText();
                        Role playerRole1 = (Role) role1.getSelectedItem();
                        Player creator = new Player(playerName1, playerRole1,
                                true);

                        String playerName2 = name2.getText();
                        Role playerRole2 = (Role) role2.getSelectedItem();
                        Player joiner = new Player(playerName2, playerRole2,
                                false);

                        GameController.getInstance().newGame();
                        Game currentGame = ModelLocator.getInstance()
                                .getCurrentGame();
                        currentGame.addObserver(chessPanel);
                        currentGame.addObserver(inforPanel);
                        currentGame.addObserver(stackPanel);
                        currentGame.addObserver(statusBar);

                        // Player ming = new Player("Ming", new RedRole());
                        // Player allen = new Player("Allen", new BlackRole());
                        currentGame.addPlayer(creator);
                        currentGame.addPlayer(joiner);
                    }

                }
            });
            openItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JFileChooser chooser = new JFileChooser();
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                "Chinese Chess Game File", "cnchess");
                        chooser.setFileFilter(filter);
                        chooser.showOpenDialog(null);
                        String filepath = chooser.getSelectedFile()
                                .getAbsolutePath();
                        ObjectInputStream ois = new ObjectInputStream(
                                new FileInputStream(filepath));
                        Game game = (Game) ois.readObject();

                        new ReplayBoard(game);

                        ois.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Open Game?");
                    }
                }
            });
            saveItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // GameController.getInstance().newGame();
                }
            });

            createGameItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // create multiple result JOptionPane
                    JTextField name = new JTextField(10);
                    Role[] roles = { new BlackRole(), new RedRole(),
                            new ObseverRole() };
                    JComboBox role = new JComboBox(roles);
                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Name:"));
                    myPanel.add(name);
                    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Role"));
                    myPanel.add(role);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                            "Please Enter Your Name and Role",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        Game currentGame = new Game();
                        currentGame.setGameType(1);
                        ModelLocator.getInstance().setCurrentGame(currentGame);

                        currentGame.addObserver(chessPanel);
                        currentGame.addObserver(inforPanel);
                        currentGame.addObserver(stackPanel);
                        currentGame.addObserver(statusBar);

                        String playerName = name.getText();
                        Role playerRole = (Role) role.getSelectedItem();
                        Player creator = new Player(playerName, playerRole,
                                true);

                        ServerProxy.getInstance().startServer();
                        GameClient client = new GameClient("127.0.0.1", 9090);
                        ClientProxy.getInstance().setClient(client);
                        ClientProxy.getInstance().startListening();
                        // ClientProxy.getInstance().initGame();
                        ClientProxy.getInstance().executeRemoteCommand(
                                new JoinGameCommand(creator));
                        ModelLocator.getInstance().setPlayer(creator);
                    }
                }
            });
            joinItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // String sCreator = "Allen";
                    // Player player = new Player(sCreator);
                    // player.setRole(new RedRole());
                    // ChessLogin login = new ChessLogin(player);
                    // login.setVisible(true);
                    JTextField name = new JTextField(10);
                    JTextField serverIp = new JTextField("127.0.0.1");
                    Role[] roles = { new RedRole(), new BlackRole(),
                            new ObseverRole() };
                    JComboBox role = new JComboBox(roles);
                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Name:"));
                    myPanel.add(name);
                    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Role:"));
                    myPanel.add(role);
                    myPanel.add(new JLabel("Server IP:"));
                    myPanel.add(serverIp);
                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                            "Please Enter Your Name and Role",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        Game currentGame = new Game();
                        currentGame.setGameType(1);
                        ModelLocator.getInstance().setCurrentGame(currentGame);

                        currentGame.addObserver(chessPanel);
                        currentGame.addObserver(inforPanel);
                        currentGame.addObserver(stackPanel);
                        currentGame.addObserver(statusBar);

                        String playerName = name.getText();
                        Role playerRole = (Role) role.getSelectedItem();
                        Player joiner = new Player(playerName, playerRole,
                                false);
                        String ip = serverIp.getText();

                        // ServerProxy.getInstance().startServer();
                        GameClient client = new GameClient(ip, 9090);
                        ClientProxy.getInstance().setClient(client);
                        ClientProxy.getInstance().startListening();
                        // ClientProxy.getInstance().initGame();
                        ClientProxy.getInstance().executeRemoteCommand(
                                new JoinGameCommand(joiner));
                        ModelLocator.getInstance().setPlayer(joiner);
                    }
                }
            });

            bgmusic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCheckBoxMenuItem menu = (JCheckBoxMenuItem) e.getSource();
                    ModelLocator.getInstance().setBgmusic(menu.getState());
                    GameController.getInstance().fireChessEvent(
                            new ChessMessage(ChessMessage.BG_MUSIC,
                                    "background music settings changed!"));
                }
            });
            soundEffect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCheckBoxMenuItem menu = (JCheckBoxMenuItem) e.getSource();
                    ModelLocator.getInstance().setSoundEffect(menu.getState());
                }
            });

            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // GameController.getInstance().newGame();
                }
            });
            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JLabel label = new JLabel(new ImageIcon(ImageIO
                                .read(new File("res/chess.jpg"))));
                        JTextArea aboutText = new JTextArea();
                        aboutText.setEditable(false);
                        // aboutText.setFont(FontUtil.myFont1);

                        aboutText
                                .setText("Author: Ming Chen \nVersion: 1.0 \n");
                        JDialog dialog = new JDialog();
                        dialog.add(label, BorderLayout.WEST);
                        // dialog.add(new JScrollPane(ablutText),
                        // BorderLayout.CENTER);
                        dialog.add(aboutText, BorderLayout.CENTER);
                        dialog.setIconImage(ImageIO.read(new File(
                                "res/chess.jpg")));
                        dialog.setTitle("About");

                        dialog.setSize(480, 160);
                        dialog.setVisible(true);
                        dialog.setAlwaysOnTop(true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // add menuItems into menu
            gameMenu.add(startItem);
            gameMenu.add(openItem);
            gameMenu.add(saveItem);
            gameMenu.addSeparator();

            gameMenu.addSeparator();
            gameMenu.add(exitItem);

            soundItem.add(bgmusic);
            soundItem.add(soundEffect);

            helpMenu.add(aboutItem);
            // Create menu bar
            menuBar.add(gameMenu);
            menuBar.add(localNetItem);
            menuBar.add(soundItem);
            menuBar.add(helpMenu);
            // return menu Bar
            return menuBar;
        }

    }

    public static void main(String[] args) {
        try {
            // set lookand feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // new gameboard
            new GameBoard();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
