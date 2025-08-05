package org.apache.commons.math3.exception.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/util/ExceptionContext.class */
public class ExceptionContext implements Serializable {
    private static final long serialVersionUID = -6024911025449780478L;
    private Throwable throwable;
    private List<Localizable> msgPatterns = new ArrayList();
    private List<Object[]> msgArguments = new ArrayList();
    private Map<String, Object> context = new HashMap();

    public ExceptionContext(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void addMessage(Localizable pattern, Object... arguments) {
        this.msgPatterns.add(pattern);
        this.msgArguments.add(ArgUtils.flatten(arguments));
    }

    public void setValue(String key, Object value) {
        this.context.put(key, value);
    }

    public Object getValue(String key) {
        return this.context.get(key);
    }

    public Set<String> getKeys() {
        return this.context.keySet();
    }

    public String getMessage() {
        return getMessage(Locale.US);
    }

    public String getLocalizedMessage() {
        return getMessage(Locale.getDefault());
    }

    public String getMessage(Locale locale) {
        return buildMessage(locale, ": ");
    }

    public String getMessage(Locale locale, String separator) {
        return buildMessage(locale, separator);
    }

    private String buildMessage(Locale locale, String separator) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int len = this.msgPatterns.size();
        for (int i2 = 0; i2 < len; i2++) {
            Localizable pat = this.msgPatterns.get(i2);
            Object[] args = this.msgArguments.get(i2);
            MessageFormat fmt = new MessageFormat(pat.getLocalizedString(locale), locale);
            sb.append(fmt.format(args));
            count++;
            if (count < len) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.throwable);
        serializeMessages(out);
        serializeContext(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.throwable = (Throwable) in.readObject();
        deSerializeMessages(in);
        deSerializeContext(in);
    }

    private void serializeMessages(ObjectOutputStream out) throws IOException {
        int len = this.msgPatterns.size();
        out.writeInt(len);
        for (int i2 = 0; i2 < len; i2++) {
            Localizable pat = this.msgPatterns.get(i2);
            out.writeObject(pat);
            Object[] args = this.msgArguments.get(i2);
            int aLen = args.length;
            out.writeInt(aLen);
            for (int j2 = 0; j2 < aLen; j2++) {
                if (args[j2] instanceof Serializable) {
                    out.writeObject(args[j2]);
                } else {
                    out.writeObject(nonSerializableReplacement(args[j2]));
                }
            }
        }
    }

    private void deSerializeMessages(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int len = in.readInt();
        this.msgPatterns = new ArrayList(len);
        this.msgArguments = new ArrayList(len);
        for (int i2 = 0; i2 < len; i2++) {
            Localizable pat = (Localizable) in.readObject();
            this.msgPatterns.add(pat);
            int aLen = in.readInt();
            Object[] args = new Object[aLen];
            for (int j2 = 0; j2 < aLen; j2++) {
                args[j2] = in.readObject();
            }
            this.msgArguments.add(args);
        }
    }

    private void serializeContext(ObjectOutputStream out) throws IOException {
        int len = this.context.size();
        out.writeInt(len);
        for (Map.Entry<String, Object> entry : this.context.entrySet()) {
            out.writeObject(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Serializable) {
                out.writeObject(value);
            } else {
                out.writeObject(nonSerializableReplacement(value));
            }
        }
    }

    private void deSerializeContext(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int len = in.readInt();
        this.context = new HashMap();
        for (int i2 = 0; i2 < len; i2++) {
            String key = (String) in.readObject();
            Object value = in.readObject();
            this.context.put(key, value);
        }
    }

    private String nonSerializableReplacement(Object obj) {
        return "[Object could not be serialized: " + obj.getClass().getName() + "]";
    }
}
