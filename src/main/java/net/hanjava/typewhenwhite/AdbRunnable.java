package net.hanjava.typewhenwhite;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;

class AdbRunnable implements Runnable {
    static final byte CMD_CONNECT = 0;

    static interface Listener {
        void adbConnected(IChimpDevice device);
        void adbDisconnected();
    }
    private Listener listener;
    private ChimpChat chimp;
    byte command;

    AdbRunnable(ChimpChat chimp, Listener listener, byte cmd) {
        if(listener==null) throw new NullPointerException("listener can not be null");
        this.chimp = chimp;
        this.listener = listener;
        this.command = cmd;
    }

    @Override
    public void run() {
        switch (command) {
            case CMD_CONNECT:
                IChimpDevice device = chimp.waitForConnection();
                listener.adbConnected(device);
                break;
            default:
                throw new IllegalArgumentException("Unknown command : "+command);
        }
    }
}