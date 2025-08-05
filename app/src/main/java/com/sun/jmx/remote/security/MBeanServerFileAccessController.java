package com.sun.jmx.remote.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.auth.Subject;

/* loaded from: rt.jar:com/sun/jmx/remote/security/MBeanServerFileAccessController.class */
public class MBeanServerFileAccessController extends MBeanServerAccessController {
    static final String READONLY = "readonly";
    static final String READWRITE = "readwrite";
    static final String CREATE = "create";
    static final String UNREGISTER = "unregister";
    private Map<String, Access> accessMap;
    private Properties originalProps;
    private String accessFileName;

    /* loaded from: rt.jar:com/sun/jmx/remote/security/MBeanServerFileAccessController$AccessType.class */
    private enum AccessType {
        READ,
        WRITE,
        CREATE,
        UNREGISTER
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/security/MBeanServerFileAccessController$Access.class */
    private static class Access {
        final boolean write;
        final String[] createPatterns;
        private boolean unregister;
        private final String[] NO_STRINGS = new String[0];

        Access(boolean z2, boolean z3, List<String> list) {
            this.write = z2;
            int size = list == null ? 0 : list.size();
            if (size == 0) {
                this.createPatterns = this.NO_STRINGS;
            } else {
                this.createPatterns = (String[]) list.toArray(new String[size]);
            }
            this.unregister = z3;
        }
    }

    public MBeanServerFileAccessController(String str) throws IOException {
        this.accessFileName = str;
        parseProperties(propertiesFromFile(str));
    }

    public MBeanServerFileAccessController(String str, MBeanServer mBeanServer) throws IOException {
        this(str);
        setMBeanServer(mBeanServer);
    }

    public MBeanServerFileAccessController(Properties properties) throws IOException {
        if (properties == null) {
            throw new IllegalArgumentException("Null properties");
        }
        this.originalProps = properties;
        parseProperties(properties);
    }

    public MBeanServerFileAccessController(Properties properties, MBeanServer mBeanServer) throws IOException {
        this(properties);
        setMBeanServer(mBeanServer);
    }

    @Override // com.sun.jmx.remote.security.MBeanServerAccessController
    public void checkRead() {
        checkAccess(AccessType.READ, null);
    }

    @Override // com.sun.jmx.remote.security.MBeanServerAccessController
    public void checkWrite() {
        checkAccess(AccessType.WRITE, null);
    }

    @Override // com.sun.jmx.remote.security.MBeanServerAccessController
    public void checkCreate(String str) {
        checkAccess(AccessType.CREATE, str);
    }

    @Override // com.sun.jmx.remote.security.MBeanServerAccessController
    public void checkUnregister(ObjectName objectName) {
        checkAccess(AccessType.UNREGISTER, null);
    }

    public synchronized void refresh() throws IOException {
        Properties propertiesPropertiesFromFile;
        if (this.accessFileName == null) {
            propertiesPropertiesFromFile = this.originalProps;
        } else {
            propertiesPropertiesFromFile = propertiesFromFile(this.accessFileName);
        }
        parseProperties(propertiesPropertiesFromFile);
    }

