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

import java.util.Properties;

public class ShellUtil {
    private ShellUtil() {
        // do not create an instance
    }

    public static Properties parseGetProp(String raw) {
        String lines[] = raw.split("\\r?\\n");
        Properties result = new Properties();
        for(int i=0;i<lines.length;i++) {
            String line = lines[i];
            try {
                int start = 1;  // skip beginning '['
                int end = line.indexOf(']');
                String key = line.substring(start, end);
                start = line.indexOf('[', end) + 1;
                end = line.indexOf(']', start);
                String value = line.substring(start, end);
                result.setProperty(key, value);
            } catch (StringIndexOutOfBoundsException sioobe) {
                System.err.println("Failed to parse : "+line);
                sioobe.printStackTrace();
            }
        }
        return result;
    }

    public static DeviceInfo createDeviceInfo(IDevice device, String serial, String raw) {
        Properties prop = parseGetProp(raw);
        String brand = prop.getProperty("ro.product.brand");
        String model = prop.getProperty("ro.product.model");
        DeviceInfo deviceInfo = new DeviceInfo(device, serial, brand, model);
        return deviceInfo;
    }
}
