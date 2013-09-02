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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import chess.model.ModelLocator;
import chess.model.game.BoardModel;
import chess.model.game.Game;
import chess.model.game.GameReplayer;

public class ReplayBoard extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // the width of the board frame
    private int frameWidth = 860;

    // the height of the board frame
    private int frameHeight = 800;

    // size of window
    private Dimension wndSize;

    // the default toolkit of AWT
    private Toolkit theKit;

    private ReplayPanel chessPanel;

    // private ChessMenu chessMenu;

    private StatusBar statusBar;

    private GameReplayer replayer;
    private Game game;
    private JList stepList;

    public ReplayBoard(Game game) throws IOException {
        this.game = game;
        replayer = new GameReplayer(game);
        replayer.resetGame();

        final int width = 600, height = 660;

        ModelLocator.getInstance().setBoard(new BoardModel(width, height));
        this.setJMenuBar(getChessMenu());
        this.setLayout(new BorderLayout(5, 5));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setTitle("Game Replay");
        this.setIconImage(ImageIO.read(new File("res/chess.jpg")));

        stepList = new JList(replayer.getListModel());
        getContentPane().add(stepList, BorderLayout.EAST);

        initChessPanel(width, height);

        initStatusBar();

        // the default toolkit
        theKit = getToolkit();
        // window size
        wndSize = theKit.getScreenSize();
        // determine the start point and the size of the board frame
        setBounds((wndSize.width - frameWidth) / 2, (wndSize.height - frameHeight) / 2, frameWidth,
                frameHeight);
        // fix the size of the frame
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setBounds((wndSize.width - frameWidth) / 2, (wndSize.height - frameHeight) / 2,
                        frameWidth, frameHeight);

            }
        });
        // this.add(chessPanel,BorderLayout.CENTER);
        this.update(this.getGraphics());
        this.pack();
        // this.setResizable(true);
        // this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void initStatusBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        statusBar = new StatusBar();
        bottomPanel.add(new ReplayButtonsPanel());
        bottomPanel.add(statusBar);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initChessPanel(final int width, final int height) throws IOException {

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        // leftPanel.setBorder(new LineBorder(Color.RED));

        JLayeredPane lPane = new JLayeredPane();
        lPane.setPreferredSize(new Dimension(width, height));

        // Chess Board as background image.
        Image backgroundImg = ImageIO.read(new File("res/images/ChessBoard.png"));
        ImagePanel background = new ImagePanel(backgroundImg, width, height);

        lPane.add(background, new Integer(0));

        chessPanel = new ReplayPanel(width, height, game);
        lPane.add(chessPanel, new Integer(1));

        leftPanel.add(lPane, BorderLayout.CENTER);

        getContentPane().add(leftPanel, BorderLayout.CENTER);
        // GameController.getInstance().setChessPanel(chessPanel);

    }

    public JMenuBar getChessMenu() {

        // Chinese Chess
        JMenuBar menuBar = new JMenuBar();
        // Main menu
        JMenu gameMenu = new JMenu("Game");

        JMenuItem openItem = new JMenuItem("Open Game");// load game

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectInputStream ois = null;
                try {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Chinese Chess Game File", "cnchess");
                    chooser.setFileFilter(filter);
                    chooser.showOpenDialog(null);
                    String filepath = chooser.getSelectedFile().getAbsolutePath();
                    ois = new ObjectInputStream(new FileInputStream(filepath));

                    game = (Game) ois.readObject();
                    replayer.setGame(game);
                    replayer.resetGame();
                    replayer.getListModel().removeAllElements();
                    // stepList = new JList(replayer.getListModel());
                    stepList.updateUI();
                    chessPanel.setGame(game);
                    chessPanel.updateUI();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Open Game?");
                } finally {
                    try {
                        ois.close();
                    } catch (Exception ex) {
                    }
                }
            }
        });

        gameMenu.add(openItem);
        menuBar.add(gameMenu);
        // return menu Bar
        return menuBar;
    }

    @SuppressWarnings("serial")
    public class ReplayButtonsPanel extends JPanel {

        public ReplayButtonsPanel() {

            // this.setBounds(200, 200, 600, 200);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.setLayout(new GridLayout(1, 4, 70, 10));
            // this.setSize(width, height);

            this.setBackground(Color.WHITE);
            JButton first = new JButton("First Step");
            JButton next = new JButton("Next Step");
            JButton previous = new JButton("Previous Step");
            JButton last = new JButton("Last Step");

            this.add(first);
            this.add(next);
            this.add(previous);
            this.add(last);

            first.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replayer.gotoFirstStep();
                    chessPanel.updateUI();
                }
            });
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JButton menu = (JButton) e.getSource();
                    if (replayer != null) {
                        replayer.gotoNextStep();
                        chessPanel.updateUI();
                    } else {

                    }
                }
            });
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replayer.gotoPrevousStep();
                    chessPanel.updateUI();
                }
            });
            last.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replayer.gotoLastStep();
                    chessPanel.updateUI();
                }
            });
        }
    }

    public static void main(String[] args) {
        try {
            // set lookand feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // new gameboard
            // new ReplayBoard();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}