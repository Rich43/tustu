package sun.font;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.font.GraphicAttribute;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.im.InputMethodHighlight;
import java.io.Serializable;
import java.text.Annotation;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.Action;

/* loaded from: rt.jar:sun/font/AttributeValues.class */
public final class AttributeValues implements Cloneable {
    private int defined;
    private int nondefault;
    private float posture;
    private float tracking;
    private NumericShaper numericShaping;
    private AffineTransform transform;
    private GraphicAttribute charReplacement;
    private Paint foreground;
    private Paint background;
    private Object imHighlight;
    private Font font;
    private byte superscript;
    private byte bidiEmbedding;
    private byte kerning;
    private byte ligatures;
    private boolean strikethrough;
    private boolean swapColors;
    private AffineTransform baselineTransform;
    private AffineTransform charTransform;
    private static final AttributeValues DEFAULT = new AttributeValues();
    public static final int MASK_ALL = getMask((EAttribute[]) EAttribute.class.getEnumConstants());
    private static final String DEFINED_KEY = "sun.font.attributevalues.defined_key";
    private String family = Action.DEFAULT;
    private float weight = 1.0f;
    private float width = 1.0f;
    private float size = 12.0f;
    private float justification = 1.0f;
    private byte imUnderline = -1;
    private byte underline = -1;
    private byte runDirection = -2;

    public String getFamily() {
        return this.family;
    }

    public void setFamily(String str) {
        this.family = str;
        update(EAttribute.EFAMILY);
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float f2) {
        this.weight = f2;
        update(EAttribute.EWEIGHT);
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float f2) {
        this.width = f2;
        update(EAttribute.EWIDTH);
    }

    public float getPosture() {
        return this.posture;
    }

    public void setPosture(float f2) {
        this.posture = f2;
        update(EAttribute.EPOSTURE);
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float f2) {
        this.size = f2;
        update(EAttribute.ESIZE);
    }

    public AffineTransform getTransform() {
        return this.transform;
    }

    public void setTransform(AffineTransform affineTransform) {
        this.transform = (affineTransform == null || affineTransform.isIdentity()) ? DEFAULT.transform : new AffineTransform(affineTransform);
        updateDerivedTransforms();
        update(EAttribute.ETRANSFORM);
    }

    public void setTransform(TransformAttribute transformAttribute) {
        this.transform = (transformAttribute == null || transformAttribute.isIdentity()) ? DEFAULT.transform : transformAttribute.getTransform();
        updateDerivedTransforms();
        update(EAttribute.ETRANSFORM);
    }

    public int getSuperscript() {
        return this.superscript;
    }

    public void setSuperscript(int i2) {
        this.superscript = (byte) i2;
        update(EAttribute.ESUPERSCRIPT);
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
        update(EAttribute.EFONT);
    }

    public GraphicAttribute getCharReplacement() {
        return this.charReplacement;
    }

    public void setCharReplacement(GraphicAttribute graphicAttribute) {
        this.charReplacement = graphicAttribute;
        update(EAttribute.ECHAR_REPLACEMENT);
    }

    public Paint getForeground() {
        return this.foreground;
    }

    public void setForeground(Paint paint) {
        this.foreground = paint;
        update(EAttribute.EFOREGROUND);
    }

    public Paint getBackground() {
        return this.background;
    }

    public void setBackground(Paint paint) {
        this.background = paint;
        update(EAttribute.EBACKGROUND);
    }

    public int getUnderline() {
        return this.underline;
    }

    public void setUnderline(int i2) {
        this.underline = (byte) i2;
        update(EAttribute.EUNDERLINE);
    }

    public boolean getStrikethrough() {
        return this.strikethrough;
    }

    public void setStrikethrough(boolean z2) {
        this.strikethrough = z2;
        update(EAttribute.ESTRIKETHROUGH);
    }

    public int getRunDirection() {
        return this.runDirection;
    }

    public void setRunDirection(int i2) {
        this.runDirection = (byte) i2;
        update(EAttribute.ERUN_DIRECTION);
    }

    public int getBidiEmbedding() {
        return this.bidiEmbedding;
    }

    public void setBidiEmbedding(int i2) {
        this.bidiEmbedding = (byte) i2;
        update(EAttribute.EBIDI_EMBEDDING);
    }

    public float getJustification() {
        return this.justification;
    }

