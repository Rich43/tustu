package com.efiAnalytics.remotefileaccess.http;

import W.InterfaceC0191q;
import ab.C0484a;
import ay.C0934k;
import bH.C;
import bH.C1005m;
import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryIdentifier;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import com.efiAnalytics.remotefileaccess.FileDownloadProgressListener;
import com.efiAnalytics.remotefileaccess.RefreshNeededListener;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/http/HttpFileAccess.class */
public class HttpFileAccess implements RemoteFileAccess {
    String hostName;
    File downloadDirectory;
    int port;
    List fileDownloadListeners = new ArrayList();
    List refreshListeners = new ArrayList();
    MyDownloadProgressListener dlListener = new MyDownloadProgressListener();
    private FileAccessPreferences fileAccessPreferences = null;

    /* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/http/HttpFileAccess$MyDownloadProgressListener.class */
    class MyDownloadProgressListener implements InterfaceC0191q {
        private RemoteFileDescriptor fileDescriptor = null;
        private long fileSize = -1;
        private File downloadFile = null;

        MyDownloadProgressListener() {
        }

        @Override // W.InterfaceC0191q
        public void started(long j2) {
            this.fileSize = j2;
            Iterator it = HttpFileAccess.this.fileDownloadListeners.iterator();
            while (it.hasNext()) {
                ((FileDownloadProgressListener) it.next()).fileDownloadStarted(this.fileDescriptor);
            }
        }

        @Override // W.InterfaceC0191q
        public void updateProgress(long j2, double d2) {
            Iterator it = HttpFileAccess.this.fileDownloadListeners.iterator();
            while (it.hasNext()) {
                ((FileDownloadProgressListener) it.next()).fileDownloadProgressUpdate(j2, this.fileSize);
            }
        }

        @Override // W.InterfaceC0191q
        public void completed() {
            Iterator it = HttpFileAccess.this.fileDownloadListeners.iterator();
            while (it.hasNext()) {
                ((FileDownloadProgressListener) it.next()).fileDownloadCompleted(this.fileDescriptor, this.downloadFile);
            }
        }

        public void setFileDescriptor(RemoteFileDescriptor remoteFileDescriptor) {
            this.fileDescriptor = remoteFileDescriptor;
        }

        public void setDownloadFile(File file) {
            this.downloadFile = file;
        }
    }

    public HttpFileAccess(String str, int i2, File file) {
        this.downloadDirectory = null;
        this.hostName = str;
        this.downloadDirectory = file;
        this.port = i2;
    }

