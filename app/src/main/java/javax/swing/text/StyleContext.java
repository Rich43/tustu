package javax.swing.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.AbstractDocument;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.font.FontUtilities;

/* loaded from: rt.jar:javax/swing/text/StyleContext.class */
public class StyleContext implements Serializable, AbstractDocument.AttributeContext {
    private static StyleContext defaultContext;
    public static final String DEFAULT_STYLE = "default";
    private static Hashtable<Object, String> freezeKeyMap;
    private static Hashtable<String, Object> thawKeyMap;
    private int unusedSets;
    static final int THRESHOLD = 9;
    private transient FontKey fontSearch = new FontKey(null, 0, 0);
    private transient Hashtable<FontKey, Font> fontTable = new Hashtable<>();
    private transient Map<SmallAttributeSet, WeakReference<SmallAttributeSet>> attributesPool = Collections.synchronizedMap(new WeakHashMap());
    private transient MutableAttributeSet search = new SimpleAttributeSet();
    private Style styles = new NamedStyle(this, null);

    public static final StyleContext getDefaultStyleContext() {
        if (defaultContext == null) {
            defaultContext = new StyleContext();
        }
        return defaultContext;
    }

    public StyleContext() {
        addStyle("default", null);
    }

    public Style addStyle(String str, Style style) {
        NamedStyle namedStyle = new NamedStyle(str, style);
        if (str != null) {
            this.styles.addAttribute(str, namedStyle);
        }
        return namedStyle;
    }

    public void removeStyle(String str) {
        this.styles.removeAttribute(str);
    }

    public Style getStyle(String str) {
        return (Style) this.styles.getAttribute(str);
    }

