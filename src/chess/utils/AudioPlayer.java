package chess.utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class AudioPlayer extends Frame {
    AudioClip clip = null;
    FileDialog dialog = new FileDialog(this);
    Label labelCopyright = new Label();
    TextField textFieldFileName = new TextField();
    Panel panel1 = new Panel();
    Button buttonOpen = new Button();
    Button buttonPlay = new Button();
    Button buttonLoop = new Button();
    Button buttonStop = new Button();

    public AudioPlayer() {
        setTitle("AudioPlayer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        setVisible(true);
    }

    private void jbInit() throws Exception {
        labelCopyright.setBackground(Color.darkGray);
        labelCopyright.setForeground(Color.orange);
        labelCopyright.setText("Copyright (c) 2003 BeanSoft Studio. All rights reserved.");
        this.setTitle("Java Audio Player");
        textFieldFileName.setEditable(false);
        buttonOpen.setLabel("open");
        buttonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonOpen_actionPerformed(e);
            }
        });
        buttonPlay.setLabel("play");
        buttonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });
        buttonLoop.setLabel("rewind");
        buttonLoop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loop();
            }
        });
        buttonStop.setLabel("stop");
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        this.add(labelCopyright, BorderLayout.SOUTH);
        this.add(textFieldFileName, BorderLayout.NORTH);
        this.add(panel1, BorderLayout.CENTER);
        panel1.add(buttonOpen, null);
        panel1.add(buttonPlay, null);
        panel1.add(buttonLoop, null);
        panel1.add(buttonStop, null);
    }

    public AudioClip getAudioClip() {
        return this.clip;
    }

    public void setAudioClip(AudioClip clip) {
        this.clip = clip;
    }

    public static void main(String args[]) {
        new AudioPlayer();
    }

    @SuppressWarnings("deprecation")
    void buttonOpen_actionPerformed(ActionEvent e) {
        stop();
        dialog.show();
        if (dialog.getFile() != null) {
            String filename = dialog.getDirectory() + dialog.getFile();
            try {
                setAudioClip(Applet.newAudioClip((new java.io.File(filename)).toURL()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            textFieldFileName.setText(filename);
        }
        play();
    }

    void play() {
        if (getAudioClip() != null) {
            getAudioClip().play();
        }
    }

    void loop() {
        if (getAudioClip() != null) {
            getAudioClip().loop();
        }
    }

    void stop() {
        if (getAudioClip() != null) {
            getAudioClip().stop();
        }
    }
}
