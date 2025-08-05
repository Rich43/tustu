package org.icepdf.core.pobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Destination.class */
public class Destination {
    private static final Logger logger = Logger.getLogger(Destination.class.toString());
    public static final Name D_KEY = new Name(PdfOps.D_TOKEN);
    public static final Name TYPE_XYZ = new Name("XYZ");
    public static final Name TYPE_FIT = new Name("Fit");
    public static final Name TYPE_FITH = new Name("FitH");
    public static final Name TYPE_FITV = new Name("FitV");
    public static final Name TYPE_FITR = new Name("FitR");
    public static final Name TYPE_FITB = new Name("FitB");
    public static final Name TYPE_FITBH = new Name("FitBH");
    public static final Name TYPE_FITBV = new Name("FitBV");
    private Library library;
    private Object object;
    private Reference ref;
    private Name type;
    private Float left = null;
    private Float bottom = null;
    private Float right = null;
    private Float top = null;
    private Float zoom = null;
    private Name namedDestination;
    private boolean inited;

    public Destination(Library l2, Object h2) {
        this.library = l2;
        this.object = h2;
        init();
    }

    private void init() {
        String s2;
        Dictionary dests;
        Object o2;
        if (this.inited) {
            return;
        }
        this.inited = true;
        if (this.object instanceof List) {
            parse((List) this.object);
            return;
        }
        if ((this.object instanceof Name) || (this.object instanceof StringObject)) {
            if (this.object instanceof StringObject) {
                StringObject stringObject = (StringObject) this.object;
                s2 = stringObject.getDecryptedLiteralString(this.library.securityManager);
            } else {
                s2 = this.object.toString();
            }
            this.namedDestination = new Name(s2);
            boolean found = false;
            Catalog catalog = this.library.getCatalog();
            if (catalog != null && catalog.getNames() != null) {
                NameTree nameTree = catalog.getNames().getDestsNameTree();
                if (nameTree != null && (o2 = nameTree.searchName(s2)) != null) {
                    if (o2 instanceof List) {
                        parse((List) o2);
                        found = true;
                    } else if (o2 instanceof HashMap) {
                        HashMap h2 = (HashMap) o2;
                        Object o1 = h2.get(D_KEY);
                        if (o1 instanceof List) {
                            parse((List) o1);
                            found = true;
                        }
                    }
                }
                if (!found && (dests = catalog.getDestinations()) != null) {
                    Object ob = dests.getObject((Name) this.object);
                    if (ob instanceof List) {
                        parse((List) ob);
                    } else if (ob instanceof HashMap) {
                        parse((List) ((HashMap) ob).get(D_KEY));
                    } else if (logger.isLoggable(Level.FINE)) {
                        logger.warning("Destination type missed=" + ob);
                    }
                }
            }
        }
    }

    public Object getObject() {
        return this.object;
    }

    private void parse(List v2) {
        Object ob;
        Object ob2 = v2.get(0);
        if (ob2 instanceof Reference) {
            this.ref = (Reference) ob2;
        }
        Object ob3 = v2.get(1);
        if (ob3 instanceof Name) {
            this.type = (Name) ob3;
        } else {
            this.type = new Name(ob3.toString());
        }
        if (this.type.equals(TYPE_XYZ)) {
            Object ob4 = v2.get(2);
            if (ob4 != null && !ob4.equals(FXMLLoader.NULL_KEYWORD)) {
                this.left = Float.valueOf(((Number) ob4).floatValue());
            }
            Object ob5 = v2.get(3);
            if (ob5 != null && !ob5.equals(FXMLLoader.NULL_KEYWORD)) {
                this.top = Float.valueOf(((Number) ob5).floatValue());
            }
            Object ob6 = v2.get(4);
            if (ob6 != null && !ob6.equals(FXMLLoader.NULL_KEYWORD) && !ob6.equals("0")) {
                this.zoom = Float.valueOf(((Number) ob6).floatValue());
                return;
            }
            return;
        }
        if (this.type.equals(TYPE_FITH)) {
            Object ob7 = v2.get(2);
            if (ob7 != null && !ob7.equals(FXMLLoader.NULL_KEYWORD)) {
                this.top = Float.valueOf(((Number) ob7).floatValue());
                return;
            }
            return;
        }
        if (this.type.equals(TYPE_FITR)) {
            Object ob8 = v2.get(2);
            if (ob8 != null && !ob8.equals(FXMLLoader.NULL_KEYWORD)) {
                this.left = Float.valueOf(((Number) ob8).floatValue());
            }
            Object ob9 = v2.get(3);
            if (ob9 != null && !ob9.equals(FXMLLoader.NULL_KEYWORD)) {
                this.bottom = Float.valueOf(((Number) ob9).floatValue());
            }
            Object ob10 = v2.get(4);
            if (ob10 != null && !ob10.equals(FXMLLoader.NULL_KEYWORD)) {
                this.right = Float.valueOf(((Number) ob10).floatValue());
            }
            Object ob11 = v2.get(5);
            if (ob11 != null && !ob11.equals(FXMLLoader.NULL_KEYWORD)) {
                this.top = Float.valueOf(((Number) ob11).floatValue());
                return;
            }
            return;
        }
        if (!this.type.equals(TYPE_FITB)) {
            if (this.type.equals(TYPE_FITBH)) {
                Object ob12 = v2.get(2);
                if (ob12 != null && !ob12.equals(FXMLLoader.NULL_KEYWORD)) {
                    this.top = Float.valueOf(((Number) ob12).floatValue());
                    return;
                }
                return;
            }
            if (this.type.equals(TYPE_FITBV) && (ob = v2.get(2)) != null && !ob.equals(FXMLLoader.NULL_KEYWORD)) {
                this.left = Float.valueOf(((Number) ob).floatValue());
            }
        }
    }