    public Enumeration<?> getStyleNames() {
        return this.styles.getAttributeNames();
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.styles.addChangeListener(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.styles.removeChangeListener(changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return ((NamedStyle) this.styles).getChangeListeners();
    }

    public Font getFont(AttributeSet attributeSet) {
        int i2 = 0;
        if (StyleConstants.isBold(attributeSet)) {
            i2 = 0 | 1;
        }
        if (StyleConstants.isItalic(attributeSet)) {
            i2 |= 2;
        }
        String fontFamily = StyleConstants.getFontFamily(attributeSet);
        int fontSize = StyleConstants.getFontSize(attributeSet);
        if (StyleConstants.isSuperscript(attributeSet) || StyleConstants.isSubscript(attributeSet)) {
            fontSize -= 2;
        }
        return getFont(fontFamily, i2, fontSize);
    }

    public Color getForeground(AttributeSet attributeSet) {
        return StyleConstants.getForeground(attributeSet);
    }

    public Color getBackground(AttributeSet attributeSet) {
        return StyleConstants.getBackground(attributeSet);
    }

    public Font getFont(String str, int i2, int i3) {
        Font font;
        this.fontSearch.setValue(str, i2, i3);
        Font compositeFontUIResource = this.fontTable.get(this.fontSearch);
        if (compositeFontUIResource == null) {
            Style style = getStyle("default");
            if (style != null && (font = (Font) style.getAttribute("FONT_ATTRIBUTE_KEY")) != null && font.getFamily().equalsIgnoreCase(str)) {
                compositeFontUIResource = font.deriveFont(i2, i3);
            }
            if (compositeFontUIResource == null) {
                compositeFontUIResource = new Font(str, i2, i3);
            }
            if (!FontUtilities.fontSupportsDefaultEncoding(compositeFontUIResource)) {
                compositeFontUIResource = FontUtilities.getCompositeFontUIResource(compositeFontUIResource);
            }
            this.fontTable.put(new FontKey(str, i2, i3), compositeFontUIResource);
        }
        return compositeFontUIResource;
    }

    public FontMetrics getFontMetrics(Font font) {
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public synchronized AttributeSet addAttribute(AttributeSet attributeSet, Object obj, Object obj2) {
        if (attributeSet.getAttributeCount() + 1 <= getCompressionThreshold()) {
            this.search.removeAttributes(this.search);
            this.search.addAttributes(attributeSet);
            this.search.addAttribute(obj, obj2);
            reclaim(attributeSet);
            return getImmutableUniqueSet();
        }
        MutableAttributeSet mutableAttributeSet = getMutableAttributeSet(attributeSet);
        mutableAttributeSet.addAttribute(obj, obj2);
        return mutableAttributeSet;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public synchronized AttributeSet addAttributes(AttributeSet attributeSet, AttributeSet attributeSet2) {
        if (attributeSet.getAttributeCount() + attributeSet2.getAttributeCount() <= getCompressionThreshold()) {
            this.search.removeAttributes(this.search);
            this.search.addAttributes(attributeSet);
            this.search.addAttributes(attributeSet2);
            reclaim(attributeSet);
            return getImmutableUniqueSet();
        }
        MutableAttributeSet mutableAttributeSet = getMutableAttributeSet(attributeSet);
        mutableAttributeSet.addAttributes(attributeSet2);
        return mutableAttributeSet;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public synchronized AttributeSet removeAttribute(AttributeSet attributeSet, Object obj) {
        if (attributeSet.getAttributeCount() - 1 <= getCompressionThreshold()) {
            this.search.removeAttributes(this.search);
            this.search.addAttributes(attributeSet);
            this.search.removeAttribute(obj);
            reclaim(attributeSet);
            return getImmutableUniqueSet();
        }
        MutableAttributeSet mutableAttributeSet = getMutableAttributeSet(attributeSet);
        mutableAttributeSet.removeAttribute(obj);
        return mutableAttributeSet;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public synchronized AttributeSet removeAttributes(AttributeSet attributeSet, Enumeration<?> enumeration) {
        if (attributeSet.getAttributeCount() <= getCompressionThreshold()) {
            this.search.removeAttributes(this.search);
            this.search.addAttributes(attributeSet);
            this.search.removeAttributes(enumeration);
            reclaim(attributeSet);
            return getImmutableUniqueSet();
        }
        MutableAttributeSet mutableAttributeSet = getMutableAttributeSet(attributeSet);
        mutableAttributeSet.removeAttributes(enumeration);
        return mutableAttributeSet;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public synchronized AttributeSet removeAttributes(AttributeSet attributeSet, AttributeSet attributeSet2) {
        if (attributeSet.getAttributeCount() <= getCompressionThreshold()) {
            this.search.removeAttributes(this.search);
            this.search.addAttributes(attributeSet);
            this.search.removeAttributes(attributeSet2);
            reclaim(attributeSet);
            return getImmutableUniqueSet();
        }
        MutableAttributeSet mutableAttributeSet = getMutableAttributeSet(attributeSet);
        mutableAttributeSet.removeAttributes(attributeSet2);
        return mutableAttributeSet;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet getEmptySet() {
        return SimpleAttributeSet.EMPTY;
    }

    @Override // javax.swing.text.AbstractDocument.AttributeContext
    public void reclaim(AttributeSet attributeSet) {
        if (SwingUtilities.isEventDispatchThread()) {
            this.attributesPool.size();
        }
    }

    protected int getCompressionThreshold() {
        return 9;
    }

    protected SmallAttributeSet createSmallAttributeSet(AttributeSet attributeSet) {
        return new SmallAttributeSet(attributeSet);
    }

    protected MutableAttributeSet createLargeAttributeSet(AttributeSet attributeSet) {
        return new SimpleAttributeSet(attributeSet);
    }

    synchronized void removeUnusedSets() {
        this.attributesPool.size();
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    javax.swing.text.AttributeSet getImmutableUniqueSet() {
        /*
            r6 = this;
            r0 = r6
            r1 = r6
            javax.swing.text.MutableAttributeSet r1 = r1.search
            javax.swing.text.StyleContext$SmallAttributeSet r0 = r0.createSmallAttributeSet(r1)
            r7 = r0
            r0 = r6
            java.util.Map<javax.swing.text.StyleContext$SmallAttributeSet, java.lang.ref.WeakReference<javax.swing.text.StyleContext$SmallAttributeSet>> r0 = r0.attributesPool
            r1 = r7
            java.lang.Object r0 = r0.get(r1)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            r8 = r0
            r0 = r8
            if (r0 == 0) goto L27
            r0 = r8
            java.lang.Object r0 = r0.get()
            javax.swing.text.StyleContext$SmallAttributeSet r0 = (javax.swing.text.StyleContext.SmallAttributeSet) r0
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L3c
        L27:
            r0 = r7
            r9 = r0
            r0 = r6
            java.util.Map<javax.swing.text.StyleContext$SmallAttributeSet, java.lang.ref.WeakReference<javax.swing.text.StyleContext$SmallAttributeSet>> r0 = r0.attributesPool
            r1 = r9
            java.lang.ref.WeakReference r2 = new java.lang.ref.WeakReference
            r3 = r2
            r4 = r9
            r3.<init>(r4)
            java.lang.Object r0 = r0.put(r1, r2)
        L3c:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.StyleContext.getImmutableUniqueSet():javax.swing.text.AttributeSet");
    }

    MutableAttributeSet getMutableAttributeSet(AttributeSet attributeSet) {
        if ((attributeSet instanceof MutableAttributeSet) && attributeSet != SimpleAttributeSet.EMPTY) {
            return (MutableAttributeSet) attributeSet;
        }
        return createLargeAttributeSet(attributeSet);
    }

    public String toString() {
        removeUnusedSets();
        String str = "";
        Iterator<SmallAttributeSet> it = this.attributesPool.keySet().iterator();
        while (it.hasNext()) {
            str = str + ((Object) it.next()) + "\n";
        }
        return str;
    }

    public void writeAttributes(ObjectOutputStream objectOutputStream, AttributeSet attributeSet) throws IOException {
        writeAttributeSet(objectOutputStream, attributeSet);
    }

    public void readAttributes(ObjectInputStream objectInputStream, MutableAttributeSet mutableAttributeSet) throws ClassNotFoundException, IOException {
        readAttributeSet(objectInputStream, mutableAttributeSet);
    }

    public static void writeAttributeSet(ObjectOutputStream objectOutputStream, AttributeSet attributeSet) throws IOException {
        objectOutputStream.writeInt(attributeSet.getAttributeCount());
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            if (objNextElement instanceof Serializable) {
                objectOutputStream.writeObject(objNextElement);
            } else {
                String str = freezeKeyMap.get(objNextElement);
                if (str == null) {
                    throw new NotSerializableException(objNextElement.getClass().getName() + " is not serializable as a key in an AttributeSet");
                }
                objectOutputStream.writeObject(str);
            }
            Object attribute = attributeSet.getAttribute(objNextElement);
            String str2 = freezeKeyMap.get(attribute);
            if (attribute instanceof Serializable) {
                objectOutputStream.writeObject(str2 != null ? str2 : attribute);
            } else {
                if (str2 == null) {
                    throw new NotSerializableException(attribute.getClass().getName() + " is not serializable as a value in an AttributeSet");
                }
                objectOutputStream.writeObject(str2);
            }
        }
    }

    public static void readAttributeSet(ObjectInputStream objectInputStream, MutableAttributeSet mutableAttributeSet) throws ClassNotFoundException, IOException {
        int i2 = objectInputStream.readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            Object object = objectInputStream.readObject();
            Object object2 = objectInputStream.readObject();
            if (thawKeyMap != null) {
                Object obj = thawKeyMap.get(object);
                if (obj != null) {
                    object = obj;
                }
                Object obj2 = thawKeyMap.get(object2);
                if (obj2 != null) {
                    object2 = obj2;
                }
            }
            mutableAttributeSet.addAttribute(object, object2);
        }
    }

    public static void registerStaticAttributeKey(Object obj) {
        String str = obj.getClass().getName() + "." + obj.toString();
        if (freezeKeyMap == null) {
            freezeKeyMap = new Hashtable<>();
            thawKeyMap = new Hashtable<>();
        }
        freezeKeyMap.put(obj, str);
        thawKeyMap.put(str, obj);
    }

    public static Object getStaticAttribute(Object obj) {
        if (thawKeyMap == null || obj == null) {
            return null;
        }
        return thawKeyMap.get(obj);
    }

    public static Object getStaticAttributeKey(Object obj) {
        return obj.getClass().getName() + "." + obj.toString();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        removeUnusedSets();
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.fontSearch = new FontKey(null, 0, 0);
        this.fontTable = new Hashtable<>();
        this.search = new SimpleAttributeSet();
        this.attributesPool = Collections.synchronizedMap(new WeakHashMap());
        objectInputStream.defaultReadObject();
    }

    /* loaded from: rt.jar:javax/swing/text/StyleContext$SmallAttributeSet.class */
    public class SmallAttributeSet implements AttributeSet {
        Object[] attributes;
        AttributeSet resolveParent;

        public SmallAttributeSet(Object[] objArr) {
            this.attributes = objArr;
            updateResolveParent();
        }

        public SmallAttributeSet(AttributeSet attributeSet) {
            Object[] objArr = new Object[2 * attributeSet.getAttributeCount()];
            Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            int i2 = 0;
            while (attributeNames.hasMoreElements()) {
                objArr[i2] = attributeNames.nextElement2();
                objArr[i2 + 1] = attributeSet.getAttribute(objArr[i2]);
                i2 += 2;
            }
            this.attributes = objArr;
            updateResolveParent();
        }

        private void updateResolveParent() {
            this.resolveParent = null;
            Object[] objArr = this.attributes;
            for (int i2 = 0; i2 < objArr.length; i2 += 2) {
                if (objArr[i2] == StyleConstants.ResolveAttribute) {
                    this.resolveParent = (AttributeSet) objArr[i2 + 1];
                    return;
                }
            }
        }

        Object getLocalAttribute(Object obj) {
            if (obj == StyleConstants.ResolveAttribute) {
                return this.resolveParent;
            }
            Object[] objArr = this.attributes;
            for (int i2 = 0; i2 < objArr.length; i2 += 2) {
                if (obj.equals(objArr[i2])) {
                    return objArr[i2 + 1];
                }
            }
            return null;
        }

        public String toString() {
            String str;
            String str2 = VectorFormat.DEFAULT_PREFIX;
            Object[] objArr = this.attributes;
            for (int i2 = 0; i2 < objArr.length; i2 += 2) {
                if (objArr[i2 + 1] instanceof AttributeSet) {
                    str = str2 + objArr[i2] + "=AttributeSet,";
                } else {
                    str = str2 + objArr[i2] + "=" + objArr[i2 + 1] + ",";
                }
                str2 = str;
            }
            return str2 + "}";
        }

        public int hashCode() {
            int iHashCode = 0;
            Object[] objArr = this.attributes;
            for (int i2 = 1; i2 < objArr.length; i2 += 2) {
                iHashCode ^= objArr[i2].hashCode();
            }
            return iHashCode;
        }

        public boolean equals(Object obj) {
            if (obj instanceof AttributeSet) {
                AttributeSet attributeSet = (AttributeSet) obj;
                return getAttributeCount() == attributeSet.getAttributeCount() && containsAttributes(attributeSet);
            }
            return false;
        }

        public Object clone() {
            return this;
        }

        @Override // javax.swing.text.AttributeSet
        public int getAttributeCount() {
            return this.attributes.length / 2;
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            Object[] objArr = this.attributes;
            int length = objArr.length;
            for (int i2 = 0; i2 < length; i2 += 2) {
                if (obj.equals(objArr[i2])) {
                    return true;
                }
            }
            return false;
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isEqual(AttributeSet attributeSet) {
            return attributeSet instanceof SmallAttributeSet ? attributeSet == this : getAttributeCount() == attributeSet.getAttributeCount() && containsAttributes(attributeSet);
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet copyAttributes() {
            return this;
        }

        @Override // javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            AttributeSet resolveParent;
            Object localAttribute = getLocalAttribute(obj);
            if (localAttribute == null && (resolveParent = getResolveParent()) != null) {
                localAttribute = resolveParent.getAttribute(obj);
            }
            return localAttribute;
        }

        @Override // javax.swing.text.AttributeSet
        public Enumeration<?> getAttributeNames() {
            return StyleContext.this.new KeyEnumeration(this.attributes);
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttribute(Object obj, Object obj2) {
            return obj2.equals(getAttribute(obj));
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttributes(AttributeSet attributeSet) {
            boolean zEquals = true;
            Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            while (zEquals && attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                zEquals = attributeSet.getAttribute(objNextElement).equals(getAttribute(objNextElement));
            }
            return zEquals;
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            return this.resolveParent;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleContext$KeyEnumeration.class */
    class KeyEnumeration implements Enumeration<Object> {
        Object[] attr;

        /* renamed from: i, reason: collision with root package name */
        int f12841i = 0;

        KeyEnumeration(Object[] objArr) {
            this.attr = objArr;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.f12841i < this.attr.length;
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Object nextElement2() {
            if (this.f12841i < this.attr.length) {
                Object obj = this.attr[this.f12841i];
                this.f12841i += 2;
                return obj;
            }
            throw new NoSuchElementException();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleContext$KeyBuilder.class */
    class KeyBuilder {
        private Vector<Object> keys = new Vector<>();
        private Vector<Object> data = new Vector<>();

        KeyBuilder() {
        }

        public void initialize(AttributeSet attributeSet) {
            if (attributeSet instanceof SmallAttributeSet) {
                initialize(((SmallAttributeSet) attributeSet).attributes);
                return;
            }
            this.keys.removeAllElements();
            this.data.removeAllElements();
            Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
            }
        }

        private void initialize(Object[] objArr) {
            this.keys.removeAllElements();
            this.data.removeAllElements();
            int length = objArr.length;
            for (int i2 = 0; i2 < length; i2 += 2) {
                this.keys.addElement(objArr[i2]);
                this.data.addElement(objArr[i2 + 1]);
            }
        }

        public Object[] createTable() {
            int size = this.keys.size();
            Object[] objArr = new Object[2 * size];
            for (int i2 = 0; i2 < size; i2++) {
                int i3 = 2 * i2;
                objArr[i3] = this.keys.elementAt(i2);
                objArr[i3 + 1] = this.data.elementAt(i2);
            }
            return objArr;
        }

        int getCount() {
            return this.keys.size();
        }

        public void addAttribute(Object obj, Object obj2) {
            this.keys.addElement(obj);
            this.data.addElement(obj2);
        }

        public void addAttributes(AttributeSet attributeSet) {
            if (attributeSet instanceof SmallAttributeSet) {
                Object[] objArr = ((SmallAttributeSet) attributeSet).attributes;
                int length = objArr.length;
                for (int i2 = 0; i2 < length; i2 += 2) {
                    addAttribute(objArr[i2], objArr[i2 + 1]);
                }
                return;
            }
            Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
            }
        }

        public void removeAttribute(Object obj) {
            int size = this.keys.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.keys.elementAt(i2).equals(obj)) {
                    this.keys.removeElementAt(i2);
                    this.data.removeElementAt(i2);
                    return;
                }
            }
        }

        public void removeAttributes(Enumeration enumeration) {
            while (enumeration.hasMoreElements()) {
                removeAttribute(enumeration.nextElement2());
            }
        }

        public void removeAttributes(AttributeSet attributeSet) {
            Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                removeSearchAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
            }
        }

        private void removeSearchAttribute(Object obj, Object obj2) {
            int size = this.keys.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.keys.elementAt(i2).equals(obj)) {
                    if (this.data.elementAt(i2).equals(obj2)) {
                        this.keys.removeElementAt(i2);
                        this.data.removeElementAt(i2);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleContext$FontKey.class */
    static class FontKey {
        private String family;
        private int style;
        private int size;

        public FontKey(String str, int i2, int i3) {
            setValue(str, i2, i3);
        }

        public void setValue(String str, int i2, int i3) {
            this.family = str != null ? str.intern() : null;
            this.style = i2;
            this.size = i3;
        }

        public int hashCode() {
            return ((this.family != null ? this.family.hashCode() : 0) ^ this.style) ^ this.size;
        }

        public boolean equals(Object obj) {
            if (obj instanceof FontKey) {
                FontKey fontKey = (FontKey) obj;
                return this.size == fontKey.size && this.style == fontKey.style && this.family == fontKey.family;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleContext$NamedStyle.class */
    public class NamedStyle implements Style, Serializable {
        protected EventListenerList listenerList;
        protected transient ChangeEvent changeEvent;
        private transient AttributeSet attributes;

        public NamedStyle(String str, Style style) {
            this.listenerList = new EventListenerList();
            this.changeEvent = null;
            this.attributes = StyleContext.this.getEmptySet();
            if (str != null) {
                setName(str);
            }
            if (style != null) {
                setResolveParent(style);
            }
        }

        public NamedStyle(StyleContext styleContext, Style style) {
            this(null, style);
        }

        public NamedStyle() {
            this.listenerList = new EventListenerList();
            this.changeEvent = null;
            this.attributes = StyleContext.this.getEmptySet();
        }

        public String toString() {
            return "NamedStyle:" + getName() + " " + ((Object) this.attributes);
        }

        @Override // javax.swing.text.Style
        public String getName() {
            if (isDefined(StyleConstants.NameAttribute)) {
                return getAttribute(StyleConstants.NameAttribute).toString();
            }
            return null;
        }

        public void setName(String str) {
            if (str != null) {
                addAttribute(StyleConstants.NameAttribute, str);
            }
        }

        @Override // javax.swing.text.Style
        public void addChangeListener(ChangeListener changeListener) {
            this.listenerList.add(ChangeListener.class, changeListener);
        }

        @Override // javax.swing.text.Style
        public void removeChangeListener(ChangeListener changeListener) {
            this.listenerList.remove(ChangeListener.class, changeListener);
        }

        public ChangeListener[] getChangeListeners() {
            return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
        }

        protected void fireStateChanged() {
            Object[] listenerList = this.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ChangeListener.class) {
                    if (this.changeEvent == null) {
                        this.changeEvent = new ChangeEvent(this);
                    }
                    ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
                }
            }
        }

        public <T extends EventListener> T[] getListeners(Class<T> cls) {
            return (T[]) this.listenerList.getListeners(cls);
        }

        @Override // javax.swing.text.AttributeSet
        public int getAttributeCount() {
            return this.attributes.getAttributeCount();
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            return this.attributes.isDefined(obj);
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isEqual(AttributeSet attributeSet) {
            return this.attributes.isEqual(attributeSet);
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet copyAttributes() {
            NamedStyle namedStyle = StyleContext.this.new NamedStyle();
            namedStyle.attributes = this.attributes.copyAttributes();
            return namedStyle;
        }

        @Override // javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            return this.attributes.getAttribute(obj);
        }

        @Override // javax.swing.text.AttributeSet
        public Enumeration<?> getAttributeNames() {
            return this.attributes.getAttributeNames();
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttribute(Object obj, Object obj2) {
            return this.attributes.containsAttribute(obj, obj2);
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttributes(AttributeSet attributeSet) {
            return this.attributes.containsAttributes(attributeSet);
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            return this.attributes.getResolveParent();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttribute(Object obj, Object obj2) {
            this.attributes = StyleContext.this.addAttribute(this.attributes, obj, obj2);
            fireStateChanged();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttributes(AttributeSet attributeSet) {
            this.attributes = StyleContext.this.addAttributes(this.attributes, attributeSet);
            fireStateChanged();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttribute(Object obj) {
            this.attributes = StyleContext.this.removeAttribute(this.attributes, obj);
            fireStateChanged();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(Enumeration<?> enumeration) {
            this.attributes = StyleContext.this.removeAttributes(this.attributes, enumeration);
            fireStateChanged();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(AttributeSet attributeSet) {
            StyleContext styleContext = StyleContext.this;
            if (attributeSet == this) {
                this.attributes = styleContext.getEmptySet();
            } else {
                this.attributes = styleContext.removeAttributes(this.attributes, attributeSet);
            }
            fireStateChanged();
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void setResolveParent(AttributeSet attributeSet) {
            if (attributeSet != null) {
                addAttribute(StyleConstants.ResolveAttribute, attributeSet);
            } else {
                removeAttribute(StyleConstants.ResolveAttribute);
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            StyleContext.writeAttributeSet(objectOutputStream, this.attributes);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.attributes = SimpleAttributeSet.EMPTY;
            StyleContext.readAttributeSet(objectInputStream, this);
        }
    }

    static {
        try {
            int length = StyleConstants.keys.length;
            for (int i2 = 0; i2 < length; i2++) {
                registerStaticAttributeKey(StyleConstants.keys[i2]);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
