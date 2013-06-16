/*
 * Copyright 2013 Alan Goo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.hanjava.typewhenwhite;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.ddmlib.AndroidDebugBridge;

import javax.swing.*;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InputOntoDroid extends JLabel implements KeyListener, FocusListener, MouseListener, WindowListener, AdbRunnable.Listener {
    IChimpDevice device;
    private Executor cmdExecutor = Executors.newSingleThreadExecutor();
    private DeviceChooser deviceChooser;
    ChimpChat chimp;

    public InputOntoDroid() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        URL logoUrl = InputOntoDroid.class.getClassLoader().getResource("HP-Keyboard-icon.png");
        setIcon(new ImageIcon(logoUrl));
        setFont(getFont().deriveFont(20f));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.TOP);
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
        if(device==null) return;
        System.out.println("[InputOntoDroid] "+keyEvent);
        try {
            AwtDroidExchange.handleKeyPressed(keyEvent, device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(device==null) return;
        System.out.println("[InputOntoDroid] "+keyEvent);
        try {
            AwtDroidExchange.handleKeyReleased(keyEvent, device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void windowOpened(WindowEvent windowEvent) {
        showDeviceChooser();
        requestFocus();
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
        // do nothing
    }

    @Override
    public void adbConnected(IChimpDevice device) {
        this.device = device;
        updateStatusToUI();
    }

    @Override
    public void adbDisconnected() {
        this.device = null;
        updateStatusToUI();
    }

    private void updateStatusToUI() {
        boolean connected = device!=null;
        if(connected) {
            setText("Connected!! Type something");
        } else {
            setText("Connecting via adb...");
        }

        boolean running = connected && hasFocus();
        setBackground( running ? Color.WHITE : Color.GRAY);
    }

    void showDeviceChooser() {
        if(deviceChooser==null) {
            deviceChooser = new DeviceChooser((Window) getTopLevelAncestor(), this);
            AndroidDebugBridge.addDeviceChangeListener(deviceChooser);
            chimp = ChimpChat.getInstance();
            deviceChooser.pack();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                deviceChooser.setLocationRelativeTo(InputOntoDroid.this);
                System.out.println("[InputOntoDroid] showDeviceChooser.show ");
                deviceChooser.setVisible(true);
            }
        });
    }

    void requestBackgroundTask(Runnable r) {
        cmdExecutor.execute(r);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDevice = new JMenu("Device");
        JMenuItem itemShowChooser = new JMenuItem("Choose...");
        itemShowChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showDeviceChooser();
            }
        });
        menuDevice.add(itemShowChooser);
        menuBar.add(menuDevice);
        return menuBar;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        JFrame frame = new JFrame("typewhenwhite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InputOntoDroid main = new InputOntoDroid();
        frame.addWindowListener(main);
        frame.setContentPane(main);
        frame.setJMenuBar( main.createMenuBar() );
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}