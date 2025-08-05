package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.EncodingMap;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLEntityDescriptionImpl;
import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.stream.StaxEntityResolverWrapper;
import com.sun.xml.internal.stream.StaxXMLInputSource;
import com.sun.xml.internal.stream.XMLEntityStorage;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.xml.stream.XMLInputFactory;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityManager.class */
public class XMLEntityManager implements XMLComponent, XMLEntityResolver {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 64;
    public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 1024;
    protected boolean fStrictURI;
    protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String STAX_ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/stax-entity-resolver";
    protected static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
    protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    static final String EXTERNAL_ACCESS_DEFAULT = "all";
    private static final boolean DEBUG_BUFFER = false;
    protected boolean fWarnDuplicateEntityDef;
    private static final boolean DEBUG_ENTITIES = false;
    private static final boolean DEBUG_ENCODINGS = false;
    private static final boolean DEBUG_RESOLVER = false;
    protected boolean fValidation;
    protected boolean fExternalGeneralEntities;
    protected boolean fExternalParameterEntities;
    protected boolean fAllowJavaEncodings;
    protected boolean fLoadExternalDTD;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected StaxEntityResolverWrapper fStaxEntityResolver;
    protected PropertyManager fPropertyManager;
    boolean fSupportDTD;
    boolean fReplaceEntityReferences;
    boolean fSupportExternalEntities;
    protected String fAccessExternalDTD;
    protected ValidationManager fValidationManager;
    protected int fBufferSize;
    protected XMLSecurityManager fSecurityManager;
    protected XMLLimitAnalyzer fLimitAnalyzer;
    protected int entityExpansionIndex;
    protected boolean fStandalone;
    protected boolean fInExternalSubset;
    protected XMLEntityHandler fEntityHandler;
    protected XMLEntityScanner fEntityScanner;
    protected XMLEntityScanner fXML10EntityScanner;
    protected XMLEntityScanner fXML11EntityScanner;
    protected int fEntityExpansionCount;
    protected Map<String, Entity> fEntities;
    protected Stack<Entity> fEntityStack;
    protected Entity.ScannedEntity fCurrentEntity;
    boolean fISCreatedByResolver;
    protected XMLEntityStorage fEntityStorage;
    protected final Object[] defaultEncoding;
    private final XMLResourceIdentifierImpl fResourceIdentifier;
    private final Augmentations fEntityAugs;
    private CharacterBufferPool fBufferPool;
    protected Stack<Reader> fReaderStack;
    private static String gUserDir;
    private static URI gUserDirURI;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
    protected static final String STANDARD_URI_CONFORMANT = "http://apache.org/xml/features/standard-uri-conformant";
    private static final String[] RECOGNIZED_FEATURES = {VALIDATION, "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", ALLOW_JAVA_ENCODINGS, WARN_ON_DUPLICATE_ENTITYDEF, STANDARD_URI_CONFORMANT};
    private static final Boolean[] FEATURE_DEFAULTS = {null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", VALIDATION_MANAGER, "http://apache.org/xml/properties/input-buffer-size", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null, new Integer(8192), null, null};
    private static final String XMLEntity = "[xml]".intern();
    private static final String DTDEntity = "[dtd]".intern();
    private static boolean[] gNeedEscaping = new boolean[128];
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    static {
        for (int i2 = 0; i2 <= 31; i2++) {
            gNeedEscaping[i2] = true;
            gAfterEscaping1[i2] = gHexChs[i2 >> 4];
            gAfterEscaping2[i2] = gHexChs[i2 & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        char[] escChs = {' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
        for (char ch : escChs) {
            gNeedEscaping[ch] = true;
            gAfterEscaping1[ch] = gHexChs[ch >> 4];
            gAfterEscaping2[ch] = gHexChs[ch & 15];
        }
    }

    public XMLEntityManager() {
        this.fAllowJavaEncodings = true;
        this.fLoadExternalDTD = true;
        this.fSupportDTD = true;
        this.fReplaceEntityReferences = true;
        this.fSupportExternalEntities = true;
        this.fAccessExternalDTD = "all";
        this.fBufferSize = 8192;
        this.fSecurityManager = null;
        this.fLimitAnalyzer = null;
        this.fInExternalSubset = false;
        this.fEntityExpansionCount = 0;
        this.fEntities = new HashMap();
        this.fEntityStack = new Stack<>();
        this.fCurrentEntity = null;
        this.fISCreatedByResolver = false;
        this.defaultEncoding = new Object[]{"UTF-8", null};
        this.fResourceIdentifier = new XMLResourceIdentifierImpl();
        this.fEntityAugs = new AugmentationsImpl();
        this.fBufferPool = new CharacterBufferPool(this.fBufferSize, 1024);
        this.fReaderStack = new Stack<>();
        this.fSecurityManager = new XMLSecurityManager(true);
        this.fEntityStorage = new XMLEntityStorage(this);
        setScannerVersion((short) 1);
    }

    public XMLEntityManager(PropertyManager propertyManager) {
        this.fAllowJavaEncodings = true;
        this.fLoadExternalDTD = true;
        this.fSupportDTD = true;
        this.fReplaceEntityReferences = true;
        this.fSupportExternalEntities = true;
        this.fAccessExternalDTD = "all";
        this.fBufferSize = 8192;
        this.fSecurityManager = null;
        this.fLimitAnalyzer = null;
        this.fInExternalSubset = false;
        this.fEntityExpansionCount = 0;
        this.fEntities = new HashMap();
        this.fEntityStack = new Stack<>();
        this.fCurrentEntity = null;
        this.fISCreatedByResolver = false;
        this.defaultEncoding = new Object[]{"UTF-8", null};
        this.fResourceIdentifier = new XMLResourceIdentifierImpl();
        this.fEntityAugs = new AugmentationsImpl();
        this.fBufferPool = new CharacterBufferPool(this.fBufferSize, 1024);
        this.fReaderStack = new Stack<>();
        this.fPropertyManager = propertyManager;
        this.fEntityStorage = new XMLEntityStorage(this);
        this.fEntityScanner = new XMLEntityScanner(propertyManager, this);
        reset(propertyManager);
    }

    public void addInternalEntity(String name, String text) throws XNIException {
        if (!this.fEntities.containsKey(name)) {
            Entity entity = new Entity.InternalEntity(name, text, this.fInExternalSubset);
            this.fEntities.put(name, entity);
        } else if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public void addExternalEntity(String name, String publicId, String literalSystemId, String baseSystemId) throws IOException, XNIException {
        if (!this.fEntities.containsKey(name)) {
            if (baseSystemId == null) {
                int size = this.fEntityStack.size();
                if (size == 0 && this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null) {
                    baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
                }
                int i2 = size - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    }
                    Entity.ScannedEntity externalEntity = (Entity.ScannedEntity) this.fEntityStack.elementAt(i2);
                    if (externalEntity.entityLocation == null || externalEntity.entityLocation.getExpandedSystemId() == null) {
                        i2--;
                    } else {
                        baseSystemId = externalEntity.entityLocation.getExpandedSystemId();
                        break;
                    }
                }
            }
            Entity entity = new Entity.ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, literalSystemId, baseSystemId, expandSystemId(literalSystemId, baseSystemId, false)), null, this.fInExternalSubset);
            this.fEntities.put(name, entity);
            return;
        }
        if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public void addUnparsedEntity(String name, String publicId, String systemId, String baseSystemId, String notation) throws XNIException {
        if (!this.fEntities.containsKey(name)) {
            Entity.ExternalEntity entity = new Entity.ExternalEntity(name, new XMLEntityDescriptionImpl(name, publicId, systemId, baseSystemId, null), notation, this.fInExternalSubset);
            this.fEntities.put(name, entity);
        } else if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public XMLEntityStorage getEntityStore() {
        return this.fEntityStorage;
    }

    public XMLEntityScanner getEntityScanner() {
        if (this.fEntityScanner == null) {
            if (this.fXML10EntityScanner == null) {
                this.fXML10EntityScanner = new XMLEntityScanner();
            }
            this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
            this.fEntityScanner = this.fXML10EntityScanner;
        }
        return this.fEntityScanner;
    }

    public void setScannerVersion(short version) {
        if (version == 1) {
            if (this.fXML10EntityScanner == null) {
                this.fXML10EntityScanner = new XMLEntityScanner();
            }
            this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
            this.fEntityScanner = this.fXML10EntityScanner;
            this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
            return;
        }
        if (this.fXML11EntityScanner == null) {
            this.fXML11EntityScanner = new XML11EntityScanner();
        }
        this.fXML11EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
        this.fEntityScanner = this.fXML11EntityScanner;
        this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
    }

    public String setupCurrentEntity(boolean reference, String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal) throws IOException, XNIException {
        String publicId = xmlInputSource.getPublicId();
        String literalSystemId = xmlInputSource.getSystemId();
        String baseSystemId = xmlInputSource.getBaseSystemId();
        String encoding = xmlInputSource.getEncoding();
        boolean encodingExternallySpecified = encoding != null;
        Boolean isBigEndian = null;
        InputStream stream = null;
        Reader reader = xmlInputSource.getCharacterStream();
        String expandedSystemId = expandSystemId(literalSystemId, baseSystemId, this.fStrictURI);
        if (baseSystemId == null) {
            baseSystemId = expandedSystemId;
        }
        if (reader == null) {
            InputStream stream2 = xmlInputSource.getByteStream();
            if (stream2 == null) {
                URL location = new URL(expandedSystemId);
                URLConnection connect = location.openConnection();
                if (!(connect instanceof HttpURLConnection)) {
                    stream2 = connect.getInputStream();
                } else {
                    boolean followRedirects = true;
                    if (xmlInputSource instanceof HTTPInputSource) {
                        HttpURLConnection urlConnection = (HttpURLConnection) connect;
                        HTTPInputSource httpInputSource = (HTTPInputSource) xmlInputSource;
                        Iterator<Map.Entry<String, String>> propIter = httpInputSource.getHTTPRequestProperties();
                        while (propIter.hasNext()) {
                            Map.Entry<String, String> entry = propIter.next();
                            urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                        }
                        followRedirects = httpInputSource.getFollowHTTPRedirects();
                        if (!followRedirects) {
                            setInstanceFollowRedirects(urlConnection, followRedirects);
                        }
                    }
                    stream2 = connect.getInputStream();
                    if (followRedirects) {
                        String redirect = connect.getURL().toString();
                        if (!redirect.equals(expandedSystemId)) {
                            literalSystemId = redirect;
                            expandedSystemId = redirect;
                        }
                    }
                }
            }
            stream = new RewindableInputStream(stream2);
            if (encoding == null) {
                byte[] b4 = new byte[4];
                int count = 0;
                while (count < 4) {
                    b4[count] = (byte) stream.read();
                    count++;
                }
                if (count == 4) {
                    Object[] encodingDesc = getEncodingName(b4, count);
                    encoding = (String) encodingDesc[0];
                    Boolean isBigEndian2 = (Boolean) encodingDesc[1];
                    stream.reset();
                    if (count > 2 && encoding.equals("UTF-8")) {
                        int b0 = b4[0] & 255;
                        int b1 = b4[1] & 255;
                        int b2 = b4[2] & 255;
                        if (b0 == 239 && b1 == 187 && b2 == 191) {
                            stream.skip(3L);
                        }
                    }
                    reader = createReader(stream, encoding, isBigEndian2);
                } else {
                    reader = createReader(stream, encoding, null);
                }
            } else {
                encoding = encoding.toUpperCase(Locale.ENGLISH);
                if (encoding.equals("UTF-8")) {
                    int[] b3 = new int[3];
                    int count2 = 0;
                    while (count2 < 3) {
                        b3[count2] = stream.read();
                        if (b3[count2] == -1) {
                            break;
                        }
                        count2++;
                    }
                    if (count2 != 3 || b3[0] != 239 || b3[1] != 187 || b3[2] != 191) {
                        stream.reset();
                    }
                } else if (encoding.equals("UTF-16")) {
                    int[] b42 = new int[4];
                    int count3 = 0;
                    while (count3 < 4) {
                        b42[count3] = stream.read();
                        if (b42[count3] == -1) {
                            break;
                        }
                        count3++;
                    }
                    stream.reset();
                    String utf16Encoding = "UTF-16";
                    if (count3 >= 2) {
                        int b02 = b42[0];
                        int b12 = b42[1];
                        if (b02 == 254 && b12 == 255) {
                            utf16Encoding = FastInfosetSerializer.UTF_16BE;
                            isBigEndian = Boolean.TRUE;
                        } else if (b02 == 255 && b12 == 254) {
                            utf16Encoding = "UTF-16LE";
                            isBigEndian = Boolean.FALSE;
                        } else if (count3 == 4) {
                            int b22 = b42[2];
                            int b32 = b42[3];
                            if (b02 == 0 && b12 == 60 && b22 == 0 && b32 == 63) {
                                utf16Encoding = FastInfosetSerializer.UTF_16BE;
                                isBigEndian = Boolean.TRUE;
                            }
                            if (b02 == 60 && b12 == 0 && b22 == 63 && b32 == 0) {
                                utf16Encoding = "UTF-16LE";
                                isBigEndian = Boolean.FALSE;
                            }
                        }
                    }
                    createReader(stream, utf16Encoding, isBigEndian);
                } else if (encoding.equals("ISO-10646-UCS-4")) {
                    int[] b43 = new int[4];
                    int count4 = 0;
                    while (count4 < 4) {
                        b43[count4] = stream.read();
                        if (b43[count4] == -1) {
                            break;
                        }
                        count4++;
                    }
                    stream.reset();
                    if (count4 == 4) {
                        if (b43[0] == 0 && b43[1] == 0 && b43[2] == 0 && b43[3] == 60) {
                            isBigEndian = Boolean.TRUE;
                        } else if (b43[0] == 60 && b43[1] == 0 && b43[2] == 0 && b43[3] == 0) {
                            isBigEndian = Boolean.FALSE;
                        }
                    }
                } else if (encoding.equals("ISO-10646-UCS-2")) {
                    int[] b44 = new int[4];
                    int count5 = 0;
                    while (count5 < 4) {
                        b44[count5] = stream.read();
                        if (b44[count5] == -1) {
                            break;
                        }
                        count5++;
                    }
                    stream.reset();
                    if (count5 == 4) {
                        if (b44[0] == 0 && b44[1] == 60 && b44[2] == 0 && b44[3] == 63) {
                            isBigEndian = Boolean.TRUE;
                        } else if (b44[0] == 60 && b44[1] == 0 && b44[2] == 63 && b44[3] == 0) {
                            isBigEndian = Boolean.FALSE;
                        }
                    }
                }
                reader = createReader(stream, encoding, isBigEndian);
            }
        }
        this.fReaderStack.push(reader);
        if (this.fCurrentEntity != null) {
            this.fEntityStack.push(this.fCurrentEntity);
        }
        this.fCurrentEntity = new Entity.ScannedEntity(reference, name, new XMLResourceIdentifierImpl(publicId, literalSystemId, baseSystemId, expandedSystemId), stream, reader, encoding, literal, encodingExternallySpecified, isExternal);
        this.fCurrentEntity.setEncodingExternallySpecified(encodingExternallySpecified);
        this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
        this.fResourceIdentifier.setValues(publicId, literalSystemId, baseSystemId, expandedSystemId);
        if (this.fLimitAnalyzer != null) {
            this.fLimitAnalyzer.startEntity(name);
        }
        return encoding;
    }

    public boolean isExternalEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isExternal();
    }

    public boolean isEntityDeclInExternalSubset(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isEntityDeclInExternalSubset();
    }

    public void setStandalone(boolean standalone) {
        this.fStandalone = standalone;
    }

    public boolean isStandalone() {
        return this.fStandalone;
    }

    public boolean isDeclaredEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        return entity != null;
    }

    public boolean isUnparsedEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isUnparsed();
    }

