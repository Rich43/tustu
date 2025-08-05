package java.awt;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:java/awt/JobAttributes.class */
public final class JobAttributes implements Cloneable {
    private int copies;
    private DefaultSelectionType defaultSelection;
    private DestinationType destination;
    private DialogType dialog;
    private String fileName;
    private int fromPage;
    private int maxPage;
    private int minPage;
    private MultipleDocumentHandlingType multipleDocumentHandling;
    private int[][] pageRanges;
    private int prFirst;
    private int prLast;
    private String printer;
    private SidesType sides;
    private int toPage;

    /* loaded from: rt.jar:java/awt/JobAttributes$DefaultSelectionType.class */
    public static final class DefaultSelectionType extends AttributeValue {
        private static final int I_ALL = 0;
        private static final int I_RANGE = 1;
        private static final int I_SELECTION = 2;
        private static final String[] NAMES = {"all", AsmConstants.CODERANGE, "selection"};
        public static final DefaultSelectionType ALL = new DefaultSelectionType(0);
        public static final DefaultSelectionType RANGE = new DefaultSelectionType(1);
        public static final DefaultSelectionType SELECTION = new DefaultSelectionType(2);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private DefaultSelectionType(int i2) {
            super(i2, NAMES);
        }
    }

    /* loaded from: rt.jar:java/awt/JobAttributes$DestinationType.class */
    public static final class DestinationType extends AttributeValue {
        private static final int I_FILE = 0;
        private static final int I_PRINTER = 1;
        private static final String[] NAMES = {DeploymentDescriptorParser.ATTR_FILE, "printer"};
        public static final DestinationType FILE = new DestinationType(0);
        public static final DestinationType PRINTER = new DestinationType(1);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private DestinationType(int i2) {
            super(i2, NAMES);
        }
    }

    /* loaded from: rt.jar:java/awt/JobAttributes$DialogType.class */
    public static final class DialogType extends AttributeValue {
        private static final int I_COMMON = 0;
        private static final int I_NATIVE = 1;
        private static final int I_NONE = 2;
        private static final String[] NAMES = {"common", "native", Separation.COLORANT_NONE};
        public static final DialogType COMMON = new DialogType(0);
        public static final DialogType NATIVE = new DialogType(1);
        public static final DialogType NONE = new DialogType(2);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private DialogType(int i2) {
            super(i2, NAMES);
        }
    }

    /* loaded from: rt.jar:java/awt/JobAttributes$MultipleDocumentHandlingType.class */
    public static final class MultipleDocumentHandlingType extends AttributeValue {
        private static final int I_SEPARATE_DOCUMENTS_COLLATED_COPIES = 0;
        private static final int I_SEPARATE_DOCUMENTS_UNCOLLATED_COPIES = 1;
        private static final String[] NAMES = {"separate-documents-collated-copies", "separate-documents-uncollated-copies"};
        public static final MultipleDocumentHandlingType SEPARATE_DOCUMENTS_COLLATED_COPIES = new MultipleDocumentHandlingType(0);
        public static final MultipleDocumentHandlingType SEPARATE_DOCUMENTS_UNCOLLATED_COPIES = new MultipleDocumentHandlingType(1);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private MultipleDocumentHandlingType(int i2) {
            super(i2, NAMES);
        }
    }

    /* loaded from: rt.jar:java/awt/JobAttributes$SidesType.class */
    public static final class SidesType extends AttributeValue {
        private static final int I_ONE_SIDED = 0;
        private static final int I_TWO_SIDED_LONG_EDGE = 1;
        private static final int I_TWO_SIDED_SHORT_EDGE = 2;
        private static final String[] NAMES = {"one-sided", "two-sided-long-edge", "two-sided-short-edge"};
        public static final SidesType ONE_SIDED = new SidesType(0);
        public static final SidesType TWO_SIDED_LONG_EDGE = new SidesType(1);
        public static final SidesType TWO_SIDED_SHORT_EDGE = new SidesType(2);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private SidesType(int i2) {
            super(i2, NAMES);
        }
    }

    public JobAttributes() {
        setCopiesToDefault();
        setDefaultSelection(DefaultSelectionType.ALL);
        setDestination(DestinationType.PRINTER);
        setDialog(DialogType.NATIVE);
        setMaxPage(Integer.MAX_VALUE);
        setMinPage(1);
        setMultipleDocumentHandlingToDefault();
        setSidesToDefault();
    }

    public JobAttributes(JobAttributes jobAttributes) {
        set(jobAttributes);
    }

