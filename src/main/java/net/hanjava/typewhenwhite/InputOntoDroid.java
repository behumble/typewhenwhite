package net.hanjava.typewhenwhite;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InputOntoDroid extends JLabel implements KeyListener, FocusListener, MouseListener, WindowListener, AdbRunnable.Listener {
    private IChimpDevice device;
    private Executor cmdExecutor = Executors.newSingleThreadExecutor();

    public InputOntoDroid() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        URL logoUrl = InputOntoDroid.class.getClassLoader().getResource("HP-Keyboard-icon.png");
        setIcon(new ImageIcon(logoUrl));
        setFont(getFont().deriveFont(20f));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.TOP);
        enableInputMethods(false);
        addKeyListener(this);
        addFocusListener(this);
        addMouseListener(this);
        setOpaque(true);
        updateStatusToUI();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        System.out.println("[InputOntoDroid] "+keyEvent);

        try {
            AwtDroidExchange.handleKeyTyped(keyEvent, device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("[InputOntoDroid] "+keyEvent);
        try {
            AwtDroidExchange.handleKeyPressed(keyEvent, device.getManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // do nothing
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
        ChimpChat chimp = ChimpChat.getInstance();
        AdbRunnable task = new AdbRunnable(chimp, this, AdbRunnable.CMD_CONNECT);
        requestFocus();
        cmdExecutor.execute(task);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("typewhenwhite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InputOntoDroid mainPanel = new InputOntoDroid();
        frame.addWindowListener(mainPanel);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}