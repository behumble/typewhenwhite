package net.hanjava.typewhenwhite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

class FaceLabel extends JLabel implements KeyListener, FocusListener, MouseListener {
    private InputOntoDroid main;
    FaceLabel(InputOntoDroid main) {
        this.main = main;
        URL logoUrl = getClass().getClassLoader().getResource("HP-Keyboard-icon.png");
        setIcon(new ImageIcon(logoUrl));
        setFont(getFont().deriveFont(20f));
        setHorizontalTextPosition(JLabel.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setVerticalAlignment(SwingConstants.CENTER);
        enableInputMethods(false);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        addFocusListener(this);
        addMouseListener(this);
        setOpaque(true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // nothing to do
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(main.device==null) return;
        System.out.println("[InputOntoDroid] "+keyEvent);
        try {
            AwtDroidExchange.handleKeyPressed(keyEvent, main.device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(main.device==null) return;
        System.out.println("[InputOntoDroid] "+keyEvent);
        try {
            AwtDroidExchange.handleKeyReleased(keyEvent, main.device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void updateStatusToUI() {
        boolean connected = main.device!=null;
        if(connected) {
            String model = main.device.getProperty("build.model");
            setText(String.format("Connected to %s!! Type something", model));
        } else {
            setText("Connecting via adb...");
        }

        boolean running = connected && hasFocus();
        setBackground( running ? Color.WHITE : Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        updateStatusToUI();
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        updateStatusToUI();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        // do nothing
    }
}