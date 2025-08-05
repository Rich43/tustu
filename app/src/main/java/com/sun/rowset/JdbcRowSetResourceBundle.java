package com.sun.rowset;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/rowset/JdbcRowSetResourceBundle.class */
public class JdbcRowSetResourceBundle implements Serializable {
    private static String fileName;
    private transient PropertyResourceBundle propResBundle = (PropertyResourceBundle) ResourceBundle.getBundle(PATH, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
    private static volatile JdbcRowSetResourceBundle jpResBundle;
    private static final String PROPERTIES = "properties";
    private static final String UNDERSCORE = "_";
    private static final String DOT = ".";
    private static final String SLASH = "/";
    private static final String PATH = "com/sun/rowset/RowSetResourceBundle";
    static final long serialVersionUID = 436199386225359954L;

    private JdbcRowSetResourceBundle() throws IOException {
    }

    public static JdbcRowSetResourceBundle getJdbcRowSetResourceBundle() throws IOException {
        if (jpResBundle == null) {
            synchronized (JdbcRowSetResourceBundle.class) {
                if (jpResBundle == null) {
                    jpResBundle = new JdbcRowSetResourceBundle();
                }
            }
        }
        return jpResBundle;
    }

    public Enumeration getKeys() {
        return this.propResBundle.getKeys();
    }

    public Object handleGetObject(String str) {
        return this.propResBundle.handleGetObject(str);
    }
}
