package com.efiAnalytics.remotefileaccess;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/RemoteAccessException.class */
public class RemoteAccessException extends Exception {
    private boolean terminalToBatch;

    public RemoteAccessException(String str) {
        super(str);
        this.terminalToBatch = false;
    }

    public boolean isTerminalToBatch() {
        return this.terminalToBatch;
    }

    public void setTerminalToBatch(boolean z2) {
        this.terminalToBatch = z2;
    }
}
