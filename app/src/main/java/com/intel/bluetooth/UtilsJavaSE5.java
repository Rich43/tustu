package com.intel.bluetooth;

import com.intel.bluetooth.UtilsJavaSE;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/UtilsJavaSE5.class */
public class UtilsJavaSE5 implements UtilsJavaSE.JavaSE5Features {
    @Override // com.intel.bluetooth.UtilsJavaSE.JavaSE5Features
    public void clearProperty(String propertyName) {
        System.clearProperty(propertyName);
    }
}
