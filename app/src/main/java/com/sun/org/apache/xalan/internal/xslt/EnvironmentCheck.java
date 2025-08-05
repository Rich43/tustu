package com.sun.org.apache.xalan.internal.xslt;

import com.sun.jmx.defaults.ServiceName;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xslt/EnvironmentCheck.class */
public class EnvironmentCheck {
    public static final String ERROR = "ERROR.";
    public static final String WARNING = "WARNING.";
    public static final String ERROR_FOUND = "At least one error was found!";
    public static final String VERSION = "version.";
    public static final String FOUNDCLASSES = "foundclasses.";
    public static final String CLASS_PRESENT = "present-unknown-version";
    public static final String CLASS_NOTPRESENT = "not-present";
    private static final Map<Long, String> JARVERSIONS;
    public String[] jarNames = {"xalan.jar", "xalansamples.jar", "xalanj1compat.jar", "xalanservlet.jar", "serializer.jar", "xerces.jar", "xercesImpl.jar", "testxsl.jar", "crimson.jar", "lotusxsl.jar", "jaxp.jar", "parser.jar", "dom.jar", "sax.jar", "xml.jar", "xml-apis.jar", "xsltc.jar"};
    protected PrintWriter outWriter = new PrintWriter((OutputStream) System.out, true);

    public static void main(String[] args) throws ConfigurationError {
        PrintWriter sendOutputTo = new PrintWriter((OutputStream) System.out, true);
        int i2 = 0;
        while (i2 < args.length) {
            if ("-out".equalsIgnoreCase(args[i2])) {
                i2++;
                if (i2 < args.length) {
                    try {
                        sendOutputTo = new PrintWriter(new FileWriter(args[i2], true));
                    } catch (Exception e2) {
                        System.err.println("# WARNING: -out " + args[i2] + " threw " + e2.toString());
                    }
                } else {
                    System.err.println("# WARNING: -out argument should have a filename, output sent to console");
                }
            }
            i2++;
        }
        EnvironmentCheck app = new EnvironmentCheck();
        app.checkEnvironment(sendOutputTo);
    }

    public boolean checkEnvironment(PrintWriter pw) throws ConfigurationError {
        if (null != pw) {
            this.outWriter = pw;
        }
        Map<String, Object> hash = getEnvironmentHash();
        boolean environmentHasErrors = writeEnvironmentReport(hash);
        if (environmentHasErrors) {
            logMsg("# WARNING: Potential problems found in your environment!");
            logMsg("#    Check any 'ERROR' items above against the Xalan FAQs");
            logMsg("#    to correct potential problems with your classes/jars");
            logMsg("#    http://xml.apache.org/xalan-j/faq.html");
            if (null != this.outWriter) {
                this.outWriter.flush();
                return false;
            }
            return false;
        }
        logMsg("# YAHOO! Your environment seems to be OK.");
        if (null != this.outWriter) {
            this.outWriter.flush();
            return true;
        }
        return true;
    }

    public Map<String, Object> getEnvironmentHash() throws ConfigurationError {
        Map<String, Object> hash = new HashMap<>();
        checkJAXPVersion(hash);
        checkProcessorVersion(hash);
        checkParserVersion(hash);
        checkAntVersion(hash);
        if (!checkDOML3(hash)) {
            checkDOMVersion(hash);
        }
        checkSAXVersion(hash);
        checkSystemProperties(hash);
        return hash;
    }

    protected boolean writeEnvironmentReport(Map<String, Object> h2) {
        if (null == h2) {
            logMsg("# ERROR: writeEnvironmentReport called with null Map");
            return false;
        }
        boolean errors = false;
        logMsg("#---- BEGIN writeEnvironmentReport($Revision: 1.10 $): Useful stuff found: ----");
        for (Map.Entry<String, Object> entry : h2.entrySet()) {
            String keyStr = entry.getKey();
            try {
                if (keyStr.startsWith(FOUNDCLASSES)) {
                    List<Map> v2 = (ArrayList) entry.getValue();
                    errors |= logFoundJars(v2, keyStr);
                } else {
                    if (keyStr.startsWith(ERROR)) {
                        errors = true;
                    }
                    logMsg(keyStr + "=" + h2.get(keyStr));
                }
            } catch (Exception e2) {
                logMsg("Reading-" + keyStr + "= threw: " + e2.toString());
            }
        }
        logMsg("#----- END writeEnvironmentReport: Useful properties found: -----");
        return errors;
    }

