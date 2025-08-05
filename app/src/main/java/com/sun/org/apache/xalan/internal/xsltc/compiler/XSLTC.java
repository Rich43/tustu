package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.xalan.internal.XalanConstants;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import javax.xml.XMLConstants;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.XMLSecurityManager;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/XSLTC.class */
public final class XSLTC {
    private Parser _parser;
    private Stylesheet _stylesheet;
    private int[] _numberFieldIndexes;
    private int _nextGType;
    private Vector _namesIndex;
    private Map<String, Integer> _elements;
    private Map<String, Integer> _attributes;
    private int _nextNSType;
    private Vector _namespaceIndex;
    private Map<String, Integer> _namespaces;
    private Map<String, Integer> _namespacePrefixes;
    private Vector m_characterData;
    public static final int FILE_OUTPUT = 0;
    public static final int JAR_OUTPUT = 1;
    public static final int BYTEARRAY_OUTPUT = 2;
    public static final int CLASSLOADER_OUTPUT = 3;
    public static final int BYTEARRAY_AND_FILE_OUTPUT = 4;
    public static final int BYTEARRAY_AND_JAR_OUTPUT = 5;
    private Vector _classes;
    private Vector _bcelClasses;
    private boolean _overrideDefaultParser;
    private XMLSecurityManager _xmlSecurityManager;
    private final JdkXmlFeatures _xmlFeatures;
    private XMLReader _reader = null;
    private SourceLoader _loader = null;
    private int _modeSerial = 1;
    private int _stylesheetSerial = 1;
    private int _stepPatternSerial = 1;
    private int _helperClassSerial = 0;
    private int _attributeSetSerial = 0;
    private boolean _debug = false;
    private String _jarFileName = null;
    private String _className = null;
    private String _packageName = null;
    private File _destDir = null;
    private int _outputType = 0;
    private boolean _callsNodeset = false;
    private boolean _multiDocument = false;
    private boolean _hasIdCall = false;
    private boolean _templateInlining = false;
    private boolean _isSecureProcessing = false;
    private String _accessExternalStylesheet = "all";
    private String _accessExternalDTD = "all";
    private ClassLoader _extensionClassLoader = null;
    private final Map<String, Class> _externalExtensionFunctions = new HashMap();

    public XSLTC(JdkXmlFeatures featureManager) {
        this._overrideDefaultParser = featureManager.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
        this._parser = new Parser(this, this._overrideDefaultParser);
        this._xmlFeatures = featureManager;
    }

    public void setSecureProcessing(boolean flag) {
        this._isSecureProcessing = flag;
    }

    public boolean isSecureProcessing() {
        return this._isSecureProcessing;
    }

    public boolean getFeature(JdkXmlFeatures.XmlFeature name) {
        return this._xmlFeatures.getFeature(name);
    }

