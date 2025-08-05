package org.icepdf.core.pobjects;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontFactory;
import org.icepdf.core.pobjects.graphics.ExtGState;
import org.icepdf.core.pobjects.graphics.PColorSpace;
import org.icepdf.core.pobjects.graphics.Pattern;
import org.icepdf.core.pobjects.graphics.ShadingPattern;
import org.icepdf.core.pobjects.graphics.TilingPattern;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Resources.class */
public class Resources extends Dictionary {
    public static final Name COLORSPACE_KEY = new Name(PdfOps.CS_NAME);
    public static final Name FONT_KEY = new Name("Font");
    public static final Name XOBJECT_KEY = new Name("XObject");
    public static final Name PATTERN_KEY = new Name("Pattern");
    public static final Name SHADING_KEY = new Name("Shading");
    public static final Name EXTGSTATE_KEY = new Name("ExtGState");
    public static final Name PROPERTIES_KEY = new Name("Properties");
    private static int uniqueCounter = 0;
    private static final Logger logger = Logger.getLogger(Resources.class.toString());
    HashMap fonts;
    HashMap xobjects;
    HashMap colorspaces;
    HashMap patterns;
    HashMap shading;
    HashMap extGStates;
    HashMap properties;

    private static synchronized int getUniqueId() {
        int i2 = uniqueCounter;
        uniqueCounter = i2 + 1;
        return i2;
    }

    public Resources(Library l2, HashMap h2) {
        super(l2, h2);
        this.colorspaces = this.library.getDictionary(this.entries, COLORSPACE_KEY);
        this.fonts = this.library.getDictionary(this.entries, FONT_KEY);
        this.xobjects = this.library.getDictionary(this.entries, XOBJECT_KEY);
        this.patterns = this.library.getDictionary(this.entries, PATTERN_KEY);
        this.shading = this.library.getDictionary(this.entries, SHADING_KEY);
        this.extGStates = this.library.getDictionary(this.entries, EXTGSTATE_KEY);
        this.properties = this.library.getDictionary(this.entries, PROPERTIES_KEY);
    }

    public PColorSpace getColorSpace(Object o2) {
        if (o2 == null) {
            return null;
        }
        if (this.colorspaces != null && this.colorspaces.get(o2) != null) {
            Object tmp = this.colorspaces.get(o2);
            PColorSpace cs = PColorSpace.getColorSpace(this.library, tmp);
            if (cs != null) {
                cs.init();
            }
            return cs;
        }
        if (this.patterns != null && this.patterns.get(o2) != null) {
            Object tmp2 = this.patterns.get(o2);
            PColorSpace cs2 = PColorSpace.getColorSpace(this.library, tmp2);
            if (cs2 != null) {
                cs2.init();
            }
            return cs2;
        }
        PColorSpace cs3 = PColorSpace.getColorSpace(this.library, o2);
        if (cs3 != null) {
            cs3.init();
        }
        return cs3;
    }

    public Font getFont(Name s2) {
        Font font = null;
        if (this.fonts != null) {
            Object ob = this.fonts.get(s2);
            if (ob instanceof Font) {
                font = (Font) ob;
            } else if (ob instanceof Reference) {
                Reference ref = (Reference) ob;
                Object ob2 = this.library.getObject((Reference) ob);
                if (ob2 instanceof PObject) {
                    ob2 = ((PObject) ob2).getObject();
                }
                if (ob2 instanceof Font) {
                    font = (Font) ob2;
                } else {
                    font = FontFactory.getInstance().getFont(this.library, (HashMap) ob2);
                }
                this.library.addObject(font, ref);
                font.setPObjectReference(ref);
            }
            if (font == null) {
                Iterator i$ = this.fonts.values().iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    Object tmp = i$.next();
                    if (tmp instanceof Reference) {
                        Object ob3 = this.library.getObject((Reference) tmp);
                        if (ob3 instanceof PObject) {
                            ob3 = ((PObject) ob3).getObject();
                        }
                        if (ob3 instanceof Font) {
                            font = (Font) ob3;
                            if (s2.getName().equals(font.getBaseFont())) {
                                this.library.addObject(font, (Reference) tmp);
                                font.setPObjectReference((Reference) tmp);
                                break;
                            }
                            font = null;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        if (font != null) {
            try {
                font.setParentResource(this);
                font.init();
            } catch (Exception e2) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Error initializing font, falling back to font substitution.");
                } else {
                    logger.log(Level.FINER, "Error initializing font, falling back to font substitution. " + ((Object) font));
                }
            }
        }
        return font;
    }

    public Image getImage(Name s2, Color fill) {
        ImageStream st = (ImageStream) this.library.getObject(this.xobjects, s2);
        if (st == null || !st.isImageSubtype()) {
            return null;
        }
        Image image = null;
        try {
            image = st.getImage(fill, this);
        } catch (Exception e2) {
            logger.log(Level.FINE, "Error getting image by name: " + ((Object) s2), (Throwable) e2);
        }
        return image;
    }

    public ImageStream getImageStream(Name s2) {
        Object st = this.library.getObject(this.xobjects, s2);
        if (st instanceof ImageStream) {
            return (ImageStream) st;
        }
        return null;
    }

    public int getImageCount() {
        int count = 0;
        if (this.xobjects != null) {
            for (Object tmp : this.xobjects.values()) {
                if ((tmp instanceof Reference) && (this.library.getObject((Reference) tmp) instanceof ImageStream)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isForm(Name s2) {
        Object o2 = this.library.getObject(this.xobjects, s2);
        return o2 instanceof Form;
    }

    public Form getForm(Name nameReference) {
        Form formXObject = null;
        Object tempForm = this.library.getObject(this.xobjects, nameReference);
        if (tempForm instanceof Form) {
            formXObject = (Form) tempForm;
        }
        return formXObject;
    }

    public Pattern getPattern(Name name) {
        if (this.patterns != null) {
            Object attribute = this.library.getObject(this.patterns, name);
            if (attribute != null && (attribute instanceof TilingPattern)) {
                return (TilingPattern) attribute;
            }
            if (attribute != null && (attribute instanceof Stream)) {
                return new TilingPattern((Stream) attribute);
            }
            if (attribute != null && (attribute instanceof HashMap)) {
                return ShadingPattern.getShadingPattern(this.library, (HashMap) attribute);
            }
            return null;
        }
        return null;
    }

    public ShadingPattern getShading(Name name) {
        Object shadingDictionary;
        if (this.shading != null && (shadingDictionary = this.library.getObject(this.shading, name)) != null && (shadingDictionary instanceof HashMap)) {
            return ShadingPattern.getShadingPattern(this.library, this.entries, (HashMap) shadingDictionary);
        }
        return null;
    }

    public ExtGState getExtGState(Name namedReference) {
        ExtGState gsState = null;
        if (this.extGStates != null) {
            Object attribute = this.library.getObject(this.extGStates, namedReference);
            if (attribute instanceof HashMap) {
                gsState = new ExtGState(this.library, (HashMap) attribute);
            } else if (attribute instanceof Reference) {
                gsState = new ExtGState(this.library, (HashMap) this.library.getObject((Reference) attribute));
            }
        }
        return gsState;
    }

    public OptionalContents getPropertyEntry(Name key) {
        if (this.properties != null) {
            return (OptionalContents) this.library.getObject(this.properties.get(key));
        }
        return null;
    }

    public boolean isShading() {
        return this.shading != null;
    }
}
