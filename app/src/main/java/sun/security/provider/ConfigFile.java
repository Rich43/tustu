package sun.security.provider;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Security;
import java.security.URIParameter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.AuthPermission;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.ConfigurationSpi;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:sun/security/provider/ConfigFile.class */
public final class ConfigFile extends Configuration {
    private final Spi spi = new Spi();

    @Override // javax.security.auth.login.Configuration
    public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
        return this.spi.engineGetAppConfigurationEntry(str);
    }

    @Override // javax.security.auth.login.Configuration
    public synchronized void refresh() {
        this.spi.engineRefresh();
    }

    /* loaded from: rt.jar:sun/security/provider/ConfigFile$Spi.class */
    public static final class Spi extends ConfigurationSpi {
        private URL url;
        private boolean expandProp = true;
        private Map<String, List<AppConfigurationEntry>> configuration;
        private int linenum;
        private StreamTokenizer st;
        private int lookahead;
        private static Debug debugConfig = Debug.getInstance("configfile");
        private static Debug debugParser = Debug.getInstance("configparser");

        public Spi() {
            try {
                init();
            } catch (IOException e2) {
                throw new SecurityException(e2);
            }
        }

        public Spi(URI uri) {
            try {
                this.url = uri.toURL();
                init();
            } catch (IOException e2) {
                throw new SecurityException(e2);
            }
        }

        public Spi(final Configuration.Parameters parameters) throws IOException {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.security.provider.ConfigFile.Spi.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws IOException {
                        if (parameters == null) {
                            Spi.this.init();
                            return null;
                        }
                        if (!(parameters instanceof URIParameter)) {
                            throw new IllegalArgumentException("Unrecognized parameter: " + ((Object) parameters));
                        }
                        Spi.this.url = ((URIParameter) parameters).getURI().toURL();
                        Spi.this.init();
                        return null;
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void init() throws IOException {
            String strReplace;
            URL url;
            boolean z2 = false;
            String property = Security.getProperty("policy.expandProperties");
            if (property == null) {
                property = System.getProperty("policy.expandProperties");
            }
            if ("false".equals(property)) {
                this.expandProp = false;
            }
            Map<String, List<AppConfigurationEntry>> map = new HashMap<>();
            if (this.url != null) {
                if (debugConfig != null) {
                    debugConfig.println("reading " + ((Object) this.url));
                }
                init(this.url, map);
                this.configuration = map;
                return;
            }
            if ("true".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
                String property2 = System.getProperty("java.security.auth.login.config");
                if (property2 != null) {
                    boolean z3 = false;
                    if (property2.startsWith("=")) {
                        z3 = true;
                        property2 = property2.substring(1);
                    }
                    try {
                        property2 = PropertyExpander.expand(property2);
                        try {
                            url = new URL(property2);
                        } catch (MalformedURLException e2) {
                            File file = new File(property2);
                            if (file.exists()) {
                                url = file.toURI().toURL();
                            } else {
                                throw ioException("extra.config.No.such.file.or.directory.", property2);
                            }
                        }
                        if (debugConfig != null) {
                            debugConfig.println("reading " + ((Object) url));
                        }
                        init(url, map);
                        z2 = true;
                        if (z3) {
                            if (debugConfig != null) {
                                debugConfig.println("overriding other policies!");
                            }
                            this.configuration = map;
                            return;
                        }
                    } catch (PropertyExpander.ExpandException e3) {
                        throw ioException("Unable.to.properly.expand.config", property2);
                    }
                }
            }
            int i2 = 1;
            while (true) {
                String property3 = Security.getProperty("login.config.url." + i2);
                strReplace = property3;
                if (property3 == null) {
                    break;
                }
                try {
                    strReplace = PropertyExpander.expand(strReplace).replace(File.separatorChar, '/');
                    if (debugConfig != null) {
                        debugConfig.println("\tReading config: " + strReplace);
                    }
                    init(new URL(strReplace), map);
                    z2 = true;
                    i2++;
                } catch (PropertyExpander.ExpandException e4) {
                    throw ioException("Unable.to.properly.expand.config", strReplace);
                }
            }
            if (!z2 && i2 == 1 && strReplace == null) {
                if (debugConfig != null) {
                    debugConfig.println("\tReading Policy from ~/.java.login.config");
                }
                String str = System.getProperty("user.home") + File.separatorChar + ".java.login.config";
                if (new File(str).exists()) {
                    init(new File(str).toURI().toURL(), map);
                }
            }
            this.configuration = map;
        }

        private void init(URL url, Map<String, List<AppConfigurationEntry>> map) throws IOException {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(getInputStream(url), "UTF-8");
                Throwable th = null;
                try {
                    try {
                        readConfig(inputStreamReader, map);
                        if (inputStreamReader != null) {
                            if (0 != 0) {
                                try {
                                    inputStreamReader.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                inputStreamReader.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (FileNotFoundException e2) {
                if (debugConfig != null) {
                    debugConfig.println(e2.toString());
                }
                throw new IOException(ResourcesMgr.getString("Configuration.Error.No.such.file.or.directory", "sun.security.util.AuthResources"));
            }
        }

        @Override // javax.security.auth.login.ConfigurationSpi
        public AppConfigurationEntry[] engineGetAppConfigurationEntry(String str) {
            List<AppConfigurationEntry> list;
            synchronized (this.configuration) {
                list = this.configuration.get(str);
            }
            if (list == null || list.size() == 0) {
                return null;
            }
            AppConfigurationEntry[] appConfigurationEntryArr = new AppConfigurationEntry[list.size()];
            int i2 = 0;
            for (AppConfigurationEntry appConfigurationEntry : list) {
                appConfigurationEntryArr[i2] = new AppConfigurationEntry(appConfigurationEntry.getLoginModuleName(), appConfigurationEntry.getControlFlag(), appConfigurationEntry.getOptions());
                i2++;
            }
            return appConfigurationEntryArr;
        }

        @Override // javax.security.auth.login.ConfigurationSpi
        public synchronized void engineRefresh() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new AuthPermission("refreshLoginConfiguration"));
            }
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.ConfigFile.Spi.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    try {
                        Spi.this.init();
                        return null;
                    } catch (IOException e2) {
                        throw new SecurityException(e2.getLocalizedMessage(), e2);
                    }
                }
            });
        }

        private void readConfig(Reader reader, Map<String, List<AppConfigurationEntry>> map) throws IOException {
            this.linenum = 1;
            if (!(reader instanceof BufferedReader)) {
                reader = new BufferedReader(reader);
            }
            this.st = new StreamTokenizer(reader);
            this.st.quoteChar(34);
            this.st.wordChars(36, 36);
            this.st.wordChars(95, 95);
            this.st.wordChars(45, 45);
            this.st.wordChars(42, 42);
            this.st.lowerCaseMode(false);
            this.st.slashSlashComments(true);
            this.st.slashStarComments(true);
            this.st.eolIsSignificant(true);
            this.lookahead = nextToken();
            while (this.lookahead != -1) {
                parseLoginEntry(map);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00f0  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00f8  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0100  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0108  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0132  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0178  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0110 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:56:0x01f1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void parseLoginEntry(java.util.Map<java.lang.String, java.util.List<javax.security.auth.login.AppConfigurationEntry>> r8) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 569
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.ConfigFile.Spi.parseLoginEntry(java.util.Map):void");
        }

        private String match(String str) throws IOException {
            String str2 = null;
            switch (this.lookahead) {
                case -3:
                case 34:
                    if (str.equalsIgnoreCase("module class name") || str.equalsIgnoreCase("controlFlag") || str.equalsIgnoreCase("option key") || str.equalsIgnoreCase("option value")) {
                        str2 = this.st.sval;
                        this.lookahead = nextToken();
                        break;
                    } else {
                        throw ioException("Configuration.Error.Line.line.expected.expect.found.value.", new Integer(this.linenum), str, this.st.sval);
                    }
                    break;
                case -1:
                    throw ioException("Configuration.Error.expected.expect.read.end.of.file.", str);
                case 59:
                    if (str.equalsIgnoreCase(";")) {
                        this.lookahead = nextToken();
                        break;
                    } else {
                        throw ioException("Configuration.Error.Line.line.expected.expect.", new Integer(this.linenum), str, this.st.sval);
                    }
                case 61:
                    if (str.equalsIgnoreCase("=")) {
                        this.lookahead = nextToken();
                        break;
                    } else {
                        throw ioException("Configuration.Error.Line.line.expected.expect.", new Integer(this.linenum), str, this.st.sval);
                    }
                case 123:
                    if (str.equalsIgnoreCase(VectorFormat.DEFAULT_PREFIX)) {
                        this.lookahead = nextToken();
                        break;
                    } else {
                        throw ioException("Configuration.Error.Line.line.expected.expect.", new Integer(this.linenum), str, this.st.sval);
                    }
                case 125:
                    if (str.equalsIgnoreCase("}")) {
                        this.lookahead = nextToken();
                        break;
                    } else {
                        throw ioException("Configuration.Error.Line.line.expected.expect.", new Integer(this.linenum), str, this.st.sval);
                    }
                default:
                    throw ioException("Configuration.Error.Line.line.expected.expect.found.value.", new Integer(this.linenum), str, this.st.sval);
            }
            return str2;
        }

        private boolean peek(String str) {
            switch (this.lookahead) {
                case 44:
                    return str.equalsIgnoreCase(",");
                case 59:
                    return str.equalsIgnoreCase(";");
                case 123:
                    return str.equalsIgnoreCase(VectorFormat.DEFAULT_PREFIX);
                case 125:
                    return str.equalsIgnoreCase("}");
                default:
                    return false;
            }
        }

        private int nextToken() throws IOException {
            while (true) {
                int iNextToken = this.st.nextToken();
                if (iNextToken == 10) {
                    this.linenum++;
                } else {
                    return iNextToken;
                }
            }
        }

        private InputStream getInputStream(URL url) throws IOException {
            if (DeploymentDescriptorParser.ATTR_FILE.equalsIgnoreCase(url.getProtocol())) {
                try {
                    return url.openStream();
                } catch (Exception e2) {
                    String path = url.getPath();
                    if (url.getHost().length() > 0) {
                        path = "//" + url.getHost() + path;
                    }
                    if (debugConfig != null) {
                        debugConfig.println("cannot read " + ((Object) url) + ", try " + path);
                    }
                    return new FileInputStream(path);
                }
            }
            return url.openStream();
        }

        private String expand(String str) throws IOException, PropertyExpander.ExpandException {
            if (str.isEmpty()) {
                return str;
            }
            if (!this.expandProp) {
                return str;
            }
            String strExpand = PropertyExpander.expand(str);
            if (strExpand == null || strExpand.length() == 0) {
                throw ioException("Configuration.Error.Line.line.system.property.value.expanded.to.empty.value", new Integer(this.linenum), str);
            }
            return strExpand;
        }

        private IOException ioException(String str, Object... objArr) {
            return new IOException(new MessageFormat(ResourcesMgr.getString(str, "sun.security.util.AuthResources")).format(objArr));
        }
    }
}
