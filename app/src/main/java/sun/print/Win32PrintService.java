package sun.print;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.Window;
import java.awt.print.PrinterJob;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.QueuedJobCount;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.Severity;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintServiceAttributeListener;
import sun.awt.windows.WPrinterJob;

/* loaded from: rt.jar:sun/print/Win32PrintService.class */
public class Win32PrintService implements PrintService, AttributeUpdater, SunPrinterJobService {
    public static MediaSize[] predefMedia = Win32MediaSize.getPredefMedia();
    private static final DocFlavor[] supportedFlavors = {DocFlavor.BYTE_ARRAY.GIF, DocFlavor.INPUT_STREAM.GIF, DocFlavor.URL.GIF, DocFlavor.BYTE_ARRAY.JPEG, DocFlavor.INPUT_STREAM.JPEG, DocFlavor.URL.JPEG, DocFlavor.BYTE_ARRAY.PNG, DocFlavor.INPUT_STREAM.PNG, DocFlavor.URL.PNG, DocFlavor.SERVICE_FORMATTED.PAGEABLE, DocFlavor.SERVICE_FORMATTED.PRINTABLE, DocFlavor.BYTE_ARRAY.AUTOSENSE, DocFlavor.URL.AUTOSENSE, DocFlavor.INPUT_STREAM.AUTOSENSE};
    private static final Class[] serviceAttrCats = {PrinterName.class, PrinterIsAcceptingJobs.class, QueuedJobCount.class, ColorSupported.class};
    private static Class[] otherAttrCats = {JobName.class, RequestingUserName.class, Copies.class, Destination.class, OrientationRequested.class, PageRanges.class, Media.class, MediaPrintableArea.class, Fidelity.class, SheetCollate.class, SunAlternateMedia.class, Chromaticity.class};
    public static final MediaSizeName[] dmPaperToPrintService = {MediaSizeName.NA_LETTER, MediaSizeName.NA_LETTER, MediaSizeName.TABLOID, MediaSizeName.LEDGER, MediaSizeName.NA_LEGAL, MediaSizeName.INVOICE, MediaSizeName.EXECUTIVE, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.JIS_B4, MediaSizeName.JIS_B5, MediaSizeName.FOLIO, MediaSizeName.QUARTO, MediaSizeName.NA_10X14_ENVELOPE, MediaSizeName.f12793B, MediaSizeName.NA_LETTER, MediaSizeName.NA_NUMBER_9_ENVELOPE, MediaSizeName.NA_NUMBER_10_ENVELOPE, MediaSizeName.NA_NUMBER_11_ENVELOPE, MediaSizeName.NA_NUMBER_12_ENVELOPE, MediaSizeName.NA_NUMBER_14_ENVELOPE, MediaSizeName.f12794C, MediaSizeName.f12795D, MediaSizeName.f12796E, MediaSizeName.ISO_DESIGNATED_LONG, MediaSizeName.ISO_C5, MediaSizeName.ISO_C3, MediaSizeName.ISO_C4, MediaSizeName.ISO_C6, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5, MediaSizeName.ISO_B6, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.MONARCH_ENVELOPE, MediaSizeName.PERSONAL_ENVELOPE, MediaSizeName.NA_10X15_ENVELOPE, MediaSizeName.NA_9X12_ENVELOPE, MediaSizeName.FOLIO, MediaSizeName.ISO_B4, MediaSizeName.JAPANESE_POSTCARD, MediaSizeName.NA_9X11_ENVELOPE};
    private static final MediaTray[] dmPaperBinToPrintService = {MediaTray.TOP, MediaTray.BOTTOM, MediaTray.MIDDLE, MediaTray.MANUAL, MediaTray.ENVELOPE, Win32MediaTray.ENVELOPE_MANUAL, Win32MediaTray.AUTO, Win32MediaTray.TRACTOR, Win32MediaTray.SMALL_FORMAT, Win32MediaTray.LARGE_FORMAT, MediaTray.LARGE_CAPACITY, null, null, MediaTray.MAIN, Win32MediaTray.FORMSOURCE};
    private static int DM_PAPERSIZE = 2;
    private static int DM_PRINTQUALITY = 1024;
    private static int DM_YRESOLUTION = 8192;
    private static final int DMRES_MEDIUM = -3;
    private static final int DMRES_HIGH = -4;
    private static final int DMORIENT_LANDSCAPE = 2;
    private static final int DMDUP_VERTICAL = 2;
    private static final int DMDUP_HORIZONTAL = 3;
    private static final int DMCOLLATE_TRUE = 1;
    private static final int DMCOLOR_MONOCHROME = 1;
    private static final int DMCOLOR_COLOR = 2;
    private static final int DMPAPER_A2 = 66;
    private static final int DMPAPER_A6 = 70;
    private static final int DMPAPER_B6_JIS = 88;
    private static final int DEVCAP_COLOR = 1;
    private static final int DEVCAP_DUPLEX = 2;
    private static final int DEVCAP_COLLATE = 4;
    private static final int DEVCAP_QUALITY = 8;
    private static final int DEVCAP_POSTSCRIPT = 16;
    private String printer;
    private PrinterName name;
    private String port;
    private transient PrintServiceAttributeSet lastSet;
    private MediaSizeName[] mediaSizeNames;
    private MediaPrintableArea[] mediaPrintables;
    private MediaTray[] mediaTrays;
    private PrinterResolution[] printRes;
    private HashMap mpaMap;
    private int nCopies;
    private int prnCaps;
    private int[] defaultSettings;
    private boolean gotTrays;
    private boolean gotCopies;
    private boolean mediaInitialized;
    private boolean mpaListInitialized;
    private ArrayList idList;
    private MediaSize[] mediaSizes;
    private boolean isInvalid;
    private transient ServiceNotifier notifier = null;
    private Win32DocumentPropertiesUI docPropertiesUI = null;
    private Win32ServiceUIFactory uiFactory = null;

    private native int[] getAllMediaIDs(String str, String str2);

    private native int[] getAllMediaSizes(String str, String str2);

    private native int[] getAllMediaTrays(String str, String str2);

    private native float[] getMediaPrintableArea(String str, int i2);

    private native String[] getAllMediaNames(String str, String str2);

