package com.intel.bluetooth;

import com.ibm.oti.vm.VM;
import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/IBMJ9Helper.class */
class IBMJ9Helper {
    IBMJ9Helper() {
    }

    static synchronized void loadLibrary(String libname) throws IOException {
        VM.loadLibrary(libname);
    }

    static void addShutdownClass(Runnable hook) {
        VM.addShutdownClass(hook);
    }
}
