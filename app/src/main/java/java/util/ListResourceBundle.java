package java.util;

import sun.util.ResourceBundleEnumeration;

/* loaded from: rt.jar:java/util/ListResourceBundle.class */
public abstract class ListResourceBundle extends ResourceBundle {
    private Map<String, Object> lookup = null;

    protected abstract Object[][] getContents();

    @Override // java.util.ResourceBundle
    public final Object handleGetObject(String str) {
        if (this.lookup == null) {
            loadLookup();
        }
        if (str == null) {
            throw new NullPointerException();
        }
        return this.lookup.get(str);
    }

    @Override // java.util.ResourceBundle
    public Enumeration<String> getKeys() {
        if (this.lookup == null) {
            loadLookup();
        }
        ResourceBundle resourceBundle = this.parent;
        return new ResourceBundleEnumeration(this.lookup.keySet(), resourceBundle != null ? resourceBundle.getKeys() : null);
    }

    @Override // java.util.ResourceBundle
    protected Set<String> handleKeySet() {
        if (this.lookup == null) {
            loadLookup();
        }
        return this.lookup.keySet();
    }

    private synchronized void loadLookup() {
        if (this.lookup != null) {
            return;
        }
        Object[][] contents = getContents();
        HashMap map = new HashMap(contents.length);
        for (int i2 = 0; i2 < contents.length; i2++) {
            String str = (String) contents[i2][0];
            Object obj = contents[i2][1];
            if (str == null || obj == null) {
                throw new NullPointerException();
            }
            map.put(str, obj);
        }
        this.lookup = map;
    }
}
