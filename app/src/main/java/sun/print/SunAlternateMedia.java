package sun.print;

import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.standard.Media;

/* loaded from: rt.jar:sun/print/SunAlternateMedia.class */
public class SunAlternateMedia implements PrintRequestAttribute {
    private static final long serialVersionUID = -8878868345472850201L;
    private Media media;

    public SunAlternateMedia(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return this.media;
    }

    @Override // javax.print.attribute.Attribute
    public final Class getCategory() {
        return SunAlternateMedia.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "sun-alternate-media";
    }

    public String toString() {
        return "alternate-media: " + this.media.toString();
    }

    public int hashCode() {
        return this.media.hashCode();
    }
}
