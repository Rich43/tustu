package sun.print;

import java.io.OutputStream;
import java.util.Locale;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.ServiceUIFactory;
import javax.print.StreamPrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintServiceAttributeListener;

/* loaded from: rt.jar:sun/print/PSStreamPrintService.class */
public class PSStreamPrintService extends StreamPrintService implements SunPrinterJobService {
    private static final Class[] suppAttrCats = {Chromaticity.class, Copies.class, Fidelity.class, JobName.class, Media.class, MediaPrintableArea.class, OrientationRequested.class, PageRanges.class, RequestingUserName.class, SheetCollate.class, Sides.class};
    private static int MAXCOPIES = 1000;
    private static final MediaSizeName[] mediaSizes = {MediaSizeName.NA_LETTER, MediaSizeName.TABLOID, MediaSizeName.LEDGER, MediaSizeName.NA_LEGAL, MediaSizeName.EXECUTIVE, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5};

    public PSStreamPrintService(OutputStream outputStream) {
        super(outputStream);
    }

    @Override // javax.print.StreamPrintService
    public String getOutputFormat() {
        return "application/postscript";
    }

    @Override // javax.print.PrintService
    public DocFlavor[] getSupportedDocFlavors() {
        return PSStreamPrinterFactory.getFlavors();
    }

    @Override // javax.print.PrintService
    public DocPrintJob createPrintJob() {
        return new PSStreamPrintJob(this);
    }

    @Override // sun.print.SunPrinterJobService
    public boolean usesClass(Class cls) {
        return cls == PSPrinterJob.class;
    }

    @Override // javax.print.PrintService
    public String getName() {
        return "Postscript output";
    }

