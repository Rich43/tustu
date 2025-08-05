package com.sun.javafx.fxml.builder;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Builder;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/JavaFXFontBuilder.class */
public final class JavaFXFontBuilder extends AbstractMap<String, Object> implements Builder<Font> {
    private String name = null;
    private double size = 12.0d;
    private FontWeight weight = null;
    private FontPosture posture = null;
    private URL url = null;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Font build2() {
        Font f2;
        if (this.url != null) {
            InputStream in = null;
            try {
                try {
                    in = this.url.openStream();
                    f2 = Font.loadFont(in, this.size);
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (Exception e4) {
                throw new RuntimeException("Load of font file failed from " + ((Object) this.url), e4);
            }
        } else if (this.weight == null && this.posture == null) {
            f2 = new Font(this.name, this.size);
        } else {
            if (this.weight == null) {
                this.weight = FontWeight.NORMAL;
            }
            if (this.posture == null) {
                this.posture = FontPosture.REGULAR;
            }
            f2 = Font.font(this.name, this.weight, this.posture, this.size);
        }
        return f2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        FontWeight fw;
        if ("name".equals(key)) {
            if (value instanceof URL) {
                this.url = (URL) value;
                return null;
            }
            this.name = (String) value;
            return null;
        }
        if ("size".equals(key)) {
            this.size = Double.parseDouble((String) value);
            return null;
        }
        if (!Constants.ATTRNAME_STYLE.equals(key)) {
            if ("url".equals(key)) {
                if (value instanceof URL) {
                    this.url = (URL) value;
                    return null;
                }
                try {
                    this.url = new URL(value.toString());
                    return null;
                } catch (MalformedURLException e2) {
                    throw new IllegalArgumentException("Invalid url " + value.toString(), e2);
                }
            }
            throw new IllegalArgumentException("Unknown Font property: " + key);
        }
        String style = (String) value;
        if (style != null && style.length() > 0) {
            boolean isWeightSet = false;
            StringTokenizer st = new StringTokenizer(style, " ");
            while (st.hasMoreTokens()) {
                String stylePart = st.nextToken();
                if (!isWeightSet && (fw = FontWeight.findByName(stylePart)) != null) {
                    this.weight = fw;
                    isWeightSet = true;
                } else {
                    FontPosture fp = FontPosture.findByName(stylePart);
                    if (fp != null) {
                        this.posture = fp;
                    }
                }
            }
            return null;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
