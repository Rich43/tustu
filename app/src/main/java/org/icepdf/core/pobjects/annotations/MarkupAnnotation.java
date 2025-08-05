package org.icepdf.core.pobjects.annotations;

import java.util.HashMap;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/MarkupAnnotation.class */
public abstract class MarkupAnnotation extends Annotation {
    public static final Name T_KEY = new Name("T");
    public static final Name CA_KEY = new Name("CA");
    public static final Name RC_KEY = new Name("RC");
    public static final Name CREATION_DATE_KEY = new Name("CreationDate");
    public static final Name IRT_KEY = new Name("IRT");
    public static final Name SUBJ_KEY = new Name("Subj");
    public static final Name POPUP_KEY = new Name("Popup");
    public static final Name RT_KEY = new Name("RT");
    public static final Name IT_KEY = new Name("IT");
    public static final Name EX_DATA_KEY = new Name("ExData");
    protected String titleText;
    protected PopupAnnotation popupAnnotation;
    protected float opacity;
    protected String richText;
    protected PDate creationDate;
    protected MarkupAnnotation inReplyToAnnotation;
    protected String subject;
    protected Name replyToRelation;
    protected Name intent;

    public MarkupAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
        this.opacity = 1.0f;
        this.replyToRelation = new Name("R");
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        Object value = this.library.getObject(this.entries, T_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            this.titleText = text.getDecryptedLiteralString(this.securityManager);
        } else if (value instanceof String) {
            this.titleText = (String) value;
        }
        Object value2 = this.library.getObject(this.entries, POPUP_KEY);
        if (value2 != null && (value2 instanceof PopupAnnotation)) {
            this.popupAnnotation = (PopupAnnotation) value2;
        }
        float ca = this.library.getFloat(this.entries, CA_KEY);
        if (ca != 0.0f) {
            this.opacity = ca;
        }
        Object value3 = this.library.getObject(this.entries, RC_KEY);
        if (value3 != null && (value3 instanceof StringObject)) {
            StringObject text2 = (StringObject) value3;
            this.richText = text2.getDecryptedLiteralString(this.securityManager);
        } else if (value3 instanceof String) {
            this.richText = (String) value3;
        }
        Object value4 = this.library.getObject(this.entries, CREATION_DATE_KEY);
        if (value4 != null && (value4 instanceof StringObject)) {
            StringObject text3 = (StringObject) value4;
            this.creationDate = new PDate(this.securityManager, text3.getDecryptedLiteralString(this.securityManager));
        }
        Object value5 = this.library.getObject(this.entries, IRT_KEY);
        if (value5 != null && (value5 instanceof MarkupAnnotation)) {
            this.inReplyToAnnotation = (MarkupAnnotation) value5;
        }
        Object value6 = this.library.getObject(this.entries, SUBJ_KEY);
        if (value6 != null && (value6 instanceof StringObject)) {
            StringObject text4 = (StringObject) value6;
            this.subject = text4.getDecryptedLiteralString(this.securityManager);
        } else if (value6 instanceof String) {
            this.subject = (String) value6;
        }
        Object value7 = this.library.getName(this.entries, RT_KEY);
        if (value7 != null) {
            this.replyToRelation = (Name) value7;
        }
        Object value8 = this.library.getName(this.entries, IT_KEY);
        if (value8 != null) {
            this.intent = (Name) value8;
        }
    }

    public String getTitleText() {
        return this.titleText;
    }

    public PopupAnnotation getPopupAnnotation() {
        return this.popupAnnotation;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public String getRichText() {
        return this.richText;
    }

    public PDate getCreationDate() {
        return this.creationDate;
    }

    public MarkupAnnotation getInReplyToAnnotation() {
        return this.inReplyToAnnotation;
    }

    public String getSubject() {
        return this.subject;
    }

    public Name getReplyToRelation() {
        return this.replyToRelation;
    }

    public Name getIntent() {
        return this.intent;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        this.entries.put(T_KEY, new LiteralStringObject(titleText));
    }

    public void setPopupAnnotation(PopupAnnotation popupAnnotation) {
        this.popupAnnotation = popupAnnotation;
        this.entries.put(POPUP_KEY, popupAnnotation.getPObjectReference());
    }

    public void setRichText(String richText) {
        this.richText = richText;
        this.entries.put(RC_KEY, new LiteralStringObject(richText));
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = new PDate(this.securityManager, creationDate);
        this.entries.put(CREATION_DATE_KEY, new LiteralStringObject(creationDate));
    }

    public void setInReplyToAnnotation(MarkupAnnotation inReplyToAnnotation) {
        this.inReplyToAnnotation = inReplyToAnnotation;
        this.entries.put(IRT_KEY, inReplyToAnnotation.getPObjectReference());
    }

    public void setSubject(String subject) {
        this.subject = subject;
        this.entries.put(SUBTYPE_KEY, new LiteralStringObject(subject));
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return getTitleText();
    }
}
