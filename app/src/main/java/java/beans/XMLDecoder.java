package java.beans;

import com.sun.beans.decoder.DocumentHandler;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:java/beans/XMLDecoder.class */
public class XMLDecoder implements AutoCloseable {
    private final AccessControlContext acc;
    private final DocumentHandler handler;
    private final InputSource input;
    private Object owner;
    private Object[] array;
    private int index;

    public XMLDecoder(InputStream inputStream) {
        this(inputStream, null);
    }

    public XMLDecoder(InputStream inputStream, Object obj) {
        this(inputStream, obj, null);
    }

    public XMLDecoder(InputStream inputStream, Object obj, ExceptionListener exceptionListener) {
        this(inputStream, obj, exceptionListener, (ClassLoader) null);
    }

    public XMLDecoder(InputStream inputStream, Object obj, ExceptionListener exceptionListener, ClassLoader classLoader) {
        this(new InputSource(inputStream), obj, exceptionListener, classLoader);
    }

    public XMLDecoder(InputSource inputSource) {
        this(inputSource, (Object) null, (ExceptionListener) null, (ClassLoader) null);
    }

    private XMLDecoder(InputSource inputSource, Object obj, ExceptionListener exceptionListener, ClassLoader classLoader) {
        this.acc = AccessController.getContext();
        this.handler = new DocumentHandler();
        this.input = inputSource;
        this.owner = obj;
        setExceptionListener(exceptionListener);
        this.handler.setClassLoader(classLoader);
        this.handler.setOwner(this);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (parsingComplete()) {
            close(this.input.getCharacterStream());
            close(this.input.getByteStream());
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e2) {
                getExceptionListener().exceptionThrown(e2);
            }
        }
    }

    private boolean parsingComplete() {
        if (this.input == null) {
            return false;
        }
        if (this.array == null) {
            if (this.acc == null && null != System.getSecurityManager()) {
                throw new SecurityException("AccessControlContext is not set");
            }
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.beans.XMLDecoder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    XMLDecoder.this.handler.parse(XMLDecoder.this.input);
                    return null;
                }
            }, this.acc);
            this.array = this.handler.getObjects();
            return true;
        }
        return true;
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        if (exceptionListener == null) {
            exceptionListener = Statement.defaultExceptionListener;
        }
        this.handler.setExceptionListener(exceptionListener);
    }

    public ExceptionListener getExceptionListener() {
        return this.handler.getExceptionListener();
    }

    public Object readObject() {
        if (!parsingComplete()) {
            return null;
        }
        Object[] objArr = this.array;
        int i2 = this.index;
        this.index = i2 + 1;
        return objArr[i2];
    }

    public void setOwner(Object obj) {
        this.owner = obj;
    }

    public Object getOwner() {
        return this.owner;
    }

    public static DefaultHandler createHandler(Object obj, ExceptionListener exceptionListener, ClassLoader classLoader) {
        DocumentHandler documentHandler = new DocumentHandler();
        documentHandler.setOwner(obj);
        documentHandler.setExceptionListener(exceptionListener);
        documentHandler.setClassLoader(classLoader);
        return documentHandler;
    }
}
