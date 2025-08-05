package javafx.print;

import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.tk.PrintPipeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:javafx/print/PrinterJob.class */
public final class PrinterJob {
    private PrinterJobImpl jobImpl;
    private ObjectProperty<Printer> printer;
    private JobSettings settings;
    private ReadOnlyObjectWrapper<JobStatus> jobStatus = new ReadOnlyObjectWrapper<>(JobStatus.NOT_STARTED);

    /* loaded from: jfxrt.jar:javafx/print/PrinterJob$JobStatus.class */
    public enum JobStatus {
        NOT_STARTED,
        PRINTING,
        CANCELED,
        ERROR,
        DONE
    }

    public static final PrinterJob createPrinterJob() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPrintJobAccess();
        }
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            return null;
        }
        return new PrinterJob(printer);
    }

    public static final PrinterJob createPrinterJob(Printer printer) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPrintJobAccess();
        }
        return new PrinterJob(printer);
    }

    private PrinterJob(Printer printer) {
        this.printer = createPrinterProperty(printer);
        this.settings = printer.getDefaultJobSettings();
        this.settings.setPrinterJob(this);
        createImplJob(printer, this.settings);
    }

    private synchronized PrinterJobImpl createImplJob(Printer printer, JobSettings settings) {
        if (this.jobImpl == null) {
            this.jobImpl = PrintPipeline.getPrintPipeline().createPrinterJob(this);
        }
        return this.jobImpl;
    }

    boolean isJobNew() {
        return getJobStatus() == JobStatus.NOT_STARTED;
    }

    private ObjectProperty<Printer> createPrinterProperty(Printer printer) {
        return new SimpleObjectProperty<Printer>(printer) { // from class: javafx.print.PrinterJob.1
            @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
            public void set(Printer value) {
                if (value == get() || !PrinterJob.this.isJobNew()) {
                    return;
                }
                if (value == null) {
                    value = Printer.getDefaultPrinter();
                }
                super.set((AnonymousClass1) value);
                PrinterJob.this.jobImpl.setPrinterImpl(value.getPrinterImpl());
                PrinterJob.this.settings.updateForPrinter(value);
            }

            @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
            public void bind(ObservableValue<? extends Printer> rawObservable) {
                throw new RuntimeException("Printer property cannot be bound");
            }

            @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.Property
            public void bindBidirectional(Property<Printer> other) {
                throw new RuntimeException("Printer property cannot be bound");
            }

            @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PrinterJob.this;
            }

            @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "printer";
            }
        };
    }

    public final ObjectProperty<Printer> printerProperty() {
        return this.printer;
    }

    public synchronized Printer getPrinter() {
        return printerProperty().get();
    }

    public synchronized void setPrinter(Printer printer) {
        printerProperty().set(printer);
    }

    public synchronized JobSettings getJobSettings() {
        return this.settings;
    }

    public synchronized boolean showPrintDialog(Window owner) {
        if (!isJobNew()) {
            return false;
        }
        return this.jobImpl.showPrintDialog(owner);
    }

    public synchronized boolean showPageSetupDialog(Window owner) {
        if (!isJobNew()) {
            return false;
        }
        return this.jobImpl.showPageDialog(owner);
    }

    synchronized PageLayout validatePageLayout(PageLayout pageLayout) {
        if (pageLayout == null) {
            throw new NullPointerException("pageLayout cannot be null");
        }
        return this.jobImpl.validatePageLayout(pageLayout);
    }

    public synchronized boolean printPage(PageLayout pageLayout, Node node) {
        if (this.jobStatus.get().ordinal() > JobStatus.PRINTING.ordinal()) {
            return false;
        }
        if (this.jobStatus.get() == JobStatus.NOT_STARTED) {
            this.jobStatus.set(JobStatus.PRINTING);
        }
        if (pageLayout == null || node == null) {
            this.jobStatus.set(JobStatus.ERROR);
            throw new NullPointerException("Parameters cannot be null");
        }
        boolean rv = this.jobImpl.print(pageLayout, node);
        if (!rv) {
            this.jobStatus.set(JobStatus.ERROR);
        }
        return rv;
    }

    public synchronized boolean printPage(Node node) {
        return printPage(this.settings.getPageLayout(), node);
    }

    public ReadOnlyObjectProperty<JobStatus> jobStatusProperty() {
        return this.jobStatus.getReadOnlyProperty();
    }

    public JobStatus getJobStatus() {
        return this.jobStatus.get();
    }

    public void cancelJob() {
        if (this.jobStatus.get().ordinal() <= JobStatus.PRINTING.ordinal()) {
            this.jobStatus.set(JobStatus.CANCELED);
            this.jobImpl.cancelJob();
        }
    }

    public synchronized boolean endJob() {
        if (this.jobStatus.get() == JobStatus.NOT_STARTED) {
            cancelJob();
            return false;
        }
        if (this.jobStatus.get() == JobStatus.PRINTING) {
            boolean rv = this.jobImpl.endJob();
            this.jobStatus.set(rv ? JobStatus.DONE : JobStatus.ERROR);
            return rv;
        }
        return false;
    }

    public String toString() {
        return "JavaFX PrinterJob " + ((Object) getPrinter()) + "\n" + ((Object) getJobSettings()) + "\nJob Status = " + ((Object) getJobStatus());
    }
}