    public XMLResourceIdentifier getCurrentResourceIdentifier() {
        return this.fResourceIdentifier;
    }

    public void setEntityHandler(XMLEntityHandler entityHandler) {
        this.fEntityHandler = entityHandler;
    }

    public StaxXMLInputSource resolveEntityAsPerStax(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        XMLResourceIdentifierImpl ri;
        if (resourceIdentifier == null) {
            return null;
        }
        String publicId = resourceIdentifier.getPublicId();
        String literalSystemId = resourceIdentifier.getLiteralSystemId();
        String baseSystemId = resourceIdentifier.getBaseSystemId();
        String expandedSystemId = resourceIdentifier.getExpandedSystemId();
        boolean needExpand = expandedSystemId == null;
        if (baseSystemId == null && this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null) {
            baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
            if (baseSystemId != null) {
                needExpand = true;
            }
        }
        if (needExpand) {
            expandedSystemId = expandSystemId(literalSystemId, baseSystemId, false);
        }
        StaxXMLInputSource staxInputSource = null;
        XMLInputSource xmlInputSource = null;
        if (resourceIdentifier instanceof XMLResourceIdentifierImpl) {
            ri = (XMLResourceIdentifierImpl) resourceIdentifier;
        } else {
            this.fResourceIdentifier.clear();
            ri = this.fResourceIdentifier;
        }
        ri.setValues(publicId, literalSystemId, baseSystemId, expandedSystemId);
        this.fISCreatedByResolver = false;
        if (this.fStaxEntityResolver != null) {
            staxInputSource = this.fStaxEntityResolver.resolveEntity(ri);
            if (staxInputSource != null) {
                this.fISCreatedByResolver = true;
            }
        }
        if (this.fEntityResolver != null) {
            xmlInputSource = this.fEntityResolver.resolveEntity(ri);
            if (xmlInputSource != null) {
                this.fISCreatedByResolver = true;
            }
        }
        if (xmlInputSource != null) {
            staxInputSource = new StaxXMLInputSource(xmlInputSource, this.fISCreatedByResolver);
        }
        if (staxInputSource == null) {
            staxInputSource = new StaxXMLInputSource(new XMLInputSource(publicId, literalSystemId, baseSystemId), false);
        } else if (staxInputSource.hasXMLStreamOrXMLEventReader()) {
        }
        return staxInputSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        if (resourceIdentifier == null) {
            return null;
        }
        String publicId = resourceIdentifier.getPublicId();
        String literalSystemId = resourceIdentifier.getLiteralSystemId();
        String baseSystemId = resourceIdentifier.getBaseSystemId();
        String expandedSystemId = resourceIdentifier.getExpandedSystemId();
        boolean needExpand = expandedSystemId == null;
        if (baseSystemId == null && this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null) {
            baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
            if (baseSystemId != null) {
                needExpand = true;
            }
        }
        if (needExpand) {
            expandedSystemId = expandSystemId(literalSystemId, baseSystemId, false);
        }
        XMLInputSource xmlInputSource = null;
        if (this.fEntityResolver != null) {
            resourceIdentifier.setBaseSystemId(baseSystemId);
            resourceIdentifier.setExpandedSystemId(expandedSystemId);
            xmlInputSource = this.fEntityResolver.resolveEntity(resourceIdentifier);
        }
        if (xmlInputSource == null) {
            xmlInputSource = new XMLInputSource(publicId, literalSystemId, baseSystemId);
        }
        return xmlInputSource;
    }

