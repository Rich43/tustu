package com.efiAnalytics.remotefileaccess;

import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/RemoteFileAccess.class */
public interface RemoteFileAccess {
    DirectoryFiles getFilesIn(DirectoryIdentifier directoryIdentifier);

    File readRemoteFile(File file, RemoteFileDescriptor remoteFileDescriptor);

    boolean deleteFile(RemoteFileDescriptor remoteFileDescriptor);

    File getDownloadDirectory();

    void addRefreshNeededListener(RefreshNeededListener refreshNeededListener);

    void removeRefreshNeededListener(RefreshNeededListener refreshNeededListener);

    void addFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener);

    void removeFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener);

    void cancelReadFile();
}
