package javafx.print;

import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/print/JobSettings.class */
public final class JobSettings {
    private PrinterJob job;
    private Printer printer;
    private PrinterAttributes printerCaps;
    private int oldCopies;
    private PrintSides oldSides;
    private Collation oldCollation;
    private PrintColor oldPrintColor;
    private PrintQuality oldPrintQuality;
    private PrintResolution oldPrintResolution;
    private PaperSource oldPaperSource;
    private PageLayout oldPageLayout;
    private static final String DEFAULT_JOBNAME = "JavaFX Print Job";
    private SimpleStringProperty jobName;
    private IntegerProperty copies;
    private boolean defaultCopies = true;
    private boolean hasOldCopies = false;
    private boolean defaultSides = true;
    private boolean hasOldSides = false;
    private boolean defaultCollation = true;
    private boolean hasOldCollation = false;
    private boolean defaultPrintColor = true;
    private boolean hasOldPrintColor = false;
    private boolean defaultPrintQuality = true;
    private boolean hasOldPrintQuality = false;
    private boolean defaultPrintResolution = true;
    private boolean hasOldPrintResolution = false;
    private boolean defaultPaperSource = true;
    private boolean hasOldPaperSource = false;
    private boolean defaultPageLayout = true;
    private boolean hasOldPageLayout = false;
    private ObjectProperty<PageRange[]> pageRanges = null;
    private ObjectProperty<PrintSides> sides = null;
    private ObjectProperty<Collation> collation = null;
    private ObjectProperty<PrintColor> color = null;
    private ObjectProperty<PrintQuality> quality = null;
    private ObjectProperty<PrintResolution> resolution = null;
    private ObjectProperty<PaperSource> paperSource = null;
    private ObjectProperty<PageLayout> layout = null;

    JobSettings(Printer printer) {
        this.printer = printer;
        this.printerCaps = printer.getPrinterAttributes();
    }

