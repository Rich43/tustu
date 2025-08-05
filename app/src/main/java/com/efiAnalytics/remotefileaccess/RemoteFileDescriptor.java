package com.efiAnalytics.remotefileaccess;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/RemoteFileDescriptor.class */
public class RemoteFileDescriptor implements Serializable {
    private String name = "";
    private long size = -1;
    private long lastModified = 0;
    private DirectoryIdentifier directory = null;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j2) {
        this.size = j2;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long j2) {
        this.lastModified = j2;
    }

    public DirectoryIdentifier getDirectory() {
        return this.directory;
    }

    public void setDirectory(DirectoryIdentifier directoryIdentifier) {
        this.directory = directoryIdentifier;
    }
}
