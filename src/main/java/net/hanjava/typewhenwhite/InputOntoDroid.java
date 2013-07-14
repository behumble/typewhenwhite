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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InputOntoDroid extends JPanel implements WindowListener, AdbRunnable.Listener {
    IChimpDevice device;
    private Executor cmdExecutor = Executors.newSingleThreadExecutor();
    private DeviceChooser deviceChooser;
    ChimpChat chimp;
    private FaceLabel faceLabel;
    private PropertyPanel propertyPanel;

    public InputOntoDroid() {
        setLayout(new BorderLayout());
        faceLabel = new FaceLabel(this);
        faceLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(BorderLayout.CENTER, faceLabel);
        propertyPanel = new PropertyPanel(this);
        propertyPanel.setBorder(BorderFactory.createTitledBorder("Properties"));
        add(BorderLayout.EAST, propertyPanel);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
        showDeviceChooser();
        faceLabel.requestFocus();
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
        propertyPanel.extractFrom(device);
        updateStatusToUI();
    }

    @Override
    public void adbDisconnected() {
        this.device = null;
        updateStatusToUI();
    }

    private void updateStatusToUI() {
        faceLabel.updateStatusToUI();
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