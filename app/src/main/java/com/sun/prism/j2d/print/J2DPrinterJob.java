package com.sun.prism.j2d.print;

import com.sun.glass.ui.Application;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.PrismPrintGraphics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Set;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob.class */
public class J2DPrinterJob implements PrinterJobImpl {
    PrinterJob fxPrinterJob;
    Printer fxPrinter;
    J2DPrinter j2dPrinter;
    private JobSettings settings;
    private PrintRequestAttributeSet printReqAttrSet;
    private static Class onTopClass = null;
    private J2DPageable j2dPageable;
    private volatile Object elo = null;
    private boolean jobRunning = false;
    private boolean jobError = false;
    private boolean jobDone = false;
    private Object monitor = new Object();
    java.awt.print.PrinterJob pJob2D = java.awt.print.PrinterJob.getPrinterJob();

    PrintRequestAttribute getAlwaysOnTop(long id) {
        return (PrintRequestAttribute) AccessController.doPrivileged(() -> {
            PrintRequestAttribute alwaysOnTop = null;
            try {
                if (onTopClass == null) {
                    onTopClass = Class.forName("sun.print.DialogOnTop");
                }
                Constructor<PrintRequestAttribute> cons = onTopClass.getConstructor(Long.TYPE);
                alwaysOnTop = cons.newInstance(Long.valueOf(id));
            } catch (Throwable th) {
            }
            return alwaysOnTop;
        });
    }

