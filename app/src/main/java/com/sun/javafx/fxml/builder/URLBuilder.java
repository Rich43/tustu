package com.sun.javafx.fxml.builder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import javafx.util.Builder;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/URLBuilder.class */
public class URLBuilder extends AbstractMap<String, Object> implements Builder<URL> {
    private ClassLoader classLoader;
    private Object value = null;
    public static final String VALUE_KEY = "value";

    public URLBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.equals("value")) {
            this.value = value;
            return null;
        }
        throw new IllegalArgumentException(key + " is not a valid property.");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public URL build2() {
        URL url;
        if (this.value == null) {
            throw new IllegalStateException();
        }
        if (this.value instanceof URL) {
            url = (URL) this.value;
        } else {
            String spec = this.value.toString();
            if (spec.startsWith("/")) {
                url = this.classLoader.getResource(spec);
            } else {
                try {
                    url = new URL(spec);
                } catch (MalformedURLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        return url;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