    private static Properties propertiesFromFile(String str) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(str);
        try {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
            return properties;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    private synchronized void checkAccess(AccessType accessType, String str) {
        boolean zCheckCreateAccess;
        final AccessControlContext context = AccessController.getContext();
        Subject subject = (Subject) AccessController.doPrivileged(new PrivilegedAction<Subject>() { // from class: com.sun.jmx.remote.security.MBeanServerFileAccessController.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Subject run2() {
                return Subject.getSubject(context);
            }
        });
        if (subject == null) {
            return;
        }
        String str2 = null;
        Iterator<Principal> it = subject.getPrincipals().iterator();
        while (it.hasNext()) {
            Access access = this.accessMap.get(it.next().getName());
            if (access != null) {
                switch (accessType) {
                    case READ:
                        zCheckCreateAccess = true;
                        break;
                    case WRITE:
                        zCheckCreateAccess = access.write;
                        break;
                    case UNREGISTER:
                        zCheckCreateAccess = access.unregister;
                        if (!zCheckCreateAccess && access.write) {
                            str2 = UNREGISTER;
                            break;
                        }
                        break;
                    case CREATE:
                        zCheckCreateAccess = checkCreateAccess(access, str);
                        if (!zCheckCreateAccess && access.write) {
                            str2 = "create " + str;
                            break;
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
                if (zCheckCreateAccess) {
                    return;
                }
            }
        }
        SecurityException securityException = new SecurityException("Access denied! Invalid access level for requested MBeanServer operation.");
        if (str2 != null) {
            securityException.initCause(new SecurityException("Access property for this identity should be similar to: readwrite " + str2));
        }
        throw securityException;
    }

    private static boolean checkCreateAccess(Access access, String str) {
        for (String str2 : access.createPatterns) {
            if (classNameMatch(str2, str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean classNameMatch(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer stringTokenizer = new StringTokenizer(str, "*", true);
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.equals("*")) {
                sb.append("[^.]*");
            } else {
                sb.append(Pattern.quote(strNextToken));
            }
        }
        return str2.matches(sb.toString());
    }

    private void parseProperties(Properties properties) {
        this.accessMap = new HashMap();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String str = (String) entry.getKey();
            this.accessMap.put(str, Parser.parseAccess(str, (String) entry.getValue()));
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/security/MBeanServerFileAccessController$Parser.class */
    private static class Parser {
        private static final int EOS = -1;
        private final String identity;

        /* renamed from: s, reason: collision with root package name */
        private final String f11975s;
        private final int len;

        /* renamed from: i, reason: collision with root package name */
        private int f11976i = 0;

        /* renamed from: c, reason: collision with root package name */
        private int f11977c;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MBeanServerFileAccessController.class.desiredAssertionStatus();
            if (!$assertionsDisabled && Character.isWhitespace(-1)) {
                throw new AssertionError();
            }
        }

        private Parser(String str, String str2) {
            this.identity = str;
            this.f11975s = str2;
            this.len = str2.length();
            if (this.f11976i < this.len) {
                this.f11977c = str2.codePointAt(this.f11976i);
            } else {
                this.f11977c = -1;
            }
        }

        static Access parseAccess(String str, String str2) {
            return new Parser(str, str2).parseAccess();
        }

        private Access parseAccess() {
            Access readWrite;
            skipSpace();
            String word = parseWord();
            if (word.equals(MBeanServerFileAccessController.READONLY)) {
                readWrite = new Access(false, false, null);
            } else if (word.equals(MBeanServerFileAccessController.READWRITE)) {
                readWrite = parseReadWrite();
            } else {
                throw syntax("Expected readonly or readwrite: " + word);
            }
            if (this.f11977c != -1) {
                throw syntax("Extra text at end of line");
            }
            return readWrite;
        }

        private Access parseReadWrite() {
            ArrayList arrayList = new ArrayList();
            boolean z2 = false;
            while (true) {
                skipSpace();
                if (this.f11977c != -1) {
                    String word = parseWord();
                    if (word.equals(MBeanServerFileAccessController.UNREGISTER)) {
                        z2 = true;
                    } else if (word.equals(MBeanServerFileAccessController.CREATE)) {
                        parseCreate(arrayList);
                    } else {
                        throw syntax("Unrecognized keyword " + word);
                    }
                } else {
                    return new Access(true, z2, arrayList);
                }
            }
        }

        private void parseCreate(List<String> list) {
            while (true) {
                skipSpace();
                list.add(parseClassName());
                skipSpace();
                if (this.f11977c == 44) {
                    next();
                } else {
                    return;
                }
            }
        }

        private String parseClassName() {
            boolean z2;
            int i2 = this.f11976i;
            boolean z3 = false;
            while (true) {
                if (this.f11977c == 46) {
                    if (!z3) {
                        throw syntax("Bad . in class name");
                    }
                    z2 = false;
                } else if (this.f11977c == 42 || Character.isJavaIdentifierPart(this.f11977c)) {
                    z2 = true;
                } else {
                    String strSubstring = this.f11975s.substring(i2, this.f11976i);
                    if (!z3) {
                        throw syntax("Bad class name " + strSubstring);
                    }
                    return strSubstring;
                }
                z3 = z2;
                next();
            }
        }

        private void next() {
            if (this.f11977c != -1) {
                this.f11976i += Character.charCount(this.f11977c);
                if (this.f11976i < this.len) {
                    this.f11977c = this.f11975s.codePointAt(this.f11976i);
                } else {
                    this.f11977c = -1;
                }
            }
        }

        private void skipSpace() {
            while (Character.isWhitespace(this.f11977c)) {
                next();
            }
        }

        private String parseWord() {
            skipSpace();
            if (this.f11977c == -1) {
                throw syntax("Expected word at end of line");
            }
            int i2 = this.f11976i;
            while (this.f11977c != -1 && !Character.isWhitespace(this.f11977c)) {
                next();
            }
            String strSubstring = this.f11975s.substring(i2, this.f11976i);
            skipSpace();
            return strSubstring;
        }

        private IllegalArgumentException syntax(String str) {
            return new IllegalArgumentException(str + " [" + this.identity + " " + this.f11975s + "]");
        }
    }
}