    public JobAttributes(int i2, DefaultSelectionType defaultSelectionType, DestinationType destinationType, DialogType dialogType, String str, int i3, int i4, MultipleDocumentHandlingType multipleDocumentHandlingType, int[][] iArr, String str2, SidesType sidesType) {
        setCopies(i2);
        setDefaultSelection(defaultSelectionType);
        setDestination(destinationType);
        setDialog(dialogType);
        setFileName(str);
        setMaxPage(i3);
        setMinPage(i4);
        setMultipleDocumentHandling(multipleDocumentHandlingType);
        setPageRanges(iArr);
        setPrinter(str2);
        setSides(sidesType);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public void set(JobAttributes jobAttributes) {
        this.copies = jobAttributes.copies;
        this.defaultSelection = jobAttributes.defaultSelection;
        this.destination = jobAttributes.destination;
        this.dialog = jobAttributes.dialog;
        this.fileName = jobAttributes.fileName;
        this.fromPage = jobAttributes.fromPage;
        this.maxPage = jobAttributes.maxPage;
        this.minPage = jobAttributes.minPage;
        this.multipleDocumentHandling = jobAttributes.multipleDocumentHandling;
        this.pageRanges = jobAttributes.pageRanges;
        this.prFirst = jobAttributes.prFirst;
        this.prLast = jobAttributes.prLast;
        this.printer = jobAttributes.printer;
        this.sides = jobAttributes.sides;
        this.toPage = jobAttributes.toPage;
    }

    public int getCopies() {
        return this.copies;
    }

    public void setCopies(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Invalid value for attribute copies");
        }
        this.copies = i2;
    }

    public void setCopiesToDefault() {
        setCopies(1);
    }

    public DefaultSelectionType getDefaultSelection() {
        return this.defaultSelection;
    }

    public void setDefaultSelection(DefaultSelectionType defaultSelectionType) {
        if (defaultSelectionType == null) {
            throw new IllegalArgumentException("Invalid value for attribute defaultSelection");
        }
        this.defaultSelection = defaultSelectionType;
    }

    public DestinationType getDestination() {
        return this.destination;
    }

    public void setDestination(DestinationType destinationType) {
        if (destinationType == null) {
            throw new IllegalArgumentException("Invalid value for attribute destination");
        }
        this.destination = destinationType;
    }

    public DialogType getDialog() {
        return this.dialog;
    }