    void setPrinterJob(PrinterJob job) {
        this.job = job;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isJobNew() {
        return this.job == null || this.job.isJobNew();
    }

    void updateForPrinter(Printer printer) {
        this.printer = printer;
        this.printerCaps = printer.getPrinterAttributes();
        if (this.defaultCopies) {
            if (getCopies() != this.printerCaps.getDefaultCopies()) {
                setCopies(this.printerCaps.getDefaultCopies());
                this.defaultCopies = true;
            }
        } else {
            int copies = getCopies();
            if (this.hasOldCopies && this.oldCopies > copies) {
                copies = this.oldCopies;
            }
            int maxCopies = this.printerCaps.getMaxCopies();
            if (!this.hasOldCopies && getCopies() > maxCopies) {
                this.hasOldCopies = true;
                this.oldCopies = getCopies();
            }
            if (copies > maxCopies) {
                copies = maxCopies;
            }
            setCopies(copies);
        }
        PrintSides currSides = getPrintSides();
        PrintSides defSides = this.printerCaps.getDefaultPrintSides();
        Set<PrintSides> suppSides = this.printerCaps.getSupportedPrintSides();
        if (this.defaultSides) {
            if (currSides != defSides) {
                setPrintSides(defSides);
                this.defaultSides = true;
            }
        } else if (this.hasOldSides) {
            if (suppSides.contains(this.oldSides)) {
                setPrintSides(this.oldSides);
                this.hasOldSides = false;
            } else {
                setPrintSides(defSides);
            }
        } else if (!suppSides.contains(currSides)) {
            this.hasOldSides = true;
            this.oldSides = currSides;
            setPrintSides(defSides);
        }
        Collation currColl = getCollation();
        Collation defColl = this.printerCaps.getDefaultCollation();
        Set<Collation> suppColl = this.printerCaps.getSupportedCollations();
        if (this.defaultCollation) {
            if (currColl != defColl) {
                setCollation(defColl);
                this.defaultCollation = true;
            }
        } else if (this.hasOldCollation) {
            if (suppColl.contains(this.oldCollation)) {
                setCollation(this.oldCollation);
                this.hasOldCollation = false;
            } else {
                setCollation(defColl);
            }
        } else if (!suppColl.contains(currColl)) {
            this.hasOldCollation = true;
            this.oldCollation = currColl;
            setCollation(defColl);
        }
        PrintColor currColor = getPrintColor();
        PrintColor defColor = this.printerCaps.getDefaultPrintColor();
        Set<PrintColor> suppColors = this.printerCaps.getSupportedPrintColors();
        if (this.defaultPrintColor) {
            if (currColor != defColor) {
                setPrintColor(defColor);
                this.defaultPrintColor = true;
            }
        } else if (this.hasOldPrintColor) {
            if (suppColors.contains(this.oldPrintColor)) {
                setPrintColor(this.oldPrintColor);
                this.hasOldPrintColor = false;
            } else {
                setPrintColor(defColor);
            }
        } else if (!suppColors.contains(currColor)) {
            this.hasOldPrintColor = true;
            this.oldPrintColor = currColor;
            setPrintColor(defColor);
        }
        PrintQuality currQuality = getPrintQuality();
        PrintQuality defQuality = this.printerCaps.getDefaultPrintQuality();
        Set<PrintQuality> suppQuality = this.printerCaps.getSupportedPrintQuality();
        if (this.defaultPrintQuality) {
            if (currQuality != defQuality) {
                setPrintQuality(defQuality);
                this.defaultPrintQuality = true;
            }
        } else if (this.hasOldPrintQuality) {
            if (suppQuality.contains(this.oldPrintQuality)) {
                setPrintQuality(this.oldPrintQuality);
                this.hasOldPrintQuality = false;
            } else {
                setPrintQuality(defQuality);
            }
        } else if (!suppQuality.contains(currQuality)) {
            this.hasOldPrintQuality = true;
            this.oldPrintQuality = currQuality;
            setPrintQuality(defQuality);
        }
        PrintResolution currRes = getPrintResolution();
        PrintResolution defResolution = this.printerCaps.getDefaultPrintResolution();
        Set<PrintResolution> suppRes = this.printerCaps.getSupportedPrintResolutions();
        if (this.defaultPrintResolution) {
            if (currRes != defResolution) {
                setPrintResolution(defResolution);
                this.defaultPrintResolution = true;
            }
        } else if (this.hasOldPrintResolution) {
            if (suppRes.contains(this.oldPrintResolution)) {
                setPrintResolution(this.oldPrintResolution);
                this.hasOldPrintResolution = false;
            } else {
                setPrintResolution(defResolution);
            }
        } else if (!suppRes.contains(currRes)) {
            this.hasOldPrintResolution = true;
            this.oldPrintResolution = currRes;
            setPrintResolution(defResolution);
        }
        PaperSource currSource = getPaperSource();
        PaperSource defSource = this.printerCaps.getDefaultPaperSource();
        Set<PaperSource> suppSources = this.printerCaps.getSupportedPaperSources();
        if (this.defaultPaperSource) {
            if (currSource != defSource) {
                setPaperSource(defSource);
                this.defaultPaperSource = true;
            }
        } else if (this.hasOldPaperSource) {
            if (suppSources.contains(this.oldPaperSource)) {
                setPaperSource(this.oldPaperSource);
                this.hasOldPaperSource = false;
            } else {
                setPaperSource(defSource);
            }
        } else if (!suppSources.contains(currSource)) {
            this.hasOldPaperSource = true;
            this.oldPaperSource = currSource;
            setPaperSource(defSource);
        }
        PageLayout currPageLayout = getPageLayout();
        PageLayout defPageLayout = printer.getDefaultPageLayout();
        if (this.defaultPageLayout) {
            if (!currPageLayout.equals(defPageLayout)) {
                setPageLayout(defPageLayout);
                this.defaultPageLayout = true;
                return;
            }
            return;
        }
        if (this.hasOldPageLayout) {
            PageLayout valPageLayout = this.job.validatePageLayout(this.oldPageLayout);
            if (valPageLayout.equals(this.oldPageLayout)) {
                setPageLayout(this.oldPageLayout);
                this.hasOldPageLayout = false;
                return;
            } else {
                setPageLayout(defPageLayout);
                return;
            }
        }
        PageLayout valPageLayout2 = this.job.validatePageLayout(currPageLayout);
        if (!valPageLayout2.equals(currPageLayout)) {
            this.hasOldPageLayout = true;
            this.oldPageLayout = currPageLayout;
            setPageLayout(defPageLayout);
        }
    }

    public final StringProperty jobNameProperty() {
        if (this.jobName == null) {
            this.jobName = new SimpleStringProperty(this, "jobName", DEFAULT_JOBNAME) { // from class: javafx.print.JobSettings.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(String value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        value = JobSettings.DEFAULT_JOBNAME;
                    }
                    super.set(value);
                }

                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends String> rawObservable) {
                    throw new RuntimeException("Jobname property cannot be bound");
                }

                @Override // javafx.beans.property.StringProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<String> other) {
                    throw new RuntimeException("Jobname property cannot be bound");
                }
            };
        }
        return this.jobName;
    }

    public String getJobName() {
        return jobNameProperty().get();
    }

    public void setJobName(String name) {
        jobNameProperty().set(name);
    }

    public final IntegerProperty copiesProperty() {
        if (this.copies == null) {
            this.copies = new SimpleIntegerProperty(this, "copies", this.printerCaps.getDefaultCopies()) { // from class: javafx.print.JobSettings.2
                @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.value.WritableIntegerValue
                public void set(int value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value <= 0) {
                        if (JobSettings.this.defaultCopies) {
                            return;
                        }
                        super.set(JobSettings.this.printerCaps.getDefaultCopies());
                        JobSettings.this.defaultCopies = true;
                        return;
                    }
                    super.set(value);
                    JobSettings.this.defaultCopies = false;
                }

                @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends Number> rawObservable) {
                    throw new RuntimeException("Copies property cannot be bound");
                }

                @Override // javafx.beans.property.IntegerProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<Number> other) {
                    throw new RuntimeException("Copies property cannot be bound");
                }
            };
        }
        return this.copies;
    }

    public int getCopies() {
        return copiesProperty().get();
    }

    public final void setCopies(int nCopies) {
        copiesProperty().set(nCopies);
    }

    public final ObjectProperty pageRangesProperty() {
        if (this.pageRanges == null) {
            this.pageRanges = new SimpleObjectProperty(this, "pageRanges", null) { // from class: javafx.print.JobSettings.3
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(Object o2) {
                    try {
                        set((PageRange[]) o2);
                    } catch (ClassCastException e2) {
                    }
                }

                public void set(PageRange[] value) {
                    PageRange[] value2;
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null || value.length == 0 || value[0] == null) {
                        value2 = null;
                    } else {
                        int len = value.length;
                        PageRange[] arr = new PageRange[len];
                        int curr = 0;
                        for (int i2 = 0; i2 < len; i2++) {
                            PageRange r2 = value[i2];
                            if (r2 == null || curr >= r2.getStartPage()) {
                                return;
                            }
                            curr = r2.getEndPage();
                            arr[i2] = r2;
                        }
                        value2 = arr;
                    }
                    super.set((AnonymousClass3) value2);
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue rawObservable) {
                    throw new RuntimeException("PageRanges property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property other) {
                    throw new RuntimeException("PageRanges property cannot be bound");
                }
            };
        }
        return this.pageRanges;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PageRange[] getPageRanges() {
        return (PageRange[]) pageRangesProperty().get();
    }

    public void setPageRanges(PageRange... pages) {
        pageRangesProperty().set(pages);
    }

    public final ObjectProperty<PrintSides> printSidesProperty() {
        if (this.sides == null) {
            this.sides = new SimpleObjectProperty<PrintSides>(this, "printSides", this.printerCaps.getDefaultPrintSides()) { // from class: javafx.print.JobSettings.4
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PrintSides value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultSides) {
                            return;
                        }
                        super.set((AnonymousClass4) JobSettings.this.printerCaps.getDefaultPrintSides());
                        JobSettings.this.defaultSides = true;
                    }
                    if (JobSettings.this.printerCaps.getSupportedPrintSides().contains(value)) {
                        super.set((AnonymousClass4) value);
                        JobSettings.this.defaultSides = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PrintSides> rawObservable) {
                    throw new RuntimeException("PrintSides property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PrintSides> other) {
                    throw new RuntimeException("PrintSides property cannot be bound");
                }
            };
        }
        return this.sides;
    }

    public PrintSides getPrintSides() {
        return printSidesProperty().get();
    }

    public void setPrintSides(PrintSides sides) {
        if (sides == getPrintSides()) {
            return;
        }
        printSidesProperty().set(sides);
    }

    public final ObjectProperty<Collation> collationProperty() {
        if (this.collation == null) {
            Collation coll = this.printerCaps.getDefaultCollation();
            this.collation = new SimpleObjectProperty<Collation>(this, "collation", coll) { // from class: javafx.print.JobSettings.5
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(Collation value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultCollation) {
                            return;
                        }
                        super.set((AnonymousClass5) JobSettings.this.printerCaps.getDefaultCollation());
                        JobSettings.this.defaultCollation = true;
                        return;
                    }
                    if (JobSettings.this.printerCaps.getSupportedCollations().contains(value)) {
                        super.set((AnonymousClass5) value);
                        JobSettings.this.defaultCollation = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends Collation> rawObservable) {
                    throw new RuntimeException("Collation property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<Collation> other) {
                    throw new RuntimeException("Collation property cannot be bound");
                }
            };
        }
        return this.collation;
    }

    public Collation getCollation() {
        return collationProperty().get();
    }

    public void setCollation(Collation collation) {
        if (collation == getCollation()) {
            return;
        }
        collationProperty().set(collation);
    }

    public final ObjectProperty<PrintColor> printColorProperty() {
        if (this.color == null) {
            this.color = new SimpleObjectProperty<PrintColor>(this, "printColor", this.printerCaps.getDefaultPrintColor()) { // from class: javafx.print.JobSettings.6
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PrintColor value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultPrintColor) {
                            return;
                        }
                        super.set((AnonymousClass6) JobSettings.this.printerCaps.getDefaultPrintColor());
                        JobSettings.this.defaultPrintColor = true;
                    }
                    if (JobSettings.this.printerCaps.getSupportedPrintColors().contains(value)) {
                        super.set((AnonymousClass6) value);
                        JobSettings.this.defaultPrintColor = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PrintColor> rawObservable) {
                    throw new RuntimeException("PrintColor property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PrintColor> other) {
                    throw new RuntimeException("PrintColor property cannot be bound");
                }
            };
        }
        return this.color;
    }

    public PrintColor getPrintColor() {
        return printColorProperty().get();
    }

    public void setPrintColor(PrintColor color) {
        if (color == getPrintColor()) {
            return;
        }
        printColorProperty().set(color);
    }

    public final ObjectProperty<PrintQuality> printQualityProperty() {
        if (this.quality == null) {
            this.quality = new SimpleObjectProperty<PrintQuality>(this, "printQuality", this.printerCaps.getDefaultPrintQuality()) { // from class: javafx.print.JobSettings.7
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PrintQuality value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultPrintQuality) {
                            return;
                        }
                        super.set((AnonymousClass7) JobSettings.this.printerCaps.getDefaultPrintQuality());
                        JobSettings.this.defaultPrintQuality = true;
                    }
                    if (JobSettings.this.printerCaps.getSupportedPrintQuality().contains(value)) {
                        super.set((AnonymousClass7) value);
                        JobSettings.this.defaultPrintQuality = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PrintQuality> rawObservable) {
                    throw new RuntimeException("PrintQuality property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PrintQuality> other) {
                    throw new RuntimeException("PrintQuality property cannot be bound");
                }
            };
        }
        return this.quality;
    }

    public PrintQuality getPrintQuality() {
        return printQualityProperty().get();
    }

    public void setPrintQuality(PrintQuality quality) {
        if (quality == getPrintQuality()) {
            return;
        }
        printQualityProperty().set(quality);
    }

    public final ObjectProperty<PrintResolution> printResolutionProperty() {
        if (this.resolution == null) {
            this.resolution = new SimpleObjectProperty<PrintResolution>(this, "printResolution", this.printerCaps.getDefaultPrintResolution()) { // from class: javafx.print.JobSettings.8
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PrintResolution value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultPrintResolution) {
                            return;
                        }
                        super.set((AnonymousClass8) JobSettings.this.printerCaps.getDefaultPrintResolution());
                        JobSettings.this.defaultPrintResolution = true;
                    }
                    if (JobSettings.this.printerCaps.getSupportedPrintResolutions().contains(value)) {
                        super.set((AnonymousClass8) value);
                        JobSettings.this.defaultPrintResolution = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PrintResolution> rawObservable) {
                    throw new RuntimeException("PrintResolution property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PrintResolution> other) {
                    throw new RuntimeException("PrintResolution property cannot be bound");
                }
            };
        }
        return this.resolution;
    }

    public PrintResolution getPrintResolution() {
        return printResolutionProperty().get();
    }

    public void setPrintResolution(PrintResolution resolution) {
        if (resolution == null || resolution == getPrintResolution()) {
            return;
        }
        printResolutionProperty().set(resolution);
    }

    public final ObjectProperty<PaperSource> paperSourceProperty() {
        if (this.paperSource == null) {
            this.paperSource = new SimpleObjectProperty<PaperSource>(this, "paperSource", this.printerCaps.getDefaultPaperSource()) { // from class: javafx.print.JobSettings.9
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PaperSource value) {
                    if (!JobSettings.this.isJobNew()) {
                        return;
                    }
                    if (value == null) {
                        if (JobSettings.this.defaultPaperSource) {
                            return;
                        }
                        super.set((AnonymousClass9) JobSettings.this.printerCaps.getDefaultPaperSource());
                        JobSettings.this.defaultPaperSource = true;
                    }
                    if (JobSettings.this.printerCaps.getSupportedPaperSources().contains(value)) {
                        super.set((AnonymousClass9) value);
                        JobSettings.this.defaultPaperSource = false;
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PaperSource> rawObservable) {
                    throw new RuntimeException("PaperSource property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PaperSource> other) {
                    throw new RuntimeException("PaperSource property cannot be bound");
                }
            };
        }
        return this.paperSource;
    }

    public PaperSource getPaperSource() {
        return paperSourceProperty().get();
    }

    public void setPaperSource(PaperSource value) {
        paperSourceProperty().set(value);
    }

    public final ObjectProperty<PageLayout> pageLayoutProperty() {
        if (this.layout == null) {
            this.layout = new SimpleObjectProperty<PageLayout>(this, "pageLayout", this.printer.getDefaultPageLayout()) { // from class: javafx.print.JobSettings.10
                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(PageLayout value) {
                    if (JobSettings.this.isJobNew() && value != null) {
                        JobSettings.this.defaultPageLayout = false;
                        super.set((AnonymousClass10) value);
                    }
                }

                @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
                public void bind(ObservableValue<? extends PageLayout> rawObservable) {
                    throw new RuntimeException("PageLayout property cannot be bound");
                }

                @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
                public void bindBidirectional(Property<PageLayout> other) {
                    throw new RuntimeException("PageLayout property cannot be bound");
                }
            };
        }
        return this.layout;
    }

    public PageLayout getPageLayout() {
        return pageLayoutProperty().get();
    }

    public void setPageLayout(PageLayout pageLayout) {
        pageLayoutProperty().set(pageLayout);
    }

    public String toString() {
        String nl = System.lineSeparator();
        return " Collation = " + ((Object) getCollation()) + nl + " Copies = " + getCopies() + nl + " Sides = " + ((Object) getPrintSides()) + nl + " JobName = " + getJobName() + nl + " Page ranges = " + ((Object) getPageRanges()) + nl + " Print color = " + ((Object) getPrintColor()) + nl + " Print quality = " + ((Object) getPrintQuality()) + nl + " Print resolution = " + ((Object) getPrintResolution()) + nl + " Paper source = " + ((Object) getPaperSource()) + nl + " Page layout = " + ((Object) getPageLayout());
    }
}
