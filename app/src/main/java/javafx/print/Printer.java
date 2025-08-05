package javafx.print;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import com.sun.javafx.tk.PrintPipeline;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableSet;
import javafx.geometry.Rectangle2D;

/* loaded from: jfxrt.jar:javafx/print/Printer.class */
public final class Printer {
    private static ReadOnlyObjectWrapper<Printer> defaultPrinter;
    private PrinterImpl impl;
    private PrinterAttributes attributes;
    private PageLayout defPageLayout;

    /* loaded from: jfxrt.jar:javafx/print/Printer$MarginType.class */
    public enum MarginType {
        DEFAULT,
        HARDWARE_MINIMUM,
        EQUAL,
        EQUAL_OPPOSITES
    }

    public static ObservableSet<Printer> getAllPrinters() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPrintJobAccess();
        }
        return PrintPipeline.getPrintPipeline().getAllPrinters();
    }

    private static ReadOnlyObjectWrapper<Printer> defaultPrinterImpl() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPrintJobAccess();
        }
        if (defaultPrinter == null) {
            Printer p2 = PrintPipeline.getPrintPipeline().getDefaultPrinter();
            defaultPrinter = new ReadOnlyObjectWrapper<>(null, "defaultPrinter", p2);
        }
        return defaultPrinter;
    }

    public static ReadOnlyObjectProperty<Printer> defaultPrinterProperty() {
        return defaultPrinterImpl().getReadOnlyProperty();
    }

    public static Printer getDefaultPrinter() {
        return defaultPrinterProperty().get();
    }

    Printer(PrinterImpl impl) {
        this.impl = impl;
        impl.setPrinter(this);
    }

    PrinterImpl getPrinterImpl() {
        return this.impl;
    }

    public String getName() {
        return this.impl.getName();
    }

    public PrinterAttributes getPrinterAttributes() {
        if (this.attributes == null) {
            this.attributes = new PrinterAttributes(this.impl);
        }
        return this.attributes;
    }

    JobSettings getDefaultJobSettings() {
        return this.impl.getDefaultJobSettings();
    }

    public PageLayout getDefaultPageLayout() {
        if (this.defPageLayout == null) {
            PrinterAttributes printerCaps = getPrinterAttributes();
            this.defPageLayout = createPageLayout(printerCaps.getDefaultPaper(), printerCaps.getDefaultPageOrientation(), MarginType.DEFAULT);
        }
        return this.defPageLayout;
    }

    public PageLayout createPageLayout(Paper paper, PageOrientation orient, MarginType mType) {
        double lm;
        double rm;
        double tm;
        double bm2;
        if (paper == null || orient == null || mType == null) {
            throw new NullPointerException("Parameters cannot be null");
        }
        Rectangle2D imgArea = this.impl.printableArea(paper);
        double width = paper.getWidth() / 72.0d;
        double height = paper.getHeight() / 72.0d;
        double plm = imgArea.getMinX();
        double ptm = imgArea.getMinY();
        double prm = width - imgArea.getMaxX();
        double pbm = height - imgArea.getMaxY();
        if (Math.abs(plm) < 0.01d) {
            plm = 0.0d;
        }
        if (Math.abs(prm) < 0.01d) {
            prm = 0.0d;
        }
        if (Math.abs(ptm) < 0.01d) {
            ptm = 0.0d;
        }
        if (Math.abs(pbm) < 0.01d) {
            pbm = 0.0d;
        }
        switch (mType) {
            case DEFAULT:
                plm = plm <= 0.75d ? 0.75d : plm;
                prm = prm <= 0.75d ? 0.75d : prm;
                ptm = ptm <= 0.75d ? 0.75d : ptm;
                pbm = pbm <= 0.75d ? 0.75d : pbm;
                break;
            case EQUAL:
                double maxM = Math.max(Math.max(plm, prm), Math.max(ptm, pbm));
                pbm = maxM;
                ptm = maxM;
                prm = maxM;
                plm = maxM;
                break;
            case EQUAL_OPPOSITES:
                double maxH = Math.max(plm, prm);
                double maxV = Math.max(ptm, pbm);
                prm = maxH;
                plm = maxH;
                pbm = maxV;
                ptm = maxV;
                break;
        }
        switch (orient) {
            case LANDSCAPE:
                lm = pbm;
                rm = ptm;
                tm = plm;
                bm2 = prm;
                break;
            case REVERSE_LANDSCAPE:
                lm = ptm;
                rm = pbm;
                tm = prm;
                bm2 = plm;
                break;
            case REVERSE_PORTRAIT:
                lm = prm;
                rm = plm;
                tm = pbm;
                bm2 = ptm;
                break;
            default:
                lm = plm;
                rm = prm;
                tm = ptm;
                bm2 = pbm;
                break;
        }
        return new PageLayout(paper, orient, lm * 72.0d, rm * 72.0d, tm * 72.0d, bm2 * 72.0d);
    }

    public PageLayout createPageLayout(Paper paper, PageOrientation orient, double lMargin, double rMargin, double tMargin, double bMargin) {
        double lm;
        double rm;
        double tm;
        double bm2;
        if (paper == null || orient == null) {
            throw new NullPointerException("Parameters cannot be null");
        }
        if (lMargin < 0.0d || rMargin < 0.0d || tMargin < 0.0d || bMargin < 0.0d) {
            throw new IllegalArgumentException("Margins must be >= 0");
        }
        Rectangle2D imgArea = this.impl.printableArea(paper);
        double width = paper.getWidth() / 72.0d;
        double height = paper.getHeight() / 72.0d;
        double plm = imgArea.getMinX();
        double ptm = imgArea.getMinY();
        double prm = width - imgArea.getMaxX();
        double pbm = height - imgArea.getMaxY();
        double lMargin2 = lMargin / 72.0d;
        double rMargin2 = rMargin / 72.0d;
        double tMargin2 = tMargin / 72.0d;
        double bMargin2 = bMargin / 72.0d;
        boolean useDefault = false;
        if (orient == PageOrientation.PORTRAIT || orient == PageOrientation.REVERSE_PORTRAIT) {
            if (lMargin2 + rMargin2 > width || tMargin2 + bMargin2 > height) {
                useDefault = true;
            }
        } else if (lMargin2 + rMargin2 > height || tMargin2 + bMargin2 > width) {
            useDefault = true;
        }
        if (useDefault) {
            return createPageLayout(paper, orient, MarginType.DEFAULT);
        }
        switch (orient) {
            case LANDSCAPE:
                lm = pbm;
                rm = ptm;
                tm = plm;
                bm2 = prm;
                break;
            case REVERSE_LANDSCAPE:
                lm = ptm;
                rm = pbm;
                tm = prm;
                bm2 = plm;
                break;
            case REVERSE_PORTRAIT:
                lm = prm;
                rm = plm;
                tm = pbm;
                bm2 = ptm;
                break;
            default:
                lm = plm;
                rm = prm;
                tm = ptm;
                bm2 = pbm;
                break;
        }
        return new PageLayout(paper, orient, (lMargin2 >= lm ? lMargin2 : lm) * 72.0d, (rMargin2 >= rm ? rMargin2 : rm) * 72.0d, (tMargin2 >= tm ? tMargin2 : tm) * 72.0d, (bMargin2 >= bm2 ? bMargin2 : bm2) * 72.0d);
    }

    public String toString() {
        return "Printer " + getName();
    }

    static {
        PrintHelper.setPrintAccessor(new PrintHelper.PrintAccessor() { // from class: javafx.print.Printer.1
            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public PrintResolution createPrintResolution(int fr, int cfr) {
                return new PrintResolution(fr, cfr);
            }

            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public Paper createPaper(String paperName, double paperWidth, double paperHeight, Units units) {
                return new Paper(paperName, paperWidth, paperHeight, units);
            }

            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public PaperSource createPaperSource(String name) {
                return new PaperSource(name);
            }

            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public JobSettings createJobSettings(Printer printer) {
                return new JobSettings(printer);
            }

            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public Printer createPrinter(PrinterImpl impl) {
                return new Printer(impl);
            }

            @Override // com.sun.javafx.print.PrintHelper.PrintAccessor
            public PrinterImpl getPrinterImpl(Printer printer) {
                return printer.getPrinterImpl();
            }
        });
    }
}