    public void setDialog(DialogType dialogType) {
        if (dialogType == null) {
            throw new IllegalArgumentException("Invalid value for attribute dialog");
        }
        this.dialog = dialogType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public int getFromPage() {
        if (this.fromPage != 0) {
            return this.fromPage;
        }
        if (this.toPage != 0) {
            return getMinPage();
        }
        if (this.pageRanges != null) {
            return this.prFirst;
        }
        return getMinPage();
    }

    public void setFromPage(int i2) {
        if (i2 <= 0 || ((this.toPage != 0 && i2 > this.toPage) || i2 < this.minPage || i2 > this.maxPage)) {
            throw new IllegalArgumentException("Invalid value for attribute fromPage");
        }
        this.fromPage = i2;
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public void setMaxPage(int i2) {
        if (i2 <= 0 || i2 < this.minPage) {
            throw new IllegalArgumentException("Invalid value for attribute maxPage");
        }
        this.maxPage = i2;
    }

    public int getMinPage() {
        return this.minPage;
    }

    public void setMinPage(int i2) {
        if (i2 <= 0 || i2 > this.maxPage) {
            throw new IllegalArgumentException("Invalid value for attribute minPage");
        }
        this.minPage = i2;
    }

    public MultipleDocumentHandlingType getMultipleDocumentHandling() {
        return this.multipleDocumentHandling;
    }

    public void setMultipleDocumentHandling(MultipleDocumentHandlingType multipleDocumentHandlingType) {
        if (multipleDocumentHandlingType == null) {
            throw new IllegalArgumentException("Invalid value for attribute multipleDocumentHandling");
        }
        this.multipleDocumentHandling = multipleDocumentHandlingType;
    }

    public void setMultipleDocumentHandlingToDefault() {
        setMultipleDocumentHandling(MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_UNCOLLATED_COPIES);
    }

    /* JADX WARN: Type inference failed for: r0v15, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v9, types: [int[], int[][]] */
    public int[][] getPageRanges() {
        if (this.pageRanges != null) {
            int[][] iArr = new int[this.pageRanges.length][2];
            for (int i2 = 0; i2 < this.pageRanges.length; i2++) {
                iArr[i2][0] = this.pageRanges[i2][0];
                iArr[i2][1] = this.pageRanges[i2][1];
            }
            return iArr;
        }
        if (this.fromPage != 0 || this.toPage != 0) {
            return new int[]{new int[]{getFromPage(), getToPage()}};
        }
        int minPage = getMinPage();
        return new int[]{new int[]{minPage, minPage}};
    }

    public void setPageRanges(int[][] iArr) {
        int i2 = 0;
        int i3 = 0;
        if (iArr == null) {
            throw new IllegalArgumentException("Invalid value for attribute pageRanges");
        }
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (iArr[i4] == null || iArr[i4].length != 2 || iArr[i4][0] <= i3 || iArr[i4][1] < iArr[i4][0]) {
                throw new IllegalArgumentException("Invalid value for attribute pageRanges");
            }
            i3 = iArr[i4][1];
            if (i2 == 0) {
                i2 = iArr[i4][0];
            }
        }
        if (i2 < this.minPage || i3 > this.maxPage) {
            throw new IllegalArgumentException("Invalid value for attribute pageRanges");
        }
        int[][] iArr2 = new int[iArr.length][2];
        for (int i5 = 0; i5 < iArr.length; i5++) {
            iArr2[i5][0] = iArr[i5][0];
            iArr2[i5][1] = iArr[i5][1];
        }
        this.pageRanges = iArr2;
        this.prFirst = i2;
        this.prLast = i3;
    }

    public String getPrinter() {
        return this.printer;
    }

    public void setPrinter(String str) {
        this.printer = str;
    }

    public SidesType getSides() {
        return this.sides;
    }

    public void setSides(SidesType sidesType) {
        if (sidesType == null) {
            throw new IllegalArgumentException("Invalid value for attribute sides");
        }
        this.sides = sidesType;
    }

    public void setSidesToDefault() {
        setSides(SidesType.ONE_SIDED);
    }

    public int getToPage() {
        if (this.toPage != 0) {
            return this.toPage;
        }
        if (this.fromPage != 0) {
            return this.fromPage;
        }
        if (this.pageRanges != null) {
            return this.prLast;
        }
        return getMinPage();
    }

    public void setToPage(int i2) {
        if (i2 <= 0 || ((this.fromPage != 0 && i2 < this.fromPage) || i2 < this.minPage || i2 > this.maxPage)) {
            throw new IllegalArgumentException("Invalid value for attribute toPage");
        }
        this.toPage = i2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JobAttributes)) {
            return false;
        }
        JobAttributes jobAttributes = (JobAttributes) obj;
        if (this.fileName == null) {
            if (jobAttributes.fileName != null) {
                return false;
            }
        } else if (!this.fileName.equals(jobAttributes.fileName)) {
            return false;
        }
        if (this.pageRanges == null) {
            if (jobAttributes.pageRanges != null) {
                return false;
            }
        } else {
            if (jobAttributes.pageRanges == null || this.pageRanges.length != jobAttributes.pageRanges.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.pageRanges.length; i2++) {
                if (this.pageRanges[i2][0] != jobAttributes.pageRanges[i2][0] || this.pageRanges[i2][1] != jobAttributes.pageRanges[i2][1]) {
                    return false;
                }
            }
        }
        if (this.printer == null) {
            if (jobAttributes.printer != null) {
                return false;
            }
        } else if (!this.printer.equals(jobAttributes.printer)) {
            return false;
        }
        return this.copies == jobAttributes.copies && this.defaultSelection == jobAttributes.defaultSelection && this.destination == jobAttributes.destination && this.dialog == jobAttributes.dialog && this.fromPage == jobAttributes.fromPage && this.maxPage == jobAttributes.maxPage && this.minPage == jobAttributes.minPage && this.multipleDocumentHandling == jobAttributes.multipleDocumentHandling && this.prFirst == jobAttributes.prFirst && this.prLast == jobAttributes.prLast && this.sides == jobAttributes.sides && this.toPage == jobAttributes.toPage;
    }

    public int hashCode() {
        int iHashCode = (((((((this.copies + this.fromPage) + this.maxPage) + this.minPage) + this.prFirst) + this.prLast) + this.toPage) * 31) << 21;
        if (this.pageRanges != null) {
            int i2 = 0;
            for (int i3 = 0; i3 < this.pageRanges.length; i3++) {
                i2 += this.pageRanges[i3][0] + this.pageRanges[i3][1];
            }
            iHashCode ^= (i2 * 31) << 11;
        }
        if (this.fileName != null) {
            iHashCode ^= this.fileName.hashCode();
        }
        if (this.printer != null) {
            iHashCode ^= this.printer.hashCode();
        }
        return (((((this.defaultSelection.hashCode() << 6) ^ (this.destination.hashCode() << 5)) ^ (this.dialog.hashCode() << 3)) ^ (this.multipleDocumentHandling.hashCode() << 2)) ^ this.sides.hashCode()) ^ iHashCode;
    }

    public String toString() {
        int[][] pageRanges = getPageRanges();
        String str = "[";
        boolean z2 = true;
        for (int i2 = 0; i2 < pageRanges.length; i2++) {
            if (z2) {
                z2 = false;
            } else {
                str = str + ",";
            }
            str = str + pageRanges[i2][0] + CallSiteDescriptor.TOKEN_DELIMITER + pageRanges[i2][1];
        }
        return "copies=" + getCopies() + ",defaultSelection=" + ((Object) getDefaultSelection()) + ",destination=" + ((Object) getDestination()) + ",dialog=" + ((Object) getDialog()) + ",fileName=" + getFileName() + ",fromPage=" + getFromPage() + ",maxPage=" + getMaxPage() + ",minPage=" + getMinPage() + ",multiple-document-handling=" + ((Object) getMultipleDocumentHandling()) + ",page-ranges=" + (str + "]") + ",printer=" + getPrinter() + ",sides=" + ((Object) getSides()) + ",toPage=" + getToPage();
    }
}
