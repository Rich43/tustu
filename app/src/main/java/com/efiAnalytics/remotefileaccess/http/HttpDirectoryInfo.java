package com.efiAnalytics.remotefileaccess.http;

import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/http/HttpDirectoryInfo.class */
public class HttpDirectoryInfo implements DirectoryInformation, Serializable {
    private int fileCount = 0;
    private long totalBytes = 0;
    private long usedBytes = 0;
    private String description = "";

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public int getFileCount() {
        return this.fileCount;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getTotalBytes() {
        return this.totalBytes;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getUsedBytes() {
        return this.usedBytes;
    }
}