    public Name getNamedDestination() {
        return this.namedDestination;
    }

    public void setNamedDestination(Name dest) {
        this.namedDestination = dest;
        this.object = dest;
        this.inited = false;
        init();
    }

    public void setDestinationSyntax(List destinationSyntax) {
        this.namedDestination = null;
        this.object = destinationSyntax;
        this.inited = false;
        init();
    }

    public static List<Object> destinationSyntax(Reference page, Name type) {
        List<Object> destSyntax = new ArrayList<>(2);
        destSyntax.add(page);
        destSyntax.add(type);
        return destSyntax;
    }

    public static List<Object> destinationSyntax(Reference page, Name type, Object offset) {
        List<Object> destSyntax = new ArrayList<>(3);
        destSyntax.add(page);
        destSyntax.add(type);
        destSyntax.add(offset);
        return destSyntax;
    }

    public static List<Object> destinationSyntax(Reference page, Object type, Object left, Object top, Object zoom) {
        List<Object> destSyntax = new ArrayList<>(5);
        destSyntax.add(page);
        destSyntax.add(type);
        destSyntax.add(left);
        destSyntax.add(top);
        destSyntax.add(zoom);
        return destSyntax;
    }

    public static List<Object> destinationSyntax(Reference page, Object type, Object left, Object bottom, Object right, Object top) {
        List<Object> destSyntax = new ArrayList<>(6);
        destSyntax.add(page);
        destSyntax.add(type);
        destSyntax.add(left);
        destSyntax.add(bottom);
        destSyntax.add(right);
        destSyntax.add(top);
        return destSyntax;
    }

    public Reference getPageReference() {
        return this.ref;
    }

    public Float getLeft() {
        return this.left;
    }

    public Float getTop() {
        return this.top;
    }

    public Float getZoom() {
        return this.zoom;
    }

    public Reference getRef() {
        return this.ref;
    }

    public Name getType() {
        return this.type;
    }

    public Float getBottom() {
        return this.bottom;
    }

    public Float getRight() {
        return this.right;
    }

    public Object getEncodedDestination() {
        if (this.namedDestination != null) {
            return this.namedDestination;
        }
        if (this.object instanceof List) {
            List<Object> v2 = new ArrayList<>(7);
            if (this.ref != null) {
                v2.add(this.ref);
            }
            if (this.type != null) {
                v2.add(this.type);
            }
            if (this.left.floatValue() != Float.NaN) {
                v2.add(this.left);
            }
            if (this.bottom.floatValue() != Float.NaN) {
                v2.add(this.bottom);
            }
            if (this.right.floatValue() != Float.NaN) {
                v2.add(this.right);
            }
            if (this.top.floatValue() != Float.NaN) {
                v2.add(this.top);
            }
            if (this.zoom.floatValue() != Float.NaN) {
                v2.add(this.zoom);
            }
            return v2;
        }
        return null;
    }

    public String toString() {
        return "Destination  ref: " + ((Object) getPageReference()) + " ,  top: " + ((Object) getTop()) + " ,  left: " + ((Object) getLeft()) + " ,  zoom: " + ((Object) getZoom());
    }
}