    public Object getProperty(String name) {
        if (name.equals(XMLConstants.ACCESS_EXTERNAL_STYLESHEET)) {
            return this._accessExternalStylesheet;
        }
        if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD")) {
            return this._accessExternalDTD;
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            return this._xmlSecurityManager;
        }
        if (name.equals(XalanConstants.JDK_EXTENSION_CLASSLOADER)) {
            return this._extensionClassLoader;
        }
        return null;
    }

    public void setProperty(String name, Object value) {
        if (name.equals(XMLConstants.ACCESS_EXTERNAL_STYLESHEET)) {
            this._accessExternalStylesheet = (String) value;
            return;
        }
        if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD")) {
            this._accessExternalDTD = (String) value;
            return;
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            this._xmlSecurityManager = (XMLSecurityManager) value;
        } else if (name.equals(XalanConstants.JDK_EXTENSION_CLASSLOADER)) {
            this._extensionClassLoader = (ClassLoader) value;
            this._externalExtensionFunctions.clear();
        }
    }

    public Parser getParser() {
        return this._parser;
    }

    public void setOutputType(int type) {
        this._outputType = type;
    }

    public Properties getOutputProperties() {
        return this._parser.getOutputProperties();
    }

    public void init() {
        reset();
        this._reader = null;
        this._classes = new Vector();
        this._bcelClasses = new Vector();
    }

    private void setExternalExtensionFunctions(String name, Class clazz) {
        if (this._isSecureProcessing && clazz != null && !this._externalExtensionFunctions.containsKey(name)) {
            this._externalExtensionFunctions.put(name, clazz);
        }
    }

    Class loadExternalFunction(String name) throws ClassNotFoundException {
        Class loaded = null;
        if (this._externalExtensionFunctions.containsKey(name)) {
            loaded = this._externalExtensionFunctions.get(name);
        } else if (this._extensionClassLoader != null) {
            loaded = Class.forName(name, true, this._extensionClassLoader);
            setExternalExtensionFunctions(name, loaded);
        }
        if (loaded == null) {
            throw new ClassNotFoundException(name);
        }
        return loaded;
    }

    public Map<String, Class> getExternalExtensionFunctions() {
        return Collections.unmodifiableMap(this._externalExtensionFunctions);
    }

    private void reset() {
        this._nextGType = 14;
        this._elements = new HashMap();
        this._attributes = new HashMap();
        this._namespaces = new HashMap();
        this._namespaces.put("", new Integer(this._nextNSType));
        this._namesIndex = new Vector(128);
        this._namespaceIndex = new Vector(32);
        this._namespacePrefixes = new HashMap();
        this._stylesheet = null;
        this._parser.init();
        this._modeSerial = 1;
        this._stylesheetSerial = 1;
        this._stepPatternSerial = 1;
        this._helperClassSerial = 0;
        this._attributeSetSerial = 0;
        this._multiDocument = false;
        this._hasIdCall = false;
        this._numberFieldIndexes = new int[]{-1, -1, -1};
        this._externalExtensionFunctions.clear();
    }

    public void setSourceLoader(SourceLoader loader) {
        this._loader = loader;
    }

    public void setTemplateInlining(boolean templateInlining) {
        this._templateInlining = templateInlining;
    }

    public boolean getTemplateInlining() {
        return this._templateInlining;
    }

    public void setPIParameters(String media, String title, String charset) {
        this._parser.setPIParameters(media, title, charset);
    }

    public boolean compile(URL url) {
        try {
            InputStream stream = url.openStream();
            InputSource input = new InputSource(stream);
            input.setSystemId(url.toString());
            return compile(input, this._className);
        } catch (IOException e2) {
            this._parser.reportError(2, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e2));
            return false;
        }
    }

    public boolean compile(URL url, String name) {
        try {
            InputStream stream = url.openStream();
            InputSource input = new InputSource(stream);
            input.setSystemId(url.toString());
            return compile(input, name);
        } catch (IOException e2) {
            this._parser.reportError(2, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e2));
            return false;
        }
    }

    public boolean compile(InputStream stream, String name) {
        InputSource input = new InputSource(stream);
        input.setSystemId(name);
        return compile(input, name);
    }

    public boolean compile(InputSource input, String name) {
        try {
            try {
                try {
                    reset();
                    String systemId = null;
                    if (input != null) {
                        systemId = input.getSystemId();
                    }
                    if (this._className == null) {
                        if (name != null) {
                            setClassName(name);
                        } else if (systemId != null && !systemId.equals("")) {
                            setClassName(Util.baseName(systemId));
                        }
                        if (this._className == null || this._className.length() == 0) {
                            setClassName("GregorSamsa");
                        }
                    }
                    SyntaxTreeNode element = this._reader == null ? this._parser.parse(input) : this._parser.parse(this._reader, input);
                    if (!this._parser.errorsFound() && element != null) {
                        this._stylesheet = this._parser.makeStylesheet(element);
                        this._stylesheet.setSourceLoader(this._loader);
                        this._stylesheet.setSystemId(systemId);
                        this._stylesheet.setParentStylesheet(null);
                        this._stylesheet.setTemplateInlining(this._templateInlining);
                        this._parser.setCurrentStylesheet(this._stylesheet);
                        this._parser.createAST(this._stylesheet);
                    }
                    if (!this._parser.errorsFound() && this._stylesheet != null) {
                        this._stylesheet.setCallsNodeset(this._callsNodeset);
                        this._stylesheet.setMultiDocument(this._multiDocument);
                        this._stylesheet.setHasIdCall(this._hasIdCall);
                        synchronized (getClass()) {
                            this._stylesheet.translate();
                        }
                    }
                    this._reader = null;
                } catch (Exception e2) {
                    if (this._debug) {
                        e2.printStackTrace();
                    }
                    if (ErrorMsg.XPATH_LIMIT.equals(e2.getMessage())) {
                        boolean z2 = !this._parser.errorsFound();
                        this._reader = null;
                        return z2;
                    }
                    this._parser.reportError(2, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e2));
                    this._reader = null;
                }
            } catch (Error e3) {
                if (this._debug) {
                    e3.printStackTrace();
                }
                this._parser.reportError(2, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e3));
                this._reader = null;
            }
            return !this._parser.errorsFound();
        } catch (Throwable th) {
            this._reader = null;
            throw th;
        }
    }

    public boolean compile(Vector stylesheets) {
        int count = stylesheets.size();
        if (count == 0) {
            return true;
        }
        if (count == 1) {
            Object url = stylesheets.firstElement();
            if (url instanceof URL) {
                return compile((URL) url);
            }
            return false;
        }
        Enumeration urls = stylesheets.elements();
        while (urls.hasMoreElements()) {
            this._className = null;
            Object url2 = urls.nextElement();
            if ((url2 instanceof URL) && !compile((URL) url2)) {
                return false;
            }
        }
        return true;
    }

    public byte[][] getBytecodes() {
        int count = this._classes.size();
        byte[][] result = new byte[count][1];
        for (int i2 = 0; i2 < count; i2++) {
            result[i2] = (byte[]) this._classes.elementAt(i2);
        }
        return result;
    }

    public byte[][] compile(String name, InputSource input, int outputType) {
        this._outputType = outputType;
        if (compile(input, name)) {
            return getBytecodes();
        }
        return (byte[][]) null;
    }

    public byte[][] compile(String name, InputSource input) {
        return compile(name, input, 2);
    }

    public void setXMLReader(XMLReader reader) {
        this._reader = reader;
    }

    public XMLReader getXMLReader() {
        return this._reader;
    }

    public ArrayList<ErrorMsg> getErrors() {
        return this._parser.getErrors();
    }

    public ArrayList<ErrorMsg> getWarnings() {
        return this._parser.getWarnings();
    }

    public void printErrors() {
        this._parser.printErrors();
    }

    public void printWarnings() {
        this._parser.printWarnings();
    }

    protected void setMultiDocument(boolean flag) {
        this._multiDocument = flag;
    }

    public boolean isMultiDocument() {
        return this._multiDocument;
    }

    protected void setCallsNodeset(boolean flag) {
        if (flag) {
            setMultiDocument(flag);
        }
        this._callsNodeset = flag;
    }

    public boolean callsNodeset() {
        return this._callsNodeset;
    }

    protected void setHasIdCall(boolean flag) {
        this._hasIdCall = flag;
    }

    public boolean hasIdCall() {
        return this._hasIdCall;
    }

    public void setClassName(String className) {
        String base = Util.baseName(className);
        String noext = Util.noExtName(base);
        String name = Util.toJavaName(noext);
        if (this._packageName == null) {
            this._className = name;
        } else {
            this._className = this._packageName + '.' + name;
        }
    }

    public String getClassName() {
        return this._className;
    }

    private String classFileName(String className) {
        return className.replace('.', File.separatorChar) + ".class";
    }

    private File getOutputFile(String className) {
        if (this._destDir != null) {
            return new File(this._destDir, classFileName(className));
        }
        return new File(classFileName(className));
    }

    public boolean setDestDirectory(String dstDirName) {
        File dir = new File(dstDirName);
        if (SecuritySupport.getFileExists(dir) || dir.mkdirs()) {
            this._destDir = dir;
            return true;
        }
        this._destDir = null;
        return false;
    }

    public void setPackageName(String packageName) {
        this._packageName = packageName;
        if (this._className != null) {
            setClassName(this._className);
        }
    }

    public void setJarFileName(String jarFileName) {
        if (jarFileName.endsWith(".jar")) {
            this._jarFileName = jarFileName;
        } else {
            this._jarFileName = jarFileName + ".jar";
        }
        this._outputType = 1;
    }

    public String getJarFileName() {
        return this._jarFileName;
    }

    public void setStylesheet(Stylesheet stylesheet) {
        if (this._stylesheet == null) {
            this._stylesheet = stylesheet;
        }
    }

    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    public int registerAttribute(QName name) {
        Integer code = this._attributes.get(name.toString());
        if (code == null) {
            int i2 = this._nextGType;
            this._nextGType = i2 + 1;
            code = Integer.valueOf(i2);
            this._attributes.put(name.toString(), code);
            String uri = name.getNamespace();
            String local = "@" + name.getLocalPart();
            if (uri != null && !uri.equals("")) {
                this._namesIndex.addElement(uri + CallSiteDescriptor.TOKEN_DELIMITER + local);
            } else {
                this._namesIndex.addElement(local);
            }
            if (name.getLocalPart().equals("*")) {
                registerNamespace(name.getNamespace());
            }
        }
        return code.intValue();
    }

    public int registerElement(QName name) {
        Integer code = this._elements.get(name.toString());
        if (code == null) {
            Map<String, Integer> map = this._elements;
            String string = name.toString();
            int i2 = this._nextGType;
            this._nextGType = i2 + 1;
            Integer numValueOf = Integer.valueOf(i2);
            code = numValueOf;
            map.put(string, numValueOf);
            this._namesIndex.addElement(name.toString());
        }
        if (name.getLocalPart().equals("*")) {
            registerNamespace(name.getNamespace());
        }
        return code.intValue();
    }

    public int registerNamespacePrefix(QName name) {
        Integer code = this._namespacePrefixes.get(name.toString());
        if (code == null) {
            int i2 = this._nextGType;
            this._nextGType = i2 + 1;
            code = Integer.valueOf(i2);
            this._namespacePrefixes.put(name.toString(), code);
            String uri = name.getNamespace();
            if (uri != null && !uri.equals("")) {
                this._namesIndex.addElement("?");
            } else {
                this._namesIndex.addElement("?" + name.getLocalPart());
            }
        }
        return code.intValue();
    }

    public int registerNamespace(String namespaceURI) {
        Integer code = this._namespaces.get(namespaceURI);
        if (code == null) {
            int i2 = this._nextNSType;
            this._nextNSType = i2 + 1;
            code = Integer.valueOf(i2);
            this._namespaces.put(namespaceURI, code);
            this._namespaceIndex.addElement(namespaceURI);
        }
        return code.intValue();
    }

    public int nextModeSerial() {
        int i2 = this._modeSerial;
        this._modeSerial = i2 + 1;
        return i2;
    }

    public int nextStylesheetSerial() {
        int i2 = this._stylesheetSerial;
        this._stylesheetSerial = i2 + 1;
        return i2;
    }

    public int nextStepPatternSerial() {
        int i2 = this._stepPatternSerial;
        this._stepPatternSerial = i2 + 1;
        return i2;
    }

    public int[] getNumberFieldIndexes() {
        return this._numberFieldIndexes;
    }

    public int nextHelperClassSerial() {
        int i2 = this._helperClassSerial;
        this._helperClassSerial = i2 + 1;
        return i2;
    }

    public int nextAttributeSetSerial() {
        int i2 = this._attributeSetSerial;
        this._attributeSetSerial = i2 + 1;
        return i2;
    }

    public Vector getNamesIndex() {
        return this._namesIndex;
    }

    public Vector getNamespaceIndex() {
        return this._namespaceIndex;
    }

    public String getHelperClassName() {
        StringBuilder sbAppend = new StringBuilder().append(getClassName()).append('$');
        int i2 = this._helperClassSerial;
        this._helperClassSerial = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    public void dumpClass(JavaClass clazz) {
        if (this._outputType == 0 || this._outputType == 4) {
            File outFile = getOutputFile(clazz.getClassName());
            String parentDir = outFile.getParent();
            if (parentDir != null) {
                File parentFile = new File(parentDir);
                if (!SecuritySupport.getFileExists(parentFile)) {
                    parentFile.mkdirs();
                }
            }
        }
        try {
            switch (this._outputType) {
                case 0:
                    clazz.dump(new BufferedOutputStream(new FileOutputStream(getOutputFile(clazz.getClassName()))));
                    break;
                case 1:
                    this._bcelClasses.addElement(clazz);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
                    clazz.dump(out);
                    this._classes.addElement(out.toByteArray());
                    if (this._outputType == 4) {
                        clazz.dump(new BufferedOutputStream(new FileOutputStream(getOutputFile(clazz.getClassName()))));
                        break;
                    } else if (this._outputType == 5) {
                        this._bcelClasses.addElement(clazz);
                        break;
                    }
                    break;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private String entryName(File f2) throws IOException {
        return f2.getName().replace(File.separatorChar, '/');
    }

    public void outputToJar() throws IOException {
        Manifest manifest = new Manifest();
        Attributes atrs = manifest.getMainAttributes();
        atrs.put(Attributes.Name.MANIFEST_VERSION, "1.2");
        Map map = manifest.getEntries();
        Enumeration classes = this._bcelClasses.elements();
        String now = new Date().toString();
        Attributes.Name dateAttr = new Attributes.Name("Date");
        while (classes.hasMoreElements()) {
            String className = ((JavaClass) classes.nextElement()).getClassName().replace('.', '/');
            Attributes attr = new Attributes();
            attr.put(dateAttr, now);
            map.put(className + ".class", attr);
        }
        File jarFile = new File(this._destDir, this._jarFileName);
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);
        Enumeration classes2 = this._bcelClasses.elements();
        while (classes2.hasMoreElements()) {
            JavaClass clazz = (JavaClass) classes2.nextElement();
            String className2 = clazz.getClassName().replace('.', '/');
            jos.putNextEntry(new JarEntry(className2 + ".class"));
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            clazz.dump(out);
            out.writeTo(jos);
        }
        jos.close();
    }

    public void setDebug(boolean debug) {
        this._debug = debug;
    }

    public boolean debug() {
        return this._debug;
    }

    public String getCharacterData(int index) {
        return ((StringBuffer) this.m_characterData.elementAt(index)).toString();
    }

    public int getCharacterDataCount() {
        if (this.m_characterData != null) {
            return this.m_characterData.size();
        }
        return 0;
    }

    public int addCharacterData(String newData) {
        StringBuffer currData;
        if (this.m_characterData == null) {
            this.m_characterData = new Vector();
            currData = new StringBuffer();
            this.m_characterData.addElement(currData);
        } else {
            currData = (StringBuffer) this.m_characterData.elementAt(this.m_characterData.size() - 1);
        }
        if (newData.length() + currData.length() > 21845) {
            currData = new StringBuffer();
            this.m_characterData.addElement(currData);
        }
        int newDataOffset = currData.length();
        currData.append(newData);
        return newDataOffset;
    }
}
