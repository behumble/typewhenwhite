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
import com.android.ddmlib.IDevice;

import java.util.Arrays;

class AdbRunnable implements Runnable {
    static final byte CMD_CONNECT = 0;

    static interface Listener {
        void adbConnected(IChimpDevice device);
        void adbDisconnected();
    }

    private InputOntoDroid main;
    byte command;
    private Object param;

    AdbRunnable(InputOntoDroid main, byte cmd, Object param) {
        this.main = main;
        this.command = cmd;
        this.param = param;
    }

    @Override
    public void run() {
        switch (command) {
            case CMD_CONNECT:
                String serial = param.toString();
                System.out.println("[AdbRunnable] trying to connect to "+serial);
                if(main.device!=null) {
                    main.device.dispose();
                    main.adbDisconnected();
                }
                ChimpChat chimp = main.chimp;
                IChimpDevice device = chimp.waitForConnection(5000, serial);
                System.out.println("[AdbRunnable] connected to "+serial);
                main.adbConnected(device);
                break;
            default:
                throw new IllegalArgumentException("Unknown command : "+command);
        }
    }
}