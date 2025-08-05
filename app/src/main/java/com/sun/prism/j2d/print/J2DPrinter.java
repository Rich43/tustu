package com.sun.prism.j2d.print;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.geometry.Rectangle2D;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinter.class */
public class J2DPrinter implements PrinterImpl {
    private PrintService service;
    private Printer fxPrinter;
    private int defaultCopies = 0;
    private int maxCopies = 0;
    private Collation defaultCollation;
    private Set<Collation> collateSet;
    private PrintColor defColor;
    private Set<PrintColor> colorSet;
    private PrintSides defSides;
    private Set<PrintSides> sidesSet;
    private PageOrientation defOrient;
    private Set<PageOrientation> orientSet;
    private PrintResolution defRes;
    private Set<PrintResolution> resSet;
    private PrintQuality defQuality;
    private Set<PrintQuality> qualitySet;
    private Paper defPaper;
    private Set<Paper> paperSet;
    private static Map<MediaTray, PaperSource> preDefinedTrayMap = null;
    private static Map<MediaSizeName, Paper> predefinedPaperMap = null;
    private PaperSource defPaperSource;
    private Set<PaperSource> paperSourceSet;
    private Map<PaperSource, MediaTray> sourceToTrayMap;
    private Map<MediaTray, PaperSource> trayToSourceMap;
    private Map<MediaSizeName, Paper> mediaToPaperMap;
    private Map<Paper, MediaSizeName> paperToMediaMap;
    private PageLayout defaultLayout;

    public J2DPrinter(PrintService s2) {
        this.service = s2;
    }

