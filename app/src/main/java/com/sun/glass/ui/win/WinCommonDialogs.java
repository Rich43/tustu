package com.sun.glass.ui.win;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Window;
import java.io.File;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinCommonDialogs.class */
final class WinCommonDialogs {
    private static native void _initIDs();

    private static native CommonDialogs.FileChooserResult _showFileChooser(long j2, String str, String str2, String str3, int i2, boolean z2, CommonDialogs.ExtensionFilter[] extensionFilterArr, int i3);

    private static native String _showFolderChooser(long j2, String str, String str2);

    WinCommonDialogs() {
    }

    static {
        _initIDs();
    }

    static CommonDialogs.FileChooserResult showFileChooser_impl(Window owner, String folder, String filename, String title, int type, boolean multipleMode, CommonDialogs.ExtensionFilter[] extensionFilters, int defaultFilterIndex) {
        long nativeWindow;
        if (owner != null) {
            ((WinWindow) owner).setDeferredClosing(true);
        }
        if (owner != null) {
            try {
                nativeWindow = owner.getNativeWindow();
            } catch (Throwable th) {
                if (owner != null) {
                    ((WinWindow) owner).setDeferredClosing(false);
                }
                throw th;
            }
        } else {
            nativeWindow = 0;
        }
        CommonDialogs.FileChooserResult fileChooserResult_showFileChooser = _showFileChooser(nativeWindow, folder, filename, title, type, multipleMode, extensionFilters, defaultFilterIndex);
        if (owner != null) {
            ((WinWindow) owner).setDeferredClosing(false);
        }
        return fileChooserResult_showFileChooser;
    }

    static File showFolderChooser_impl(Window owner, String folder, String title) {
        long nativeWindow;
        if (owner != null) {
            ((WinWindow) owner).setDeferredClosing(true);
        }
        if (owner != null) {
            try {
                nativeWindow = owner.getNativeWindow();
            } finally {
                if (owner != null) {
                    ((WinWindow) owner).setDeferredClosing(false);
                }
            }
        } else {
            nativeWindow = 0;
        }
        String filename = _showFolderChooser(nativeWindow, folder, title);
        return filename != null ? new File(filename) : null;
    }
}