    @Override // javax.print.PrintService
    public void addPrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener) {
    }

    @Override // javax.print.PrintService
    public void removePrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener) {
    }

    @Override // javax.print.PrintService
    public <T extends PrintServiceAttribute> T getAttribute(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("category");
        }
        if (!PrintServiceAttribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Not a PrintServiceAttribute");
        }
        if (cls == ColorSupported.class) {
            return ColorSupported.SUPPORTED;
        }
        return null;
    }

    @Override // javax.print.PrintService
    public PrintServiceAttributeSet getAttributes() {
        HashPrintServiceAttributeSet hashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
        hashPrintServiceAttributeSet.add(ColorSupported.SUPPORTED);
        return AttributeSetUtilities.unmodifiableView((PrintServiceAttributeSet) hashPrintServiceAttributeSet);
    }

    @Override // javax.print.PrintService
    public boolean isDocFlavorSupported(DocFlavor docFlavor) {
        for (DocFlavor docFlavor2 : getSupportedDocFlavors()) {
            if (docFlavor.equals(docFlavor2)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.print.PrintService
    public Class<?>[] getSupportedAttributeCategories() {
        Class<?>[] clsArr = new Class[suppAttrCats.length];
        System.arraycopy(suppAttrCats, 0, clsArr, 0, clsArr.length);
        return clsArr;
    }

    @Override // javax.print.PrintService
    public boolean isAttributeCategorySupported(Class<? extends Attribute> cls) {
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " is not an Attribute");
        }
        for (int i2 = 0; i2 < suppAttrCats.length; i2++) {
            if (cls == suppAttrCats[i2]) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.print.PrintService
    public Object getDefaultAttributeValue(Class<? extends Attribute> cls) {
        float x2;
        float y2;
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " is not an Attribute");
        }
        if (!isAttributeCategorySupported(cls)) {
            return null;
        }
        if (cls == Copies.class) {
            return new Copies(1);
        }
        if (cls == Chromaticity.class) {
            return Chromaticity.COLOR;
        }
        if (cls == Fidelity.class) {
            return Fidelity.FIDELITY_FALSE;
        }
        if (cls == Media.class) {
            String country = Locale.getDefault().getCountry();
            if (country != null && (country.equals("") || country.equals(Locale.US.getCountry()) || country.equals(Locale.CANADA.getCountry()))) {
                return MediaSizeName.NA_LETTER;
            }
            return MediaSizeName.ISO_A4;
        }
        if (cls == MediaPrintableArea.class) {
            String country2 = Locale.getDefault().getCountry();
            if (country2 == null || (!country2.equals("") && !country2.equals(Locale.US.getCountry()) && !country2.equals(Locale.CANADA.getCountry()))) {
                x2 = MediaSize.ISO.A4.getX(25400) - (2.0f * 0.5f);
                y2 = MediaSize.ISO.A4.getY(25400) - (2.0f * 0.5f);
            } else {
                x2 = MediaSize.NA.LETTER.getX(25400) - (2.0f * 0.5f);
                y2 = MediaSize.NA.LETTER.getY(25400) - (2.0f * 0.5f);
            }
            return new MediaPrintableArea(0.5f, 0.5f, x2, y2, 25400);
        }
        if (cls == OrientationRequested.class) {
            return OrientationRequested.PORTRAIT;
        }
        if (cls == PageRanges.class) {
            return new PageRanges(1, Integer.MAX_VALUE);
        }
        if (cls == SheetCollate.class) {
            return SheetCollate.UNCOLLATED;
        }
        if (cls == Sides.class) {
            return Sides.ONE_SIDED;
        }
        return null;
    }

    @Override // javax.print.PrintService
    public Object getSupportedAttributeValues(Class<? extends Attribute> cls, DocFlavor docFlavor, AttributeSet attributeSet) {
        Media media;
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " does not implement Attribute");
        }
        if (docFlavor != null && !isDocFlavorSupported(docFlavor)) {
            throw new IllegalArgumentException(((Object) docFlavor) + " is an unsupported flavor");
        }
        if (!isAttributeCategorySupported(cls)) {
            return null;
        }
        if (cls == Chromaticity.class) {
            return new Chromaticity[]{Chromaticity.COLOR};
        }
        if (cls == JobName.class) {
            return new JobName("", null);
        }
        if (cls == RequestingUserName.class) {
            return new RequestingUserName("", null);
        }
        if (cls == OrientationRequested.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new OrientationRequested[]{OrientationRequested.PORTRAIT, OrientationRequested.LANDSCAPE, OrientationRequested.REVERSE_LANDSCAPE};
            }
            return null;
        }
        if (cls == Copies.class || cls == CopiesSupported.class) {
            return new CopiesSupported(1, MAXCOPIES);
        }
        if (cls == Media.class) {
            Media[] mediaArr = new Media[mediaSizes.length];
            System.arraycopy(mediaSizes, 0, mediaArr, 0, mediaSizes.length);
            return mediaArr;
        }
        if (cls == Fidelity.class) {
            return new Fidelity[]{Fidelity.FIDELITY_FALSE, Fidelity.FIDELITY_TRUE};
        }
        if (cls == MediaPrintableArea.class) {
            if (attributeSet == null) {
                return null;
            }
            MediaSize mediaSizeForName = (MediaSize) attributeSet.get(MediaSize.class);
            if (mediaSizeForName == null && (media = (Media) attributeSet.get(Media.class)) != null && (media instanceof MediaSizeName)) {
                mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media);
            }
            if (mediaSizeForName == null) {
                return null;
            }
            MediaPrintableArea[] mediaPrintableAreaArr = new MediaPrintableArea[1];
            float x2 = mediaSizeForName.getX(25400);
            float y2 = mediaSizeForName.getY(25400);
            float f2 = 0.5f;
            float f3 = 0.5f;
            if (x2 < 5.0f) {
                f2 = x2 / 10.0f;
            }
            if (y2 < 5.0f) {
                f3 = y2 / 10.0f;
            }
            mediaPrintableAreaArr[0] = new MediaPrintableArea(f2, f3, x2 - (2.0f * f2), y2 - (2.0f * f3), 25400);
            return mediaPrintableAreaArr;
        }
        if (cls == PageRanges.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new PageRanges[]{new PageRanges(1, Integer.MAX_VALUE)};
            }
            return null;
        }
        if (cls == SheetCollate.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new SheetCollate[]{SheetCollate.UNCOLLATED, SheetCollate.COLLATED};
            }
            return new SheetCollate[]{SheetCollate.UNCOLLATED};
        }
        if (cls == Sides.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new Sides[]{Sides.ONE_SIDED, Sides.TWO_SIDED_LONG_EDGE, Sides.TWO_SIDED_SHORT_EDGE};
            }
            return null;
        }
        return null;
    }

    private boolean isSupportedCopies(Copies copies) {
        int value = copies.getValue();
        return value > 0 && value < MAXCOPIES;
    }

    private boolean isSupportedMedia(MediaSizeName mediaSizeName) {
        for (int i2 = 0; i2 < mediaSizes.length; i2++) {
            if (mediaSizeName.equals(mediaSizes[i2])) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.print.PrintService
    public boolean isAttributeValueSupported(Attribute attribute, DocFlavor docFlavor, AttributeSet attributeSet) {
        if (attribute == null) {
            throw new NullPointerException("null attribute");
        }
        if (docFlavor != null && !isDocFlavorSupported(docFlavor)) {
            throw new IllegalArgumentException(((Object) docFlavor) + " is an unsupported flavor");
        }
        if (!isAttributeCategorySupported(attribute.getCategory())) {
            return false;
        }
        if (attribute.getCategory() == Chromaticity.class) {
            return attribute == Chromaticity.COLOR;
        }
        if (attribute.getCategory() == Copies.class) {
            return isSupportedCopies((Copies) attribute);
        }
        if (attribute.getCategory() == Media.class && (attribute instanceof MediaSizeName)) {
            return isSupportedMedia((MediaSizeName) attribute);
        }
        if (attribute.getCategory() == OrientationRequested.class) {
            if (attribute != OrientationRequested.REVERSE_PORTRAIT) {
                if (docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        if (attribute.getCategory() == PageRanges.class) {
            if (docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return false;
            }
            return true;
        }
        if (attribute.getCategory() == SheetCollate.class) {
            if (docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return false;
            }
            return true;
        }
        if (attribute.getCategory() == Sides.class && docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
            return false;
        }
        return true;
    }

    @Override // javax.print.PrintService
    public AttributeSet getUnsupportedAttributes(DocFlavor docFlavor, AttributeSet attributeSet) {
        if (docFlavor != null && !isDocFlavorSupported(docFlavor)) {
            throw new IllegalArgumentException("flavor " + ((Object) docFlavor) + "is not supported");
        }
        if (attributeSet == null) {
            return null;
        }
        HashAttributeSet hashAttributeSet = new HashAttributeSet();
        Attribute[] array = attributeSet.toArray();
        for (int i2 = 0; i2 < array.length; i2++) {
            try {
                Attribute attribute = array[i2];
                if (!isAttributeCategorySupported(attribute.getCategory()) || !isAttributeValueSupported(attribute, docFlavor, attributeSet)) {
                    hashAttributeSet.add(attribute);
                }
            } catch (ClassCastException e2) {
            }
        }
        if (hashAttributeSet.isEmpty()) {
            return null;
        }
        return hashAttributeSet;
    }

    @Override // javax.print.PrintService
    public ServiceUIFactory getServiceUIFactory() {
        return null;
    }

    public String toString() {
        return "PSStreamPrintService: " + getName();
    }

    @Override // javax.print.PrintService
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof PSStreamPrintService) && ((PSStreamPrintService) obj).getName().equals(getName()));
    }

    @Override // javax.print.PrintService
    public int hashCode() {
        return getClass().hashCode() + getName().hashCode();
    }
}
