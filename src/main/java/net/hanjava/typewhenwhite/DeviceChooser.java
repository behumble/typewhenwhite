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

import com.android.ddmlib.*;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.Window;
import java.io.IOException;
import java.util.Properties;

public class DeviceChooser extends JDialog implements AndroidDebugBridge.IDeviceChangeListener, ListSelectionListener {
    private DefaultListModel deviceListModel = new DefaultListModel();
    private InputOntoDroid main;

    DeviceChooser(Window parent, InputOntoDroid main) {
        super(parent, "Choose a device");
        JList deviceList = new JList(deviceListModel);
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deviceList.addListSelectionListener(this);
        deviceList.setPreferredSize(new Dimension(300, 300));
        add(deviceList);
        this.main = main;
    }

    @Override
    public void deviceConnected(IDevice iDevice) {
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
        try {
            iDevice.executeShellCommand("getprop", receiver);
            DeviceInfo deviceInfo = ShellUtil.createDeviceInfo(iDevice, iDevice.getSerialNumber(), receiver.getOutput());
            deviceListModel.addElement(deviceInfo);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deviceDisconnected(IDevice iDevice) {
        int size = deviceListModel.size();
        for(int i=0;i<size;i++) {
            DeviceInfo deviceInfo = (DeviceInfo) deviceListModel.get(i);
            boolean match = deviceInfo.serial.equals(iDevice.getSerialNumber());
            if(match) deviceListModel.removeElement(deviceInfo);
        }
    }

    @Override
    public void deviceChanged(IDevice iDevice, int i) {
        // nothing to do
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if(listSelectionEvent.getValueIsAdjusting()) return;
        int i = ((JList)listSelectionEvent.getSource()).getSelectedIndex();
        IDevice iDevice = ((DeviceInfo) deviceListModel.get(i)).device;
        String sn = iDevice.getSerialNumber();
        Runnable r = new AdbRunnable(main, AdbRunnable.CMD_CONNECT, sn);
        main.requestBackgroundTask(r);
        this.setVisible(false);
    }
}