    protected boolean logFoundJars(List<Map> v2, String desc) {
        if (null == v2 || v2.size() < 1) {
            return false;
        }
        boolean errors = false;
        logMsg("#---- BEGIN Listing XML-related jars in: " + desc + " ----");
        for (Map<String, String> v1 : v2) {
            for (Map.Entry<String, String> entry : v1.entrySet()) {
                String keyStr = entry.getKey();
                try {
                    if (keyStr.startsWith(ERROR)) {
                        errors = true;
                    }
                    logMsg(keyStr + "=" + entry.getValue());
                } catch (Exception e2) {
                    errors = true;
                    logMsg("Reading-" + keyStr + "= threw: " + e2.toString());
                }
            }
        }
        logMsg("#----- END Listing XML-related jars in: " + desc + " -----");
        return errors;
    }

    public void appendEnvironmentReport(Node container, Document factory, Map<String, Object> h2) {
        if (null == container || null == factory) {
            return;
        }
        try {
            Element envCheckNode = factory.createElement("EnvironmentCheck");
            envCheckNode.setAttribute("version", "$Revision: 1.10 $");
            container.appendChild(envCheckNode);
            if (null == h2) {
                Element statusNode = factory.createElement("status");
                statusNode.setAttribute("result", "ERROR");
                statusNode.appendChild(factory.createTextNode("appendEnvironmentReport called with null Map!"));
                envCheckNode.appendChild(statusNode);
                return;
            }
            boolean errors = false;
            Element hashNode = factory.createElement("environment");
            envCheckNode.appendChild(hashNode);
            for (Map.Entry<String, Object> entry : h2.entrySet()) {
                String keyStr = entry.getKey();
                try {
                    if (keyStr.startsWith(FOUNDCLASSES)) {
                        List<Map> v2 = (List) entry.getValue();
                        errors |= appendFoundJars(hashNode, factory, v2, keyStr);
                    } else {
                        if (keyStr.startsWith(ERROR)) {
                            errors = true;
                        }
                        Element node = factory.createElement("item");
                        node.setAttribute("key", keyStr);
                        node.appendChild(factory.createTextNode((String) h2.get(keyStr)));
                        hashNode.appendChild(node);
                    }
                } catch (Exception e2) {
                    errors = true;
                    Element node2 = factory.createElement("item");
                    node2.setAttribute("key", keyStr);
                    node2.appendChild(factory.createTextNode("ERROR. Reading " + keyStr + " threw: " + e2.toString()));
                    hashNode.appendChild(node2);
                }
            }
            Element statusNode2 = factory.createElement("status");
            statusNode2.setAttribute("result", errors ? "ERROR" : "OK");
            envCheckNode.appendChild(statusNode2);
        } catch (Exception e22) {
            System.err.println("appendEnvironmentReport threw: " + e22.toString());
            e22.printStackTrace();
        }
    }

    protected boolean appendFoundJars(Node container, Document factory, List<Map> v2, String desc) throws DOMException {
        if (null == v2 || v2.size() < 1) {
            return false;
        }
        boolean errors = false;
        for (Map<String, String> v1 : v2) {
            for (Map.Entry<String, String> entry : v1.entrySet()) {
                String keyStr = entry.getKey();
                try {
                    if (keyStr.startsWith(ERROR)) {
                        errors = true;
                    }
                    Element node = factory.createElement("foundJar");
                    node.setAttribute("name", keyStr.substring(0, keyStr.indexOf(LanguageTag.SEP)));
                    node.setAttribute("desc", keyStr.substring(keyStr.indexOf(LanguageTag.SEP) + 1));
                    node.appendChild(factory.createTextNode(entry.getValue()));
                    container.appendChild(node);
                } catch (Exception e2) {
                    errors = true;
                    Element node2 = factory.createElement("foundJar");
                    node2.appendChild(factory.createTextNode("ERROR. Reading " + keyStr + " threw: " + e2.toString()));
                    container.appendChild(node2);
                }
            }
        }
        return errors;
    }

