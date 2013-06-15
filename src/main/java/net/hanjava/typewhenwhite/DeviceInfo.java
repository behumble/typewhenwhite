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

import com.android.ddmlib.IDevice;

public class DeviceInfo {
    public String serial;
    public String brand;
    public String model;
    public IDevice device;

    public DeviceInfo(IDevice device, String serial, String brand, String model) {
        this.device = device;
        this.serial = serial;
        this.brand = brand;
        this.model = model;
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s]", brand, model, serial);
    }
}