    private native String[] getAllMediaTrayNames(String str, String str2);

    private native int getCopiesSupported(String str, String str2);

    private native int[] getAllResolutions(String str, String str2);

    private native int getCapabilities(String str, String str2);

    private native int[] getDefaultSettings(String str, String str2);

    private native int getJobStatus(String str, int i2);

    private native String getPrinterPort(String str);

    Win32PrintService(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null printer name");
        }
        this.printer = str;
        this.mediaInitialized = false;
        this.gotTrays = false;
        this.gotCopies = false;
        this.isInvalid = false;
        this.printRes = null;
        this.prnCaps = 0;
        this.defaultSettings = null;
        this.port = null;
    }

    public void invalidateService() {
        this.isInvalid = true;
    }

    @Override // javax.print.PrintService
    public String getName() {
        return this.printer;
    }

    private PrinterName getPrinterName() {
        if (this.name == null) {
            this.name = new PrinterName(this.printer, null);
        }
        return this.name;
    }

    public int findPaperID(MediaSizeName mediaSizeName) {
        if (mediaSizeName instanceof Win32MediaSize) {
            return ((Win32MediaSize) mediaSizeName).getDMPaper();
        }
        for (int i2 = 0; i2 < dmPaperToPrintService.length; i2++) {
            if (dmPaperToPrintService[i2].equals(mediaSizeName)) {
                return i2 + 1;
            }
        }
        if (mediaSizeName.equals(MediaSizeName.ISO_A2)) {
            return 66;
        }
        if (mediaSizeName.equals(MediaSizeName.ISO_A6)) {
            return 70;
        }
        if (mediaSizeName.equals(MediaSizeName.JIS_B6)) {
            return 88;
        }
        initMedia();
        if (this.idList != null && this.mediaSizes != null && this.idList.size() == this.mediaSizes.length) {
            for (int i3 = 0; i3 < this.idList.size(); i3++) {
                if (this.mediaSizes[i3].getMediaSizeName() == mediaSizeName) {
                    return ((Integer) this.idList.get(i3)).intValue();
                }
            }
            return 0;
        }
        return 0;
    }

    public int findTrayID(MediaTray mediaTray) {
        getMediaTrays();
        if (mediaTray instanceof Win32MediaTray) {
            return ((Win32MediaTray) mediaTray).getDMBinID();
        }
        for (int i2 = 0; i2 < dmPaperBinToPrintService.length; i2++) {
            if (mediaTray.equals(dmPaperBinToPrintService[i2])) {
                return i2 + 1;
            }
        }
        return 0;
    }

    public MediaTray findMediaTray(int i2) {
        if (i2 >= 1 && i2 <= dmPaperBinToPrintService.length) {
            return dmPaperBinToPrintService[i2 - 1];
        }
        MediaTray[] mediaTrays = getMediaTrays();
        if (mediaTrays != null) {
            for (int i3 = 0; i3 < mediaTrays.length; i3++) {
                if (mediaTrays[i3] instanceof Win32MediaTray) {
                    Win32MediaTray win32MediaTray = (Win32MediaTray) mediaTrays[i3];
                    if (win32MediaTray.winID == i2) {
                        return win32MediaTray;
                    }
                }
            }
        }
        return Win32MediaTray.AUTO;
    }

    public MediaSizeName findWin32Media(int i2) {
        if (i2 >= 1 && i2 <= dmPaperToPrintService.length) {
            return dmPaperToPrintService[i2 - 1];
        }
        switch (i2) {
            case 66:
                return MediaSizeName.ISO_A2;
            case 70:
                return MediaSizeName.ISO_A6;
            case 88:
                return MediaSizeName.JIS_B6;
            default:
                return null;
        }
    }

    private boolean addToUniqueList(ArrayList arrayList, MediaSizeName mediaSizeName) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((MediaSizeName) arrayList.get(i2)) == mediaSizeName) {
                return false;
            }
        }
        arrayList.add(mediaSizeName);
        return true;
    }

    private synchronized void initMedia() {
        if (this.mediaInitialized) {
            return;
        }
        this.mediaInitialized = true;
        int[] allMediaIDs = getAllMediaIDs(this.printer, getPort());
        if (allMediaIDs == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        new ArrayList();
        this.idList = new ArrayList();
        for (int i2 : allMediaIDs) {
            this.idList.add(Integer.valueOf(i2));
        }
        ArrayList<String> arrayList3 = new ArrayList<>();
        this.mediaSizes = getMediaSizes(this.idList, allMediaIDs, arrayList3);
        for (int i3 = 0; i3 < this.idList.size(); i3++) {
            MediaSizeName mediaSizeNameFindWin32Media = findWin32Media(((Integer) this.idList.get(i3)).intValue());
            if (mediaSizeNameFindWin32Media != null && this.idList.size() == this.mediaSizes.length) {
                MediaSize mediaSizeForName = MediaSize.getMediaSizeForName(mediaSizeNameFindWin32Media);
                MediaSize mediaSize = this.mediaSizes[i3];
                if (Math.abs(mediaSizeForName.getX(1) - mediaSize.getX(1)) > 2540 || Math.abs(mediaSizeForName.getY(1) - mediaSize.getY(1)) > 2540) {
                    mediaSizeNameFindWin32Media = null;
                }
            }
            boolean z2 = mediaSizeNameFindWin32Media != null;
            if (mediaSizeNameFindWin32Media == null && this.idList.size() == this.mediaSizes.length) {
                mediaSizeNameFindWin32Media = this.mediaSizes[i3].getMediaSizeName();
            }
            boolean zAddToUniqueList = false;
            if (mediaSizeNameFindWin32Media != null) {
                zAddToUniqueList = addToUniqueList(arrayList, mediaSizeNameFindWin32Media);
            }
            if ((!z2 || !zAddToUniqueList) && this.idList.size() == arrayList3.size()) {
                Win32MediaSize win32MediaSizeFindMediaName = Win32MediaSize.findMediaName(arrayList3.get(i3));
                if (win32MediaSizeFindMediaName == null && this.idList.size() == this.mediaSizes.length) {
                    win32MediaSizeFindMediaName = new Win32MediaSize(arrayList3.get(i3), ((Integer) this.idList.get(i3)).intValue());
                    this.mediaSizes[i3] = new MediaSize(this.mediaSizes[i3].getX(1000), this.mediaSizes[i3].getY(1000), 1000, win32MediaSizeFindMediaName);
                }
                if (win32MediaSizeFindMediaName != null && win32MediaSizeFindMediaName != mediaSizeNameFindWin32Media) {
                    if (!zAddToUniqueList) {
                        addToUniqueList(arrayList, win32MediaSizeFindMediaName);
                    } else {
                        arrayList2.add(win32MediaSizeFindMediaName);
                    }
                }
            }
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            addToUniqueList(arrayList, (Win32MediaSize) it.next());
        }
        this.mediaSizeNames = new MediaSizeName[arrayList.size()];
        arrayList.toArray(this.mediaSizeNames);
    }

    private synchronized MediaPrintableArea[] getMediaPrintables(MediaSizeName mediaSizeName) {
        MediaSizeName[] mediaSizeNameArr;
        if (mediaSizeName == null) {
            if (this.mpaListInitialized) {
                return this.mediaPrintables;
            }
        } else if (this.mpaMap != null && this.mpaMap.get(mediaSizeName) != null) {
            return new MediaPrintableArea[]{(MediaPrintableArea) this.mpaMap.get(mediaSizeName)};
        }
        initMedia();
        if (this.mediaSizeNames == null || this.mediaSizeNames.length == 0) {
            return null;
        }
        if (mediaSizeName != null) {
            mediaSizeNameArr = new MediaSizeName[]{mediaSizeName};
        } else {
            mediaSizeNameArr = this.mediaSizeNames;
        }
        if (this.mpaMap == null) {
            this.mpaMap = new HashMap();
        }
        for (MediaSizeName mediaSizeName2 : mediaSizeNameArr) {
            if (this.mpaMap.get(mediaSizeName2) == null && mediaSizeName2 != null) {
                int iFindPaperID = findPaperID(mediaSizeName2);
                float[] mediaPrintableArea = iFindPaperID != 0 ? getMediaPrintableArea(this.printer, iFindPaperID) : null;
                if (mediaPrintableArea != null) {
                    try {
                        this.mpaMap.put(mediaSizeName2, new MediaPrintableArea(mediaPrintableArea[0], mediaPrintableArea[1], mediaPrintableArea[2], mediaPrintableArea[3], 25400));
                    } catch (IllegalArgumentException e2) {
                    }
                } else {
                    MediaSize mediaSizeForName = MediaSize.getMediaSizeForName(mediaSizeName2);
                    if (mediaSizeForName != null) {
                        try {
                            this.mpaMap.put(mediaSizeName2, new MediaPrintableArea(0.0f, 0.0f, mediaSizeForName.getX(25400), mediaSizeForName.getY(25400), 25400));
                        } catch (IllegalArgumentException e3) {
                        }
                    }
                }
            }
        }
        if (this.mpaMap.size() == 0) {
            return null;
        }
        if (mediaSizeName != null) {
            if (this.mpaMap.get(mediaSizeName) == null) {
                return null;
            }
            return new MediaPrintableArea[]{(MediaPrintableArea) this.mpaMap.get(mediaSizeName)};
        }
        this.mediaPrintables = (MediaPrintableArea[]) this.mpaMap.values().toArray(new MediaPrintableArea[0]);
        this.mpaListInitialized = true;
        return this.mediaPrintables;
    }

    private synchronized MediaTray[] getMediaTrays() {
        if (this.gotTrays && this.mediaTrays != null) {
            return this.mediaTrays;
        }
        String port = getPort();
        int[] allMediaTrays = getAllMediaTrays(this.printer, port);
        String[] allMediaTrayNames = getAllMediaTrayNames(this.printer, port);
        if (allMediaTrays == null || allMediaTrayNames == null) {
            return null;
        }
        int i2 = 0;
        for (int i3 : allMediaTrays) {
            if (i3 > 0) {
                i2++;
            }
        }
        MediaTray[] mediaTrayArr = new MediaTray[i2];
        int i4 = 0;
        for (int i5 = 0; i5 < Math.min(allMediaTrays.length, allMediaTrayNames.length); i5++) {
            int i6 = allMediaTrays[i5];
            if (i6 > 0) {
                if (i6 > dmPaperBinToPrintService.length || dmPaperBinToPrintService[i6 - 1] == null) {
                    int i7 = i4;
                    i4++;
                    mediaTrayArr[i7] = new Win32MediaTray(i6, allMediaTrayNames[i5]);
                } else {
                    int i8 = i4;
                    i4++;
                    mediaTrayArr[i8] = dmPaperBinToPrintService[i6 - 1];
                }
            }
        }
        this.mediaTrays = mediaTrayArr;
        this.gotTrays = true;
        return this.mediaTrays;
    }

    private boolean isSameSize(float f2, float f3, float f4, float f5) {
        float f6 = f3 - f5;
        float f7 = f2 - f5;
        float f8 = f3 - f4;
        if (Math.abs(f2 - f4) <= 1.0f && Math.abs(f6) <= 1.0f) {
            return true;
        }
        if (Math.abs(f7) <= 1.0f && Math.abs(f8) <= 1.0f) {
            return true;
        }
        return false;
    }

    public MediaSizeName findMatchingMediaSizeNameMM(float f2, float f3) {
        if (predefMedia != null) {
            for (int i2 = 0; i2 < predefMedia.length; i2++) {
                if (predefMedia[i2] != null && isSameSize(predefMedia[i2].getX(1000), predefMedia[i2].getY(1000), f2, f3)) {
                    return predefMedia[i2].getMediaSizeName();
                }
            }
            return null;
        }
        return null;
    }

    private MediaSize[] getMediaSizes(ArrayList arrayList, int[] iArr, ArrayList<String> arrayList2) {
        if (arrayList2 == null) {
            arrayList2 = new ArrayList<>();
        }
        String port = getPort();
        int[] allMediaSizes = getAllMediaSizes(this.printer, port);
        String[] allMediaNames = getAllMediaNames(this.printer, port);
        MediaSize mediaSizeForName = null;
        if (allMediaSizes == null || allMediaNames == null) {
            return null;
        }
        int length = allMediaSizes.length / 2;
        ArrayList arrayList3 = new ArrayList();
        int i2 = 0;
        while (i2 < length) {
            float f2 = allMediaSizes[i2 * 2] / 10.0f;
            float f3 = allMediaSizes[(i2 * 2) + 1] / 10.0f;
            if (f2 <= 0.0f || f3 <= 0.0f) {
                if (length == iArr.length) {
                    arrayList.remove(arrayList.indexOf(Integer.valueOf(iArr[i2])));
                }
            } else {
                MediaSizeName mediaSizeNameFindMatchingMediaSizeNameMM = findMatchingMediaSizeNameMM(f2, f3);
                if (mediaSizeNameFindMatchingMediaSizeNameMM != null) {
                    mediaSizeForName = MediaSize.getMediaSizeForName(mediaSizeNameFindMatchingMediaSizeNameMM);
                }
                if (mediaSizeForName != null) {
                    arrayList3.add(mediaSizeForName);
                    arrayList2.add(allMediaNames[i2]);
                } else {
                    Win32MediaSize win32MediaSizeFindMediaName = Win32MediaSize.findMediaName(allMediaNames[i2]);
                    if (win32MediaSizeFindMediaName == null) {
                        win32MediaSizeFindMediaName = new Win32MediaSize(allMediaNames[i2], iArr[i2]);
                    }
                    try {
                        arrayList3.add(new MediaSize(f2, f3, 1000, win32MediaSizeFindMediaName));
                        arrayList2.add(allMediaNames[i2]);
                    } catch (IllegalArgumentException e2) {
                        if (length == iArr.length) {
                            arrayList.remove(arrayList.indexOf(Integer.valueOf(iArr[i2])));
                        }
                    }
                }
            }
            i2++;
            mediaSizeForName = null;
        }
        MediaSize[] mediaSizeArr = new MediaSize[arrayList3.size()];
        arrayList3.toArray(mediaSizeArr);
        return mediaSizeArr;
    }

    private PrinterIsAcceptingJobs getPrinterIsAcceptingJobs() {
        if (getJobStatus(this.printer, 2) != 1) {
            return PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS;
        }
        return PrinterIsAcceptingJobs.ACCEPTING_JOBS;
    }

    private PrinterState getPrinterState() {
        if (this.isInvalid) {
            return PrinterState.STOPPED;
        }
        return null;
    }

    private PrinterStateReasons getPrinterStateReasons() {
        if (this.isInvalid) {
            PrinterStateReasons printerStateReasons = new PrinterStateReasons();
            printerStateReasons.put(PrinterStateReason.SHUTDOWN, Severity.ERROR);
            return printerStateReasons;
        }
        return null;
    }

    private QueuedJobCount getQueuedJobCount() {
        int jobStatus = getJobStatus(this.printer, 1);
        if (jobStatus != -1) {
            return new QueuedJobCount(jobStatus);
        }
        return new QueuedJobCount(0);
    }

    private boolean isSupportedCopies(Copies copies) {
        synchronized (this) {
            if (!this.gotCopies) {
                this.nCopies = getCopiesSupported(this.printer, getPort());
                this.gotCopies = true;
            }
        }
        int value = copies.getValue();
        return value > 0 && value <= this.nCopies;
    }

    private boolean isSupportedMedia(MediaSizeName mediaSizeName) {
        initMedia();
        if (this.mediaSizeNames != null) {
            for (int i2 = 0; i2 < this.mediaSizeNames.length; i2++) {
                if (mediaSizeName.equals(this.mediaSizeNames[i2])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean isSupportedMediaPrintableArea(MediaPrintableArea mediaPrintableArea) {
        getMediaPrintables(null);
        if (this.mediaPrintables != null) {
            for (int i2 = 0; i2 < this.mediaPrintables.length; i2++) {
                if (mediaPrintableArea.equals(this.mediaPrintables[i2])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean isSupportedMediaTray(MediaTray mediaTray) {
        MediaTray[] mediaTrays = getMediaTrays();
        if (mediaTrays != null) {
            for (MediaTray mediaTray2 : mediaTrays) {
                if (mediaTray.equals(mediaTray2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private int getPrinterCapabilities() {
        if (this.prnCaps == 0) {
            this.prnCaps = getCapabilities(this.printer, getPort());
        }
        return this.prnCaps;
    }

    private String getPort() {
        if (this.port == null) {
            this.port = getPrinterPort(this.printer);
        }
        return this.port;
    }

    private int[] getDefaultPrinterSettings() {
        if (this.defaultSettings == null) {
            this.defaultSettings = getDefaultSettings(this.printer, getPort());
        }
        return this.defaultSettings;
    }

    private PrinterResolution[] getPrintResolutions() {
        if (this.printRes == null) {
            int[] allResolutions = getAllResolutions(this.printer, getPort());
            if (allResolutions == null) {
                this.printRes = new PrinterResolution[0];
            } else {
                int length = allResolutions.length / 2;
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < length; i2++) {
                    try {
                        arrayList.add(new PrinterResolution(allResolutions[i2 * 2], allResolutions[(i2 * 2) + 1], 100));
                    } catch (IllegalArgumentException e2) {
                    }
                }
                this.printRes = (PrinterResolution[]) arrayList.toArray(new PrinterResolution[arrayList.size()]);
            }
        }
        return this.printRes;
    }

    private boolean isSupportedResolution(PrinterResolution printerResolution) {
        PrinterResolution[] printResolutions = getPrintResolutions();
        if (printResolutions != null) {
            for (PrinterResolution printerResolution2 : printResolutions) {
                if (printerResolution.equals(printerResolution2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // javax.print.PrintService
    public DocPrintJob createPrintJob() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        return new Win32PrintJob(this);
    }

    private PrintServiceAttributeSet getDynamicAttributes() {
        HashPrintServiceAttributeSet hashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
        hashPrintServiceAttributeSet.add(getPrinterIsAcceptingJobs());
        hashPrintServiceAttributeSet.add(getQueuedJobCount());
        return hashPrintServiceAttributeSet;
    }

    @Override // sun.print.AttributeUpdater
    public PrintServiceAttributeSet getUpdatedAttributes() {
        PrintServiceAttributeSet dynamicAttributes = getDynamicAttributes();
        if (this.lastSet == null) {
            this.lastSet = dynamicAttributes;
            return AttributeSetUtilities.unmodifiableView(dynamicAttributes);
        }
        HashPrintServiceAttributeSet hashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
        for (Attribute attribute : dynamicAttributes.toArray()) {
            if (!this.lastSet.containsValue(attribute)) {
                hashPrintServiceAttributeSet.add(attribute);
            }
        }
        this.lastSet = dynamicAttributes;
        return AttributeSetUtilities.unmodifiableView((PrintServiceAttributeSet) hashPrintServiceAttributeSet);
    }

    public void wakeNotifier() {
        synchronized (this) {
            if (this.notifier != null) {
                this.notifier.wake();
            }
        }
    }

    @Override // javax.print.PrintService
    public void addPrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener) {
        synchronized (this) {
            if (printServiceAttributeListener == null) {
                return;
            }
            if (this.notifier == null) {
                this.notifier = new ServiceNotifier(this);
            }
            this.notifier.addListener(printServiceAttributeListener);
        }
    }

    @Override // javax.print.PrintService
    public void removePrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener) {
        synchronized (this) {
            if (printServiceAttributeListener != null) {
                if (this.notifier != null) {
                    this.notifier.removeListener(printServiceAttributeListener);
                    if (this.notifier.isEmpty()) {
                        this.notifier.stopNotifier();
                        this.notifier = null;
                    }
                }
            }
        }
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
            if ((getPrinterCapabilities() & 1) != 0) {
                return ColorSupported.SUPPORTED;
            }
            return ColorSupported.NOT_SUPPORTED;
        }
        if (cls == PrinterName.class) {
            return getPrinterName();
        }
        if (cls == PrinterState.class) {
            return getPrinterState();
        }
        if (cls == PrinterStateReasons.class) {
            return getPrinterStateReasons();
        }
        if (cls == QueuedJobCount.class) {
            return getQueuedJobCount();
        }
        if (cls == PrinterIsAcceptingJobs.class) {
            return getPrinterIsAcceptingJobs();
        }
        return null;
    }

    @Override // javax.print.PrintService
    public PrintServiceAttributeSet getAttributes() {
        HashPrintServiceAttributeSet hashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
        hashPrintServiceAttributeSet.add(getPrinterName());
        hashPrintServiceAttributeSet.add(getPrinterIsAcceptingJobs());
        PrinterState printerState = getPrinterState();
        if (printerState != null) {
            hashPrintServiceAttributeSet.add(printerState);
        }
        PrinterStateReasons printerStateReasons = getPrinterStateReasons();
        if (printerStateReasons != null) {
            hashPrintServiceAttributeSet.add(printerStateReasons);
        }
        hashPrintServiceAttributeSet.add(getQueuedJobCount());
        if ((getPrinterCapabilities() & 1) != 0) {
            hashPrintServiceAttributeSet.add(ColorSupported.SUPPORTED);
        } else {
            hashPrintServiceAttributeSet.add(ColorSupported.NOT_SUPPORTED);
        }
        return AttributeSetUtilities.unmodifiableView((PrintServiceAttributeSet) hashPrintServiceAttributeSet);
    }

    @Override // javax.print.PrintService
    public DocFlavor[] getSupportedDocFlavors() {
        DocFlavor[] docFlavorArr;
        int length = supportedFlavors.length;
        if ((getPrinterCapabilities() & 16) != 0) {
            docFlavorArr = new DocFlavor[length + 3];
            System.arraycopy(supportedFlavors, 0, docFlavorArr, 0, length);
            docFlavorArr[length] = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
            docFlavorArr[length + 1] = DocFlavor.INPUT_STREAM.POSTSCRIPT;
            docFlavorArr[length + 2] = DocFlavor.URL.POSTSCRIPT;
        } else {
            docFlavorArr = new DocFlavor[length];
            System.arraycopy(supportedFlavors, 0, docFlavorArr, 0, length);
        }
        return docFlavorArr;
    }

    @Override // javax.print.PrintService
    public boolean isDocFlavorSupported(DocFlavor docFlavor) {
        DocFlavor[] supportedDocFlavors;
        if (isPostScriptFlavor(docFlavor)) {
            supportedDocFlavors = getSupportedDocFlavors();
        } else {
            supportedDocFlavors = supportedFlavors;
        }
        for (DocFlavor docFlavor2 : supportedDocFlavors) {
            if (docFlavor.equals(docFlavor2)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.print.PrintService
    public Class<?>[] getSupportedAttributeCategories() {
        ArrayList arrayList = new ArrayList(otherAttrCats.length + 3);
        for (int i2 = 0; i2 < otherAttrCats.length; i2++) {
            arrayList.add(otherAttrCats[i2]);
        }
        int printerCapabilities = getPrinterCapabilities();
        if ((printerCapabilities & 2) != 0) {
            arrayList.add(Sides.class);
        }
        if ((printerCapabilities & 8) != 0) {
            int[] defaultPrinterSettings = getDefaultPrinterSettings();
            if (defaultPrinterSettings[3] >= -4 && defaultPrinterSettings[3] < 0) {
                arrayList.add(PrintQuality.class);
            }
        }
        PrinterResolution[] printResolutions = getPrintResolutions();
        if (printResolutions != null && printResolutions.length > 0) {
            arrayList.add(PrinterResolution.class);
        }
        return (Class[]) arrayList.toArray(new Class[arrayList.size()]);
    }

    @Override // javax.print.PrintService
    public boolean isAttributeCategorySupported(Class<? extends Attribute> cls) {
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " is not an Attribute");
        }
        for (Class<?> cls2 : getSupportedAttributeCategories()) {
            if (cls.equals(cls2)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.print.PrintService
    public Object getDefaultAttributeValue(Class<? extends Attribute> cls) {
        int i2;
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " is not an Attribute");
        }
        if (!isAttributeCategorySupported(cls)) {
            return null;
        }
        int[] defaultPrinterSettings = getDefaultPrinterSettings();
        int iFindPaperID = defaultPrinterSettings[0];
        int i3 = defaultPrinterSettings[2];
        int i4 = defaultPrinterSettings[3];
        int i5 = defaultPrinterSettings[4];
        int i6 = defaultPrinterSettings[5];
        int i7 = defaultPrinterSettings[6];
        int i8 = defaultPrinterSettings[7];
        int i9 = defaultPrinterSettings[8];
        if (cls == Copies.class) {
            if (i5 > 0) {
                return new Copies(i5);
            }
            return new Copies(1);
        }
        if (cls == Chromaticity.class) {
            if (i9 == 2) {
                return Chromaticity.COLOR;
            }
            return Chromaticity.MONOCHROME;
        }
        if (cls == JobName.class) {
            return new JobName("Java Printing", null);
        }
        if (cls == OrientationRequested.class) {
            if (i6 == 2) {
                return OrientationRequested.LANDSCAPE;
            }
            return OrientationRequested.PORTRAIT;
        }
        if (cls == PageRanges.class) {
            return new PageRanges(1, Integer.MAX_VALUE);
        }
        if (cls == Media.class) {
            MediaSizeName mediaSizeNameFindWin32Media = findWin32Media(iFindPaperID);
            if (mediaSizeNameFindWin32Media != null) {
                if (!isSupportedMedia(mediaSizeNameFindWin32Media) && this.mediaSizeNames != null) {
                    mediaSizeNameFindWin32Media = this.mediaSizeNames[0];
                    findPaperID(mediaSizeNameFindWin32Media);
                }
                return mediaSizeNameFindWin32Media;
            }
            initMedia();
            if (this.mediaSizeNames != null && this.mediaSizeNames.length > 0) {
                if (this.idList != null && this.mediaSizes != null && this.idList.size() == this.mediaSizes.length) {
                    int iIndexOf = this.idList.indexOf(Integer.valueOf(iFindPaperID));
                    if (iIndexOf >= 0 && iIndexOf < this.mediaSizes.length) {
                        return this.mediaSizes[iIndexOf].getMediaSizeName();
                    }
                }
                return this.mediaSizeNames[0];
            }
            return null;
        }
        if (cls == MediaPrintableArea.class) {
            MediaSizeName mediaSizeNameFindWin32Media2 = findWin32Media(iFindPaperID);
            if (mediaSizeNameFindWin32Media2 != null && !isSupportedMedia(mediaSizeNameFindWin32Media2) && this.mediaSizeNames != null) {
                iFindPaperID = findPaperID(this.mediaSizeNames[0]);
            }
            float[] mediaPrintableArea = getMediaPrintableArea(this.printer, iFindPaperID);
            if (mediaPrintableArea != null) {
                MediaPrintableArea mediaPrintableArea2 = null;
                try {
                    mediaPrintableArea2 = new MediaPrintableArea(mediaPrintableArea[0], mediaPrintableArea[1], mediaPrintableArea[2], mediaPrintableArea[3], 25400);
                } catch (IllegalArgumentException e2) {
                }
                return mediaPrintableArea2;
            }
            return null;
        }
        if (cls == SunAlternateMedia.class) {
            return null;
        }
        if (cls == Destination.class) {
            try {
                return new Destination(new File("out.prn").toURI());
            } catch (SecurityException e3) {
                try {
                    return new Destination(new URI("file:out.prn"));
                } catch (URISyntaxException e4) {
                    return null;
                }
            }
        }
        if (cls == Sides.class) {
            switch (i7) {
                case 2:
                    return Sides.TWO_SIDED_LONG_EDGE;
                case 3:
                    return Sides.TWO_SIDED_SHORT_EDGE;
                default:
                    return Sides.ONE_SIDED;
            }
        }
        if (cls == PrinterResolution.class) {
            if (i4 >= 0 && i3 >= 0) {
                return new PrinterResolution(i4, i3, 100);
            }
            if (i3 > i4) {
                i2 = i3;
            } else {
                i2 = i4;
            }
            int i10 = i2;
            if (i10 > 0) {
                return new PrinterResolution(i10, i10, 100);
            }
            return null;
        }
        if (cls == ColorSupported.class) {
            if ((getPrinterCapabilities() & 1) != 0) {
                return ColorSupported.SUPPORTED;
            }
            return ColorSupported.NOT_SUPPORTED;
        }
        if (cls == PrintQuality.class) {
            if (i4 < 0 && i4 >= -4) {
                switch (i4) {
                    case -4:
                        return PrintQuality.HIGH;
                    case -3:
                        return PrintQuality.NORMAL;
                    default:
                        return PrintQuality.DRAFT;
                }
            }
            return null;
        }
        if (cls == RequestingUserName.class) {
            String property = "";
            try {
                property = System.getProperty("user.name", "");
            } catch (SecurityException e5) {
            }
            return new RequestingUserName(property, null);
        }
        if (cls == SheetCollate.class) {
            if (i8 == 1) {
                return SheetCollate.COLLATED;
            }
            return SheetCollate.UNCOLLATED;
        }
        if (cls == Fidelity.class) {
            return Fidelity.FIDELITY_FALSE;
        }
        return null;
    }

    private boolean isPostScriptFlavor(DocFlavor docFlavor) {
        if (docFlavor.equals(DocFlavor.BYTE_ARRAY.POSTSCRIPT) || docFlavor.equals(DocFlavor.INPUT_STREAM.POSTSCRIPT) || docFlavor.equals(DocFlavor.URL.POSTSCRIPT)) {
            return true;
        }
        return false;
    }

    private boolean isPSDocAttr(Class cls) {
        if (cls == OrientationRequested.class || cls == Copies.class) {
            return true;
        }
        return false;
    }

    private boolean isAutoSense(DocFlavor docFlavor) {
        if (docFlavor.equals(DocFlavor.BYTE_ARRAY.AUTOSENSE) || docFlavor.equals(DocFlavor.INPUT_STREAM.AUTOSENSE) || docFlavor.equals(DocFlavor.URL.AUTOSENSE)) {
            return true;
        }
        return false;
    }

    @Override // javax.print.PrintService
    public Object getSupportedAttributeValues(Class<? extends Attribute> cls, DocFlavor docFlavor, AttributeSet attributeSet) {
        if (cls == null) {
            throw new NullPointerException("null category");
        }
        if (!Attribute.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(((Object) cls) + " does not implement Attribute");
        }
        if (docFlavor != null) {
            if (!isDocFlavorSupported(docFlavor)) {
                throw new IllegalArgumentException(((Object) docFlavor) + " is an unsupported flavor");
            }
            if (!isAutoSense(docFlavor)) {
                if (isPostScriptFlavor(docFlavor) && isPSDocAttr(cls)) {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (!isAttributeCategorySupported(cls)) {
            return null;
        }
        if (cls == JobName.class) {
            return new JobName("Java Printing", null);
        }
        if (cls == RequestingUserName.class) {
            String property = "";
            try {
                property = System.getProperty("user.name", "");
            } catch (SecurityException e2) {
            }
            return new RequestingUserName(property, null);
        }
        if (cls == ColorSupported.class) {
            if ((getPrinterCapabilities() & 1) != 0) {
                return ColorSupported.SUPPORTED;
            }
            return ColorSupported.NOT_SUPPORTED;
        }
        if (cls == Chromaticity.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE) || docFlavor.equals(DocFlavor.BYTE_ARRAY.GIF) || docFlavor.equals(DocFlavor.INPUT_STREAM.GIF) || docFlavor.equals(DocFlavor.URL.GIF) || docFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG) || docFlavor.equals(DocFlavor.INPUT_STREAM.JPEG) || docFlavor.equals(DocFlavor.URL.JPEG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.PNG) || docFlavor.equals(DocFlavor.INPUT_STREAM.PNG) || docFlavor.equals(DocFlavor.URL.PNG)) {
                if ((getPrinterCapabilities() & 1) == 0) {
                    return new Chromaticity[]{Chromaticity.MONOCHROME};
                }
                return new Chromaticity[]{Chromaticity.MONOCHROME, Chromaticity.COLOR};
            }
            return null;
        }
        if (cls == Destination.class) {
            try {
                return new Destination(new File("out.prn").toURI());
            } catch (SecurityException e3) {
                try {
                    return new Destination(new URI("file:out.prn"));
                } catch (URISyntaxException e4) {
                    return null;
                }
            }
        }
        if (cls == OrientationRequested.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE) || docFlavor.equals(DocFlavor.INPUT_STREAM.GIF) || docFlavor.equals(DocFlavor.INPUT_STREAM.JPEG) || docFlavor.equals(DocFlavor.INPUT_STREAM.PNG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.GIF) || docFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.PNG) || docFlavor.equals(DocFlavor.URL.GIF) || docFlavor.equals(DocFlavor.URL.JPEG) || docFlavor.equals(DocFlavor.URL.PNG)) {
                return new OrientationRequested[]{OrientationRequested.PORTRAIT, OrientationRequested.LANDSCAPE, OrientationRequested.REVERSE_LANDSCAPE};
            }
            return null;
        }
        if (cls == Copies.class || cls == CopiesSupported.class) {
            synchronized (this) {
                if (!this.gotCopies) {
                    this.nCopies = getCopiesSupported(this.printer, getPort());
                    this.gotCopies = true;
                }
            }
            return new CopiesSupported(1, this.nCopies);
        }
        if (cls == Media.class) {
            initMedia();
            int length = this.mediaSizeNames == null ? 0 : this.mediaSizeNames.length;
            MediaTray[] mediaTrays = getMediaTrays();
            int length2 = length + (mediaTrays == null ? 0 : mediaTrays.length);
            Media[] mediaArr = new Media[length2];
            if (this.mediaSizeNames != null) {
                System.arraycopy(this.mediaSizeNames, 0, mediaArr, 0, this.mediaSizeNames.length);
            }
            if (mediaTrays != null) {
                System.arraycopy(mediaTrays, 0, mediaArr, length2 - mediaTrays.length, mediaTrays.length);
            }
            return mediaArr;
        }
        if (cls == MediaPrintableArea.class) {
            Cloneable cloneable = null;
            if (attributeSet != null) {
                Cloneable cloneable2 = (Media) attributeSet.get(Media.class);
                cloneable = cloneable2;
                if (cloneable2 != null && !(cloneable instanceof MediaSizeName)) {
                    cloneable = null;
                }
            }
            MediaPrintableArea[] mediaPrintables = getMediaPrintables((MediaSizeName) cloneable);
            if (mediaPrintables != null) {
                MediaPrintableArea[] mediaPrintableAreaArr = new MediaPrintableArea[mediaPrintables.length];
                System.arraycopy(mediaPrintables, 0, mediaPrintableAreaArr, 0, mediaPrintables.length);
                return mediaPrintableAreaArr;
            }
            return null;
        }
        if (cls == SunAlternateMedia.class) {
            return new SunAlternateMedia((Media) getDefaultAttributeValue(Media.class));
        }
        if (cls == PageRanges.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new PageRanges[]{new PageRanges(1, Integer.MAX_VALUE)};
            }
            return null;
        }
        if (cls == PrinterResolution.class) {
            PrinterResolution[] printResolutions = getPrintResolutions();
            if (printResolutions == null) {
                return null;
            }
            PrinterResolution[] printerResolutionArr = new PrinterResolution[printResolutions.length];
            System.arraycopy(printResolutions, 0, printerResolutionArr, 0, printResolutions.length);
            return printerResolutionArr;
        }
        if (cls == Sides.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new Sides[]{Sides.ONE_SIDED, Sides.TWO_SIDED_LONG_EDGE, Sides.TWO_SIDED_SHORT_EDGE};
            }
            return null;
        }
        if (cls == PrintQuality.class) {
            return new PrintQuality[]{PrintQuality.DRAFT, PrintQuality.HIGH, PrintQuality.NORMAL};
        }
        if (cls == SheetCollate.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return new SheetCollate[]{SheetCollate.COLLATED, SheetCollate.UNCOLLATED};
            }
            return null;
        }
        if (cls == Fidelity.class) {
            return new Fidelity[]{Fidelity.FIDELITY_FALSE, Fidelity.FIDELITY_TRUE};
        }
        return null;
    }

    @Override // javax.print.PrintService
    public boolean isAttributeValueSupported(Attribute attribute, DocFlavor docFlavor, AttributeSet attributeSet) {
        if (attribute == null) {
            throw new NullPointerException("null attribute");
        }
        Class<? extends Attribute> category = attribute.getCategory();
        if (docFlavor != null) {
            if (!isDocFlavorSupported(docFlavor)) {
                throw new IllegalArgumentException(((Object) docFlavor) + " is an unsupported flavor");
            }
            if (!isAutoSense(docFlavor)) {
                if (isPostScriptFlavor(docFlavor) && isPSDocAttr(category)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (!isAttributeCategorySupported(category)) {
            return false;
        }
        if (category == Chromaticity.class) {
            if (docFlavor == null || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE) || docFlavor.equals(DocFlavor.BYTE_ARRAY.GIF) || docFlavor.equals(DocFlavor.INPUT_STREAM.GIF) || docFlavor.equals(DocFlavor.URL.GIF) || docFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG) || docFlavor.equals(DocFlavor.INPUT_STREAM.JPEG) || docFlavor.equals(DocFlavor.URL.JPEG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.PNG) || docFlavor.equals(DocFlavor.INPUT_STREAM.PNG) || docFlavor.equals(DocFlavor.URL.PNG)) {
                return (getPrinterCapabilities() & 1) != 0 || attribute == Chromaticity.MONOCHROME;
            }
            return false;
        }
        if (category == Copies.class) {
            return isSupportedCopies((Copies) attribute);
        }
        if (category == Destination.class) {
            URI uri = ((Destination) attribute).getURI();
            if (DeploymentDescriptorParser.ATTR_FILE.equals(uri.getScheme()) && !uri.getSchemeSpecificPart().equals("")) {
                return true;
            }
            return false;
        }
        if (category == Media.class) {
            if (attribute instanceof MediaSizeName) {
                return isSupportedMedia((MediaSizeName) attribute);
            }
            if (attribute instanceof MediaTray) {
                return isSupportedMediaTray((MediaTray) attribute);
            }
            return true;
        }
        if (category == MediaPrintableArea.class) {
            return isSupportedMediaPrintableArea((MediaPrintableArea) attribute);
        }
        if (category == SunAlternateMedia.class) {
            return isAttributeValueSupported(((SunAlternateMedia) attribute).getMedia(), docFlavor, attributeSet);
        }
        if (category == PageRanges.class || category == SheetCollate.class || category == Sides.class) {
            if (docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                return false;
            }
            return true;
        }
        if (category == PrinterResolution.class) {
            if (attribute instanceof PrinterResolution) {
                return isSupportedResolution((PrinterResolution) attribute);
            }
            return true;
        }
        if (category == OrientationRequested.class) {
            if (attribute != OrientationRequested.REVERSE_PORTRAIT) {
                if (docFlavor != null && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && !docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE) && !docFlavor.equals(DocFlavor.INPUT_STREAM.GIF) && !docFlavor.equals(DocFlavor.INPUT_STREAM.JPEG) && !docFlavor.equals(DocFlavor.INPUT_STREAM.PNG) && !docFlavor.equals(DocFlavor.BYTE_ARRAY.GIF) && !docFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG) && !docFlavor.equals(DocFlavor.BYTE_ARRAY.PNG) && !docFlavor.equals(DocFlavor.URL.GIF) && !docFlavor.equals(DocFlavor.URL.JPEG) && !docFlavor.equals(DocFlavor.URL.PNG)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        if (category == ColorSupported.class) {
            boolean z2 = (getPrinterCapabilities() & 1) != 0;
            if (!z2 && attribute == ColorSupported.SUPPORTED) {
                return false;
            }
            if (z2 && attribute == ColorSupported.NOT_SUPPORTED) {
                return false;
            }
            return true;
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

    /* loaded from: rt.jar:sun/print/Win32PrintService$Win32DocumentPropertiesUI.class */
    private static class Win32DocumentPropertiesUI extends DocumentPropertiesUI {
        Win32PrintService service;

        private Win32DocumentPropertiesUI(Win32PrintService win32PrintService) {
            this.service = win32PrintService;
        }

        @Override // sun.print.DocumentPropertiesUI
        public PrintRequestAttributeSet showDocumentProperties(PrinterJob printerJob, Window window, PrintService printService, PrintRequestAttributeSet printRequestAttributeSet) {
            if (!(printerJob instanceof WPrinterJob)) {
                return null;
            }
            return ((WPrinterJob) printerJob).showDocumentProperties(window, printService, printRequestAttributeSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized DocumentPropertiesUI getDocumentPropertiesUI() {
        return new Win32DocumentPropertiesUI();
    }

    /* loaded from: rt.jar:sun/print/Win32PrintService$Win32ServiceUIFactory.class */
    private static class Win32ServiceUIFactory extends ServiceUIFactory {
        Win32PrintService service;

        Win32ServiceUIFactory(Win32PrintService win32PrintService) {
            this.service = win32PrintService;
        }

        @Override // javax.print.ServiceUIFactory
        public Object getUI(int i2, String str) {
            if (i2 <= 3) {
                return null;
            }
            if (i2 == 199 && DocumentPropertiesUI.DOCPROPERTIESCLASSNAME.equals(str)) {
                return this.service.getDocumentPropertiesUI();
            }
            throw new IllegalArgumentException("Unsupported role");
        }

        @Override // javax.print.ServiceUIFactory
        public String[] getUIClassNamesForRole(int i2) {
            if (i2 <= 3) {
                return null;
            }
            if (i2 == 199) {
                String[] strArr = new String[0];
                strArr[0] = DocumentPropertiesUI.DOCPROPERTIESCLASSNAME;
                return strArr;
            }
            throw new IllegalArgumentException("Unsupported role");
        }
    }

    @Override // javax.print.PrintService
    public synchronized ServiceUIFactory getServiceUIFactory() {
        if (this.uiFactory == null) {
            this.uiFactory = new Win32ServiceUIFactory(this);
        }
        return this.uiFactory;
    }

    public String toString() {
        return "Win32 Printer : " + getName();
    }

    @Override // javax.print.PrintService
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Win32PrintService) && ((Win32PrintService) obj).getName().equals(getName()));
    }

    @Override // javax.print.PrintService
    public int hashCode() {
        return getClass().hashCode() + getName().hashCode();
    }

    @Override // sun.print.SunPrinterJobService
    public boolean usesClass(Class cls) {
        return cls == WPrinterJob.class;
    }
}