    public J2DPrinterJob(PrinterJob fxJob) throws NullPointerException {
        this.j2dPageable = null;
        this.fxPrinterJob = fxJob;
        this.fxPrinter = this.fxPrinterJob.getPrinter();
        this.j2dPrinter = getJ2DPrinter(this.fxPrinter);
        this.settings = this.fxPrinterJob.getJobSettings();
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        } catch (PrinterException e2) {
        }
        this.printReqAttrSet = new HashPrintRequestAttributeSet();
        this.printReqAttrSet.add(DialogTypeSelection.NATIVE);
        this.j2dPageable = new J2DPageable();
        this.pJob2D.setPageable(this.j2dPageable);
    }

    private void setEnabledState(Window owner, boolean state) {
        TKStage stage;
        if (owner == null || (stage = owner.impl_getPeer()) == null) {
            return;
        }
        Application.invokeAndWait(() -> {
            stage.setEnabled(state);
        });
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public boolean showPrintDialog(Window owner) {
        boolean rv;
        if (this.jobRunning || this.jobDone) {
            return false;
        }
        if (GraphicsEnvironment.isHeadless()) {
            return true;
        }
        if (onTopClass != null) {
            this.printReqAttrSet.remove(onTopClass);
        }
        if (owner != null) {
            long id = owner.impl_getPeer().getRawHandle();
            PrintRequestAttribute alwaysOnTop = getAlwaysOnTop(id);
            if (alwaysOnTop != null) {
                this.printReqAttrSet.add(alwaysOnTop);
            }
        }
        syncSettingsToAttributes();
        try {
            setEnabledState(owner, false);
            if (!Toolkit.getToolkit().isFxUserThread()) {
                rv = this.pJob2D.printDialog(this.printReqAttrSet);
            } else {
                if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                    throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                }
                rv = showPrintDialogWithNestedLoop(owner);
            }
            if (rv) {
                updateSettingsFromDialog();
            }
            return rv;
        } finally {
            setEnabledState(owner, true);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$PrintDialogRunnable.class */
    private class PrintDialogRunnable implements Runnable {
        private PrintDialogRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean rv = false;
            try {
                rv = J2DPrinterJob.this.pJob2D.printDialog(J2DPrinterJob.this.printReqAttrSet);
                Application.invokeLater(new ExitLoopRunnable(this, Boolean.valueOf(rv)));
            } catch (Exception e2) {
                Application.invokeLater(new ExitLoopRunnable(this, Boolean.valueOf(rv)));
            } catch (Throwable th) {
                Application.invokeLater(new ExitLoopRunnable(this, Boolean.valueOf(rv)));
                throw th;
            }
        }
    }

    private boolean showPrintDialogWithNestedLoop(Window owner) {
        PrintDialogRunnable dr = new PrintDialogRunnable();
        Thread prtThread = new Thread(dr, "FX Print Dialog Thread");
        prtThread.start();
        Object rv = Toolkit.getToolkit().enterNestedEventLoop(dr);
        boolean rvbool = false;
        try {
            rvbool = ((Boolean) rv).booleanValue();
        } catch (Exception e2) {
        }
        return rvbool;
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public boolean showPageDialog(Window owner) {
        boolean rv;
        if (this.jobRunning || this.jobDone) {
            return false;
        }
        if (GraphicsEnvironment.isHeadless()) {
            return true;
        }
        if (onTopClass != null) {
            this.printReqAttrSet.remove(onTopClass);
        }
        if (owner != null) {
            long id = owner.impl_getPeer().getRawHandle();
            PrintRequestAttribute alwaysOnTop = getAlwaysOnTop(id);
            if (alwaysOnTop != null) {
                this.printReqAttrSet.add(alwaysOnTop);
            }
        }
        syncSettingsToAttributes();
        try {
            setEnabledState(owner, false);
            if (!Toolkit.getToolkit().isFxUserThread()) {
                PageFormat pf = this.pJob2D.pageDialog(this.printReqAttrSet);
                rv = pf != null;
            } else {
                if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
                    throw new IllegalStateException("Printing is not allowed during animation or layout processing");
                }
                rv = showPageDialogFromNestedLoop(owner);
            }
            if (rv) {
                updateSettingsFromDialog();
            }
            return rv;
        } finally {
            setEnabledState(owner, true);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$PageDialogRunnable.class */
    private class PageDialogRunnable implements Runnable {
        private PageDialogRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PageFormat pf = null;
            try {
                pf = J2DPrinterJob.this.pJob2D.pageDialog(J2DPrinterJob.this.printReqAttrSet);
                Boolean rv = Boolean.valueOf(pf != null);
                Application.invokeLater(new ExitLoopRunnable(this, rv));
            } catch (Exception e2) {
                Boolean rv2 = Boolean.valueOf(pf != null);
                Application.invokeLater(new ExitLoopRunnable(this, rv2));
            } catch (Throwable th) {
                Boolean rv3 = Boolean.valueOf(pf != null);
                Application.invokeLater(new ExitLoopRunnable(this, rv3));
                throw th;
            }
        }
    }

    private boolean showPageDialogFromNestedLoop(Window owner) {
        PageDialogRunnable dr = new PageDialogRunnable();
        Thread prtThread = new Thread(dr, "FX Page Setup Dialog Thread");
        prtThread.start();
        Object rv = Toolkit.getToolkit().enterNestedEventLoop(dr);
        boolean rvbool = false;
        try {
            rvbool = ((Boolean) rv).booleanValue();
        } catch (Exception e2) {
        }
        return rvbool;
    }

    private void updateJobName() {
        String name = this.pJob2D.getJobName();
        if (!name.equals(this.settings.getJobName())) {
            this.settings.setJobName(name);
        }
    }

    private void updateCopies() {
        int nCopies = this.pJob2D.getCopies();
        if (this.settings.getCopies() != nCopies) {
            this.settings.setCopies(nCopies);
        }
    }

    private void updatePageRanges() {
        PageRanges ranges = (PageRanges) this.printReqAttrSet.get(PageRanges.class);
        if (ranges != null) {
            int[][] members = ranges.getMembers();
            if (members.length == 1) {
                PageRange range = new PageRange(members[0][0], members[0][1]);
                this.settings.setPageRanges(range);
                return;
            }
            if (members.length > 0) {
                try {
                    ArrayList<PageRange> prList = new ArrayList<>();
                    int last = 0;
                    for (int i2 = 0; i2 < members.length; i2++) {
                        int s2 = members[i2][0];
                        int e2 = members[i2][1];
                        if (s2 <= last || e2 < s2) {
                            return;
                        }
                        last = e2;
                        prList.add(new PageRange(s2, e2));
                    }
                    this.settings.setPageRanges((PageRange[]) prList.toArray(new PageRange[0]));
                } catch (Exception e3) {
                }
            }
        }
    }

    private void updateSides() {
        Sides sides = (Sides) this.printReqAttrSet.get(Sides.class);
        if (sides == null) {
            sides = (Sides) this.j2dPrinter.getService().getDefaultAttributeValue(Sides.class);
        }
        if (sides == Sides.ONE_SIDED) {
            this.settings.setPrintSides(PrintSides.ONE_SIDED);
        } else if (sides == Sides.DUPLEX) {
            this.settings.setPrintSides(PrintSides.DUPLEX);
        } else if (sides == Sides.TUMBLE) {
            this.settings.setPrintSides(PrintSides.TUMBLE);
        }
    }

    private void updateCollation() {
        SheetCollate collate = (SheetCollate) this.printReqAttrSet.get(SheetCollate.class);
        if (collate == null) {
            collate = this.j2dPrinter.getDefaultSheetCollate();
        }
        if (collate == SheetCollate.UNCOLLATED) {
            this.settings.setCollation(Collation.UNCOLLATED);
        } else {
            this.settings.setCollation(Collation.COLLATED);
        }
    }

    private void updateColor() {
        Chromaticity color = (Chromaticity) this.printReqAttrSet.get(Chromaticity.class);
        if (color == null) {
            color = this.j2dPrinter.getDefaultChromaticity();
        }
        if (color == Chromaticity.COLOR) {
            this.settings.setPrintColor(PrintColor.COLOR);
        } else {
            this.settings.setPrintColor(PrintColor.MONOCHROME);
        }
    }

    private void updatePrintQuality() {
        PrintQuality quality = (PrintQuality) this.printReqAttrSet.get(PrintQuality.class);
        if (quality == null) {
            quality = this.j2dPrinter.getDefaultPrintQuality();
        }
        if (quality == PrintQuality.DRAFT) {
            this.settings.setPrintQuality(javafx.print.PrintQuality.DRAFT);
        } else if (quality == PrintQuality.HIGH) {
            this.settings.setPrintQuality(javafx.print.PrintQuality.HIGH);
        } else {
            this.settings.setPrintQuality(javafx.print.PrintQuality.NORMAL);
        }
    }

    private void updatePrintResolution() {
        PrinterResolution res = (PrinterResolution) this.printReqAttrSet.get(PrinterResolution.class);
        if (res == null) {
            res = this.j2dPrinter.getDefaultPrinterResolution();
        }
        int cfr = res.getCrossFeedResolution(100);
        int fr = res.getFeedResolution(100);
        this.settings.setPrintResolution(PrintHelper.createPrintResolution(cfr, fr));
    }

    private void updatePageLayout() {
        PageLayout newLayout;
        Media media = (Media) this.printReqAttrSet.get(Media.class);
        Paper paper = this.j2dPrinter.getPaperForMedia(media);
        OrientationRequested o2 = (OrientationRequested) this.printReqAttrSet.get(OrientationRequested.class);
        PageOrientation orient = J2DPrinter.reverseMapOrientation(o2);
        MediaPrintableArea mpa = (MediaPrintableArea) this.printReqAttrSet.get(MediaPrintableArea.class);
        if (mpa == null) {
            newLayout = this.fxPrinter.createPageLayout(paper, orient, Printer.MarginType.DEFAULT);
        } else {
            double pWid = paper.getWidth();
            double pHgt = paper.getHeight();
            double mpaX = mpa.getX(25400) * 72.0f;
            double mpaY = mpa.getY(25400) * 72.0f;
            double mpaW = mpa.getWidth(25400) * 72.0f;
            double mpaH = mpa.getHeight(25400) * 72.0f;
            double lm = 0.0d;
            double rm = 0.0d;
            double tm = 0.0d;
            double bm2 = 0.0d;
            switch (orient) {
                case PORTRAIT:
                    lm = mpaX;
                    rm = (pWid - mpaX) - mpaW;
                    tm = mpaY;
                    bm2 = (pHgt - mpaY) - mpaH;
                    break;
                case REVERSE_PORTRAIT:
                    lm = (pWid - mpaX) - mpaW;
                    rm = mpaX;
                    tm = (pHgt - mpaY) - mpaH;
                    bm2 = mpaY;
                    break;
                case LANDSCAPE:
                    lm = mpaY;
                    rm = (pHgt - mpaY) - mpaH;
                    tm = (pWid - mpaX) - mpaW;
                    bm2 = mpaX;
                    break;
                case REVERSE_LANDSCAPE:
                    lm = (pHgt - mpaY) - mpaH;
                    tm = mpaX;
                    rm = mpaY;
                    bm2 = (pWid - mpaX) - mpaW;
                    break;
            }
            if (Math.abs(lm) < 0.01d) {
                lm = 0.0d;
            }
            if (Math.abs(rm) < 0.01d) {
                rm = 0.0d;
            }
            if (Math.abs(tm) < 0.01d) {
                tm = 0.0d;
            }
            if (Math.abs(bm2) < 0.01d) {
                bm2 = 0.0d;
            }
            newLayout = this.fxPrinter.createPageLayout(paper, orient, lm, rm, tm, bm2);
        }
        this.settings.setPageLayout(newLayout);
    }

    private void updatePaperSource() {
        PaperSource s2;
        Media m2 = (Media) this.printReqAttrSet.get(Media.class);
        if ((m2 instanceof MediaTray) && (s2 = this.j2dPrinter.getPaperSource((MediaTray) m2)) != null) {
            this.settings.setPaperSource(s2);
        }
    }

    private Printer getFXPrinterForService(PrintService service) {
        Set<Printer> printerSet = Printer.getAllPrinters();
        for (Printer p2 : printerSet) {
            J2DPrinter p2d = (J2DPrinter) PrintHelper.getPrinterImpl(p2);
            PrintService s2 = p2d.getService();
            if (s2.equals(service)) {
                return p2;
            }
        }
        return this.fxPrinter;
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public void setPrinterImpl(PrinterImpl impl) {
        this.j2dPrinter = (J2DPrinter) impl;
        this.fxPrinter = this.j2dPrinter.getPrinter();
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        } catch (PrinterException e2) {
        }
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public PrinterImpl getPrinterImpl() {
        return this.j2dPrinter;
    }

    private J2DPrinter getJ2DPrinter(Printer printer) {
        return (J2DPrinter) PrintHelper.getPrinterImpl(printer);
    }

    public Printer getPrinter() {
        return this.fxPrinter;
    }

    public void setPrinter(Printer printer) {
        this.fxPrinter = printer;
        this.j2dPrinter = getJ2DPrinter(printer);
        try {
            this.pJob2D.setPrintService(this.j2dPrinter.getService());
        } catch (PrinterException e2) {
        }
    }

    private void updatePrinter() {
        PrintService currService = this.j2dPrinter.getService();
        PrintService jobService = this.pJob2D.getPrintService();
        if (currService.equals(jobService)) {
            return;
        }
        Printer newFXPrinter = getFXPrinterForService(jobService);
        this.fxPrinterJob.setPrinter(newFXPrinter);
    }

    private void updateSettingsFromDialog() {
        updatePrinter();
        updateJobName();
        updateCopies();
        updatePageRanges();
        updateSides();
        updateCollation();
        updatePageLayout();
        updatePaperSource();
        updateColor();
        updatePrintQuality();
        updatePrintResolution();
    }

    private void syncSettingsToAttributes() {
        syncJobName();
        syncCopies();
        syncPageRanges();
        syncSides();
        syncCollation();
        syncPageLayout();
        syncPaperSource();
        syncColor();
        syncPrintQuality();
        syncPrintResolution();
    }

    private void syncJobName() {
        this.pJob2D.setJobName(this.settings.getJobName());
    }

    private void syncCopies() {
        this.pJob2D.setCopies(this.settings.getCopies());
        this.printReqAttrSet.add(new Copies(this.settings.getCopies()));
    }

    private void syncPageRanges() {
        this.printReqAttrSet.remove(PageRanges.class);
        PageRange[] prArr = this.settings.getPageRanges();
        if (prArr != null && prArr.length > 0) {
            int len = prArr.length;
            int[][] ranges = new int[len][2];
            for (int i2 = 0; i2 < len; i2++) {
                ranges[i2][0] = prArr[i2].getStartPage();
                ranges[i2][1] = prArr[i2].getEndPage();
            }
            this.printReqAttrSet.add(new PageRanges(ranges));
        }
    }

    private void syncSides() {
        Sides j2dSides = Sides.ONE_SIDED;
        PrintSides sides = this.settings.getPrintSides();
        if (sides == PrintSides.DUPLEX) {
            j2dSides = Sides.DUPLEX;
        } else if (sides == PrintSides.TUMBLE) {
            j2dSides = Sides.TUMBLE;
        }
        this.printReqAttrSet.add(j2dSides);
    }

    private void syncCollation() {
        if (this.settings.getCollation() == Collation.UNCOLLATED) {
            this.printReqAttrSet.add(SheetCollate.UNCOLLATED);
        } else {
            this.printReqAttrSet.add(SheetCollate.COLLATED);
        }
    }

    private void syncPageLayout() {
        PageLayout layout = this.settings.getPageLayout();
        PageOrientation orient = layout.getPageOrientation();
        this.printReqAttrSet.add(J2DPrinter.mapOrientation(orient));
        double pWid = layout.getPaper().getWidth();
        double pHgt = layout.getPaper().getHeight();
        float widthInInches = (float) (pWid / 72.0d);
        float heightInInches = (float) (pHgt / 72.0d);
        MediaSizeName media = MediaSize.findMedia(widthInInches, heightInInches, 25400);
        if (media == null) {
            media = MediaSizeName.NA_LETTER;
        }
        this.printReqAttrSet.add(media);
        double ix = 0.0d;
        double iy = 0.0d;
        double iw = pWid;
        double ih = pHgt;
        switch (orient) {
            case PORTRAIT:
                ix = layout.getLeftMargin();
                iy = layout.getTopMargin();
                iw = (pWid - ix) - layout.getRightMargin();
                ih = (pHgt - iy) - layout.getBottomMargin();
                break;
            case REVERSE_PORTRAIT:
                ix = layout.getRightMargin();
                iy = layout.getBottomMargin();
                iw = (pWid - ix) - layout.getLeftMargin();
                ih = (pHgt - iy) - layout.getTopMargin();
                break;
            case LANDSCAPE:
                ix = layout.getBottomMargin();
                iy = layout.getLeftMargin();
                iw = (pWid - ix) - layout.getTopMargin();
                ih = (pHgt - iy) - layout.getRightMargin();
                break;
            case REVERSE_LANDSCAPE:
                ix = layout.getTopMargin();
                iy = layout.getRightMargin();
                iw = (pWid - ix) - layout.getBottomMargin();
                ih = (pHgt - iy) - layout.getLeftMargin();
                break;
        }
        MediaPrintableArea mpa = new MediaPrintableArea((float) (ix / 72.0d), (float) (iy / 72.0d), (float) (iw / 72.0d), (float) (ih / 72.0d), 25400);
        this.printReqAttrSet.add(mpa);
    }

    private void syncPaperSource() {
        MediaTray tray;
        Media m2 = (Media) this.printReqAttrSet.get(Media.class);
        if (m2 != null && (m2 instanceof MediaTray)) {
            this.printReqAttrSet.remove(Media.class);
        }
        PaperSource source = this.settings.getPaperSource();
        if (!source.equals(this.j2dPrinter.defaultPaperSource()) && (tray = this.j2dPrinter.getTrayForPaperSource(source)) != null) {
            this.printReqAttrSet.add(tray);
        }
    }

    private void syncColor() {
        if (this.settings.getPrintColor() == PrintColor.MONOCHROME) {
            this.printReqAttrSet.add(Chromaticity.MONOCHROME);
        } else {
            this.printReqAttrSet.add(Chromaticity.COLOR);
        }
    }

    private void syncPrintQuality() {
        PrintQuality j2DQuality;
        javafx.print.PrintQuality quality = this.settings.getPrintQuality();
        if (quality == javafx.print.PrintQuality.DRAFT) {
            j2DQuality = PrintQuality.DRAFT;
        } else if (quality == javafx.print.PrintQuality.HIGH) {
            j2DQuality = PrintQuality.HIGH;
        } else {
            j2DQuality = PrintQuality.NORMAL;
        }
        this.printReqAttrSet.add(j2DQuality);
    }

    private void syncPrintResolution() {
        PrintService ps = this.pJob2D.getPrintService();
        if (!ps.isAttributeCategorySupported(PrinterResolution.class)) {
            this.printReqAttrSet.remove(PrinterResolution.class);
            return;
        }
        PrinterResolution pres = (PrinterResolution) this.printReqAttrSet.get(PrinterResolution.class);
        if (pres != null && !ps.isAttributeValueSupported(pres, null, null)) {
            this.printReqAttrSet.remove(PrinterResolution.class);
        }
        PrintResolution res = this.settings.getPrintResolution();
        if (res == null) {
            return;
        }
        int cfRes = res.getCrossFeedResolution();
        int fRes = res.getFeedResolution();
        PrinterResolution pres2 = new PrinterResolution(cfRes, fRes, 100);
        if (!ps.isAttributeValueSupported(pres2, null, null)) {
            return;
        }
        this.printReqAttrSet.add(pres2);
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public PageLayout validatePageLayout(PageLayout pageLayout) {
        boolean needsNewLayout = false;
        PrinterAttributes caps = this.fxPrinter.getPrinterAttributes();
        Paper p2 = pageLayout.getPaper();
        if (!caps.getSupportedPapers().contains(p2)) {
            needsNewLayout = true;
            p2 = caps.getDefaultPaper();
        }
        PageOrientation o2 = pageLayout.getPageOrientation();
        if (!caps.getSupportedPageOrientations().contains(o2)) {
            needsNewLayout = true;
            o2 = caps.getDefaultPageOrientation();
        }
        if (needsNewLayout) {
            pageLayout = this.fxPrinter.createPageLayout(p2, o2, Printer.MarginType.DEFAULT);
        }
        return pageLayout;
    }

    private void checkPermissions() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPrintJobAccess();
        }
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public boolean print(PageLayout pageLayout, Node node) {
        if (Toolkit.getToolkit().isFxUserThread() && !Toolkit.getToolkit().canStartNestedEventLoop()) {
            throw new IllegalStateException("Printing is not allowed during animation or layout processing");
        }
        if (this.jobError || this.jobDone) {
            return false;
        }
        if (!this.jobRunning) {
            checkPermissions();
            syncSettingsToAttributes();
            PrintJobRunnable runnable = new PrintJobRunnable();
            Thread prtThread = new Thread(runnable, "Print Job Thread");
            prtThread.start();
            this.jobRunning = true;
        }
        try {
            this.j2dPageable.implPrintPage(pageLayout, node);
        } catch (Throwable t2) {
            if (PrismSettings.debug) {
                System.err.println("printPage caught exception.");
                t2.printStackTrace();
            }
            this.jobError = true;
            this.jobDone = true;
        }
        return !this.jobError;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$PrintJobRunnable.class */
    private class PrintJobRunnable implements Runnable {
        private PrintJobRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                J2DPrinterJob.this.pJob2D.print(J2DPrinterJob.this.printReqAttrSet);
                J2DPrinterJob.this.jobDone = true;
            } catch (Throwable t2) {
                if (PrismSettings.debug) {
                    System.err.println("print caught exception.");
                    t2.printStackTrace();
                }
                J2DPrinterJob.this.jobError = true;
                J2DPrinterJob.this.jobDone = true;
            }
            if (J2DPrinterJob.this.elo != null) {
                Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, null));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$LayoutRunnable.class */
    static class LayoutRunnable implements Runnable {
        PageInfo pageInfo;

        LayoutRunnable(PageInfo info) {
            this.pageInfo = info;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.pageInfo.tempScene && this.pageInfo.root.getScene() == null) {
                new Scene(this.pageInfo.root);
            }
            NodeHelper.layoutNodeForPrinting(this.pageInfo.root);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$ClearSceneRunnable.class */
    static class ClearSceneRunnable implements Runnable {
        PageInfo pageInfo;

        ClearSceneRunnable(PageInfo info) {
            this.pageInfo = info;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.pageInfo.clearScene();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$PageInfo.class */
    private static class PageInfo {
        private PageLayout pageLayout;
        private Node node;
        private Parent root;
        private Node topNode;
        private Group group;
        private boolean tempGroup;
        private boolean tempScene;
        private boolean sceneInited;

        PageInfo(PageLayout pageLayout, Node node) {
            this.pageLayout = pageLayout;
            this.node = node;
        }

        Node getNode() {
            initScene();
            return this.node;
        }

        PageLayout getPageLayout() {
            return this.pageLayout;
        }

        void initScene() {
            Node topNode;
            if (this.sceneInited) {
                return;
            }
            if (this.node.getScene() == null) {
                this.tempScene = true;
                Node parent = this.node;
                while (true) {
                    topNode = parent;
                    if (topNode.getParent() == null) {
                        break;
                    } else {
                        parent = topNode.getParent();
                    }
                }
                if (topNode instanceof Group) {
                    this.group = (Group) topNode;
                } else {
                    this.tempGroup = true;
                    this.group = new Group();
                    this.group.getChildren().add(topNode);
                }
                this.root = this.group;
            } else {
                this.root = this.node.getScene().getRoot();
            }
            if (Toolkit.getToolkit().isFxUserThread()) {
                if (this.tempScene && this.root.getScene() == null) {
                    new Scene(this.root);
                }
                NodeHelper.layoutNodeForPrinting(this.root);
            } else {
                Application.invokeAndWait(new LayoutRunnable(this));
            }
            this.sceneInited = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearScene() {
            if (this.tempGroup) {
                this.group.getChildren().removeAll(this.root);
            }
            this.tempGroup = false;
            this.tempScene = false;
            this.root = null;
            this.group = null;
            this.topNode = null;
            this.sceneInited = false;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$ExitLoopRunnable.class */
    static class ExitLoopRunnable implements Runnable {
        Object elo;
        Object rv;

        ExitLoopRunnable(Object elo, Object rv) {
            this.elo = elo;
            this.rv = rv;
        }

        @Override // java.lang.Runnable
        public void run() {
            Toolkit.getToolkit().exitNestedEventLoop(this.elo, this.rv);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinterJob$J2DPageable.class */
    private class J2DPageable implements Pageable, Printable {
        private volatile boolean pageDone;
        private int currPageIndex;
        private volatile PageInfo newPageInfo;
        private PageInfo currPageInfo;
        private PageFormat currPageFormat;

        private J2DPageable() {
            this.currPageIndex = -1;
            this.newPageInfo = null;
        }

        private boolean waitForNextPage(int pageIndex) {
            if (J2DPrinterJob.this.elo != null && this.currPageInfo != null) {
                Application.invokeLater(new ExitLoopRunnable(J2DPrinterJob.this.elo, null));
            }
            if (this.currPageInfo != null) {
                if (Toolkit.getToolkit().isFxUserThread()) {
                    this.currPageInfo.clearScene();
                } else {
                    Application.invokeAndWait(new ClearSceneRunnable(this.currPageInfo));
                }
            }
            this.currPageInfo = null;
            this.pageDone = true;
            synchronized (J2DPrinterJob.this.monitor) {
                if (this.newPageInfo == null) {
                    J2DPrinterJob.this.monitor.notify();
                }
                while (this.newPageInfo == null && !J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
                    try {
                        J2DPrinterJob.this.monitor.wait(1000L);
                    } catch (InterruptedException e2) {
                    }
                }
            }
            if (J2DPrinterJob.this.jobDone || J2DPrinterJob.this.jobError) {
                return false;
            }
            this.currPageInfo = this.newPageInfo;
            this.newPageInfo = null;
            this.currPageIndex = pageIndex;
            this.currPageFormat = getPageFormatFromLayout(this.currPageInfo.getPageLayout());
            return true;
        }

        private PageFormat getPageFormatFromLayout(PageLayout layout) throws IllegalArgumentException {
            java.awt.print.Paper paper = new java.awt.print.Paper();
            double pWid = layout.getPaper().getWidth();
            double pHgt = layout.getPaper().getHeight();
            double ix = 0.0d;
            double iy = 0.0d;
            double iw = pWid;
            double ih = pHgt;
            PageOrientation orient = layout.getPageOrientation();
            switch (orient) {
                case PORTRAIT:
                    ix = layout.getLeftMargin();
                    iy = layout.getTopMargin();
                    iw = (pWid - ix) - layout.getRightMargin();
                    ih = (pHgt - iy) - layout.getBottomMargin();
                    break;
                case REVERSE_PORTRAIT:
                    ix = layout.getRightMargin();
                    iy = layout.getBottomMargin();
                    iw = (pWid - ix) - layout.getLeftMargin();
                    ih = (pHgt - iy) - layout.getTopMargin();
                    break;
                case LANDSCAPE:
                    ix = layout.getBottomMargin();
                    iy = layout.getLeftMargin();
                    iw = (pWid - ix) - layout.getTopMargin();
                    ih = (pHgt - iy) - layout.getRightMargin();
                    break;
                case REVERSE_LANDSCAPE:
                    ix = layout.getTopMargin();
                    iy = layout.getRightMargin();
                    iw = (pWid - ix) - layout.getBottomMargin();
                    ih = (pHgt - iy) - layout.getLeftMargin();
                    break;
            }
            paper.setSize(pWid, pHgt);
            paper.setImageableArea(ix, iy, iw, ih);
            PageFormat format = new PageFormat();
            format.setOrientation(J2DPrinter.getOrientID(orient));
            format.setPaper(paper);
            return format;
        }

        private boolean getPage(int pageIndex) {
            if (pageIndex == this.currPageIndex) {
                return true;
            }
            boolean nextPage = false;
            if (pageIndex > this.currPageIndex) {
                nextPage = waitForNextPage(pageIndex);
            }
            return nextPage;
        }

        @Override // java.awt.print.Printable
        public int print(Graphics g2, PageFormat pf, int pageIndex) {
            if (J2DPrinterJob.this.jobError || J2DPrinterJob.this.jobDone || !getPage(pageIndex)) {
                return 1;
            }
            int x2 = (int) pf.getImageableX();
            int y2 = (int) pf.getImageableY();
            int w2 = (int) pf.getImageableWidth();
            int h2 = (int) pf.getImageableHeight();
            Node appNode = this.currPageInfo.getNode();
            g2.translate(x2, y2);
            printNode(appNode, g2, w2, h2);
            return 0;
        }

        private void printNode(Node node, Graphics g2, int w2, int h2) {
            PrismPrintGraphics ppg = new PrismPrintGraphics((Graphics2D) g2, w2, h2);
            NGNode pgNode = node.impl_getPeer();
            boolean errored = false;
            try {
                pgNode.render(ppg);
            } catch (Throwable t2) {
                if (PrismSettings.debug) {
                    System.err.println("printNode caught exception.");
                    t2.printStackTrace();
                }
                errored = true;
            }
            ppg.getResourceFactory().getTextureResourcePool().freeDisposalRequestedAndCheckResources(errored);
        }

        @Override // java.awt.print.Pageable
        public Printable getPrintable(int pageIndex) {
            getPage(pageIndex);
            return this;
        }

        @Override // java.awt.print.Pageable
        public PageFormat getPageFormat(int pageIndex) {
            getPage(pageIndex);
            return this.currPageFormat;
        }

        @Override // java.awt.print.Pageable
        public int getNumberOfPages() {
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void implPrintPage(PageLayout pageLayout, Node node) {
            this.pageDone = false;
            synchronized (J2DPrinterJob.this.monitor) {
                this.newPageInfo = new PageInfo(pageLayout, node);
                J2DPrinterJob.this.monitor.notify();
            }
            if (Toolkit.getToolkit().isFxUserThread()) {
                J2DPrinterJob.this.elo = new Object();
                Toolkit.getToolkit().enterNestedEventLoop(J2DPrinterJob.this.elo);
                J2DPrinterJob.this.elo = null;
                return;
            }
            while (!this.pageDone && !J2DPrinterJob.this.jobDone && !J2DPrinterJob.this.jobError) {
                synchronized (J2DPrinterJob.this.monitor) {
                    try {
                        if (!this.pageDone) {
                            J2DPrinterJob.this.monitor.wait(1000L);
                        }
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public boolean endJob() {
        boolean z2;
        if (this.jobRunning && !this.jobDone && !this.jobError) {
            this.jobDone = true;
            try {
                synchronized (this.monitor) {
                    this.monitor.notify();
                    z2 = this.jobDone;
                }
                return z2;
            } catch (IllegalStateException e2) {
                if (PrismSettings.debug) {
                    System.err.println("Internal Error " + ((Object) e2));
                }
                return this.jobDone;
            }
        }
        return false;
    }

    @Override // com.sun.javafx.print.PrinterJobImpl
    public void cancelJob() {
        if (!this.pJob2D.isCancelled()) {
            this.pJob2D.cancel();
        }
        this.jobDone = true;
        if (this.jobRunning) {
            this.jobRunning = false;
            try {
                synchronized (this.monitor) {
                    this.monitor.notify();
                }
            } catch (IllegalStateException e2) {
                if (PrismSettings.debug) {
                    System.err.println("Internal Error " + ((Object) e2));
                }
            }
        }
    }
}
