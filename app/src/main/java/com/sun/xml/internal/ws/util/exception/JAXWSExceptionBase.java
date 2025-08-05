package com.sun.xml.internal.ws.util.exception;

import com.sun.istack.internal.localization.Localizable;
import com.sun.istack.internal.localization.LocalizableMessage;
import com.sun.istack.internal.localization.LocalizableMessageFactory;
import com.sun.istack.internal.localization.Localizer;
import com.sun.istack.internal.localization.NullLocalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/exception/JAXWSExceptionBase.class */
public abstract class JAXWSExceptionBase extends WebServiceException implements Localizable {
    private static final long serialVersionUID = 1;
    private transient Localizable msg;

    protected abstract String getDefaultResourceBundleName();

    protected JAXWSExceptionBase(String key, Object... args) {
        super(findNestedException(args));
        this.msg = new LocalizableMessage(getDefaultResourceBundleName(), key, args);
    }

    protected JAXWSExceptionBase(String message) {
        this(new NullLocalizable(message));
    }

    protected JAXWSExceptionBase(Throwable throwable) {
        this(new NullLocalizable(throwable.toString()), throwable);
    }

    protected JAXWSExceptionBase(Localizable msg) {
        this.msg = msg;
    }

    protected JAXWSExceptionBase(Localizable msg, Throwable cause) {
        super(cause);
        this.msg = msg;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.msg.getResourceBundleName());
        out.writeObject(this.msg.getKey());
        Object[] args = this.msg.getArguments();
        if (args == null) {
            out.writeInt(-1);
            return;
        }
        out.writeInt(args.length);
        for (int i2 = 0; i2 < args.length; i2++) {
            if (args[i2] == null || (args[i2] instanceof Serializable)) {
                out.writeObject(args[i2]);
            } else {
                out.writeObject(args[i2].toString());
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object[] args;
        in.defaultReadObject();
        String resourceBundleName = (String) in.readObject();
        String key = (String) in.readObject();
        int len = in.readInt();
        if (len < -1) {
            throw new NegativeArraySizeException();
        }
        if (len == -1) {
            args = null;
        } else if (len < 255) {
            args = new Object[len];
            for (int i2 = 0; i2 < args.length; i2++) {
                args[i2] = in.readObject();
            }
        } else {
            List<Object> argList = new ArrayList<>(Math.min(len, 1024));
            for (int i3 = 0; i3 < len; i3++) {
                argList.add(in.readObject());
            }
            args = argList.toArray(new Object[argList.size()]);
        }
        this.msg = new LocalizableMessageFactory(resourceBundleName).getMessage(key, args);
    }

    private static Throwable findNestedException(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object o2 : args) {
            if (o2 instanceof Throwable) {
                return (Throwable) o2;
            }
        }
        return null;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        Localizer localizer = new Localizer();
        return localizer.localize(this);
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public final String getKey() {
        return this.msg.getKey();
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public final Object[] getArguments() {
        return this.msg.getArguments();
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public final String getResourceBundleName() {
        return this.msg.getResourceBundleName();
    }
}
