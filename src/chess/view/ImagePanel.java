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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Image image;

    public ImagePanel(String img, int width, int height) {
        this(new ImageIcon(img).getImage(), width, height);
    }

    public ImagePanel(Image img, int width, int height) {
        this.image = img;
        // Dimension size = new Dimension(img.getWidth(null),
        // img.getHeight(null));
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Dimension d = getSize();
            g.drawImage(image, 0, 0, d.width, d.height, this);
        }
    }

    public static void main(String[] args) {
        ImagePanel panel = new ImagePanel(new ImageIcon("images/ChessBoard.png").getImage(), 800,
                600);
        panel.setSize(1000, 600);

        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

}
