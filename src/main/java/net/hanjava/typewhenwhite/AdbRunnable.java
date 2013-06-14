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