    public void startEntity(boolean isGE, String entityName, boolean literal) throws IOException, XNIException {
        XMLInputSource xmlInputSource;
        String accessError;
        Entity entity = this.fEntityStorage.getEntity(entityName);
        if (entity == null) {
            if (this.fEntityHandler != null) {
                this.fResourceIdentifier.clear();
                this.fEntityAugs.removeAllItems();
                this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, null, this.fEntityAugs);
                this.fEntityAugs.removeAllItems();
                this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
                return;
            }
            return;
        }
        boolean external = entity.isExternal();
        Entity.ExternalEntity externalEntity = null;
        String extLitSysId = null;
        String extBaseSysId = null;
        String expandedSystemId = null;
        if (external) {
            externalEntity = (Entity.ExternalEntity) entity;
            extLitSysId = externalEntity.entityLocation != null ? externalEntity.entityLocation.getLiteralSystemId() : null;
            extBaseSysId = externalEntity.entityLocation != null ? externalEntity.entityLocation.getBaseSystemId() : null;
            expandedSystemId = expandSystemId(extLitSysId, extBaseSysId, this.fStrictURI);
            boolean unparsed = entity.isUnparsed();
            boolean parameter = entityName.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX);
            boolean general = !parameter;
            if (unparsed || ((general && !this.fExternalGeneralEntities) || ((parameter && !this.fExternalParameterEntities) || !this.fSupportDTD || !this.fSupportExternalEntities))) {
                if (this.fEntityHandler != null) {
                    this.fResourceIdentifier.clear();
                    this.fResourceIdentifier.setValues(externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null, extLitSysId, extBaseSysId, expandedSystemId);
                    this.fEntityAugs.removeAllItems();
                    this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                    this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, null, this.fEntityAugs);
                    this.fEntityAugs.removeAllItems();
                    this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                    this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
                    return;
                }
                return;
            }
        }
        int size = this.fEntityStack.size();
        int i2 = size;
        while (i2 >= 0) {
            Entity activeEntity = i2 == size ? this.fCurrentEntity : this.fEntityStack.elementAt(i2);
            if (activeEntity.name == entityName) {
                String path = entityName;
                for (int j2 = i2 + 1; j2 < size; j2++) {
                    Entity activeEntity2 = this.fEntityStack.elementAt(j2);
                    path = path + " -> " + activeEntity2.name;
                }
                this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "RecursiveReference", new Object[]{entityName, (path + " -> " + this.fCurrentEntity.name) + " -> " + entityName}, (short) 2);
                if (this.fEntityHandler != null) {
                    this.fResourceIdentifier.clear();
                    if (external) {
                        this.fResourceIdentifier.setValues(externalEntity.entityLocation != null ? externalEntity.entityLocation.getPublicId() : null, extLitSysId, extBaseSysId, expandedSystemId);
                    }
                    this.fEntityAugs.removeAllItems();
                    this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                    this.fEntityHandler.startEntity(entityName, this.fResourceIdentifier, null, this.fEntityAugs);
                    this.fEntityAugs.removeAllItems();
                    this.fEntityAugs.putItem(Constants.ENTITY_SKIPPED, Boolean.TRUE);
                    this.fEntityHandler.endEntity(entityName, this.fEntityAugs);
                    return;
                }
                return;
            }
            i2--;
        }
        if (external) {
            StaxXMLInputSource staxInputSource = resolveEntityAsPerStax(externalEntity.entityLocation);
            xmlInputSource = staxInputSource.getXMLInputSource();
            if (!this.fISCreatedByResolver && (accessError = SecuritySupport.checkAccess(expandedSystemId, this.fAccessExternalDTD, "all")) != null) {
                this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "AccessExternalEntity", new Object[]{SecuritySupport.sanitizePath(expandedSystemId), accessError}, (short) 2);
            }
        } else {
            Entity.InternalEntity internalEntity = (Entity.InternalEntity) entity;
            Reader reader = new StringReader(internalEntity.text);
            xmlInputSource = new XMLInputSource((String) null, (String) null, (String) null, reader, (String) null);
        }
        startEntity(isGE, entityName, xmlInputSource, literal, external);
    }

    public void startDocumentEntity(XMLInputSource xmlInputSource) throws IOException, XNIException {
        startEntity(false, XMLEntity, xmlInputSource, false, true);
    }

    public void startDTDEntity(XMLInputSource xmlInputSource) throws IOException, XNIException {
        startEntity(false, DTDEntity, xmlInputSource, false, true);
    }

    public void startExternalSubset() {
        this.fInExternalSubset = true;
    }

    public void endExternalSubset() {
        this.fInExternalSubset = false;
    }

    public void startEntity(boolean isGE, String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal) throws IOException, XNIException {
        String encoding = setupCurrentEntity(isGE, name, xmlInputSource, literal, isExternal);
        this.fEntityExpansionCount++;
        if (this.fLimitAnalyzer != null) {
            this.fLimitAnalyzer.addValue(this.entityExpansionIndex, name, 1);
        }
        if (this.fSecurityManager != null && this.fSecurityManager.isOverLimit(this.entityExpansionIndex, this.fLimitAnalyzer)) {
            this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityExpansionLimit", new Object[]{this.fSecurityManager.getLimitValueByIndex(this.entityExpansionIndex)}, (short) 2);
            this.fEntityExpansionCount = 0;
        }
        if (this.fEntityHandler != null) {
            this.fEntityHandler.startEntity(name, this.fResourceIdentifier, encoding, null);
        }
    }

    public Entity.ScannedEntity getCurrentEntity() {
        return this.fCurrentEntity;
    }

    public Entity.ScannedEntity getTopLevelEntity() {
        return (Entity.ScannedEntity) (this.fEntityStack.empty() ? null : this.fEntityStack.elementAt(0));
    }

    public void closeReaders() {
        while (!this.fReaderStack.isEmpty()) {
            try {
                this.fReaderStack.pop().close();
            } catch (IOException e2) {
            }
        }
    }

    public void endEntity() throws IOException, XNIException {
        Entity.ScannedEntity entity = this.fEntityStack.size() > 0 ? (Entity.ScannedEntity) this.fEntityStack.pop() : null;
        if (this.fCurrentEntity != null) {
            try {
                if (this.fLimitAnalyzer != null) {
                    this.fLimitAnalyzer.endEntity(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT, this.fCurrentEntity.name);
                    if (this.fCurrentEntity.name.equals("[xml]")) {
                        this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
                    }
                }
                this.fCurrentEntity.close();
            } catch (IOException ex) {
                throw new XNIException(ex);
            }
        }
        if (!this.fReaderStack.isEmpty()) {
            this.fReaderStack.pop();
        }
        if (this.fEntityHandler != null) {
            if (entity == null) {
                this.fEntityAugs.removeAllItems();
                this.fEntityAugs.putItem(Constants.LAST_ENTITY, Boolean.TRUE);
                this.fEntityHandler.endEntity(this.fCurrentEntity.name, this.fEntityAugs);
                this.fEntityAugs.removeAllItems();
            } else {
                this.fEntityHandler.endEntity(this.fCurrentEntity.name, null);
            }
        }
        boolean documentEntity = this.fCurrentEntity.name == XMLEntity;
        this.fCurrentEntity = entity;
        this.fEntityScanner.setCurrentEntity(this.fCurrentEntity);
        if ((this.fCurrentEntity == null) & (!documentEntity)) {
            throw new EOFException();
        }
    }

    public void reset(PropertyManager propertyManager) {
        this.fSymbolTable = (SymbolTable) propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            this.fStaxEntityResolver = (StaxEntityResolverWrapper) propertyManager.getProperty(STAX_ENTITY_RESOLVER);
        } catch (XMLConfigurationException e2) {
            this.fStaxEntityResolver = null;
        }
        this.fSupportDTD = ((Boolean) propertyManager.getProperty(XMLInputFactory.SUPPORT_DTD)).booleanValue();
        this.fReplaceEntityReferences = ((Boolean) propertyManager.getProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)).booleanValue();
        this.fSupportExternalEntities = ((Boolean) propertyManager.getProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)).booleanValue();
        this.fLoadExternalDTD = !((Boolean) propertyManager.getProperty("http://java.sun.com/xml/stream/properties/ignore-external-dtd")).booleanValue();
        XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) propertyManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        this.fSecurityManager = (XMLSecurityManager) propertyManager.getProperty("http://apache.org/xml/properties/security-manager");
        this.fLimitAnalyzer = new XMLLimitAnalyzer();
        this.fEntityStorage.reset(propertyManager);
        this.fEntityScanner.reset(propertyManager);
        this.fEntities.clear();
        this.fEntityStack.removeAllElements();
        this.fCurrentEntity = null;
        this.fValidation = false;
        this.fExternalGeneralEntities = true;
        this.fExternalParameterEntities = true;
        this.fAllowJavaEncodings = true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        boolean parser_settings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!parser_settings) {
            reset();
            if (this.fEntityScanner != null) {
                this.fEntityScanner.reset(componentManager);
            }
            if (this.fEntityStorage != null) {
                this.fEntityStorage.reset(componentManager);
                return;
            }
            return;
        }
        this.fValidation = componentManager.getFeature(VALIDATION, false);
        this.fExternalGeneralEntities = componentManager.getFeature("http://xml.org/sax/features/external-general-entities", true);
        this.fExternalParameterEntities = componentManager.getFeature("http://xml.org/sax/features/external-parameter-entities", true);
        this.fAllowJavaEncodings = componentManager.getFeature(ALLOW_JAVA_ENCODINGS, false);
        this.fWarnDuplicateEntityDef = componentManager.getFeature(WARN_ON_DUPLICATE_ENTITYDEF, false);
        this.fStrictURI = componentManager.getFeature(STANDARD_URI_CONFORMANT, false);
        this.fLoadExternalDTD = componentManager.getFeature(LOAD_EXTERNAL_DTD, true);
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntityResolver = (XMLEntityResolver) componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fStaxEntityResolver = (StaxEntityResolverWrapper) componentManager.getProperty(STAX_ENTITY_RESOLVER, null);
        this.fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER, null);
        this.fSecurityManager = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager", null);
        this.entityExpansionIndex = this.fSecurityManager.getIndex("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit");
        this.fSupportDTD = true;
        this.fReplaceEntityReferences = true;
        this.fSupportExternalEntities = true;
        XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", null);
        if (spm == null) {
            spm = new XMLSecurityPropertyManager();
        }
        this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        reset();
        this.fEntityScanner.reset(componentManager);
        this.fEntityStorage.reset(componentManager);
    }

    public void reset() {
        this.fLimitAnalyzer = new XMLLimitAnalyzer();
        this.fStandalone = false;
        this.fEntities.clear();
        this.fEntityStack.removeAllElements();
        this.fEntityExpansionCount = 0;
        this.fCurrentEntity = null;
        if (this.fXML10EntityScanner != null) {
            this.fXML10EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
        }
        if (this.fXML11EntityScanner != null) {
            this.fXML11EntityScanner.reset(this.fSymbolTable, this, this.fErrorReporter);
        }
        this.fEntityHandler = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.ALLOW_JAVA_ENCODINGS_FEATURE.length() && featureId.endsWith(Constants.ALLOW_JAVA_ENCODINGS_FEATURE)) {
                this.fAllowJavaEncodings = state;
            }
            if (suffixLength == Constants.LOAD_EXTERNAL_DTD_FEATURE.length() && featureId.endsWith(Constants.LOAD_EXTERNAL_DTD_FEATURE)) {
                this.fLoadExternalDTD = state;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) {
        Integer bufferSize;
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.SYMBOL_TABLE_PROPERTY.length() && propertyId.endsWith(Constants.SYMBOL_TABLE_PROPERTY)) {
                this.fSymbolTable = (SymbolTable) value;
                return;
            }
            if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
                this.fErrorReporter = (XMLErrorReporter) value;
                return;
            }
            if (suffixLength == Constants.ENTITY_RESOLVER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_RESOLVER_PROPERTY)) {
                this.fEntityResolver = (XMLEntityResolver) value;
                return;
            }
            if (suffixLength == Constants.BUFFER_SIZE_PROPERTY.length() && propertyId.endsWith(Constants.BUFFER_SIZE_PROPERTY) && (bufferSize = (Integer) value) != null && bufferSize.intValue() > 64) {
                this.fBufferSize = bufferSize.intValue();
                this.fEntityScanner.setBufferSize(this.fBufferSize);
                this.fBufferPool.setExternalBufferSize(this.fBufferSize);
            }
            if (suffixLength == Constants.SECURITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.SECURITY_MANAGER_PROPERTY)) {
                this.fSecurityManager = (XMLSecurityManager) value;
            }
        }
        if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) value;
            this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        }
    }

    public void setLimitAnalyzer(XMLLimitAnalyzer fLimitAnalyzer) {
        this.fLimitAnalyzer = fLimitAnalyzer;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return null;
    }

    public static String expandSystemId(String systemId) {
        return expandSystemId(systemId, null);
    }

    private static synchronized URI getUserDir() throws URI.MalformedURIException {
        int ch;
        int ch2;
        String userDir = "";
        try {
            userDir = SecuritySupport.getSystemProperty("user.dir");
        } catch (SecurityException e2) {
        }
        if (userDir.length() == 0) {
            return new URI(DeploymentDescriptorParser.ATTR_FILE, "", "", null, null);
        }
        if (gUserDirURI != null && userDir.equals(gUserDir)) {
            return gUserDirURI;
        }
        gUserDir = userDir;
        char separator = File.separatorChar;
        String userDir2 = userDir.replace(separator, '/');
        int len = userDir2.length();
        StringBuilder buffer = new StringBuilder(len * 3);
        if (len >= 2 && userDir2.charAt(1) == ':' && (ch2 = Character.toUpperCase(userDir2.charAt(0))) >= 65 && ch2 <= 90) {
            buffer.append('/');
        }
        int i2 = 0;
        while (i2 < len && (ch = userDir2.charAt(i2)) < 128) {
            if (gNeedEscaping[ch]) {
                buffer.append('%');
                buffer.append(gAfterEscaping1[ch]);
                buffer.append(gAfterEscaping2[ch]);
            } else {
                buffer.append((char) ch);
            }
            i2++;
        }
        if (i2 < len) {
            try {
                byte[] bytes = userDir2.substring(i2).getBytes("UTF-8");
                for (byte b2 : bytes) {
                    if (b2 < 0) {
                        int ch3 = b2 + 256;
                        buffer.append('%');
                        buffer.append(gHexChs[ch3 >> 4]);
                        buffer.append(gHexChs[ch3 & 15]);
                    } else if (gNeedEscaping[b2]) {
                        buffer.append('%');
                        buffer.append(gAfterEscaping1[b2]);
                        buffer.append(gAfterEscaping2[b2]);
                    } else {
                        buffer.append((char) b2);
                    }
                }
            } catch (UnsupportedEncodingException e3) {
                return new URI(DeploymentDescriptorParser.ATTR_FILE, "", userDir2, null, null);
            }
        }
        if (!userDir2.endsWith("/")) {
            buffer.append('/');
        }
        gUserDirURI = new URI(DeploymentDescriptorParser.ATTR_FILE, "", buffer.toString(), null, null);
        return gUserDirURI;
    }

    public static OutputStream createOutputStream(String uri) throws IOException {
        OutputStream out;
        File parent;
        String expanded = expandSystemId(uri, null, true);
        URL url = new URL(expanded != null ? expanded : uri);
        String protocol = url.getProtocol();
        String host = url.getHost();
        if (protocol.equals(DeploymentDescriptorParser.ATTR_FILE) && (host == null || host.length() == 0 || host.equals("localhost"))) {
            File file = new File(getPathWithoutEscapes(url.getPath()));
            if (!file.exists() && (parent = file.getParentFile()) != null && !parent.exists()) {
                parent.mkdirs();
            }
            out = new FileOutputStream(file);
        } else {
            URLConnection urlCon = url.openConnection();
            urlCon.setDoInput(false);
            urlCon.setDoOutput(true);
            urlCon.setUseCaches(false);
            if (urlCon instanceof HttpURLConnection) {
                HttpURLConnection httpCon = (HttpURLConnection) urlCon;
                httpCon.setRequestMethod("PUT");
            }
            out = urlCon.getOutputStream();
        }
        return out;
    }

    private static String getPathWithoutEscapes(String origPath) {
        if (origPath != null && origPath.length() != 0 && origPath.indexOf(37) != -1) {
            StringTokenizer tokenizer = new StringTokenizer(origPath, FXMLLoader.RESOURCE_KEY_PREFIX);
            StringBuilder result = new StringBuilder(origPath.length());
            int size = tokenizer.countTokens();
            result.append(tokenizer.nextToken());
            for (int i2 = 1; i2 < size; i2++) {
                String token = tokenizer.nextToken();
                result.append((char) Integer.valueOf(token.substring(0, 2), 16).intValue());
                result.append(token.substring(2));
            }
            return result.toString();
        }
        return origPath;
    }

    public static void absolutizeAgainstUserDir(URI uri) throws URI.MalformedURIException {
        uri.absolutize(getUserDir());
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x003d A[Catch: Exception -> 0x00cc, TryCatch #0 {Exception -> 0x00cc, blocks: (B:17:0x002e, B:19:0x0035, B:22:0x005a, B:28:0x00be, B:24:0x006b, B:26:0x0075, B:27:0x008c, B:21:0x003d), top: B:36:0x002e, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String expandSystemId(java.lang.String r8, java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 219
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLEntityManager.expandSystemId(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00c0 A[Catch: Exception -> 0x0124, TryCatch #4 {Exception -> 0x0124, blocks: (B:34:0x00b1, B:36:0x00b8, B:39:0x00c8, B:45:0x0112, B:41:0x00dd, B:43:0x00e7, B:44:0x0102, B:38:0x00c0), top: B:61:0x00b1, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String expandSystemId(java.lang.String r8, java.lang.String r9, boolean r10) throws com.sun.org.apache.xerces.internal.util.URI.MalformedURIException {
        /*
            Method dump skipped, instructions count: 307
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLEntityManager.expandSystemId(java.lang.String, java.lang.String, boolean):java.lang.String");
    }

    private static String expandSystemIdStrictOn(String systemId, String baseSystemId) throws URI.MalformedURIException {
        URI baseURI;
        URI systemURI = new URI(systemId, true);
        if (systemURI.isAbsoluteURI()) {
            return systemId;
        }
        if (baseSystemId == null || baseSystemId.length() == 0) {
            baseURI = getUserDir();
        } else {
            baseURI = new URI(baseSystemId, true);
            if (!baseURI.isAbsoluteURI()) {
                baseURI.absolutize(getUserDir());
            }
        }
        systemURI.absolutize(baseURI);
        return systemURI.toString();
    }

    public static void setInstanceFollowRedirects(HttpURLConnection urlCon, boolean followRedirects) {
        try {
            Method method = HttpURLConnection.class.getMethod("setInstanceFollowRedirects", Boolean.TYPE);
            Object[] objArr = new Object[1];
            objArr[0] = followRedirects ? Boolean.TRUE : Boolean.FALSE;
            method.invoke(urlCon, objArr);
        } catch (Exception e2) {
        }
    }

    private static String expandSystemIdStrictOff(String systemId, String baseSystemId) throws URI.MalformedURIException {
        URI baseURI;
        URI systemURI = new URI(systemId, true);
        if (systemURI.isAbsoluteURI()) {
            if (systemURI.getScheme().length() > 1) {
                return systemId;
            }
            throw new URI.MalformedURIException();
        }
        if (baseSystemId == null || baseSystemId.length() == 0) {
            baseURI = getUserDir();
        } else {
            baseURI = new URI(baseSystemId, true);
            if (!baseURI.isAbsoluteURI()) {
                baseURI.absolutize(getUserDir());
            }
        }
        systemURI.absolutize(baseURI);
        return systemURI.toString();
    }

    private static String expandSystemIdStrictOff1(String systemId, String baseSystemId) throws URI.MalformedURIException, URISyntaxException {
        URI baseURI;
        java.net.URI systemURI = new java.net.URI(systemId);
        if (systemURI.isAbsolute()) {
            if (systemURI.getScheme().length() > 1) {
                return systemId;
            }
            throw new URISyntaxException(systemId, "the scheme's length is only one character");
        }
        if (baseSystemId == null || baseSystemId.length() == 0) {
            baseURI = getUserDir();
        } else {
            baseURI = new URI(baseSystemId, true);
            if (!baseURI.isAbsoluteURI()) {
                baseURI.absolutize(getUserDir());
            }
        }
        return new java.net.URI(baseURI.toString()).resolve(systemURI).toString();
    }

    protected Object[] getEncodingName(byte[] b4, int count) {
        if (count < 2) {
            return this.defaultEncoding;
        }
        int b0 = b4[0] & 255;
        int b1 = b4[1] & 255;
        if (b0 == 254 && b1 == 255) {
            return new Object[]{FastInfosetSerializer.UTF_16BE, new Boolean(true)};
        }
        if (b0 == 255 && b1 == 254) {
            return new Object[]{"UTF-16LE", new Boolean(false)};
        }
        if (count < 3) {
            return this.defaultEncoding;
        }
        int b2 = b4[2] & 255;
        if (b0 == 239 && b1 == 187 && b2 == 191) {
            return this.defaultEncoding;
        }
        if (count < 4) {
            return this.defaultEncoding;
        }
        int b3 = b4[3] & 255;
        if (b0 == 0 && b1 == 0 && b2 == 0 && b3 == 60) {
            return new Object[]{"ISO-10646-UCS-4", new Boolean(true)};
        }
        if (b0 == 60 && b1 == 0 && b2 == 0 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", new Boolean(false)};
        }
        if (b0 == 0 && b1 == 0 && b2 == 60 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", null};
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", null};
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 63) {
            return new Object[]{FastInfosetSerializer.UTF_16BE, new Boolean(true)};
        }
        if (b0 == 60 && b1 == 0 && b2 == 63 && b3 == 0) {
            return new Object[]{"UTF-16LE", new Boolean(false)};
        }
        if (b0 == 76 && b1 == 111 && b2 == 167 && b3 == 148) {
            return new Object[]{"CP037", null};
        }
        return this.defaultEncoding;
    }

    protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian) throws IOException, XNIException {
        if (encoding == null) {
            encoding = "UTF-8";
        }
        String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
        if (ENCODING.equals("UTF-8")) {
            return new UTF8Reader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        if (ENCODING.equals("US-ASCII")) {
            return new ASCIIReader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        if (ENCODING.equals("ISO-10646-UCS-4")) {
            if (isBigEndian != null) {
                boolean isBE = isBigEndian.booleanValue();
                if (isBE) {
                    return new UCSReader(inputStream, (short) 8);
                }
                return new UCSReader(inputStream, (short) 4);
            }
            this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[]{encoding}, (short) 2);
        }
        if (ENCODING.equals("ISO-10646-UCS-2")) {
            if (isBigEndian != null) {
                boolean isBE2 = isBigEndian.booleanValue();
                if (isBE2) {
                    return new UCSReader(inputStream, (short) 2);
                }
                return new UCSReader(inputStream, (short) 1);
            }
            this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[]{encoding}, (short) 2);
        }
        boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
        boolean validJava = XMLChar.isValidJavaEncoding(encoding);
        if (!validIANA || (this.fAllowJavaEncodings && !validJava)) {
            this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[]{encoding}, (short) 2);
            encoding = FTP.DEFAULT_CONTROL_ENCODING;
        }
        String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
        if (javaEncoding == null) {
            if (this.fAllowJavaEncodings) {
                javaEncoding = encoding;
            } else {
                this.fErrorReporter.reportError((XMLLocator) getEntityScanner(), "http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[]{encoding}, (short) 2);
                javaEncoding = "ISO8859_1";
            }
        }
        return new BufferedReader(new InputStreamReader(inputStream, javaEncoding));
    }

    public String getPublicId() {
        if (this.fCurrentEntity == null || this.fCurrentEntity.entityLocation == null) {
            return null;
        }
        return this.fCurrentEntity.entityLocation.getPublicId();
    }

    public String getExpandedSystemId() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getExpandedSystemId() != null) {
                return this.fCurrentEntity.entityLocation.getExpandedSystemId();
            }
            int size = this.fEntityStack.size();
            for (int i2 = size - 1; i2 >= 0; i2--) {
                Entity.ScannedEntity externalEntity = (Entity.ScannedEntity) this.fEntityStack.elementAt(i2);
                if (externalEntity.entityLocation != null && externalEntity.entityLocation.getExpandedSystemId() != null) {
                    return externalEntity.entityLocation.getExpandedSystemId();
                }
            }
            return null;
        }
        return null;
    }

    public String getLiteralSystemId() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getLiteralSystemId() != null) {
                return this.fCurrentEntity.entityLocation.getLiteralSystemId();
            }
            int size = this.fEntityStack.size();
            for (int i2 = size - 1; i2 >= 0; i2--) {
                Entity.ScannedEntity externalEntity = (Entity.ScannedEntity) this.fEntityStack.elementAt(i2);
                if (externalEntity.entityLocation != null && externalEntity.entityLocation.getLiteralSystemId() != null) {
                    return externalEntity.entityLocation.getLiteralSystemId();
                }
            }
            return null;
        }
        return null;
    }

    public int getLineNumber() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.lineNumber;
            }
            int size = this.fEntityStack.size();
            for (int i2 = size - 1; i2 > 0; i2--) {
                Entity.ScannedEntity firstExternalEntity = (Entity.ScannedEntity) this.fEntityStack.elementAt(i2);
                if (firstExternalEntity.isExternal()) {
                    return firstExternalEntity.lineNumber;
                }
            }
            return -1;
        }
        return -1;
    }

    public int getColumnNumber() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.columnNumber;
            }
            int size = this.fEntityStack.size();
            for (int i2 = size - 1; i2 > 0; i2--) {
                Entity.ScannedEntity firstExternalEntity = (Entity.ScannedEntity) this.fEntityStack.elementAt(i2);
                if (firstExternalEntity.isExternal()) {
                    return firstExternalEntity.columnNumber;
                }
            }
            return -1;
        }
        return -1;
    }

    protected static String fixURI(String str) {
        String str2 = str.replace(File.separatorChar, '/');
        if (str2.length() >= 2) {
            char ch1 = str2.charAt(1);
            if (ch1 == ':') {
                char ch0 = Character.toUpperCase(str2.charAt(0));
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    str2 = "/" + str2;
                }
            } else if (ch1 == '/' && str2.charAt(0) == '/') {
                str2 = "file:" + str2;
            }
        }
        int pos = str2.indexOf(32);
        if (pos >= 0) {
            StringBuilder sb = new StringBuilder(str2.length());
            for (int i2 = 0; i2 < pos; i2++) {
                sb.append(str2.charAt(i2));
            }
            sb.append("%20");
            for (int i3 = pos + 1; i3 < str2.length(); i3++) {
                if (str2.charAt(i3) == ' ') {
                    sb.append("%20");
                } else {
                    sb.append(str2.charAt(i3));
                }
            }
            str2 = sb.toString();
        }
        return str2;
    }

    final void print() {
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityManager$CharacterBuffer.class */
    private static class CharacterBuffer {
        private char[] ch;
        private boolean isExternal;

        public CharacterBuffer(boolean isExternal, int size) {
            this.isExternal = isExternal;
            this.ch = new char[size];
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityManager$CharacterBufferPool.class */
    private static class CharacterBufferPool {
        private static final int DEFAULT_POOL_SIZE = 3;
        private CharacterBuffer[] fInternalBufferPool;
        private CharacterBuffer[] fExternalBufferPool;
        private int fExternalBufferSize;
        private int fInternalBufferSize;
        private int poolSize;
        private int fInternalTop;
        private int fExternalTop;

        public CharacterBufferPool(int externalBufferSize, int internalBufferSize) {
            this(3, externalBufferSize, internalBufferSize);
        }

        public CharacterBufferPool(int poolSize, int externalBufferSize, int internalBufferSize) {
            this.fExternalBufferSize = externalBufferSize;
            this.fInternalBufferSize = internalBufferSize;
            this.poolSize = poolSize;
            init();
        }

        private void init() {
            this.fInternalBufferPool = new CharacterBuffer[this.poolSize];
            this.fExternalBufferPool = new CharacterBuffer[this.poolSize];
            this.fInternalTop = -1;
            this.fExternalTop = -1;
        }

        public CharacterBuffer getBuffer(boolean external) {
            if (external) {
                if (this.fExternalTop > -1) {
                    CharacterBuffer[] characterBufferArr = this.fExternalBufferPool;
                    int i2 = this.fExternalTop;
                    this.fExternalTop = i2 - 1;
                    return characterBufferArr[i2];
                }
                return new CharacterBuffer(true, this.fExternalBufferSize);
            }
            if (this.fInternalTop > -1) {
                CharacterBuffer[] characterBufferArr2 = this.fInternalBufferPool;
                int i3 = this.fInternalTop;
                this.fInternalTop = i3 - 1;
                return characterBufferArr2[i3];
            }
            return new CharacterBuffer(false, this.fInternalBufferSize);
        }

        public void returnToPool(CharacterBuffer buffer) {
            if (buffer.isExternal) {
                if (this.fExternalTop < this.fExternalBufferPool.length - 1) {
                    CharacterBuffer[] characterBufferArr = this.fExternalBufferPool;
                    int i2 = this.fExternalTop + 1;
                    this.fExternalTop = i2;
                    characterBufferArr[i2] = buffer;
                    return;
                }
                return;
            }
            if (this.fInternalTop < this.fInternalBufferPool.length - 1) {
                CharacterBuffer[] characterBufferArr2 = this.fInternalBufferPool;
                int i3 = this.fInternalTop + 1;
                this.fInternalTop = i3;
                characterBufferArr2[i3] = buffer;
            }
        }

        public void setExternalBufferSize(int bufferSize) {
            this.fExternalBufferSize = bufferSize;
            this.fExternalBufferPool = new CharacterBuffer[this.poolSize];
            this.fExternalTop = -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityManager$RewindableInputStream.class */
    protected final class RewindableInputStream extends InputStream {
        private InputStream fInputStream;
        private byte[] fData = new byte[64];
        private int fStartOffset = 0;
        private int fEndOffset = -1;
        private int fOffset = 0;
        private int fLength = 0;
        private int fMark = 0;

        public RewindableInputStream(InputStream is) {
            this.fInputStream = is;
        }

        public void setStartOffset(int offset) {
            this.fStartOffset = offset;
        }

        public void rewind() {
            this.fOffset = this.fStartOffset;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.fOffset < this.fLength) {
                byte[] bArr = this.fData;
                int i2 = this.fOffset;
                this.fOffset = i2 + 1;
                return bArr[i2] & 255;
            }
            if (this.fOffset == this.fEndOffset) {
                return -1;
            }
            if (this.fOffset == this.fData.length) {
                byte[] newData = new byte[this.fOffset << 1];
                System.arraycopy(this.fData, 0, newData, 0, this.fOffset);
                this.fData = newData;
            }
            int b2 = this.fInputStream.read();
            if (b2 == -1) {
                this.fEndOffset = this.fOffset;
                return -1;
            }
            byte[] bArr2 = this.fData;
            int i3 = this.fLength;
            this.fLength = i3 + 1;
            bArr2[i3] = (byte) b2;
            this.fOffset++;
            return b2 & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int len) throws IOException {
            int bytesLeft = this.fLength - this.fOffset;
            if (bytesLeft == 0) {
                if (this.fOffset == this.fEndOffset) {
                    return -1;
                }
                if (XMLEntityManager.this.fCurrentEntity.mayReadChunks || !XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead) {
                    if (!XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead) {
                        XMLEntityManager.this.fCurrentEntity.xmlDeclChunkRead = true;
                        len = 28;
                    }
                    return this.fInputStream.read(b2, off, len);
                }
                int returnedVal = read();
                if (returnedVal == -1) {
                    this.fEndOffset = this.fOffset;
                    return -1;
                }
                b2[off] = (byte) returnedVal;
                return 1;
            }
            if (len < bytesLeft) {
                if (len <= 0) {
                    return 0;
                }
            } else {
                len = bytesLeft;
            }
            if (b2 != null) {
                System.arraycopy(this.fData, this.fOffset, b2, off, len);
            }
            this.fOffset += len;
            return len;
        }

        @Override // java.io.InputStream
        public long skip(long n2) throws IOException {
            if (n2 <= 0) {
                return 0L;
            }
            int bytesLeft = this.fLength - this.fOffset;
            if (bytesLeft == 0) {
                if (this.fOffset == this.fEndOffset) {
                    return 0L;
                }
                return this.fInputStream.skip(n2);
            }
            if (n2 <= bytesLeft) {
                this.fOffset = (int) (this.fOffset + n2);
                return n2;
            }
            this.fOffset += bytesLeft;
            if (this.fOffset == this.fEndOffset) {
                return bytesLeft;
            }
            return this.fInputStream.skip(n2 - bytesLeft) + bytesLeft;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            int bytesLeft = this.fLength - this.fOffset;
            if (bytesLeft == 0) {
                if (this.fOffset == this.fEndOffset) {
                    return -1;
                }
                if (XMLEntityManager.this.fCurrentEntity.mayReadChunks) {
                    return this.fInputStream.available();
                }
                return 0;
            }
            return bytesLeft;
        }

        @Override // java.io.InputStream
        public void mark(int howMuch) {
            this.fMark = this.fOffset;
        }

        @Override // java.io.InputStream
        public void reset() {
            this.fOffset = this.fMark;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.fInputStream != null) {
                this.fInputStream.close();
                this.fInputStream = null;
            }
        }
    }

    public void test() {
        this.fEntityStorage.addExternalEntity("entityUsecase1", null, "/space/home/stax/sun/6thJan2004/zephyr/data/test.txt", "/space/home/stax/sun/6thJan2004/zephyr/data/entity.xml");
        this.fEntityStorage.addInternalEntity("entityUsecase2", "<Test>value</Test>");
        this.fEntityStorage.addInternalEntity("entityUsecase3", "value3");
        this.fEntityStorage.addInternalEntity("text", "Hello World.");
        this.fEntityStorage.addInternalEntity("empty-element", "<foo/>");
        this.fEntityStorage.addInternalEntity("balanced-element", "<foo></foo>");
        this.fEntityStorage.addInternalEntity("balanced-element-with-text", "<foo>Hello, World</foo>");
        this.fEntityStorage.addInternalEntity("balanced-element-with-entity", "<foo>&text;</foo>");
        this.fEntityStorage.addInternalEntity("unbalanced-entity", "<foo>");
        this.fEntityStorage.addInternalEntity("recursive-entity", "<foo>&recursive-entity2;</foo>");
        this.fEntityStorage.addInternalEntity("recursive-entity2", "<bar>&recursive-entity3;</bar>");
        this.fEntityStorage.addInternalEntity("recursive-entity3", "<baz>&recursive-entity;</baz>");
        this.fEntityStorage.addInternalEntity("ch", "&#x00A9;");
        this.fEntityStorage.addInternalEntity("ch1", "&#84;");
        this.fEntityStorage.addInternalEntity("% ch2", com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PARAMVARIABLE_STRING);
    }
}
