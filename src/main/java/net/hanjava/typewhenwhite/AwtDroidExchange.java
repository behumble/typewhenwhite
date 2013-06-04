package net.hanjava.typewhenwhite;

import com.android.chimpchat.ChimpManager;

import java.awt.event.KeyEvent;
import java.io.IOException;

class AwtDroidExchange {
    private AwtDroidExchange() {
        // nothing to do
    }

    static void handleKeyTyped(KeyEvent ev, ChimpManager cm) throws IOException {
        char keyChar = ev.getKeyChar();
        switch (keyChar) {
            case KeyEvent.VK_SPACE:
                cm.press("KEYCODE_SPACE");
                break;
            case KeyEvent.VK_BACK_SPACE:
                cm.press("KEYCODE_DEL");
                break;
            case KeyEvent.VK_ESCAPE:
                cm.press("KEYCODE_ESCAPE");
                break;
            default:
                cm.type(keyChar);
        }
    }

    static void handleKeyPressed(KeyEvent ev, ChimpManager cm) throws IOException {
        int code = ev.getKeyCode();
        switch (code) {
            case KeyEvent.VK_HOME:
                cm.press("KEYCODE_HOME");
                break;
            case KeyEvent.VK_UP:
                cm.press("KEYCODE_DPAD_UP");
                break;
            case KeyEvent.VK_LEFT:
                cm.press("KEYCODE_DPAD_LEFT");
                break;
            case KeyEvent.VK_DOWN:
                cm.press("KEYCODE_DPAD_DOWN");
                break;
            case KeyEvent.VK_RIGHT:
                cm.press("KEYCODE_DPAD_RIGHT");
                break;
        }
    }
}