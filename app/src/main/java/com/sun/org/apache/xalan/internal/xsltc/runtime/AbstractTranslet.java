package com.sun.org.apache.xalan.internal.xsltc.runtime;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMAdapter;
import com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex;
import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet.class */
public abstract class AbstractTranslet implements Translet {
    public static final int FIRST_TRANSLET_VERSION = 100;
    public static final int VER_SPLIT_NAMES_ARRAY = 101;
    public static final int CURRENT_TRANSLET_VERSION = 101;
    protected String[] namesArray;
    protected String[] urisArray;
    protected int[] typesArray;
    protected String[] namespaceArray;
    private static final String EMPTYSTRING = "";
    private static final String ID_INDEX_NAME = "##id";
    private boolean _overrideDefaultParser;
    public String _version = "1.0";
    public String _method = null;
    public String _encoding = "UTF-8";
    public boolean _omitHeader = false;
    public String _standalone = null;
    public boolean _isStandalone = false;
    public String _doctypePublic = null;
    public String _doctypeSystem = null;
    public boolean _indent = false;
    public String _mediaType = null;
    public ArrayList<String> _cdata = null;
    public int _indentamount = -1;
    protected int transletVersion = 100;
    protected Templates _templates = null;
    protected boolean _hasIdCall = false;
    protected StringValueHandler stringValueHandler = new StringValueHandler();
    private String _accessExternalStylesheet = "all";
    private String _accessErr = null;
    protected int pbase = 0;
    protected int pframe = 0;
    protected ArrayList paramsStack = new ArrayList();
    private MessageHandler _msgHandler = null;
    public Map<String, DecimalFormat> _formatSymbols = null;
    private Map<String, KeyIndex> _keyIndexes = null;
    private KeyIndex _emptyKeyIndex = null;
    private int _indexSize = 0;
    private int _currentRootForKeys = 0;
    private DOMCache _domCache = null;
    private Map<String, Class<?>> _auxClasses = null;
    protected DOMImplementation _domImplementation = null;

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public abstract void transform(DOM dom, DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException;

    public void printInternalState() {
        System.out.println("-------------------------------------");
        System.out.println("AbstractTranslet this = " + ((Object) this));
        System.out.println("pbase = " + this.pbase);
        System.out.println("vframe = " + this.pframe);
        System.out.println("paramsStack.size() = " + this.paramsStack.size());
        System.out.println("namesArray.size = " + this.namesArray.length);
        System.out.println("namespaceArray.size = " + this.namespaceArray.length);
        System.out.println("");
        System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
    }

    public final DOMAdapter makeDOMAdapter(DOM dom) throws TransletException {
        setRootForKeys(dom.getDocument());
        return new DOMAdapter(dom, this.namesArray, this.urisArray, this.typesArray, this.namespaceArray);
    }

    public final void pushParamFrame() {
        this.paramsStack.add(this.pframe, new Integer(this.pbase));
        int i2 = this.pframe + 1;
        this.pframe = i2;
        this.pbase = i2;
    }

    public final void popParamFrame() {
        if (this.pbase > 0) {
            ArrayList arrayList = this.paramsStack;
            int i2 = this.pbase - 1;
            this.pbase = i2;
            int oldpbase = ((Integer) arrayList.get(i2)).intValue();
            for (int i3 = this.pframe - 1; i3 >= this.pbase; i3--) {
                this.paramsStack.remove(i3);
            }
            this.pframe = this.pbase;
            this.pbase = oldpbase;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public final Object addParameter(String name, Object value) {
        return addParameter(BasisLibrary.mapQNameToJavaName(name), value, false);
    }

    public final Object addParameter(String name, Object value, boolean isDefault) {
        for (int i2 = this.pframe - 1; i2 >= this.pbase; i2--) {
            Parameter param = (Parameter) this.paramsStack.get(i2);
            if (param._name.equals(name)) {
                if (param._isDefault || !isDefault) {
                    param._value = value;
                    param._isDefault = isDefault;
                    return value;
                }
                return param._value;
            }
        }
        ArrayList arrayList = this.paramsStack;
        int i3 = this.pframe;
        this.pframe = i3 + 1;
        arrayList.add(i3, new Parameter(name, value, isDefault));
        return value;
    }

    public void clearParameters() {
        this.pframe = 0;
        this.pbase = 0;
        this.paramsStack.clear();
    }

    public final Object getParameter(String name) {
        String name2 = BasisLibrary.mapQNameToJavaName(name);
        for (int i2 = this.pframe - 1; i2 >= this.pbase; i2--) {
            Parameter param = (Parameter) this.paramsStack.get(i2);
            if (param._name.equals(name2)) {
                return param._value;
            }
        }
        return null;
    }

    public final void setMessageHandler(MessageHandler handler) {
        this._msgHandler = handler;
    }

    public final void displayMessage(String msg) {
        if (this._msgHandler == null) {
            System.err.println(msg);
        } else {
            this._msgHandler.displayMessage(msg);
        }
    }

    public void addDecimalFormat(String name, DecimalFormatSymbols symbols) {
        if (this._formatSymbols == null) {
            this._formatSymbols = new HashMap();
        }
        if (name == null) {
            name = "";
        }
        DecimalFormat df = new DecimalFormat();
        if (symbols != null) {
            df.setDecimalFormatSymbols(symbols);
        }
        this._formatSymbols.put(name, df);
    }

    public final DecimalFormat getDecimalFormat(String name) {
        if (this._formatSymbols != null) {
            if (name == null) {
                name = "";
            }
            DecimalFormat df = this._formatSymbols.get(name);
            if (df == null) {
                df = this._formatSymbols.get("");
            }
            return df;
        }
        return null;
    }

    public final void prepassDocument(DOM document) {
        setIndexSize(document.getSize());
        buildIDIndex(document);
    }

    private final void buildIDIndex(DOM document) {
        setRootForKeys(document.getDocument());
        if (document instanceof DOMEnhancedForDTM) {
            DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM) document;
            if (enhancedDOM.hasDOMSource()) {
                buildKeyIndex(ID_INDEX_NAME, document);
                return;
            }
            Map<String, Integer> elementsByID = enhancedDOM.getElementsWithIDs();
            if (elementsByID == null) {
                return;
            }
            boolean hasIDValues = false;
            for (Map.Entry<String, Integer> entry : elementsByID.entrySet()) {
                int element = document.getNodeHandle(entry.getValue().intValue());
                buildKeyIndex(ID_INDEX_NAME, element, entry.getKey());
                hasIDValues = true;
            }
            if (hasIDValues) {
                setKeyIndexDom(ID_INDEX_NAME, document);
            }
        }
    }

    public final void postInitialization() {
        if (this.transletVersion < 101) {
            int arraySize = this.namesArray.length;
            String[] newURIsArray = new String[arraySize];
            String[] newNamesArray = new String[arraySize];
            int[] newTypesArray = new int[arraySize];
            for (int i2 = 0; i2 < arraySize; i2++) {
                String name = this.namesArray[i2];
                int colonIndex = name.lastIndexOf(58);
                int lNameStartIdx = colonIndex + 1;
                if (colonIndex > -1) {
                    newURIsArray[i2] = name.substring(0, colonIndex);
                }
                if (name.charAt(lNameStartIdx) == '@') {
                    lNameStartIdx++;
                    newTypesArray[i2] = 2;
                } else if (name.charAt(lNameStartIdx) == '?') {
                    lNameStartIdx++;
                    newTypesArray[i2] = 13;
                } else {
                    newTypesArray[i2] = 1;
                }
                newNamesArray[i2] = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
            }
            this.namesArray = newNamesArray;
            this.urisArray = newURIsArray;
            this.typesArray = newTypesArray;
        }
        if (this.transletVersion > 101) {
            BasisLibrary.runTimeError(BasisLibrary.UNKNOWN_TRANSLET_VERSION_ERR, getClass().getName());
        }
    }

    public void setIndexSize(int size) {
        if (size > this._indexSize) {
            this._indexSize = size;
        }
    }

    public KeyIndex createKeyIndex() {
        return new KeyIndex(this._indexSize);
    }

    public void buildKeyIndex(String name, int node, String value) {
        KeyIndex index = buildKeyIndexHelper(name);
        index.add(value, node, this._currentRootForKeys);
    }

    public void buildKeyIndex(String name, DOM dom) {
        KeyIndex index = buildKeyIndexHelper(name);
        index.setDom(dom, dom.getDocument());
    }

    private KeyIndex buildKeyIndexHelper(String name) {
        if (this._keyIndexes == null) {
            this._keyIndexes = new HashMap();
        }
        KeyIndex index = this._keyIndexes.get(name);
        if (index == null) {
            Map<String, KeyIndex> map = this._keyIndexes;
            KeyIndex keyIndex = new KeyIndex(this._indexSize);
            index = keyIndex;
            map.put(name, keyIndex);
        }
        return index;
    }

    public KeyIndex getKeyIndex(String name) {
        if (this._keyIndexes == null) {
            if (this._emptyKeyIndex != null) {
                return this._emptyKeyIndex;
            }
            KeyIndex keyIndex = new KeyIndex(1);
            this._emptyKeyIndex = keyIndex;
            return keyIndex;
        }
        KeyIndex index = this._keyIndexes.get(name);
        if (index == null) {
            if (this._emptyKeyIndex != null) {
                return this._emptyKeyIndex;
            }
            KeyIndex keyIndex2 = new KeyIndex(1);
            this._emptyKeyIndex = keyIndex2;
            return keyIndex2;
        }
        return index;
    }

    private void setRootForKeys(int root) {
        this._currentRootForKeys = root;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public void buildKeys(DOM document, DTMAxisIterator iterator, SerializationHandler handler, int root) throws TransletException {
    }

    public void setKeyIndexDom(String name, DOM document) {
        getKeyIndex(name).setDom(document, document.getDocument());
    }

    public void setDOMCache(DOMCache cache) {
        this._domCache = cache;
    }

    public DOMCache getDOMCache() {
        return this._domCache;
    }

    public SerializationHandler openOutputHandler(String filename, boolean append) throws TransletException {
        try {
            TransletOutputHandlerFactory factory = TransletOutputHandlerFactory.newInstance(this._overrideDefaultParser);
            String dirStr = new File(filename).getParent();
            if (null != dirStr && dirStr.length() > 0) {
                File dir = new File(dirStr);
                dir.mkdirs();
            }
            factory.setEncoding(this._encoding);
            factory.setOutputMethod(this._method);
            factory.setOutputStream(new BufferedOutputStream(new FileOutputStream(filename, append)));
            factory.setOutputType(0);
            SerializationHandler handler = factory.getSerializationHandler();
            transferOutputSettings(handler);
            handler.startDocument();
            return handler;
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    public SerializationHandler openOutputHandler(String filename) throws TransletException {
        return openOutputHandler(filename, false);
    }

    public void closeOutputHandler(SerializationHandler handler) {
        try {
            handler.endDocument();
            handler.close();
        } catch (Exception e2) {
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public final void transform(DOM document, SerializationHandler handler) throws TransletException {
        try {
            transform(document, document.getIterator(), handler);
        } finally {
            this._keyIndexes = null;
        }
    }

    public final void characters(String string, SerializationHandler handler) throws TransletException {
        if (string != null) {
            try {
                handler.characters(string);
            } catch (Exception e2) {
                throw new TransletException(e2);
            }
        }
    }

    public void addCdataElement(String name) {
        if (this._cdata == null) {
            this._cdata = new ArrayList<>();
        }
        int lastColon = name.lastIndexOf(58);
        if (lastColon > 0) {
            String uri = name.substring(0, lastColon);
            String localName = name.substring(lastColon + 1);
            this._cdata.add(uri);
            this._cdata.add(localName);
            return;
        }
        this._cdata.add(null);
        this._cdata.add(name);
    }

    protected void transferOutputSettings(SerializationHandler handler) {
        if (this._method != null) {
            if (this._method.equals("xml")) {
                if (this._standalone != null) {
                    handler.setStandalone(this._standalone);
                }
                if (this._omitHeader) {
                    handler.setOmitXMLDeclaration(true);
                }
                handler.setCdataSectionElements(this._cdata);
                if (this._version != null) {
                    handler.setVersion(this._version);
                }
                handler.setIndent(this._indent);
                handler.setIndentAmount(this._indentamount);
                if (this._doctypeSystem != null) {
                    handler.setDoctype(this._doctypeSystem, this._doctypePublic);
                }
                handler.setIsStandalone(this._isStandalone);
                return;
            }
            if (this._method.equals("html")) {
                handler.setIndent(this._indent);
                handler.setDoctype(this._doctypeSystem, this._doctypePublic);
                if (this._mediaType != null) {
                    handler.setMediaType(this._mediaType);
                    return;
                }
                return;
            }
            return;
        }
        handler.setCdataSectionElements(this._cdata);
        if (this._version != null) {
            handler.setVersion(this._version);
        }
        if (this._standalone != null) {
            handler.setStandalone(this._standalone);
        }
        if (this._omitHeader) {
            handler.setOmitXMLDeclaration(true);
        }
        handler.setIndent(this._indent);
        handler.setDoctype(this._doctypeSystem, this._doctypePublic);
        handler.setIsStandalone(this._isStandalone);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public void addAuxiliaryClass(Class auxClass) {
        if (this._auxClasses == null) {
            this._auxClasses = new HashMap();
        }
        this._auxClasses.put(auxClass.getName(), auxClass);
    }

    public void setAuxiliaryClasses(Map<String, Class<?>> auxClasses) {
        this._auxClasses = auxClasses;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public Class getAuxiliaryClass(String className) {
        if (this._auxClasses == null) {
            return null;
        }
        return this._auxClasses.get(className);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public String[] getNamesArray() {
        return this.namesArray;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public String[] getUrisArray() {
        return this.urisArray;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public int[] getTypesArray() {
        return this.typesArray;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public String[] getNamespaceArray() {
        return this.namespaceArray;
    }

    public boolean hasIdCall() {
        return this._hasIdCall;
    }

    public Templates getTemplates() {
        return this._templates;
    }

    public void setTemplates(Templates templates) {
        this._templates = templates;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public boolean overrideDefaultParser() {
        return this._overrideDefaultParser;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.Translet
    public void setOverrideDefaultParser(boolean flag) {
        this._overrideDefaultParser = flag;
    }

    public String getAllowedProtocols() {
        return this._accessExternalStylesheet;
    }

    public void setAllowedProtocols(String protocols) {
        this._accessExternalStylesheet = protocols;
    }

    public String getAccessError() {
        return this._accessErr;
    }

    public void setAccessError(String accessErr) {
        this._accessErr = accessErr;
    }

    public Document newDocument(String uri, String qname) throws ParserConfigurationException {
        if (this._domImplementation == null) {
            DocumentBuilderFactory dbf = JdkXmlUtils.getDOMFactory(this._overrideDefaultParser);
            this._domImplementation = dbf.newDocumentBuilder().getDOMImplementation();
        }
        return this._domImplementation.createDocument(uri, qname, null);
    }
}
