package com.efiAnalytics.remotefileaccess;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/DirectoryIdentifier.class */
public class DirectoryIdentifier {
    protected long numericId = -1;
    protected boolean isNumericId = false;
    protected String directoryId = null;

    public String getDirectoryId() {
        return this.directoryId;
    }

    public long getNumericId() {
        return this.numericId;
    }
}
