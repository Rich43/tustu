package sun.nio.fs;

/* loaded from: rt.jar:sun/nio/fs/WindowsSecurity.class */
class WindowsSecurity {
    static final long processTokenWithDuplicateAccess = openProcessToken(2);
    static final long processTokenWithQueryAccess = openProcessToken(8);

    /* loaded from: rt.jar:sun/nio/fs/WindowsSecurity$Privilege.class */
    interface Privilege {
        void drop();
    }

    private WindowsSecurity() {
    }

    private static long openProcessToken(int i2) {
        try {
            return WindowsNativeDispatcher.OpenProcessToken(WindowsNativeDispatcher.GetCurrentProcess(), i2);
        } catch (WindowsException e2) {
            return 0L;
        }
    }

    static Privilege enablePrivilege(String str) {
        try {
            long jLookupPrivilegeValue = WindowsNativeDispatcher.LookupPrivilegeValue(str);
            long jOpenThreadToken = 0;
            boolean z2 = false;
            boolean z3 = false;
            try {
                jOpenThreadToken = WindowsNativeDispatcher.OpenThreadToken(WindowsNativeDispatcher.GetCurrentThread(), 32, false);
                if (jOpenThreadToken == 0 && processTokenWithDuplicateAccess != 0) {
                    jOpenThreadToken = WindowsNativeDispatcher.DuplicateTokenEx(processTokenWithDuplicateAccess, 36);
                    WindowsNativeDispatcher.SetThreadToken(0L, jOpenThreadToken);
                    z2 = true;
                }
                if (jOpenThreadToken != 0) {
                    WindowsNativeDispatcher.AdjustTokenPrivileges(jOpenThreadToken, jLookupPrivilegeValue, 2);
                    z3 = true;
                }
            } catch (WindowsException e2) {
            }
            long j2 = jOpenThreadToken;
            boolean z4 = z2;
            boolean z5 = z3;
            return () -> {
                try {
                    try {
                        if (j2 != 0) {
                            try {
                                if (z4) {
                                    WindowsNativeDispatcher.SetThreadToken(0L, 0L);
                                } else {
                                    if (z5) {
                                        WindowsNativeDispatcher.AdjustTokenPrivileges(j2, jLookupPrivilegeValue, 0);
                                    }
                                    WindowsNativeDispatcher.CloseHandle(j2);
                                }
                                WindowsNativeDispatcher.CloseHandle(j2);
                            } catch (WindowsException e3) {
                                throw new AssertionError(e3);
                            }
                        }
                    } catch (Throwable th) {
                        WindowsNativeDispatcher.CloseHandle(j2);
                        throw th;
                    }
                } finally {
                    WindowsNativeDispatcher.LocalFree(jLookupPrivilegeValue);
                }
            };
        } catch (WindowsException e3) {
            throw new AssertionError(e3);
        }
    }

    /* JADX WARN: Finally extract failed */
    static boolean checkAccessMask(long j2, int i2, int i3, int i4, int i5, int i6) throws WindowsException {
        long jOpenThreadToken = WindowsNativeDispatcher.OpenThreadToken(WindowsNativeDispatcher.GetCurrentThread(), 8, false);
        if (jOpenThreadToken == 0 && processTokenWithDuplicateAccess != 0) {
            jOpenThreadToken = WindowsNativeDispatcher.DuplicateTokenEx(processTokenWithDuplicateAccess, 8);
        }
        boolean zAccessCheck = false;
        if (jOpenThreadToken != 0) {
            try {
                zAccessCheck = WindowsNativeDispatcher.AccessCheck(jOpenThreadToken, j2, i2, i3, i4, i5, i6);
                WindowsNativeDispatcher.CloseHandle(jOpenThreadToken);
            } catch (Throwable th) {
                WindowsNativeDispatcher.CloseHandle(jOpenThreadToken);
                throw th;
            }
        }
        return zAccessCheck;
    }
}