    public Printer getPrinter() {
        return this.fxPrinter;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public void setPrinter(Printer printer) {
        this.fxPrinter = printer;
    }

    public PrintService getService() {
        return this.service;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public String getName() {
        return this.service.getName();
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public JobSettings getDefaultJobSettings() {
        return PrintHelper.createJobSettings(this.fxPrinter);
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public int defaultCopies() {
        if (this.defaultCopies > 0) {
            return this.defaultCopies;
        }
        try {
            Copies copies = (Copies) this.service.getDefaultAttributeValue(Copies.class);
            this.defaultCopies = copies.getValue();
        } catch (Exception e2) {
            this.defaultCopies = 1;
        }
        return this.defaultCopies;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public int maxCopies() {
        int[][] members;
        if (this.maxCopies > 0) {
            return this.maxCopies;
        }
        CopiesSupported copies = null;
        try {
            copies = (CopiesSupported) this.service.getSupportedAttributeValues(CopiesSupported.class, null, null);
        } catch (Exception e2) {
        }
        if (copies != null && (members = copies.getMembers()) != null && members.length > 0 && members[0].length > 0) {
            this.maxCopies = members[0][1];
        }
        if (this.maxCopies == 0) {
            this.maxCopies = 999;
        }
        return this.maxCopies;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PageRange defaultPageRange() {
        try {
            PageRanges ranges = (PageRanges) this.service.getDefaultAttributeValue(PageRanges.class);
            if (ranges == null) {
                return null;
            }
            int s2 = ranges.getMembers()[0][0];
            int e2 = ranges.getMembers()[0][1];
            if (s2 == 1 && e2 == Integer.MAX_VALUE) {
                return null;
            }
            return new PageRange(s2, e2);
        } catch (Exception e3) {
            return null;
        }
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public boolean supportsPageRanges() {
        return true;
    }

    SheetCollate getDefaultSheetCollate() {
        SheetCollate collate;
        try {
            collate = (SheetCollate) this.service.getDefaultAttributeValue(SheetCollate.class);
        } catch (Exception e2) {
            collate = SheetCollate.UNCOLLATED;
        }
        return collate;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Collation defaultCollation() {
        if (this.defaultCollation != null) {
            return this.defaultCollation;
        }
        SheetCollate collate = getDefaultSheetCollate();
        this.defaultCollation = collate == SheetCollate.COLLATED ? Collation.COLLATED : Collation.UNCOLLATED;
        return this.defaultCollation;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<Collation> supportedCollations() {
        if (this.collateSet == null) {
            Set<Collation> cSet = new TreeSet<>();
            SheetCollate[] sc = null;
            try {
                sc = (SheetCollate[]) this.service.getSupportedAttributeValues(SheetCollate.class, null, null);
            } catch (Exception e2) {
            }
            if (sc != null) {
                for (int i2 = 0; i2 < sc.length; i2++) {
                    if (sc[i2] == SheetCollate.UNCOLLATED) {
                        cSet.add(Collation.UNCOLLATED);
                    }
                    if (sc[i2] == SheetCollate.COLLATED) {
                        cSet.add(Collation.COLLATED);
                    }
                }
            }
            this.collateSet = Collections.unmodifiableSet(cSet);
        }
        return this.collateSet;
    }

    Chromaticity getDefaultChromaticity() {
        Chromaticity color;
        try {
            color = (Chromaticity) this.service.getDefaultAttributeValue(Chromaticity.class);
        } catch (Exception e2) {
            color = Chromaticity.COLOR;
        }
        return color;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PrintColor defaultPrintColor() {
        if (this.defColor != null) {
            return this.defColor;
        }
        Chromaticity color = getDefaultChromaticity();
        this.defColor = color == Chromaticity.COLOR ? PrintColor.COLOR : PrintColor.MONOCHROME;
        return this.defColor;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PrintColor> supportedPrintColor() {
        if (this.colorSet == null) {
            Set<PrintColor> cSet = new TreeSet<>();
            Chromaticity[] sc = null;
            try {
                sc = (Chromaticity[]) this.service.getSupportedAttributeValues(Chromaticity.class, null, null);
            } catch (Exception e2) {
            }
            if (sc != null) {
                for (int i2 = 0; i2 < sc.length; i2++) {
                    if (sc[i2] == Chromaticity.COLOR) {
                        cSet.add(PrintColor.COLOR);
                    }
                    if (sc[i2] == Chromaticity.MONOCHROME) {
                        cSet.add(PrintColor.MONOCHROME);
                    }
                }
            }
            this.colorSet = Collections.unmodifiableSet(cSet);
        }
        return this.colorSet;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PrintSides defaultSides() {
        if (this.defSides != null) {
            return this.defSides;
        }
        Sides sides = (Sides) this.service.getDefaultAttributeValue(Sides.class);
        if (sides == null || sides == Sides.ONE_SIDED) {
            this.defSides = PrintSides.ONE_SIDED;
        } else if (sides == Sides.DUPLEX) {
            this.defSides = PrintSides.DUPLEX;
        } else {
            this.defSides = PrintSides.TUMBLE;
        }
        return this.defSides;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PrintSides> supportedSides() {
        if (this.sidesSet == null) {
            Set<PrintSides> sSet = new TreeSet<>();
            Sides[] ss = null;
            try {
                ss = (Sides[]) this.service.getSupportedAttributeValues(Sides.class, null, null);
            } catch (Exception e2) {
            }
            if (ss != null) {
                for (int i2 = 0; i2 < ss.length; i2++) {
                    if (ss[i2] == Sides.ONE_SIDED) {
                        sSet.add(PrintSides.ONE_SIDED);
                    }
                    if (ss[i2] == Sides.DUPLEX) {
                        sSet.add(PrintSides.DUPLEX);
                    }
                    if (ss[i2] == Sides.TUMBLE) {
                        sSet.add(PrintSides.TUMBLE);
                    }
                }
            }
            this.sidesSet = Collections.unmodifiableSet(sSet);
        }
        return this.sidesSet;
    }

    static int getOrientID(PageOrientation o2) {
        if (o2 == PageOrientation.LANDSCAPE) {
            return 0;
        }
        if (o2 == PageOrientation.REVERSE_LANDSCAPE) {
            return 2;
        }
        return 1;
    }

    static OrientationRequested mapOrientation(PageOrientation o2) {
        if (o2 == PageOrientation.REVERSE_PORTRAIT) {
            return OrientationRequested.REVERSE_PORTRAIT;
        }
        if (o2 == PageOrientation.LANDSCAPE) {
            return OrientationRequested.LANDSCAPE;
        }
        if (o2 == PageOrientation.REVERSE_LANDSCAPE) {
            return OrientationRequested.REVERSE_LANDSCAPE;
        }
        return OrientationRequested.PORTRAIT;
    }

    static PageOrientation reverseMapOrientation(OrientationRequested o2) {
        if (o2 == OrientationRequested.REVERSE_PORTRAIT) {
            return PageOrientation.REVERSE_PORTRAIT;
        }
        if (o2 == OrientationRequested.LANDSCAPE) {
            return PageOrientation.LANDSCAPE;
        }
        if (o2 == OrientationRequested.REVERSE_LANDSCAPE) {
            return PageOrientation.REVERSE_LANDSCAPE;
        }
        return PageOrientation.PORTRAIT;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PageOrientation defaultOrientation() {
        if (this.defOrient == null) {
            OrientationRequested orient = (OrientationRequested) this.service.getDefaultAttributeValue(OrientationRequested.class);
            this.defOrient = reverseMapOrientation(orient);
        }
        return this.defOrient;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PageOrientation> supportedOrientation() {
        if (this.orientSet != null) {
            return this.orientSet;
        }
        Set<PageOrientation> oset = new TreeSet<>();
        OrientationRequested[] or = null;
        try {
            or = (OrientationRequested[]) this.service.getSupportedAttributeValues(OrientationRequested.class, null, null);
        } catch (Exception e2) {
        }
        if (or == null || or.length == 0) {
            oset.add(defaultOrientation());
        } else {
            for (int i2 = 0; i2 < or.length; i2++) {
                if (or[i2] == OrientationRequested.PORTRAIT) {
                    oset.add(PageOrientation.PORTRAIT);
                } else if (or[i2] == OrientationRequested.REVERSE_PORTRAIT) {
                    oset.add(PageOrientation.REVERSE_PORTRAIT);
                } else if (or[i2] == OrientationRequested.LANDSCAPE) {
                    oset.add(PageOrientation.LANDSCAPE);
                } else {
                    oset.add(PageOrientation.REVERSE_LANDSCAPE);
                }
            }
        }
        this.orientSet = Collections.unmodifiableSet(oset);
        return this.orientSet;
    }

    PrinterResolution getDefaultPrinterResolution() {
        PrinterResolution res = (PrinterResolution) this.service.getDefaultAttributeValue(PrinterResolution.class);
        if (res == null) {
            res = new PrinterResolution(300, 300, 100);
        }
        return res;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PrintResolution defaultPrintResolution() {
        if (this.defRes != null) {
            return this.defRes;
        }
        PrinterResolution res = getDefaultPrinterResolution();
        int cfr = res.getCrossFeedResolution(100);
        int fr = res.getFeedResolution(100);
        this.defRes = PrintHelper.createPrintResolution(cfr, fr);
        return this.defRes;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinter$PrintResolutionComparator.class */
    private static class PrintResolutionComparator implements Comparator<PrintResolution> {
        static final PrintResolutionComparator theComparator = new PrintResolutionComparator();

        private PrintResolutionComparator() {
        }

        @Override // java.util.Comparator
        public int compare(PrintResolution r1, PrintResolution r2) {
            long r1Res = r1.getCrossFeedResolution() * r1.getFeedResolution();
            long r2Res = r2.getCrossFeedResolution() * r2.getFeedResolution();
            if (r1Res == r2Res) {
                return 0;
            }
            if (r1Res < r2Res) {
                return -1;
            }
            return 1;
        }
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PrintResolution> supportedPrintResolution() {
        if (this.resSet != null) {
            return this.resSet;
        }
        Set<PrintResolution> rSet = new TreeSet<>(PrintResolutionComparator.theComparator);
        PrinterResolution[] pr = null;
        try {
            pr = (PrinterResolution[]) this.service.getSupportedAttributeValues(PrinterResolution.class, null, null);
        } catch (Exception e2) {
        }
        if (pr == null || pr.length == 0) {
            rSet.add(defaultPrintResolution());
        } else {
            for (int i2 = 0; i2 < pr.length; i2++) {
                int cfr = pr[i2].getCrossFeedResolution(100);
                int fr = pr[i2].getFeedResolution(100);
                rSet.add(PrintHelper.createPrintResolution(cfr, fr));
            }
        }
        this.resSet = Collections.unmodifiableSet(rSet);
        return this.resSet;
    }

    javax.print.attribute.standard.PrintQuality getDefaultPrintQuality() {
        javax.print.attribute.standard.PrintQuality quality;
        try {
            quality = (javax.print.attribute.standard.PrintQuality) this.service.getDefaultAttributeValue(javax.print.attribute.standard.PrintQuality.class);
        } catch (Exception e2) {
            quality = javax.print.attribute.standard.PrintQuality.NORMAL;
        }
        return quality;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PrintQuality defaultPrintQuality() {
        if (this.defQuality != null) {
            return this.defQuality;
        }
        javax.print.attribute.standard.PrintQuality quality = getDefaultPrintQuality();
        if (quality == javax.print.attribute.standard.PrintQuality.DRAFT) {
            this.defQuality = PrintQuality.DRAFT;
        } else if (quality == javax.print.attribute.standard.PrintQuality.HIGH) {
            this.defQuality = PrintQuality.HIGH;
        } else {
            this.defQuality = PrintQuality.NORMAL;
        }
        return this.defQuality;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PrintQuality> supportedPrintQuality() {
        if (this.qualitySet == null) {
            Set<PrintQuality> set = new TreeSet<>();
            javax.print.attribute.standard.PrintQuality[] arr = null;
            try {
                arr = (javax.print.attribute.standard.PrintQuality[]) this.service.getSupportedAttributeValues(javax.print.attribute.standard.PrintQuality.class, null, null);
            } catch (Exception e2) {
            }
            if (arr == null || arr.length == 0) {
                set.add(PrintQuality.NORMAL);
            } else {
                for (int i2 = 0; i2 < arr.length; i2++) {
                    if (arr[i2] == javax.print.attribute.standard.PrintQuality.NORMAL) {
                        set.add(PrintQuality.NORMAL);
                    }
                    if (arr[i2] == javax.print.attribute.standard.PrintQuality.DRAFT) {
                        set.add(PrintQuality.DRAFT);
                    }
                    if (arr[i2] == javax.print.attribute.standard.PrintQuality.HIGH) {
                        set.add(PrintQuality.HIGH);
                    }
                }
            }
            this.qualitySet = Collections.unmodifiableSet(set);
        }
        return this.qualitySet;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinter$PaperComparator.class */
    private static class PaperComparator implements Comparator<Paper> {
        static final PaperComparator theComparator = new PaperComparator();

        private PaperComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Paper p1, Paper p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/print/J2DPrinter$PaperSourceComparator.class */
    private static class PaperSourceComparator implements Comparator<PaperSource> {
        static final PaperSourceComparator theComparator = new PaperSourceComparator();

        private PaperSourceComparator() {
        }

        @Override // java.util.Comparator
        public int compare(PaperSource p1, PaperSource p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }

    Paper getPaperForMedia(Media media) {
        populateMedia();
        if (media == null || !(media instanceof MediaSizeName)) {
            return defaultPaper();
        }
        return getPaper((MediaSizeName) media);
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Paper defaultPaper() {
        if (this.defPaper != null) {
            return this.defPaper;
        }
        Media m2 = (Media) this.service.getDefaultAttributeValue(Media.class);
        if (m2 == null || !(m2 instanceof MediaSizeName)) {
            this.defPaper = Paper.NA_LETTER;
        } else {
            this.defPaper = getPaper((MediaSizeName) m2);
        }
        return this.defPaper;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<Paper> supportedPapers() {
        if (this.paperSet == null) {
            populateMedia();
        }
        return this.paperSet;
    }

    private static void initPrefinedMediaMaps() {
        if (predefinedPaperMap == null) {
            HashMap<MediaSizeName, Paper> map = new HashMap<>();
            map.put(MediaSizeName.NA_LETTER, Paper.NA_LETTER);
            map.put(MediaSizeName.TABLOID, Paper.TABLOID);
            map.put(MediaSizeName.NA_LEGAL, Paper.LEGAL);
            map.put(MediaSizeName.EXECUTIVE, Paper.EXECUTIVE);
            map.put(MediaSizeName.NA_8X10, Paper.NA_8X10);
            map.put(MediaSizeName.MONARCH_ENVELOPE, Paper.MONARCH_ENVELOPE);
            map.put(MediaSizeName.NA_NUMBER_10_ENVELOPE, Paper.NA_NUMBER_10_ENVELOPE);
            map.put(MediaSizeName.ISO_A0, Paper.A0);
            map.put(MediaSizeName.ISO_A1, Paper.A1);
            map.put(MediaSizeName.ISO_A2, Paper.A2);
            map.put(MediaSizeName.ISO_A3, Paper.A3);
            map.put(MediaSizeName.ISO_A4, Paper.A4);
            map.put(MediaSizeName.ISO_A5, Paper.A5);
            map.put(MediaSizeName.ISO_A6, Paper.A6);
            map.put(MediaSizeName.f12794C, Paper.f12644C);
            map.put(MediaSizeName.ISO_DESIGNATED_LONG, Paper.DESIGNATED_LONG);
            map.put(MediaSizeName.JIS_B4, Paper.JIS_B4);
            map.put(MediaSizeName.JIS_B5, Paper.JIS_B5);
            map.put(MediaSizeName.JIS_B6, Paper.JIS_B6);
            map.put(MediaSizeName.JAPANESE_POSTCARD, Paper.JAPANESE_POSTCARD);
            predefinedPaperMap = map;
        }
        if (preDefinedTrayMap == null) {
            HashMap<MediaTray, PaperSource> map2 = new HashMap<>();
            map2.put(MediaTray.MAIN, PaperSource.MAIN);
            map2.put(MediaTray.MANUAL, PaperSource.MANUAL);
            map2.put(MediaTray.BOTTOM, PaperSource.BOTTOM);
            map2.put(MediaTray.MIDDLE, PaperSource.MIDDLE);
            map2.put(MediaTray.TOP, PaperSource.TOP);
            map2.put(MediaTray.SIDE, PaperSource.SIDE);
            map2.put(MediaTray.ENVELOPE, PaperSource.ENVELOPE);
            map2.put(MediaTray.LARGE_CAPACITY, PaperSource.LARGE_CAPACITY);
            preDefinedTrayMap = map2;
        }
    }

    private void populateMedia() {
        initPrefinedMediaMaps();
        if (this.paperSet != null) {
            return;
        }
        Media[] media = (Media[]) this.service.getSupportedAttributeValues(Media.class, null, null);
        Set<Paper> pSet = new TreeSet<>(PaperComparator.theComparator);
        Set<PaperSource> tSet = new TreeSet<>(PaperSourceComparator.theComparator);
        if (media != null) {
            for (Media m2 : media) {
                if (m2 instanceof MediaSizeName) {
                    pSet.add(addPaper((MediaSizeName) m2));
                } else if (m2 instanceof MediaTray) {
                    tSet.add(addPaperSource((MediaTray) m2));
                }
            }
        }
        this.paperSet = Collections.unmodifiableSet(pSet);
        this.paperSourceSet = Collections.unmodifiableSet(tSet);
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public PaperSource defaultPaperSource() {
        if (this.defPaperSource != null) {
            return this.defPaperSource;
        }
        this.defPaperSource = PaperSource.AUTOMATIC;
        return this.defPaperSource;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Set<PaperSource> supportedPaperSources() {
        if (this.paperSourceSet == null) {
            populateMedia();
        }
        return this.paperSourceSet;
    }

    final synchronized PaperSource getPaperSource(MediaTray tray) {
        if (this.paperSourceSet == null) {
            populateMedia();
        }
        PaperSource source = this.trayToSourceMap.get(tray);
        if (source != null) {
            return source;
        }
        return addPaperSource(tray);
    }

    MediaTray getTrayForPaperSource(PaperSource source) {
        if (this.paperSourceSet == null) {
            populateMedia();
        }
        return this.sourceToTrayMap.get(source);
    }

    private final synchronized PaperSource addPaperSource(MediaTray tray) {
        PaperSource source = preDefinedTrayMap.get(tray);
        if (source == null) {
            source = PrintHelper.createPaperSource(tray.toString());
        }
        if (this.trayToSourceMap == null) {
            this.trayToSourceMap = new HashMap();
        }
        this.trayToSourceMap.put(tray, source);
        if (this.sourceToTrayMap == null) {
            this.sourceToTrayMap = new HashMap();
        }
        this.sourceToTrayMap.put(source, tray);
        return source;
    }

    private final synchronized Paper addPaper(MediaSizeName media) {
        MediaSize sz;
        if (this.mediaToPaperMap == null) {
            this.mediaToPaperMap = new HashMap();
            this.paperToMediaMap = new HashMap();
        }
        Paper paper = predefinedPaperMap.get(media);
        if (paper == null && (sz = MediaSize.getMediaSizeForName(media)) != null) {
            double pw = sz.getX(1) / 1000.0d;
            double ph = sz.getY(1) / 1000.0d;
            paper = PrintHelper.createPaper(media.toString(), pw, ph, Units.MM);
        }
        if (paper == null) {
            paper = Paper.NA_LETTER;
        }
        this.paperToMediaMap.put(paper, media);
        this.mediaToPaperMap.put(media, paper);
        return paper;
    }

    private Paper getPaper(MediaSizeName m2) {
        populateMedia();
        Paper paper = this.mediaToPaperMap.get(m2);
        if (paper == null) {
            paper = Paper.NA_LETTER;
        }
        return paper;
    }

    private MediaSizeName getMediaSizeName(Paper paper) {
        populateMedia();
        MediaSizeName m2 = this.paperToMediaMap.get(paper);
        if (m2 == null) {
            m2 = MediaSize.findMedia((float) paper.getWidth(), (float) paper.getHeight(), 352);
        }
        return m2;
    }

    @Override // com.sun.javafx.print.PrinterImpl
    public Rectangle2D printableArea(Paper paper) {
        double iw;
        double ih;
        Rectangle2D area = null;
        Attribute msn = getMediaSizeName(paper);
        if (msn != null) {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(msn);
            MediaPrintableArea[] mpa = (MediaPrintableArea[]) this.service.getSupportedAttributeValues(MediaPrintableArea.class, null, pras);
            if (mpa != null && mpa.length > 0 && mpa[0] != null) {
                area = new Rectangle2D(mpa[0].getX(25400), mpa[0].getY(25400), mpa[0].getWidth(25400), mpa[0].getHeight(25400));
            }
        }
        if (area == null) {
            double pw = paper.getWidth() / 72.0d;
            double ph = paper.getHeight() / 72.0d;
            if (pw < 3.0d) {
                iw = 0.75d * pw;
            } else {
                iw = pw - 1.5d;
            }
            if (ph < 3.0d) {
                ih = 0.75d * ph;
            } else {
                ih = ph - 1.5d;
            }
            double lm = (pw - iw) / 2.0d;
            double tm = (ph - ih) / 2.0d;
            area = new Rectangle2D(lm, tm, iw, ih);
        }
        return area;
    }

    PageLayout defaultPageLayout() {
        if (this.defaultLayout == null) {
            Paper paper = defaultPaper();
            PageOrientation orient = defaultOrientation();
            this.defaultLayout = this.fxPrinter.createPageLayout(paper, orient, Printer.MarginType.DEFAULT);
        }
        return this.defaultLayout;
    }
}
