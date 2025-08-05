package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.parser.CSSParser;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Stylesheet.class */
public class Stylesheet {
    static final int BINARY_CSS_VERSION = 5;
    private final String url;
    private StyleOrigin origin;
    private final ObservableList<Rule> rules;
    private final List<FontFace> fontFaces;
    private String[] stringStore;

    public String getUrl() {
        return this.url;
    }

    public StyleOrigin getOrigin() {
        return this.origin;
    }

    public void setOrigin(StyleOrigin origin) {
        this.origin = origin;
    }

    public Stylesheet() {
        this(null);
    }

    public Stylesheet(String url) {
        this.origin = StyleOrigin.AUTHOR;
        this.rules = new TrackableObservableList<Rule>() { // from class: com.sun.javafx.css.Stylesheet.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Rule> c2) {
                c2.reset();
                while (c2.next()) {
                    if (c2.wasAdded()) {
                        Iterator<Rule> it = c2.getAddedSubList().iterator();
                        while (it.hasNext()) {
                            it.next().setStylesheet(Stylesheet.this);
                        }
                    } else if (c2.wasRemoved()) {
                        for (Rule rule : c2.getRemoved()) {
                            if (rule.getStylesheet() == Stylesheet.this) {
                                rule.setStylesheet(null);
                            }
                        }
                    }
                }
            }
        };
        this.fontFaces = new ArrayList();
        this.url = url;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public List<FontFace> getFontFaces() {
        return this.fontFaces;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Stylesheet) {
            Stylesheet other = (Stylesheet) obj;
            if (this.url == null && other.url == null) {
                return true;
            }
            if (this.url == null || other.url == null) {
                return false;
            }
            return this.url.equals(other.url);
        }
        return false;
    }

    public int hashCode() {
        int hash = (13 * 7) + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("/* ");
        if (this.url != null) {
            sbuf.append(this.url);
        }
        if (this.rules.isEmpty()) {
            sbuf.append(" */");
        } else {
            sbuf.append(" */\n");
            for (int r2 = 0; r2 < this.rules.size(); r2++) {
                sbuf.append((Object) this.rules.get(r2));
                sbuf.append('\n');
            }
        }
        return sbuf.toString();
    }

    final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        int index = stringStore.addString(this.origin.name());
        os.writeShort(index);
        os.writeShort(this.rules.size());
        for (Rule r2 : this.rules) {
            r2.writeBinary(os, stringStore);
        }
        List<FontFace> fontFaceList = getFontFaces();
        int nFontFaces = fontFaceList != null ? fontFaceList.size() : 0;
        os.writeShort(nFontFaces);
        for (int n2 = 0; n2 < nFontFaces; n2++) {
            FontFace fontFace = fontFaceList.get(n2);
            fontFace.writeBinary(os, stringStore);
        }
    }

    final void readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        this.stringStore = strings;
        int index = is.readShort();
        setOrigin(StyleOrigin.valueOf(strings[index]));
        int nRules = is.readShort();
        List<Rule> persistedRules = new ArrayList<>(nRules);
        for (int n2 = 0; n2 < nRules; n2++) {
            persistedRules.add(Rule.readBinary(bssVersion, is, strings));
        }
        this.rules.addAll(persistedRules);
        if (bssVersion >= 5) {
            List<FontFace> fontFaceList = getFontFaces();
            int nFontFaces = is.readShort();
            for (int n3 = 0; n3 < nFontFaces; n3++) {
                FontFace fontFace = FontFace.readBinary(bssVersion, is, strings);
                fontFaceList.add(fontFace);
            }
        }
    }

    final String[] getStringStore() {
        return this.stringStore;
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x00d5: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:33:0x00d5 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00d1: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('dataInputStream' java.io.DataInputStream)]) A[TRY_LEAVE], block:B:31:0x00d1 */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r9v0, names: [dataInputStream], types: [java.io.DataInputStream] */
    public static Stylesheet loadBinary(URL url) throws IOException {
        DataInputStream dataInputStream;
        Throwable th;
        short s2;
        if (url == null) {
            return null;
        }
        Stylesheet stylesheet = null;
        try {
            try {
                dataInputStream = new DataInputStream(new BufferedInputStream(url.openStream(), 40960));
                th = null;
                s2 = dataInputStream.readShort();
            } finally {
            }
        } catch (FileNotFoundException e2) {
        }
        if (s2 > 5) {
            throw new IOException(url.toString() + " wrong binary CSS version: " + ((int) s2) + ". Expected version less than or equal to5");
        }
        String[] binary = StringStore.readBinary(dataInputStream);
        stylesheet = new Stylesheet(url.toExternalForm());
        try {
            dataInputStream.mark(Integer.MAX_VALUE);
            stylesheet.readBinary(s2, dataInputStream, binary);
        } catch (Exception e3) {
            stylesheet = new Stylesheet(url.toExternalForm());
            dataInputStream.reset();
            if (s2 == 2) {
                stylesheet.readBinary(3, dataInputStream, binary);
            } else {
                stylesheet.readBinary(5, dataInputStream, binary);
            }
        }
        if (dataInputStream != null) {
            if (0 != 0) {
                try {
                    dataInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } else {
                dataInputStream.close();
            }
        }
        return stylesheet;
    }

    public static void convertToBinary(File source, File destination) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("parameters may not be null");
        }
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            throw new IllegalArgumentException("source and destination may not be the same");
        }
        if (!source.canRead()) {
            throw new IllegalArgumentException("cannot read source file");
        }
        if (!destination.exists() ? !destination.createNewFile() : !destination.canWrite()) {
            throw new IllegalArgumentException("cannot write destination file");
        }
        URI sourceURI = source.toURI();
        Stylesheet stylesheet = new CSSParser().parse(sourceURI.toURL());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        StringStore stringStore = new StringStore();
        stylesheet.writeBinary(dos, stringStore);
        dos.flush();
        dos.close();
        FileOutputStream fos = new FileOutputStream(destination);
        DataOutputStream os = new DataOutputStream(fos);
        os.writeShort(5);
        stringStore.writeBinary(os);
        os.write(baos.toByteArray());
        os.flush();
        os.close();
    }

    public void importStylesheet(Stylesheet importedStylesheet) {
        List<Rule> rulesToImport;
        if (importedStylesheet == null || (rulesToImport = importedStylesheet.getRules()) == null || rulesToImport.isEmpty()) {
            return;
        }
        List<Rule> importedRules = new ArrayList<>(rulesToImport.size());
        for (Rule rule : rulesToImport) {
            List<Selector> selectors = rule.getSelectors();
            List<Declaration> declarations = rule.getUnobservedDeclarationList();
            importedRules.add(new Rule(selectors, declarations));
        }
        this.rules.addAll(importedRules);
    }
}