    protected void checkSystemProperties(Map<String, Object> h2) {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            String javaVersion = SecuritySupport.getSystemProperty("java.version");
            h2.put("java.version", javaVersion);
        } catch (SecurityException e2) {
            h2.put("java.version", "WARNING: SecurityException thrown accessing system version properties");
        }
        try {
            String cp = SecuritySupport.getSystemProperty("java.class.path");
            h2.put("java.class.path", cp);
            List<Map> classpathJars = checkPathForJars(cp, this.jarNames);
            if (null != classpathJars) {
                h2.put("foundclasses.java.class.path", classpathJars);
            }
            String othercp = SecuritySupport.getSystemProperty("sun.boot.class.path");
            if (null != othercp) {
                h2.put("sun.boot.class.path", othercp);
                List<Map> classpathJars2 = checkPathForJars(othercp, this.jarNames);
                if (null != classpathJars2) {
                    h2.put("foundclasses.sun.boot.class.path", classpathJars2);
                }
            }
            String othercp2 = SecuritySupport.getSystemProperty("java.ext.dirs");
            if (null != othercp2) {
                h2.put("java.ext.dirs", othercp2);
                List<Map> classpathJars3 = checkPathForJars(othercp2, this.jarNames);
                if (null != classpathJars3) {
                    h2.put("foundclasses.java.ext.dirs", classpathJars3);
                }
            }
        } catch (SecurityException e3) {
            h2.put("java.class.path", "WARNING: SecurityException thrown accessing system classpath properties");
        }
    }

    protected List<Map> checkPathForJars(String cp, String[] jars) {
        if (null == cp || null == jars || 0 == cp.length() || 0 == jars.length) {
            return null;
        }
        List<Map> v2 = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String filename = st.nextToken();
            for (int i2 = 0; i2 < jars.length; i2++) {
                if (filename.indexOf(jars[i2]) > -1) {
                    File f2 = new File(filename);
                    if (f2.exists()) {
                        try {
                            Map<String, String> h2 = new HashMap<>(2);
                            h2.put(jars[i2] + "-path", f2.getAbsolutePath());
                            if (!"xalan.jar".equalsIgnoreCase(jars[i2])) {
                                h2.put(jars[i2] + "-apparent.version", getApparentVersion(jars[i2], f2.length()));
                            }
                            v2.add(h2);
                        } catch (Exception e2) {
                        }
                    } else {
                        Map<String, String> h3 = new HashMap<>(2);
                        h3.put(jars[i2] + "-path", "WARNING. Classpath entry: " + filename + " does not exist");
                        h3.put(jars[i2] + "-apparent.version", CLASS_NOTPRESENT);
                        v2.add(h3);
                    }
                }
            }
        }
        return v2;
    }

    protected String getApparentVersion(String jarName, long jarSize) {
        String foundSize = JARVERSIONS.get(new Long(jarSize));
        if (null != foundSize && foundSize.startsWith(jarName)) {
            return foundSize;
        }
        if ("xerces.jar".equalsIgnoreCase(jarName) || "xercesImpl.jar".equalsIgnoreCase(jarName)) {
            return jarName + " " + WARNING + CLASS_PRESENT;
        }
        return jarName + " " + CLASS_PRESENT;
    }

    protected void checkJAXPVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            ObjectFactory.findProviderClass("javax.xml.stream.XMLStreamConstants", true);
            h2.put("version.JAXP", ServiceName.JMX_SPEC_VERSION);
        } catch (Exception e2) {
            h2.put("ERROR.version.JAXP", "1.3");
            h2.put(ERROR, ERROR_FOUND);
        }
    }

    protected void checkProcessorVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.xslt.XSLProcessorVersion", true);
            StringBuffer buf = new StringBuffer();
            Field f2 = clazz.getField("PRODUCT");
            buf.append(f2.get(null));
            buf.append(';');
            Field f3 = clazz.getField("LANGUAGE");
            buf.append(f3.get(null));
            buf.append(';');
            Field f4 = clazz.getField("S_VERSION");
            buf.append(f4.get(null));
            buf.append(';');
            h2.put("version.xalan1", buf.toString());
        } catch (Exception e2) {
            h2.put("version.xalan1", CLASS_NOTPRESENT);
        }
        try {
            Class clazz2 = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.processor.XSLProcessorVersion", true);
            StringBuffer buf2 = new StringBuffer();
            Field f5 = clazz2.getField("S_VERSION");
            buf2.append(f5.get(null));
            h2.put("version.xalan2x", buf2.toString());
        } catch (Exception e3) {
            h2.put("version.xalan2x", CLASS_NOTPRESENT);
        }
        try {
            Class<?>[] noArgs = new Class[0];
            Class clazz3 = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.Version", true);
            Method method = clazz3.getMethod("getVersion", noArgs);
            Object returnValue = method.invoke(null, new Object[0]);
            h2.put("version.xalan2_2", (String) returnValue);
        } catch (Exception e4) {
            h2.put("version.xalan2_2", CLASS_NOTPRESENT);
        }
    }

    protected void checkParserVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.framework.Version", true);
            Field f2 = clazz.getField("fVersion");
            String parserVersion = (String) f2.get(null);
            h2.put("version.xerces1", parserVersion);
        } catch (Exception e2) {
            h2.put("version.xerces1", CLASS_NOTPRESENT);
        }
        try {
            Class clazz2 = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.impl.Version", true);
            Field f3 = clazz2.getField("fVersion");
            String parserVersion2 = (String) f3.get(null);
            h2.put("version.xerces2", parserVersion2);
        } catch (Exception e3) {
            h2.put("version.xerces2", CLASS_NOTPRESENT);
        }
        try {
            ObjectFactory.findProviderClass("org.apache.crimson.parser.Parser2", true);
            h2.put("version.crimson", CLASS_PRESENT);
        } catch (Exception e4) {
            h2.put("version.crimson", CLASS_NOTPRESENT);
        }
    }

    protected void checkAntVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            Class<?>[] noArgs = new Class[0];
            Class clazz = ObjectFactory.findProviderClass("org.apache.tools.ant.Main", true);
            Method method = clazz.getMethod("getAntVersion", noArgs);
            Object returnValue = method.invoke(null, new Object[0]);
            h2.put("version.ant", (String) returnValue);
        } catch (Exception e2) {
            h2.put("version.ant", CLASS_NOTPRESENT);
        }
    }

    protected boolean checkDOML3(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        try {
            Class clazz = ObjectFactory.findProviderClass("org.w3c.dom.Document", true);
            clazz.getMethod("getDoctype", (Class[]) null);
            h2.put("version.DOM", "3.0");
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    protected void checkDOMVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        Class<?>[] twoStringArgs = {String.class, String.class};
        try {
            Class clazz = ObjectFactory.findProviderClass("org.w3c.dom.Document", true);
            clazz.getMethod("createElementNS", twoStringArgs);
            h2.put("version.DOM", "2.0");
            try {
                Class clazz2 = ObjectFactory.findProviderClass("org.w3c.dom.Node", true);
                clazz2.getMethod("supported", twoStringArgs);
                h2.put("ERROR.version.DOM.draftlevel", "2.0wd");
                h2.put(ERROR, ERROR_FOUND);
            } catch (Exception e2) {
                try {
                    Class clazz3 = ObjectFactory.findProviderClass("org.w3c.dom.Node", true);
                    clazz3.getMethod("isSupported", twoStringArgs);
                    h2.put("version.DOM.draftlevel", "2.0fd");
                } catch (Exception e3) {
                    h2.put("ERROR.version.DOM.draftlevel", "2.0unknown");
                    h2.put(ERROR, ERROR_FOUND);
                }
            }
        } catch (Exception e4) {
            h2.put("ERROR.version.DOM", "ERROR attempting to load DOM level 2 class: " + e4.toString());
            h2.put(ERROR, ERROR_FOUND);
        }
    }

    protected void checkSAXVersion(Map<String, Object> h2) throws ConfigurationError {
        if (null == h2) {
            h2 = new HashMap();
        }
        Class<?>[] oneStringArg = {String.class};
        Class<?>[] attributesArg = {Attributes.class};
        try {
            Class clazz = ObjectFactory.findProviderClass("org.xml.sax.helpers.AttributesImpl", true);
            clazz.getMethod("setAttributes", attributesArg);
            h2.put("version.SAX", "2.0");
        } catch (Exception e2) {
            h2.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + e2.toString());
            h2.put(ERROR, ERROR_FOUND);
            try {
                Class clazz2 = ObjectFactory.findProviderClass("org.xml.sax.XMLReader", true);
                clazz2.getMethod("parse", oneStringArg);
                h2.put("version.SAX-backlevel", "2.0beta2-or-earlier");
            } catch (Exception e3) {
                h2.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + e2.toString());
                h2.put(ERROR, ERROR_FOUND);
                try {
                    Class clazz3 = ObjectFactory.findProviderClass("org.xml.sax.Parser", true);
                    clazz3.getMethod("parse", oneStringArg);
                    h2.put("version.SAX-backlevel", "1.0");
                } catch (Exception e32) {
                    h2.put("ERROR.version.SAX-backlevel", "ERROR attempting to load SAX version 1 class: " + e32.toString());
                }
            }
        }
    }

    static {
        Map<Long, String> jarVersions = new HashMap<>();
        jarVersions.put(new Long(857192L), "xalan.jar from xalan-j_1_1");
        jarVersions.put(new Long(440237L), "xalan.jar from xalan-j_1_2");
        jarVersions.put(new Long(436094L), "xalan.jar from xalan-j_1_2_1");
        jarVersions.put(new Long(426249L), "xalan.jar from xalan-j_1_2_2");
        jarVersions.put(new Long(702536L), "xalan.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(720930L), "xalan.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(732330L), "xalan.jar from xalan-j_2_1_0");
        jarVersions.put(new Long(872241L), "xalan.jar from xalan-j_2_2_D10");
        jarVersions.put(new Long(882739L), "xalan.jar from xalan-j_2_2_D11");
        jarVersions.put(new Long(923866L), "xalan.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(905872L), "xalan.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(906122L), "xalan.jar from xalan-j_2_3_0");
        jarVersions.put(new Long(906248L), "xalan.jar from xalan-j_2_3_1");
        jarVersions.put(new Long(983377L), "xalan.jar from xalan-j_2_4_D1");
        jarVersions.put(new Long(997276L), "xalan.jar from xalan-j_2_4_0");
        jarVersions.put(new Long(1031036L), "xalan.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(596540L), "xsltc.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(590247L), "xsltc.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(589914L), "xsltc.jar from xalan-j_2_3_0");
        jarVersions.put(new Long(589915L), "xsltc.jar from xalan-j_2_3_1");
        jarVersions.put(new Long(1306667L), "xsltc.jar from xalan-j_2_4_D1");
        jarVersions.put(new Long(1328227L), "xsltc.jar from xalan-j_2_4_0");
        jarVersions.put(new Long(1344009L), "xsltc.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(1348361L), "xsltc.jar from xalan-j_2_5_D1");
        jarVersions.put(new Long(1268634L), "xsltc.jar-bundled from xalan-j_2_3_0");
        jarVersions.put(new Long(100196L), "xml-apis.jar from xalan-j_2_2_0 or xalan-j_2_3_D1");
        jarVersions.put(new Long(108484L), "xml-apis.jar from xalan-j_2_3_0, or xalan-j_2_3_1 from xml-commons-1.0.b2");
        jarVersions.put(new Long(109049L), "xml-apis.jar from xalan-j_2_4_0 from xml-commons RIVERCOURT1 branch");
        jarVersions.put(new Long(113749L), "xml-apis.jar from xalan-j_2_4_1 from factoryfinder-build of xml-commons RIVERCOURT1");
        jarVersions.put(new Long(124704L), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons");
        jarVersions.put(new Long(124724L), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons, tag: xml-commons-external_1_2_01");
        jarVersions.put(new Long(194205L), "xml-apis.jar from head branch of xml-commons, tag: xml-commons-external_1_3_02");
        jarVersions.put(new Long(424490L), "xalan.jar from Xerces Tools releases - ERROR:DO NOT USE!");
        jarVersions.put(new Long(1591855L), "xerces.jar from xalan-j_1_1 from xerces-1...");
        jarVersions.put(new Long(1498679L), "xerces.jar from xalan-j_1_2 from xerces-1_2_0.bin");
        jarVersions.put(new Long(1484896L), "xerces.jar from xalan-j_1_2_1 from xerces-1_2_1.bin");
        jarVersions.put(new Long(804460L), "xerces.jar from xalan-j_1_2_2 from xerces-1_2_2.bin");
        jarVersions.put(new Long(1499244L), "xerces.jar from xalan-j_2_0_0 from xerces-1_2_3.bin");
        jarVersions.put(new Long(1605266L), "xerces.jar from xalan-j_2_0_1 from xerces-1_3_0.bin");
        jarVersions.put(new Long(904030L), "xerces.jar from xalan-j_2_1_0 from xerces-1_4.bin");
        jarVersions.put(new Long(904030L), "xerces.jar from xerces-1_4_0.bin");
        jarVersions.put(new Long(1802885L), "xerces.jar from xerces-1_4_2.bin");
        jarVersions.put(new Long(1734594L), "xerces.jar from Xerces-J-bin.2.0.0.beta3");
        jarVersions.put(new Long(1808883L), "xerces.jar from xalan-j_2_2_D10,D11,D12 or xerces-1_4_3.bin");
        jarVersions.put(new Long(1812019L), "xerces.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(1720292L), "xercesImpl.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(1730053L), "xercesImpl.jar from xalan-j_2_3_0 or xalan-j_2_3_1 from xerces-2_0_0");
        jarVersions.put(new Long(1728861L), "xercesImpl.jar from xalan-j_2_4_D1 from xerces-2_0_1");
        jarVersions.put(new Long(972027L), "xercesImpl.jar from xalan-j_2_4_0 from xerces-2_1");
        jarVersions.put(new Long(831587L), "xercesImpl.jar from xalan-j_2_4_1 from xerces-2_2");
        jarVersions.put(new Long(891817L), "xercesImpl.jar from xalan-j_2_5_D1 from xerces-2_3");
        jarVersions.put(new Long(895924L), "xercesImpl.jar from xerces-2_4");
        jarVersions.put(new Long(1010806L), "xercesImpl.jar from Xerces-J-bin.2.6.2");
        jarVersions.put(new Long(1203860L), "xercesImpl.jar from Xerces-J-bin.2.7.1");
        jarVersions.put(new Long(37485L), "xalanj1compat.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(38100L), "xalanj1compat.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(18779L), "xalanservlet.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(21453L), "xalanservlet.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(24826L), "xalanservlet.jar from xalan-j_2_3_1 or xalan-j_2_4_1");
        jarVersions.put(new Long(24831L), "xalanservlet.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(5618L), "jaxp.jar from jaxp1.0.1");
        jarVersions.put(new Long(136133L), "parser.jar from jaxp1.0.1");
        jarVersions.put(new Long(28404L), "jaxp.jar from jaxp-1.1");
        jarVersions.put(new Long(187162L), "crimson.jar from jaxp-1.1");
        jarVersions.put(new Long(801714L), "xalan.jar from jaxp-1.1");
        jarVersions.put(new Long(196399L), "crimson.jar from crimson-1.1.1");
        jarVersions.put(new Long(33323L), "jaxp.jar from crimson-1.1.1 or jakarta-ant-1.4.1b1");
        jarVersions.put(new Long(152717L), "crimson.jar from crimson-1.1.2beta2");
        jarVersions.put(new Long(88143L), "xml-apis.jar from crimson-1.1.2beta2");
        jarVersions.put(new Long(206384L), "crimson.jar from crimson-1.1.3 or jakarta-ant-1.4.1b1");
        jarVersions.put(new Long(136198L), "parser.jar from jakarta-ant-1.3 or 1.2");
        jarVersions.put(new Long(5537L), "jaxp.jar from jakarta-ant-1.3 or 1.2");
        JARVERSIONS = Collections.unmodifiableMap(jarVersions);
    }

    protected void logMsg(String s2) {
        this.outWriter.println(s2);
    }
}