    public void setJustification(float f2) {
        this.justification = f2;
        update(EAttribute.EJUSTIFICATION);
    }

    public Object getInputMethodHighlight() {
        return this.imHighlight;
    }

    public void setInputMethodHighlight(Annotation annotation) {
        this.imHighlight = annotation;
        update(EAttribute.EINPUT_METHOD_HIGHLIGHT);
    }

    public void setInputMethodHighlight(InputMethodHighlight inputMethodHighlight) {
        this.imHighlight = inputMethodHighlight;
        update(EAttribute.EINPUT_METHOD_HIGHLIGHT);
    }

    public int getInputMethodUnderline() {
        return this.imUnderline;
    }

    public void setInputMethodUnderline(int i2) {
        this.imUnderline = (byte) i2;
        update(EAttribute.EINPUT_METHOD_UNDERLINE);
    }

    public boolean getSwapColors() {
        return this.swapColors;
    }

    public void setSwapColors(boolean z2) {
        this.swapColors = z2;
        update(EAttribute.ESWAP_COLORS);
    }

    public NumericShaper getNumericShaping() {
        return this.numericShaping;
    }

    public void setNumericShaping(NumericShaper numericShaper) {
        this.numericShaping = numericShaper;
        update(EAttribute.ENUMERIC_SHAPING);
    }

    public int getKerning() {
        return this.kerning;
    }

    public void setKerning(int i2) {
        this.kerning = (byte) i2;
        update(EAttribute.EKERNING);
    }

    public float getTracking() {
        return this.tracking;
    }

    public void setTracking(float f2) {
        this.tracking = (byte) f2;
        update(EAttribute.ETRACKING);
    }

    public int getLigatures() {
        return this.ligatures;
    }

    public void setLigatures(int i2) {
        this.ligatures = (byte) i2;
        update(EAttribute.ELIGATURES);
    }

    public AffineTransform getBaselineTransform() {
        return this.baselineTransform;
    }

    public AffineTransform getCharTransform() {
        return this.charTransform;
    }

    public static int getMask(EAttribute eAttribute) {
        return eAttribute.mask;
    }

    public static int getMask(EAttribute... eAttributeArr) {
        int i2 = 0;
        for (EAttribute eAttribute : eAttributeArr) {
            i2 |= eAttribute.mask;
        }
        return i2;
    }

    public void unsetDefault() {
        this.defined &= this.nondefault;
    }

    public void defineAll(int i2) {
        this.defined |= i2;
        if ((this.defined & EAttribute.EBASELINE_TRANSFORM.mask) != 0) {
            throw new InternalError("can't define derived attribute");
        }
    }

    public boolean allDefined(int i2) {
        return (this.defined & i2) == i2;
    }

    public boolean anyDefined(int i2) {
        return (this.defined & i2) != 0;
    }

    public boolean anyNonDefault(int i2) {
        return (this.nondefault & i2) != 0;
    }

    public boolean isDefined(EAttribute eAttribute) {
        return (this.defined & eAttribute.mask) != 0;
    }

    public boolean isNonDefault(EAttribute eAttribute) {
        return (this.nondefault & eAttribute.mask) != 0;
    }

    public void setDefault(EAttribute eAttribute) {
        if (eAttribute.att == null) {
            throw new InternalError("can't set default derived attribute: " + ((Object) eAttribute));
        }
        i_set(eAttribute, DEFAULT);
        this.defined |= eAttribute.mask;
        this.nondefault &= eAttribute.mask ^ (-1);
    }

    public void unset(EAttribute eAttribute) {
        if (eAttribute.att == null) {
            throw new InternalError("can't unset derived attribute: " + ((Object) eAttribute));
        }
        i_set(eAttribute, DEFAULT);
        this.defined &= eAttribute.mask ^ (-1);
        this.nondefault &= eAttribute.mask ^ (-1);
    }

    public void set(EAttribute eAttribute, AttributeValues attributeValues) {
        if (eAttribute.att == null) {
            throw new InternalError("can't set derived attribute: " + ((Object) eAttribute));
        }
        if (attributeValues == null || attributeValues == DEFAULT) {
            setDefault(eAttribute);
        } else if ((attributeValues.defined & eAttribute.mask) != 0) {
            i_set(eAttribute, attributeValues);
            update(eAttribute);
        }
    }

