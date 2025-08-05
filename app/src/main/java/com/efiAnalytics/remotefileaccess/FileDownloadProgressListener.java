package com.efiAnalytics.remotefileaccess;

import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/FileDownloadProgressListener.class */
public interface FileDownloadProgressListener {
    void fileDownloadStarted(RemoteFileDescriptor remoteFileDescriptor);

    void fileDownloadProgressUpdate(long j2, long j3);

    void fileDownloadCompleted(RemoteFileDescriptor remoteFileDescriptor, File file);
}