    private String getCoreUrl(String str) {
        String str2 = this.port == 80 ? "http://" + this.hostName + "/LogFileActions?" : "http://" + this.hostName + CallSiteDescriptor.TOKEN_DELIMITER + this.port + "/LogFileActions?";
        if (str != null && !str.isEmpty()) {
            str2 = str2 + "projectName=" + str + "&";
        }
        return str2;
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public DirectoryFiles getFilesIn(DirectoryIdentifier directoryIdentifier) throws RemoteAccessException {
        String str = getCoreUrl(directoryIdentifier == null ? null : directoryIdentifier.getDirectoryId()) + "action=listLogs";
        try {
            String strA = C0934k.a(str);
            if (strA.toLowerCase().startsWith(Pack200.Packer.ERROR)) {
                throw new RemoteAccessException(strA);
            }
            try {
                List listA = C0484a.a(RemoteFileDescriptor[].class, strA);
                DirectoryInformation directoryInformation = getDirectoryInformation(directoryIdentifier);
                DirectoryFiles directoryFiles = new DirectoryFiles();
                directoryFiles.setDirectoryInformation(directoryInformation);
                directoryFiles.setFiles(listA);
                return directoryFiles;
            } catch (Exception e2) {
                C.a("Could not parse this json: " + strA);
                throw new RemoteAccessException("Error parsing response from " + this.hostName + ", error: " + e2.getLocalizedMessage());
            }
        } catch (IOException e3) {
            C.a("Failed to read from url: " + str);
            throw new RemoteAccessException("Unable to read file list from " + this.hostName + ", error: " + e3.getLocalizedMessage());
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public File readRemoteFile(File file, RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        String coreUrl = getCoreUrl(remoteFileDescriptor.getDirectory().getDirectoryId());
        if (remoteFileDescriptor.getName() == null || remoteFileDescriptor.getName().isEmpty()) {
            throw new RemoteAccessException("fileName is required!");
        }
        String str = coreUrl + "action=downloadLog&logFileName=" + remoteFileDescriptor.getName();
        if (this.fileAccessPreferences != null) {
            file = new File(this.fileAccessPreferences.getDownloadDirectory());
        } else if (file == null || !file.exists()) {
            file = this.downloadDirectory;
        }
        File file2 = new File(file, remoteFileDescriptor.getName());
        int i2 = 1;
        if (this.fileAccessPreferences != null && this.fileAccessPreferences.getFileExistsPreference() != 1) {
            if (this.fileAccessPreferences.getFileExistsPreference() != 0) {
                while (file2.exists()) {
                    int i3 = i2;
                    i2++;
                    file2 = new File(file, remoteFileDescriptor.getName() + "(" + i3 + ")");
                }
            } else {
                if (remoteFileDescriptor.getSize() == file2.length()) {
                    this.dlListener.setDownloadFile(file2);
                    this.dlListener.setFileDescriptor(remoteFileDescriptor);
                    this.dlListener.started(file2.length());
                    this.dlListener.updateProgress(file2.length(), 1.0d);
                    this.dlListener.completed();
                    return file2;
                }
                C.c("Preference to return local, but local file and remote file are different sizes, going to overwrite.");
            }
        }
        try {
            this.dlListener.setFileDescriptor(remoteFileDescriptor);
            this.dlListener.setDownloadFile(file2);
            C1005m.a(str, file2.getAbsolutePath(), this.dlListener);
            file2.setLastModified(remoteFileDescriptor.getLastModified());
            return file2;
        } catch (IOException e2) {
            Logger.getLogger(HttpFileAccess.class.getName()).log(Level.WARNING, "File Download Failed.", (Throwable) e2);
            throw new RemoteAccessException("Failed to download " + remoteFileDescriptor.getName() + ", Error: " + e2.getLocalizedMessage());
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public boolean deleteFile(RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        String coreUrl = getCoreUrl(URLEncoder.encode(remoteFileDescriptor.getDirectory().getDirectoryId()));
        if (remoteFileDescriptor.getName() == null || remoteFileDescriptor.getName().isEmpty()) {
            throw new RemoteAccessException("fileName is required!");
        }
        try {
            String strA = C1005m.a(coreUrl + "action=deleteLog&logFileName=" + URLEncoder.encode(remoteFileDescriptor.getName()));
            if (!strA.equals("SUCCESSFUL")) {
                throw new RemoteAccessException("Delete file " + remoteFileDescriptor.getName() + " failed, Error: " + strA);
            }
            notifyRefreshNeeded();
            return true;
        } catch (IOException e2) {
            Logger.getLogger(HttpFileAccess.class.getName()).log(Level.WARNING, "Unable to delete file", (Throwable) e2);
            throw new RemoteAccessException("Unable to delete file " + remoteFileDescriptor.getName() + ", Error: " + e2.getLocalizedMessage());
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public File getDownloadDirectory() {
        return this.downloadDirectory;
    }

    public DirectoryInformation getDirectoryInformation(DirectoryIdentifier directoryIdentifier) throws RemoteAccessException {
        String str = getCoreUrl(directoryIdentifier == null ? null : directoryIdentifier.getDirectoryId()) + "action=getDirectoryInfo";
        try {
            String strA = C1005m.a(str);
            try {
                return (DirectoryInformation) C0484a.b(HttpDirectoryInfo.class, strA);
            } catch (IOException e2) {
                C.a("Could not parse this json: " + strA);
                throw new RemoteAccessException("Error parsing response from " + this.hostName + ", error: " + e2.getLocalizedMessage());
            }
        } catch (IOException e3) {
            C.a("Failed to read from url: " + str);
            throw new RemoteAccessException("Unable to read file list from " + this.hostName + ", error: " + e3.getLocalizedMessage());
        }
    }

    private void notifyRefreshNeeded() {
        Iterator it = this.refreshListeners.iterator();
        while (it.hasNext()) {
            ((RefreshNeededListener) it.next()).refreshView();
        }
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.refreshListeners.add(refreshNeededListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeRefreshNeededListener(RefreshNeededListener refreshNeededListener) {
        this.refreshListeners.remove(refreshNeededListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void addFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.fileDownloadListeners.add(fileDownloadProgressListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void removeFileDownloadProgressListener(FileDownloadProgressListener fileDownloadProgressListener) {
        this.fileDownloadListeners.remove(fileDownloadProgressListener);
    }

    @Override // com.efiAnalytics.remotefileaccess.RemoteFileAccess
    public void cancelReadFile() {
    }

    public void setFileAccessPreferences(FileAccessPreferences fileAccessPreferences) {
        this.fileAccessPreferences = fileAccessPreferences;
    }
}