    public void set(EAttribute eAttribute, Object obj) {
        if (eAttribute.att == null) {
            throw new InternalError("can't set derived attribute: " + ((Object) eAttribute));
        }
        if (obj != null) {
            try {
                i_set(eAttribute, obj);
                update(eAttribute);
                return;
            } catch (Exception e2) {
            }
        }
        setDefault(eAttribute);
    }

    public Object get(EAttribute eAttribute) {
        if (eAttribute.att == null) {
            throw new InternalError("can't get derived attribute: " + ((Object) eAttribute));
        }
        if ((this.nondefault & eAttribute.mask) != 0) {
            return i_get(eAttribute);
        }
        return null;
    }

    public AttributeValues merge(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        return merge(map, MASK_ALL);
    }

    public AttributeValues merge(Map<? extends AttributedCharacterIterator.Attribute, ?> map, int i2) {
        if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
            merge(((AttributeMap) map).getValues(), i2);
        } else if (map != null && !map.isEmpty()) {
            for (Map.Entry<? extends AttributedCharacterIterator.Attribute, ?> entry : map.entrySet()) {
                try {
                    EAttribute eAttributeForAttribute = EAttribute.forAttribute(entry.getKey());
                    if (eAttributeForAttribute != null && (i2 & eAttributeForAttribute.mask) != 0) {
                        set(eAttributeForAttribute, entry.getValue());
                    }
                } catch (ClassCastException e2) {
                }
            }
        }
        return this;
    }

    public AttributeValues merge(AttributeValues attributeValues) {
        return merge(attributeValues, MASK_ALL);
    }

    public AttributeValues merge(AttributeValues attributeValues, int i2) {
        int i3 = i2 & attributeValues.defined;
        for (EAttribute eAttribute : EAttribute.atts) {
            if (i3 == 0) {
                break;
            }
            if ((i3 & eAttribute.mask) != 0) {
                i3 &= eAttribute.mask ^ (-1);
                i_set(eAttribute, attributeValues);
                update(eAttribute);
            }
        }
        return this;
    }

    public static AttributeValues fromMap(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        return fromMap(map, MASK_ALL);
    }

    public static AttributeValues fromMap(Map<? extends AttributedCharacterIterator.Attribute, ?> map, int i2) {
        return new AttributeValues().merge(map, i2);
    }

    public Map<TextAttribute, Object> toMap(Map<TextAttribute, Object> map) {
        if (map == null) {
            map = new HashMap();
        }
        int i2 = this.defined;
        int i3 = 0;
        while (i2 != 0) {
            EAttribute eAttribute = EAttribute.atts[i3];
            if ((i2 & eAttribute.mask) != 0) {
                i2 &= eAttribute.mask ^ (-1);
                map.put(eAttribute.att, get(eAttribute));
            }
            i3++;
        }
        return map;
    }

    public static boolean is16Hashtable(Hashtable<Object, Object> hashtable) {
        return hashtable.containsKey(DEFINED_KEY);
    }

    public static AttributeValues fromSerializableHashtable(Hashtable<Object, Object> hashtable) {
        AttributeValues attributeValues = new AttributeValues();
        if (hashtable != null && !hashtable.isEmpty()) {
            for (Map.Entry<Object, Object> entry : hashtable.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key.equals(DEFINED_KEY)) {
                    attributeValues.defineAll(((Integer) value).intValue());
                } else {
                    try {
                        EAttribute eAttributeForAttribute = EAttribute.forAttribute((AttributedCharacterIterator.Attribute) key);
                        if (eAttributeForAttribute != null) {
                            attributeValues.set(eAttributeForAttribute, value);
                        }
                    } catch (ClassCastException e2) {
                    }
                }
            }
        }
        return attributeValues;
    }

    public Hashtable<Object, Object> toSerializableHashtable() {
        Hashtable<Object, Object> hashtable = new Hashtable<>();
        int i2 = this.defined;
        int i3 = this.defined;
        int i4 = 0;
        while (i3 != 0) {
            EAttribute eAttribute = EAttribute.atts[i4];
            if ((i3 & eAttribute.mask) != 0) {
                i3 &= eAttribute.mask ^ (-1);
                Object obj = get(eAttribute);
                if (obj != null) {
                    if (obj instanceof Serializable) {
                        hashtable.put(eAttribute.att, obj);
                    } else {
                        i2 &= eAttribute.mask ^ (-1);
                    }
                }
            }
            i4++;
        }
        hashtable.put(DEFINED_KEY, Integer.valueOf(i2));
        return hashtable;
    }

    public int hashCode() {
        return (this.defined << 8) ^ this.nondefault;
    }

    public boolean equals(Object obj) {
        try {
            return equals((AttributeValues) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public boolean equals(AttributeValues attributeValues) {
        if (attributeValues == null) {
            return false;
        }
        if (attributeValues == this) {
            return true;
        }
        return this.defined == attributeValues.defined && this.nondefault == attributeValues.nondefault && this.underline == attributeValues.underline && this.strikethrough == attributeValues.strikethrough && this.superscript == attributeValues.superscript && this.width == attributeValues.width && this.kerning == attributeValues.kerning && this.tracking == attributeValues.tracking && this.ligatures == attributeValues.ligatures && this.runDirection == attributeValues.runDirection && this.bidiEmbedding == attributeValues.bidiEmbedding && this.swapColors == attributeValues.swapColors && equals(this.transform, attributeValues.transform) && equals(this.foreground, attributeValues.foreground) && equals(this.background, attributeValues.background) && equals(this.numericShaping, attributeValues.numericShaping) && equals(Float.valueOf(this.justification), Float.valueOf(attributeValues.justification)) && equals(this.charReplacement, attributeValues.charReplacement) && this.size == attributeValues.size && this.weight == attributeValues.weight && this.posture == attributeValues.posture && equals(this.family, attributeValues.family) && equals(this.font, attributeValues.font) && this.imUnderline == attributeValues.imUnderline && equals(this.imHighlight, attributeValues.imHighlight);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public AttributeValues m5731clone() {
        try {
            AttributeValues attributeValues = (AttributeValues) super.clone();
            if (this.transform != null) {
                attributeValues.transform = new AffineTransform(this.transform);
                attributeValues.updateDerivedTransforms();
            }
            return attributeValues;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        int i2 = this.defined;
        int i3 = 0;
        while (i2 != 0) {
            EAttribute eAttribute = EAttribute.atts[i3];
            if ((i2 & eAttribute.mask) != 0) {
                i2 &= eAttribute.mask ^ (-1);
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append((Object) eAttribute);
                sb.append('=');
                switch (eAttribute) {
                    case EFAMILY:
                        sb.append('\"');
                        sb.append(this.family);
                        sb.append('\"');
                        break;
                    case EWEIGHT:
                        sb.append(this.weight);
                        break;
                    case EWIDTH:
                        sb.append(this.width);
                        break;
                    case EPOSTURE:
                        sb.append(this.posture);
                        break;
                    case ESIZE:
                        sb.append(this.size);
                        break;
                    case ETRANSFORM:
                        sb.append((Object) this.transform);
                        break;
                    case ESUPERSCRIPT:
                        sb.append((int) this.superscript);
                        break;
                    case EFONT:
                        sb.append((Object) this.font);
                        break;
                    case ECHAR_REPLACEMENT:
                        sb.append((Object) this.charReplacement);
                        break;
                    case EFOREGROUND:
                        sb.append((Object) this.foreground);
                        break;
                    case EBACKGROUND:
                        sb.append((Object) this.background);
                        break;
                    case EUNDERLINE:
                        sb.append((int) this.underline);
                        break;
                    case ESTRIKETHROUGH:
                        sb.append(this.strikethrough);
                        break;
                    case ERUN_DIRECTION:
                        sb.append((int) this.runDirection);
                        break;
                    case EBIDI_EMBEDDING:
                        sb.append((int) this.bidiEmbedding);
                        break;
                    case EJUSTIFICATION:
                        sb.append(this.justification);
                        break;
                    case EINPUT_METHOD_HIGHLIGHT:
                        sb.append(this.imHighlight);
                        break;
                    case EINPUT_METHOD_UNDERLINE:
                        sb.append((int) this.imUnderline);
                        break;
                    case ESWAP_COLORS:
                        sb.append(this.swapColors);
                        break;
                    case ENUMERIC_SHAPING:
                        sb.append((Object) this.numericShaping);
                        break;
                    case EKERNING:
                        sb.append((int) this.kerning);
                        break;
                    case ELIGATURES:
                        sb.append((int) this.ligatures);
                        break;
                    case ETRACKING:
                        sb.append(this.tracking);
                        break;
                    default:
                        throw new InternalError();
                }
                if ((this.nondefault & eAttribute.mask) == 0) {
                    sb.append('*');
                }
            }
            i3++;
        }
        sb.append("[btx=" + ((Object) this.baselineTransform) + ", ctx=" + ((Object) this.charTransform) + "]");
        sb.append('}');
        return sb.toString();
    }

    private static boolean equals(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    private void update(EAttribute eAttribute) {
        this.defined |= eAttribute.mask;
        if (i_validate(eAttribute)) {
            if (i_equals(eAttribute, DEFAULT)) {
                this.nondefault &= eAttribute.mask ^ (-1);
                return;
            } else {
                this.nondefault |= eAttribute.mask;
                return;
            }
        }
        setDefault(eAttribute);
    }

    private void i_set(EAttribute eAttribute, AttributeValues attributeValues) {
        switch (eAttribute) {
            case EFAMILY:
                this.family = attributeValues.family;
                return;
            case EWEIGHT:
                this.weight = attributeValues.weight;
                return;
            case EWIDTH:
                this.width = attributeValues.width;
                return;
            case EPOSTURE:
                this.posture = attributeValues.posture;
                return;
            case ESIZE:
                this.size = attributeValues.size;
                return;
            case ETRANSFORM:
                this.transform = attributeValues.transform;
                updateDerivedTransforms();
                return;
            case ESUPERSCRIPT:
                this.superscript = attributeValues.superscript;
                return;
            case EFONT:
                this.font = attributeValues.font;
                return;
            case ECHAR_REPLACEMENT:
                this.charReplacement = attributeValues.charReplacement;
                return;
            case EFOREGROUND:
                this.foreground = attributeValues.foreground;
                return;
            case EBACKGROUND:
                this.background = attributeValues.background;
                return;
            case EUNDERLINE:
                this.underline = attributeValues.underline;
                return;
            case ESTRIKETHROUGH:
                this.strikethrough = attributeValues.strikethrough;
                return;
            case ERUN_DIRECTION:
                this.runDirection = attributeValues.runDirection;
                return;
            case EBIDI_EMBEDDING:
                this.bidiEmbedding = attributeValues.bidiEmbedding;
                return;
            case EJUSTIFICATION:
                this.justification = attributeValues.justification;
                return;
            case EINPUT_METHOD_HIGHLIGHT:
                this.imHighlight = attributeValues.imHighlight;
                return;
            case EINPUT_METHOD_UNDERLINE:
                this.imUnderline = attributeValues.imUnderline;
                return;
            case ESWAP_COLORS:
                this.swapColors = attributeValues.swapColors;
                return;
            case ENUMERIC_SHAPING:
                this.numericShaping = attributeValues.numericShaping;
                return;
            case EKERNING:
                this.kerning = attributeValues.kerning;
                return;
            case ELIGATURES:
                this.ligatures = attributeValues.ligatures;
                return;
            case ETRACKING:
                this.tracking = attributeValues.tracking;
                return;
            default:
                throw new InternalError();
        }
    }

    private boolean i_equals(EAttribute eAttribute, AttributeValues attributeValues) {
        switch (eAttribute) {
            case EFAMILY:
                return equals(this.family, attributeValues.family);
            case EWEIGHT:
                return this.weight == attributeValues.weight;
            case EWIDTH:
                return this.width == attributeValues.width;
            case EPOSTURE:
                return this.posture == attributeValues.posture;
            case ESIZE:
                return this.size == attributeValues.size;
            case ETRANSFORM:
                return equals(this.transform, attributeValues.transform);
            case ESUPERSCRIPT:
                return this.superscript == attributeValues.superscript;
            case EFONT:
                return equals(this.font, attributeValues.font);
            case ECHAR_REPLACEMENT:
                return equals(this.charReplacement, attributeValues.charReplacement);
            case EFOREGROUND:
                return equals(this.foreground, attributeValues.foreground);
            case EBACKGROUND:
                return equals(this.background, attributeValues.background);
            case EUNDERLINE:
                return this.underline == attributeValues.underline;
            case ESTRIKETHROUGH:
                return this.strikethrough == attributeValues.strikethrough;
            case ERUN_DIRECTION:
                return this.runDirection == attributeValues.runDirection;
            case EBIDI_EMBEDDING:
                return this.bidiEmbedding == attributeValues.bidiEmbedding;
            case EJUSTIFICATION:
                return this.justification == attributeValues.justification;
            case EINPUT_METHOD_HIGHLIGHT:
                return equals(this.imHighlight, attributeValues.imHighlight);
            case EINPUT_METHOD_UNDERLINE:
                return this.imUnderline == attributeValues.imUnderline;
            case ESWAP_COLORS:
                return this.swapColors == attributeValues.swapColors;
            case ENUMERIC_SHAPING:
                return equals(this.numericShaping, attributeValues.numericShaping);
            case EKERNING:
                return this.kerning == attributeValues.kerning;
            case ELIGATURES:
                return this.ligatures == attributeValues.ligatures;
            case ETRACKING:
                return this.tracking == attributeValues.tracking;
            default:
                throw new InternalError();
        }
    }

    private void i_set(EAttribute eAttribute, Object obj) {
        switch (eAttribute) {
            case EFAMILY:
                this.family = ((String) obj).trim();
                return;
            case EWEIGHT:
                this.weight = ((Number) obj).floatValue();
                return;
            case EWIDTH:
                this.width = ((Number) obj).floatValue();
                return;
            case EPOSTURE:
                this.posture = ((Number) obj).floatValue();
                return;
            case ESIZE:
                this.size = ((Number) obj).floatValue();
                return;
            case ETRANSFORM:
                if (obj instanceof TransformAttribute) {
                    TransformAttribute transformAttribute = (TransformAttribute) obj;
                    if (transformAttribute.isIdentity()) {
                        this.transform = null;
                    } else {
                        this.transform = transformAttribute.getTransform();
                    }
                } else {
                    this.transform = new AffineTransform((AffineTransform) obj);
                }
                updateDerivedTransforms();
                return;
            case ESUPERSCRIPT:
                this.superscript = (byte) ((Integer) obj).intValue();
                return;
            case EFONT:
                this.font = (Font) obj;
                return;
            case ECHAR_REPLACEMENT:
                this.charReplacement = (GraphicAttribute) obj;
                return;
            case EFOREGROUND:
                this.foreground = (Paint) obj;
                return;
            case EBACKGROUND:
                this.background = (Paint) obj;
                return;
            case EUNDERLINE:
                this.underline = (byte) ((Integer) obj).intValue();
                return;
            case ESTRIKETHROUGH:
                this.strikethrough = ((Boolean) obj).booleanValue();
                return;
            case ERUN_DIRECTION:
                if (obj instanceof Boolean) {
                    this.runDirection = (byte) (TextAttribute.RUN_DIRECTION_LTR.equals(obj) ? 0 : 1);
                    return;
                } else {
                    this.runDirection = (byte) ((Integer) obj).intValue();
                    return;
                }
            case EBIDI_EMBEDDING:
                this.bidiEmbedding = (byte) ((Integer) obj).intValue();
                return;
            case EJUSTIFICATION:
                this.justification = ((Number) obj).floatValue();
                return;
            case EINPUT_METHOD_HIGHLIGHT:
                if (obj instanceof Annotation) {
                    this.imHighlight = (InputMethodHighlight) ((Annotation) obj).getValue();
                    return;
                } else {
                    this.imHighlight = (InputMethodHighlight) obj;
                    return;
                }
            case EINPUT_METHOD_UNDERLINE:
                this.imUnderline = (byte) ((Integer) obj).intValue();
                return;
            case ESWAP_COLORS:
                this.swapColors = ((Boolean) obj).booleanValue();
                return;
            case ENUMERIC_SHAPING:
                this.numericShaping = (NumericShaper) obj;
                return;
            case EKERNING:
                this.kerning = (byte) ((Integer) obj).intValue();
                return;
            case ELIGATURES:
                this.ligatures = (byte) ((Integer) obj).intValue();
                return;
            case ETRACKING:
                this.tracking = ((Number) obj).floatValue();
                return;
            default:
                throw new InternalError();
        }
    }

    private Object i_get(EAttribute eAttribute) {
        switch (eAttribute) {
            case EFAMILY:
                return this.family;
            case EWEIGHT:
                return Float.valueOf(this.weight);
            case EWIDTH:
                return Float.valueOf(this.width);
            case EPOSTURE:
                return Float.valueOf(this.posture);
            case ESIZE:
                return Float.valueOf(this.size);
            case ETRANSFORM:
                return this.transform == null ? TransformAttribute.IDENTITY : new TransformAttribute(this.transform);
            case ESUPERSCRIPT:
                return Integer.valueOf(this.superscript);
            case EFONT:
                return this.font;
            case ECHAR_REPLACEMENT:
                return this.charReplacement;
            case EFOREGROUND:
                return this.foreground;
            case EBACKGROUND:
                return this.background;
            case EUNDERLINE:
                return Integer.valueOf(this.underline);
            case ESTRIKETHROUGH:
                return Boolean.valueOf(this.strikethrough);
            case ERUN_DIRECTION:
                switch (this.runDirection) {
                    case 0:
                        return TextAttribute.RUN_DIRECTION_LTR;
                    case 1:
                        return TextAttribute.RUN_DIRECTION_RTL;
                    default:
                        return null;
                }
            case EBIDI_EMBEDDING:
                return Integer.valueOf(this.bidiEmbedding);
            case EJUSTIFICATION:
                return Float.valueOf(this.justification);
            case EINPUT_METHOD_HIGHLIGHT:
                return this.imHighlight;
            case EINPUT_METHOD_UNDERLINE:
                return Integer.valueOf(this.imUnderline);
            case ESWAP_COLORS:
                return Boolean.valueOf(this.swapColors);
            case ENUMERIC_SHAPING:
                return this.numericShaping;
            case EKERNING:
                return Integer.valueOf(this.kerning);
            case ELIGATURES:
                return Integer.valueOf(this.ligatures);
            case ETRACKING:
                return Float.valueOf(this.tracking);
            default:
                throw new InternalError();
        }
    }

    private boolean i_validate(EAttribute eAttribute) {
        switch (eAttribute) {
            case EFAMILY:
                if (this.family != null && this.family.length() != 0) {
                    return true;
                }
                this.family = DEFAULT.family;
                return true;
            case EWEIGHT:
                return this.weight > 0.0f && this.weight < 10.0f;
            case EWIDTH:
                return this.width >= 0.5f && this.width < 10.0f;
            case EPOSTURE:
                return this.posture >= -1.0f && this.posture <= 1.0f;
            case ESIZE:
                return this.size >= 0.0f;
            case ETRANSFORM:
                if (this.transform == null || !this.transform.isIdentity()) {
                    return true;
                }
                this.transform = DEFAULT.transform;
                return true;
            case ESUPERSCRIPT:
                return this.superscript >= -7 && this.superscript <= 7;
            case EFONT:
                return true;
            case ECHAR_REPLACEMENT:
                return true;
            case EFOREGROUND:
                return true;
            case EBACKGROUND:
                return true;
            case EUNDERLINE:
                return this.underline >= -1 && this.underline < 6;
            case ESTRIKETHROUGH:
                return true;
            case ERUN_DIRECTION:
                return this.runDirection >= -2 && this.runDirection <= 1;
            case EBIDI_EMBEDDING:
                return this.bidiEmbedding >= -61 && this.bidiEmbedding < 62;
            case EJUSTIFICATION:
                this.justification = Math.max(0.0f, Math.min(this.justification, 1.0f));
                return true;
            case EINPUT_METHOD_HIGHLIGHT:
                return true;
            case EINPUT_METHOD_UNDERLINE:
                return this.imUnderline >= -1 && this.imUnderline < 6;
            case ESWAP_COLORS:
                return true;
            case ENUMERIC_SHAPING:
                return true;
            case EKERNING:
                return this.kerning >= 0 && this.kerning <= 1;
            case ELIGATURES:
                return this.ligatures >= 0 && this.ligatures <= 1;
            case ETRACKING:
                return this.tracking >= -1.0f && this.tracking <= 10.0f;
            default:
                throw new InternalError("unknown attribute: " + ((Object) eAttribute));
        }
    }

    public static float getJustification(Map<?, ?> map) {
        if (map != null) {
            if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
                return ((AttributeMap) map).getValues().justification;
            }
            Object obj = map.get(TextAttribute.JUSTIFICATION);
            if (obj != null && (obj instanceof Number)) {
                return Math.max(0.0f, Math.min(1.0f, ((Number) obj).floatValue()));
            }
        }
        return DEFAULT.justification;
    }

    public static NumericShaper getNumericShaping(Map<?, ?> map) {
        if (map != null) {
            if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
                return ((AttributeMap) map).getValues().numericShaping;
            }
            Object obj = map.get(TextAttribute.NUMERIC_SHAPING);
            if (obj != null && (obj instanceof NumericShaper)) {
                return (NumericShaper) obj;
            }
        }
        return DEFAULT.numericShaping;
    }

    public AttributeValues applyIMHighlight() throws HeadlessException {
        InputMethodHighlight inputMethodHighlight;
        if (this.imHighlight != null) {
            if (this.imHighlight instanceof InputMethodHighlight) {
                inputMethodHighlight = (InputMethodHighlight) this.imHighlight;
            } else {
                inputMethodHighlight = (InputMethodHighlight) ((Annotation) this.imHighlight).getValue();
            }
            Map<TextAttribute, ?> style = inputMethodHighlight.getStyle();
            if (style == null) {
                style = Toolkit.getDefaultToolkit().mapInputMethodHighlight(inputMethodHighlight);
            }
            if (style != null) {
                return m5731clone().merge(style);
            }
        }
        return this;
    }

    public static AffineTransform getBaselineTransform(Map<?, ?> map) {
        if (map != null) {
            AttributeValues attributeValuesFromMap = null;
            if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
                attributeValuesFromMap = ((AttributeMap) map).getValues();
            } else if (map.get(TextAttribute.TRANSFORM) != null) {
                attributeValuesFromMap = fromMap(map);
            }
            if (attributeValuesFromMap != null) {
                return attributeValuesFromMap.baselineTransform;
            }
            return null;
        }
        return null;
    }

    public static AffineTransform getCharTransform(Map<?, ?> map) {
        if (map != null) {
            AttributeValues attributeValuesFromMap = null;
            if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
                attributeValuesFromMap = ((AttributeMap) map).getValues();
            } else if (map.get(TextAttribute.TRANSFORM) != null) {
                attributeValuesFromMap = fromMap(map);
            }
            if (attributeValuesFromMap != null) {
                return attributeValuesFromMap.charTransform;
            }
            return null;
        }
        return null;
    }

    public void updateDerivedTransforms() {
        if (this.transform == null) {
            this.baselineTransform = null;
            this.charTransform = null;
        } else {
            this.charTransform = new AffineTransform(this.transform);
            this.baselineTransform = extractXRotation(this.charTransform, true);
            if (this.charTransform.isIdentity()) {
                this.charTransform = null;
            }
            if (this.baselineTransform.isIdentity()) {
                this.baselineTransform = null;
            }
        }
        if (this.baselineTransform == null) {
            this.nondefault &= EAttribute.EBASELINE_TRANSFORM.mask ^ (-1);
        } else {
            this.nondefault |= EAttribute.EBASELINE_TRANSFORM.mask;
        }
    }

    public static AffineTransform extractXRotation(AffineTransform affineTransform, boolean z2) {
        return extractRotation(new Point2D.Double(1.0d, 0.0d), affineTransform, z2);
    }

    public static AffineTransform extractYRotation(AffineTransform affineTransform, boolean z2) {
        return extractRotation(new Point2D.Double(0.0d, 1.0d), affineTransform, z2);
    }

    private static AffineTransform extractRotation(Point2D.Double r14, AffineTransform affineTransform, boolean z2) {
        affineTransform.deltaTransform(r14, r14);
        AffineTransform rotateInstance = AffineTransform.getRotateInstance(r14.f12394x, r14.f12395y);
        try {
            AffineTransform affineTransformCreateInverse = rotateInstance.createInverse();
            double translateX = affineTransform.getTranslateX();
            double translateY = affineTransform.getTranslateY();
            affineTransform.preConcatenate(affineTransformCreateInverse);
            if (z2 && (translateX != 0.0d || translateY != 0.0d)) {
                affineTransform.setTransform(affineTransform.getScaleX(), affineTransform.getShearY(), affineTransform.getShearX(), affineTransform.getScaleY(), 0.0d, 0.0d);
                rotateInstance.setTransform(rotateInstance.getScaleX(), rotateInstance.getShearY(), rotateInstance.getShearX(), rotateInstance.getScaleY(), translateX, translateY);
            }
            return rotateInstance;
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }
}
