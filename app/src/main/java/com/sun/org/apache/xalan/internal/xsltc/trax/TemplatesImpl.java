package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TemplatesImpl.class */
public final class TemplatesImpl implements Templates, Serializable {
    static final long serialVersionUID = 673094361519270707L;
    public static final String DESERIALIZE_TRANSLET = "jdk.xml.enableTemplatesImplDeserialization";
    private String _name;
    private byte[][] _bytecodes;
    private Class[] _class;
    private int _transletIndex;
    private transient Map<String, Class<?>> _auxClasses;
    private Properties _outputProperties;
    private int _indentNumber;
    private transient URIResolver _uriResolver;
    private transient ThreadLocal _sdom;
    private transient TransformerFactoryImpl _tfactory;
    private transient boolean _overrideDefaultParser;
    private transient String _accessExternalStylesheet;
    private static String ABSTRACT_TRANSLET = Constants.TRANSLET_CLASS;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("_name", String.class), new ObjectStreamField("_bytecodes", byte[][].class), new ObjectStreamField("_class", Class[].class), new ObjectStreamField("_transletIndex", Integer.TYPE), new ObjectStreamField("_outputProperties", Properties.class), new ObjectStreamField("_indentNumber", Integer.TYPE)};

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TemplatesImpl$TransletClassLoader.class */
    static final class TransletClassLoader extends ClassLoader {
        private final Map<String, Class> _loadedExternalExtensionFunctions;

        TransletClassLoader(ClassLoader parent) {
            super(parent);
            this._loadedExternalExtensionFunctions = null;
        }

        TransletClassLoader(ClassLoader parent, Map<String, Class> mapEF) {
            super(parent);
            this._loadedExternalExtensionFunctions = mapEF;
        }

        @Override // java.lang.ClassLoader
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            Class<?> ret = null;
            if (this._loadedExternalExtensionFunctions != null) {
                ret = this._loadedExternalExtensionFunctions.get(name);
            }
            if (ret == null) {
                ret = super.loadClass(name);
            }
            return ret;
        }

        Class defineClass(byte[] b2) {
            return defineClass(null, b2, 0, b2.length);
        }
    }

    protected TemplatesImpl(byte[][] bytecodes, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
        this._name = null;
        this._bytecodes = (byte[][]) null;
        this._class = null;
        this._transletIndex = -1;
        this._auxClasses = null;
        this._uriResolver = null;
        this._sdom = new ThreadLocal();
        this._tfactory = null;
        this._accessExternalStylesheet = "all";
        this._bytecodes = bytecodes;
        init(transletName, outputProperties, indentNumber, tfactory);
    }

    protected TemplatesImpl(Class[] transletClasses, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
        this._name = null;
        this._bytecodes = (byte[][]) null;
        this._class = null;
        this._transletIndex = -1;
        this._auxClasses = null;
        this._uriResolver = null;
        this._sdom = new ThreadLocal();
        this._tfactory = null;
        this._accessExternalStylesheet = "all";
        this._class = transletClasses;
        this._transletIndex = 0;
        init(transletName, outputProperties, indentNumber, tfactory);
    }

    private void init(String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
        this._name = transletName;
        this._outputProperties = outputProperties;
        this._indentNumber = indentNumber;
        this._tfactory = tfactory;
        this._overrideDefaultParser = tfactory.overrideDefaultParser();
        this._accessExternalStylesheet = (String) tfactory.getAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET);
    }

    public TemplatesImpl() {
        this._name = null;
        this._bytecodes = (byte[][]) null;
        this._class = null;
        this._transletIndex = -1;
        this._auxClasses = null;
        this._uriResolver = null;
        this._sdom = new ThreadLocal();
        this._tfactory = null;
        this._accessExternalStylesheet = "all";
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        String temp;
        SecurityManager security = System.getSecurityManager();
        if (security != null && ((temp = SecuritySupport.getSystemProperty(DESERIALIZE_TRANSLET)) == null || (temp.length() != 0 && !temp.equalsIgnoreCase("true")))) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DESERIALIZE_TRANSLET_ERR);
            throw new UnsupportedOperationException(err.toString());
        }
        ObjectInputStream.GetField gf = is.readFields();
        this._name = (String) gf.get("_name", (Object) null);
        this._bytecodes = (byte[][]) gf.get("_bytecodes", (Object) null);
        this._class = (Class[]) gf.get("_class", (Object) null);
        this._transletIndex = gf.get("_transletIndex", -1);
        this._outputProperties = (Properties) gf.get("_outputProperties", (Object) null);
        this._indentNumber = gf.get("_indentNumber", 0);
        if (is.readBoolean()) {
            this._uriResolver = (URIResolver) is.readObject();
        }
        this._tfactory = new TransformerFactoryImpl();
    }

    private void writeObject(ObjectOutputStream os) throws IOException, ClassNotFoundException {
        if (this._auxClasses != null) {
            throw new NotSerializableException("com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable");
        }
        ObjectOutputStream.PutField pf = os.putFields();
        pf.put("_name", this._name);
        pf.put("_bytecodes", this._bytecodes);
        pf.put("_class", this._class);
        pf.put("_transletIndex", this._transletIndex);
        pf.put("_outputProperties", this._outputProperties);
        pf.put("_indentNumber", this._indentNumber);
        os.writeFields();
        if (this._uriResolver instanceof Serializable) {
            os.writeBoolean(true);
            os.writeObject((Serializable) this._uriResolver);
        } else {
            os.writeBoolean(false);
        }
    }

    public boolean overrideDefaultParser() {
        return this._overrideDefaultParser;
    }

    public synchronized void setURIResolver(URIResolver resolver) {
        this._uriResolver = resolver;
    }

    private synchronized void setTransletBytecodes(byte[][] bytecodes) {
        this._bytecodes = bytecodes;
    }

    private synchronized byte[][] getTransletBytecodes() {
        return this._bytecodes;
    }

    private synchronized Class[] getTransletClasses() {
        try {
            if (this._class == null) {
                defineTransletClasses();
            }
        } catch (TransformerConfigurationException e2) {
        }
        return this._class;
    }

    public synchronized int getTransletIndex() {
        try {
            if (this._class == null) {
                defineTransletClasses();
            }
        } catch (TransformerConfigurationException e2) {
        }
        return this._transletIndex;
    }

    protected synchronized void setTransletName(String name) {
        this._name = name;
    }

    protected synchronized String getTransletName() {
        return this._name;
    }

    private void defineTransletClasses() throws TransformerConfigurationException {
        if (this._bytecodes == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.NO_TRANSLET_CLASS_ERR);
            throw new TransformerConfigurationException(err.toString());
        }
        TransletClassLoader loader = (TransletClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new TransletClassLoader(ObjectFactory.findClassLoader(), TemplatesImpl.this._tfactory.getExternalExtensionsMap());
            }
        });
        try {
            int classCount = this._bytecodes.length;
            this._class = new Class[classCount];
            if (classCount > 1) {
                this._auxClasses = new HashMap();
            }
            for (int i2 = 0; i2 < classCount; i2++) {
                this._class[i2] = loader.defineClass(this._bytecodes[i2]);
                Class superClass = this._class[i2].getSuperclass();
                if (superClass.getName().equals(ABSTRACT_TRANSLET)) {
                    this._transletIndex = i2;
                } else {
                    this._auxClasses.put(this._class[i2].getName(), this._class[i2]);
                }
            }
            if (this._transletIndex < 0) {
                ErrorMsg err2 = new ErrorMsg(ErrorMsg.NO_MAIN_TRANSLET_ERR, this._name);
                throw new TransformerConfigurationException(err2.toString());
            }
        } catch (ClassFormatError e2) {
            ErrorMsg err3 = new ErrorMsg(ErrorMsg.TRANSLET_CLASS_ERR, this._name);
            throw new TransformerConfigurationException(err3.toString());
        } catch (LinkageError e3) {
            ErrorMsg err4 = new ErrorMsg(ErrorMsg.TRANSLET_OBJECT_ERR, this._name);
            throw new TransformerConfigurationException(err4.toString());
        }
    }

    private Translet getTransletInstance() throws TransformerConfigurationException {
        try {
            if (this._name == null) {
                return null;
            }
            if (this._class == null) {
                defineTransletClasses();
            }
            AbstractTranslet translet = (AbstractTranslet) this._class[this._transletIndex].newInstance();
            translet.postInitialization();
            translet.setTemplates(this);
            translet.setOverrideDefaultParser(this._overrideDefaultParser);
            translet.setAllowedProtocols(this._accessExternalStylesheet);
            if (this._auxClasses != null) {
                translet.setAuxiliaryClasses(this._auxClasses);
            }
            return translet;
        } catch (IllegalAccessException e2) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.TRANSLET_OBJECT_ERR, this._name);
            throw new TransformerConfigurationException(err.toString());
        } catch (InstantiationException e3) {
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.TRANSLET_OBJECT_ERR, this._name);
            throw new TransformerConfigurationException(err2.toString());
        }
    }

    @Override // javax.xml.transform.Templates
    public synchronized Transformer newTransformer() throws TransformerConfigurationException {
        TransformerImpl transformer = new TransformerImpl(getTransletInstance(), this._outputProperties, this._indentNumber, this._tfactory);
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        if (this._tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
            transformer.setSecureProcessing(true);
        }
        return transformer;
    }

    @Override // javax.xml.transform.Templates
    public synchronized Properties getOutputProperties() {
        try {
            return newTransformer().getOutputProperties();
        } catch (TransformerConfigurationException e2) {
            return null;
        }
    }

    public DOM getStylesheetDOM() {
        return (DOM) this._sdom.get();
    }

    public void setStylesheetDOM(DOM sdom) {
        this._sdom.set(sdom);
    }
}
