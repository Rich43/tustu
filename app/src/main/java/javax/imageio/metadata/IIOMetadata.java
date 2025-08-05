package javax.imageio.metadata;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/imageio/metadata/IIOMetadata.class */
public abstract class IIOMetadata {
    protected boolean standardFormatSupported;
    protected String nativeMetadataFormatName;
    protected String nativeMetadataFormatClassName;
    protected String[] extraMetadataFormatNames;
    protected String[] extraMetadataFormatClassNames;
    protected IIOMetadataController defaultController;
    protected IIOMetadataController controller;

    public abstract boolean isReadOnly();

    public abstract Node getAsTree(String str);

    public abstract void mergeTree(String str, Node node) throws IIOInvalidTreeException;

    public abstract void reset();

    protected IIOMetadata() {
        this.nativeMetadataFormatName = null;
        this.nativeMetadataFormatClassName = null;
        this.extraMetadataFormatNames = null;
        this.extraMetadataFormatClassNames = null;
        this.defaultController = null;
        this.controller = null;
    }

    protected IIOMetadata(boolean z2, String str, String str2, String[] strArr, String[] strArr2) {
        this.nativeMetadataFormatName = null;
        this.nativeMetadataFormatClassName = null;
        this.extraMetadataFormatNames = null;
        this.extraMetadataFormatClassNames = null;
        this.defaultController = null;
        this.controller = null;
        this.standardFormatSupported = z2;
        this.nativeMetadataFormatName = str;
        this.nativeMetadataFormatClassName = str2;
        if (strArr != null) {
            if (strArr.length == 0) {
                throw new IllegalArgumentException("extraMetadataFormatNames.length == 0!");
            }
            if (strArr2 == null) {
                throw new IllegalArgumentException("extraMetadataFormatNames != null && extraMetadataFormatClassNames == null!");
            }
            if (strArr2.length != strArr.length) {
                throw new IllegalArgumentException("extraMetadataFormatClassNames.length != extraMetadataFormatNames.length!");
            }
            this.extraMetadataFormatNames = (String[]) strArr.clone();
            this.extraMetadataFormatClassNames = (String[]) strArr2.clone();
            return;
        }
        if (strArr2 != null) {
            throw new IllegalArgumentException("extraMetadataFormatNames == null && extraMetadataFormatClassNames != null!");
        }
    }

    public boolean isStandardMetadataFormatSupported() {
        return this.standardFormatSupported;
    }

    public String getNativeMetadataFormatName() {
        return this.nativeMetadataFormatName;
    }

    public String[] getExtraMetadataFormatNames() {
        if (this.extraMetadataFormatNames == null) {
            return null;
        }
        return (String[]) this.extraMetadataFormatNames.clone();
    }

    public String[] getMetadataFormatNames() {
        String nativeMetadataFormatName = getNativeMetadataFormatName();
        String str = isStandardMetadataFormatSupported() ? IIOMetadataFormatImpl.standardMetadataFormatName : null;
        String[] extraMetadataFormatNames = getExtraMetadataFormatNames();
        int length = 0;
        if (nativeMetadataFormatName != null) {
            length = 0 + 1;
        }
        if (str != null) {
            length++;
        }
        if (extraMetadataFormatNames != null) {
            length += extraMetadataFormatNames.length;
        }
        if (length == 0) {
            return null;
        }
        String[] strArr = new String[length];
        int i2 = 0;
        if (nativeMetadataFormatName != null) {
            i2 = 0 + 1;
            strArr[0] = nativeMetadataFormatName;
        }
        if (str != null) {
            int i3 = i2;
            i2++;
            strArr[i3] = str;
        }
        if (extraMetadataFormatNames != null) {
            for (String str2 : extraMetadataFormatNames) {
                int i4 = i2;
                i2++;
                strArr[i4] = str2;
            }
        }
        return strArr;
    }

    public IIOMetadataFormat getMetadataFormat(String str) {
        Class<?> cls;
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        if (this.standardFormatSupported && str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            return IIOMetadataFormatImpl.getStandardFormatInstance();
        }
        String str2 = null;
        if (str.equals(this.nativeMetadataFormatName)) {
            str2 = this.nativeMetadataFormatClassName;
        } else if (this.extraMetadataFormatNames != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.extraMetadataFormatNames.length) {
                    break;
                }
                if (!str.equals(this.extraMetadataFormatNames[i2])) {
                    i2++;
                } else {
                    str2 = this.extraMetadataFormatClassNames[i2];
                    break;
                }
            }
        }
        if (str2 == null) {
            throw new IllegalArgumentException("Unsupported format name");
        }
        try {
            try {
                cls = Class.forName(str2, true, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.metadata.IIOMetadata.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        return this.getClass().getClassLoader();
                    }
                }));
            } catch (ClassNotFoundException e2) {
                try {
                    cls = Class.forName(str2, true, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.metadata.IIOMetadata.2
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            return Thread.currentThread().getContextClassLoader();
                        }
                    }));
                } catch (ClassNotFoundException e3) {
                    cls = Class.forName(str2, true, ClassLoader.getSystemClassLoader());
                }
            }
            return (IIOMetadataFormat) cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e4) {
            IllegalStateException illegalStateException = new IllegalStateException("Can't obtain format");
            illegalStateException.initCause(e4);
            throw illegalStateException;
        }
    }

    protected IIOMetadataNode getStandardChromaNode() {
        return null;
    }

    protected IIOMetadataNode getStandardCompressionNode() {
        return null;
    }

    protected IIOMetadataNode getStandardDataNode() {
        return null;
    }

    protected IIOMetadataNode getStandardDimensionNode() {
        return null;
    }

    protected IIOMetadataNode getStandardDocumentNode() {
        return null;
    }

    protected IIOMetadataNode getStandardTextNode() {
        return null;
    }

    protected IIOMetadataNode getStandardTileNode() {
        return null;
    }

    protected IIOMetadataNode getStandardTransparencyNode() {
        return null;
    }

    private void append(IIOMetadataNode iIOMetadataNode, IIOMetadataNode iIOMetadataNode2) throws DOMException {
        if (iIOMetadataNode2 != null) {
            iIOMetadataNode.appendChild(iIOMetadataNode2);
        }
    }

    protected final IIOMetadataNode getStandardTree() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
        append(iIOMetadataNode, getStandardChromaNode());
        append(iIOMetadataNode, getStandardCompressionNode());
        append(iIOMetadataNode, getStandardDataNode());
        append(iIOMetadataNode, getStandardDimensionNode());
        append(iIOMetadataNode, getStandardDocumentNode());
        append(iIOMetadataNode, getStandardTextNode());
        append(iIOMetadataNode, getStandardTileNode());
        append(iIOMetadataNode, getStandardTransparencyNode());
        return iIOMetadataNode;
    }

    public void setFromTree(String str, Node node) throws IIOInvalidTreeException {
        reset();
        mergeTree(str, node);
    }

    public void setController(IIOMetadataController iIOMetadataController) {
        this.controller = iIOMetadataController;
    }

    public IIOMetadataController getController() {
        return this.controller;
    }

    public IIOMetadataController getDefaultController() {
        return this.defaultController;
    }

    public boolean hasController() {
        return getController() != null;
    }

    public boolean activateController() {
        if (!hasController()) {
            throw new IllegalStateException("hasController() == false!");
        }
        return getController().activate(this);
    }
}
