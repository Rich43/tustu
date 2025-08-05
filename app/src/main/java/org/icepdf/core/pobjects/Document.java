package org.icepdf.core.pobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.SecurityCallback;
import org.icepdf.core.application.ProductInfo;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.io.ConservativeSizingByteArrayOutputStream;
import org.icepdf.core.io.RandomAccessFileInputStream;
import org.icepdf.core.io.SeekableByteArrayInputStream;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.CrossReference;
import org.icepdf.core.pobjects.graphics.WatermarkCallback;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.LazyObjectLoader;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Document.class */
public class Document {
    private static final Logger logger = Logger.getLogger(Document.class.toString());
    private static final String INCREMENTAL_UPDATER = "org.icepdf.core.util.IncrementalUpdater";
    public static boolean foundIncrementalUpdater;
    private WatermarkCallback watermarkCallback;
    private Catalog catalog;
    private PTrailer pTrailer;
    private StateManager stateManager;
    private String origin;
    private String cachedFilePath;
    private SecurityCallback securityCallback;
    private static boolean isCachingEnabled;
    private Library library = null;
    private SeekableInput documentSeekableInput;

    static {
        try {
            Class.forName(INCREMENTAL_UPDATER);
            foundIncrementalUpdater = true;
        } catch (ClassNotFoundException e2) {
            logger.log(Level.WARNING, "PDF write support was not found on the class path");
        }
        isCachingEnabled = Defs.sysPropertyBoolean("org.icepdf.core.streamcache.enabled", true);
    }

    public static String getLibraryVersion() {
        return ProductInfo.PRIMARY + "." + ProductInfo.SECONDARY + "." + ProductInfo.TERTIARY + " " + ProductInfo.RELEASE_TYPE;
    }

    public void setWatermarkCallback(WatermarkCallback watermarkCallback) {
        this.watermarkCallback = watermarkCallback;
    }

    private void setDocumentOrigin(String o2) {
        this.origin = o2;
        if (logger.isLoggable(Level.CONFIG)) {
            logger.config("MEMFREE: " + Runtime.getRuntime().freeMemory() + " of " + Runtime.getRuntime().totalMemory());
            logger.config("LOADING: " + o2);
        }
    }

    private void setDocumentCachedFilePath(String o2) {
        this.cachedFilePath = o2;
    }

    private String getDocumentCachedFilePath() {
        return this.cachedFilePath;
    }

    public void setFile(String filepath) throws PDFException, PDFSecurityException, IOException {
        setDocumentOrigin(filepath);
        RandomAccessFileInputStream rafis = RandomAccessFileInputStream.build(new File(filepath));
        setInputStream(rafis);
    }

    public void setUrl(URL url) throws IOException, PDFException, PDFSecurityException {
        InputStream in = null;
        try {
            URLConnection urlConnection = url.openConnection();
            in = urlConnection.getInputStream();
            String pathOrURL = url.toString();
            setInputStream(in, pathOrURL);
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    public void setInputStream(InputStream in, String pathOrURL) throws IOException, PDFException, PDFSecurityException {
        setDocumentOrigin(pathOrURL);
        if (!isCachingEnabled) {
            ConservativeSizingByteArrayOutputStream byteArrayOutputStream = new ConservativeSizingByteArrayOutputStream(102400);
            byte[] buffer = new byte[4096];
            while (true) {
                int length = in.read(buffer, 0, buffer.length);
                if (length > 0) {
                    byteArrayOutputStream.write(buffer, 0, length);
                } else {
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    int size = byteArrayOutputStream.size();
                    byteArrayOutputStream.trim();
                    byte[] data = byteArrayOutputStream.relinquishByteArray();
                    SeekableByteArrayInputStream byteArrayInputStream = new SeekableByteArrayInputStream(data, 0, size);
                    setInputStream(byteArrayInputStream);
                    return;
                }
            }
        } else {
            File tempFile = File.createTempFile("ICEpdfTempFile" + getClass().hashCode(), ".tmp");
            tempFile.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile.getAbsolutePath(), true);
            byte[] buffer2 = new byte[4096];
            while (true) {
                int length2 = in.read(buffer2, 0, buffer2.length);
                if (length2 > 0) {
                    fileOutputStream.write(buffer2, 0, length2);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    setDocumentCachedFilePath(tempFile.getAbsolutePath());
                    RandomAccessFileInputStream rafis = RandomAccessFileInputStream.build(tempFile);
                    setInputStream(rafis);
                    return;
                }
            }
        }
    }

    public void setByteArray(byte[] data, int offset, int length, String pathOrURL) throws PDFException, PDFSecurityException, IOException {
        setDocumentOrigin(pathOrURL);
        if (!isCachingEnabled) {
            SeekableByteArrayInputStream byteArrayInputStream = new SeekableByteArrayInputStream(data, offset, length);
            setInputStream(byteArrayInputStream);
            return;
        }
        File tempFile = File.createTempFile("ICEpdfTempFile" + getClass().hashCode(), ".tmp");
        tempFile.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile.getAbsolutePath(), true);
        fileOutputStream.write(data, offset, length);
        fileOutputStream.flush();
        fileOutputStream.close();
        setDocumentCachedFilePath(tempFile.getAbsolutePath());
        RandomAccessFileInputStream rafis = RandomAccessFileInputStream.build(tempFile);
        setInputStream(rafis);
    }

    public void setInputStream(SeekableInput in, String pathOrURL) throws PDFException, PDFSecurityException, IOException {
        setDocumentOrigin(pathOrURL);
        setInputStream(in);
    }

    private void setInputStream(SeekableInput in) throws PDFException, PDFSecurityException, IOException {
        try {
            this.documentSeekableInput = in;
            this.library = new Library();
            boolean loaded = false;
            try {
                loadDocumentViaXRefs(in);
                if (this.catalog != null) {
                    this.catalog.init();
                }
                loaded = true;
            } catch (PDFException e2) {
                throw e2;
            } catch (PDFSecurityException e3) {
                throw e3;
            } catch (Exception e4) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("Cross reference deferred loading failed, will fall back to linear reading.");
                }
            }
            if (!loaded) {
                if (this.catalog != null) {
                    this.catalog = null;
                }
                if (this.library != null) {
                    this.library = null;
                }
                this.library = new Library();
                this.pTrailer = null;
                in.seekAbsolute(0L);
                loadDocumentViaLinearTraversal(in);
                if (this.catalog != null) {
                    this.catalog.init();
                }
            }
            this.stateManager = new StateManager(this.pTrailer);
            this.library.setStateManager(this.stateManager);
        } catch (IOException e5) {
            dispose();
            throw e5;
        } catch (PDFException e6) {
            logger.log(Level.FINE, "Error loading PDF file during linear parse.", (Throwable) e6);
            dispose();
            throw e6;
        } catch (PDFSecurityException e7) {
            dispose();
            throw e7;
        } catch (Exception e8) {
            dispose();
            logger.log(Level.SEVERE, "Error loading PDF Document.", (Throwable) e8);
            throw new IOException(e8.getMessage());
        }
    }

    private void loadDocumentViaXRefs(SeekableInput in) throws IOException, PDFException, PDFSecurityException {
        int offset = skipPastAnyPrefixJunk(in);
        long xrefPosition = getInitialCrossReferencePosition(in) + offset;
        PTrailer documentTrailer = null;
        if (xrefPosition > 0) {
            in.seekAbsolute(xrefPosition);
            Parser parser = new Parser(in);
            Object obj = parser.getObject(this.library);
            if (obj instanceof PObject) {
                obj = ((PObject) obj).getObject();
            }
            PTrailer trailer = (PTrailer) obj;
            if (trailer == null) {
                throw new RuntimeException("Could not find trailer");
            }
            if (trailer.getPrimaryCrossReference() == null) {
                throw new RuntimeException("Could not find cross reference");
            }
            trailer.setPosition(xrefPosition);
            documentTrailer = trailer;
        }
        if (documentTrailer == null) {
            throw new RuntimeException("Could not find document trailer");
        }
        if (offset > 0) {
            documentTrailer.getCrossReferenceTable().setOffset(offset);
        }
        LazyObjectLoader lol = new LazyObjectLoader(this.library, in, documentTrailer.getPrimaryCrossReference());
        this.library.setLazyObjectLoader(lol);
        this.pTrailer = documentTrailer;
        this.catalog = documentTrailer.getRootCatalog();
        this.library.setCatalog(this.catalog);
        if (this.catalog == null) {
            throw new NullPointerException("Loading via xref failed to find catalog");
        }
        boolean madeSecurityManager = makeSecurityManager(documentTrailer);
        if (madeSecurityManager) {
            attemptAuthorizeSecurityManager();
        }
    }

    private long getInitialCrossReferencePosition(SeekableInput in) throws IOException {
        in.seekEnd();
        long endOfFile = in.getAbsolutePosition();
        long currentPosition = endOfFile - 1;
        long afterStartxref = -1;
        int startxrefIndexToMatch = "startxref".length() - 1;
        while (true) {
            if (currentPosition < 0 || endOfFile - currentPosition >= 2048) {
                break;
            }
            in.seekAbsolute(currentPosition);
            int curr = in.read();
            if (curr < 0) {
                throw new EOFException("Could not find startxref at end of file");
            }
            if (curr == "startxref".charAt(startxrefIndexToMatch)) {
                if (startxrefIndexToMatch == 0) {
                    afterStartxref = currentPosition + "startxref".length();
                    break;
                }
                startxrefIndexToMatch--;
            } else {
                startxrefIndexToMatch = "startxref".length() - 1;
            }
            currentPosition--;
        }
        if (afterStartxref < 0) {
            throw new EOFException("Could not find startxref near end of file");
        }
        in.seekAbsolute(afterStartxref);
        Parser parser = new Parser(in);
        Number xrefPositionObj = (Number) parser.getToken();
        if (xrefPositionObj == null) {
            throw new RuntimeException("Could not find ending cross reference position");
        }
        return xrefPositionObj.longValue();
    }

    private void loadDocumentViaLinearTraversal(SeekableInput seekableInput) throws PDFException, PDFSecurityException, IOException {
        InputStream in = seekableInput.getInputStream();
        int objectsOffset = skipPastAnyPrefixJunk(in);
        this.library.setLinearTraversal();
        Parser parser = new Parser(in);
        PTrailer documentTrailer = null;
        List<PObject> documentObjects = new ArrayList<>();
        while (true) {
            Object pdfObject = parser.getObject(this.library);
            if (pdfObject == null) {
                break;
            }
            if (pdfObject instanceof PObject) {
                PObject tmp = (PObject) pdfObject;
                tmp.setLinearTraversalOffset(objectsOffset + parser.getLinearTraversalOffset());
                documentObjects.add(tmp);
                Object obj = tmp.getObject();
                if (obj != null) {
                    pdfObject = obj;
                }
            }
            if (pdfObject instanceof Catalog) {
                this.catalog = (Catalog) pdfObject;
            }
            if (pdfObject instanceof PTrailer) {
                if (documentTrailer == null) {
                    documentTrailer = (PTrailer) pdfObject;
                } else {
                    PTrailer nextTrailer = (PTrailer) pdfObject;
                    if (nextTrailer.getPrev() > 0) {
                        documentTrailer.addNextTrailer(nextTrailer);
                        documentTrailer = nextTrailer;
                    }
                }
            }
        }
        CrossReference refs = documentTrailer.getPrimaryCrossReference();
        for (PObject pObject : documentObjects) {
            Object entry = refs.getEntryForObject(Integer.valueOf(pObject.getReference().getObjectNumber()));
            if (entry != null && (entry instanceof CrossReference.UsedEntry)) {
                ((CrossReference.UsedEntry) entry).setFilePositionOfObject(pObject.getLinearTraversalOffset());
            } else {
                refs.addUsedEntry(pObject.getReference().getObjectNumber(), pObject.getLinearTraversalOffset(), pObject.getReference().getGenerationNumber());
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            for (PObject pobjects : documentObjects) {
                logger.finer(pobjects.getClass().getName() + " " + pobjects.getLinearTraversalOffset() + " " + ((Object) pobjects));
            }
        }
        if (documentTrailer != null) {
            LazyObjectLoader lol = new LazyObjectLoader(this.library, seekableInput, documentTrailer.getPrimaryCrossReference());
            this.library.setLazyObjectLoader(lol);
        }
        this.pTrailer = documentTrailer;
        this.library.setCatalog(this.catalog);
        if (documentTrailer != null) {
            boolean madeSecurityManager = makeSecurityManager(documentTrailer);
            if (madeSecurityManager) {
                attemptAuthorizeSecurityManager();
            }
        }
    }

    private int skipPastAnyPrefixJunk(InputStream in) {
        if (!in.markSupported()) {
            return 0;
        }
        try {
            int scanForLength = "%PDF-".length();
            int scanForIndex = 0;
            boolean scanForWhiteSpace = false;
            in.mark(2048);
            for (int i2 = 0; i2 < 2048; i2++) {
                int data = in.read();
                if (data < 0) {
                    in.reset();
                    return 0;
                }
                if (scanForWhiteSpace) {
                    scanForIndex++;
                    if (Parser.isWhitespace((char) data)) {
                        return scanForIndex;
                    }
                } else if (data == "%PDF-".charAt(scanForIndex)) {
                    scanForIndex++;
                    if (scanForIndex == scanForLength) {
                        scanForWhiteSpace = true;
                    }
                } else {
                    scanForIndex = 0;
                }
            }
            in.reset();
            return 0;
        } catch (IOException e2) {
            try {
                in.reset();
                return 0;
            } catch (IOException e3) {
                return 0;
            }
        }
    }

    private int skipPastAnyPrefixJunk(SeekableInput in) {
        if (!in.markSupported()) {
            return 0;
        }
        try {
            int scanForIndex = 0;
            in.mark(2048);
            for (int i2 = 0; i2 < 2048; i2++) {
                int data = in.read();
                if (data < 0) {
                    in.reset();
                    return 0;
                }
                if (data == "%PDF-1.".charAt(scanForIndex)) {
                    return i2;
                }
                scanForIndex = 0;
            }
            in.reset();
            return 0;
        } catch (IOException e2) {
            try {
                in.reset();
                return 0;
            } catch (IOException e3) {
                return 0;
            }
        }
    }

    private boolean makeSecurityManager(PTrailer documentTrailer) throws PDFSecurityException {
        boolean madeSecurityManager = false;
        HashMap<Object, Object> encryptDictionary = documentTrailer.getEncrypt();
        List fileID = documentTrailer.getID();
        if (encryptDictionary != null && fileID != null) {
            this.library.securityManager = new SecurityManager(this.library, encryptDictionary, fileID);
            madeSecurityManager = true;
        }
        return madeSecurityManager;
    }

    private void attemptAuthorizeSecurityManager() throws PDFSecurityException {
        if (!this.library.securityManager.isAuthorized("")) {
            int count = 1;
            while (this.securityCallback != null) {
                String password = this.securityCallback.requestPassword(this);
                if (password == null) {
                    throw new PDFSecurityException("Encryption error");
                }
                if (!this.library.securityManager.isAuthorized(password)) {
                    count++;
                    if (count > 3) {
                        throw new PDFSecurityException("Encryption error");
                    }
                }
            }
            throw new PDFSecurityException("Encryption error");
        }
        this.library.setEncrypted(true);
    }

    public PDimension getPageDimension(int pageNumber, float userRotation) {
        Page page = this.catalog.getPageTree().getPage(pageNumber);
        page.init();
        return page.getSize(userRotation);
    }

    public PDimension getPageDimension(int pageNumber, float userRotation, float userZoom) {
        Page page = this.catalog.getPageTree().getPage(pageNumber);
        if (page != null) {
            page.init();
            return page.getSize(userRotation, userZoom);
        }
        return new PDimension(0, 0);
    }

    public String getDocumentOrigin() {
        return this.origin;
    }

    public String getDocumentLocation() {
        if (this.cachedFilePath != null) {
            return this.cachedFilePath;
        }
        return this.origin;
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public int getNumberOfPages() {
        try {
            return this.catalog.getPageTree().getNumberOfPages();
        } catch (Exception e2) {
            logger.log(Level.FINE, "Error getting number of pages.", (Throwable) e2);
            return 0;
        }
    }

    public void paintPage(int pageNumber, Graphics g2, int renderHintType, int pageBoundary, float userRotation, float userZoom) {
        Page page = this.catalog.getPageTree().getPage(pageNumber);
        page.init();
        PDimension sz = page.getSize(userRotation, userZoom);
        int pageWidth = (int) sz.getWidth();
        int pageHeight = (int) sz.getHeight();
        Graphics gg = g2.create(0, 0, pageWidth, pageHeight);
        page.paint(gg, renderHintType, pageBoundary, userRotation, userZoom);
        gg.dispose();
    }

    public void dispose() {
        if (this.documentSeekableInput != null) {
            try {
                this.documentSeekableInput.close();
            } catch (IOException e2) {
                logger.log(Level.FINE, "Error closing document input stream.", (Throwable) e2);
            }
            this.documentSeekableInput = null;
        }
        String fileToDelete = getDocumentCachedFilePath();
        if (fileToDelete != null) {
            File file = new File(fileToDelete);
            boolean success = file.delete();
            if (!success && logger.isLoggable(Level.WARNING)) {
                logger.warning("Error deleting URL cached to file " + fileToDelete);
            }
        }
    }

    public long writeToOutputStream(OutputStream out) throws IOException {
        IOException iOException;
        long documentLength = this.documentSeekableInput.getLength();
        SeekableInputConstrainedWrapper wrapper = new SeekableInputConstrainedWrapper(this.documentSeekableInput, 0L, documentLength);
        try {
            try {
                byte[] buffer = new byte[4096];
                while (true) {
                    int length = wrapper.read(buffer, 0, buffer.length);
                    if (length <= 0) {
                        break;
                    }
                    out.write(buffer, 0, length);
                }
                return documentLength;
            } finally {
                try {
                    wrapper.close();
                } catch (IOException e2) {
                }
            }
        } finally {
        }
    }

    public long saveToOutputStream(OutputStream out) throws IOException {
        long documentLength = writeToOutputStream(out);
        if (foundIncrementalUpdater) {
            try {
                Class<?> incrementalUpdaterClass = Class.forName(INCREMENTAL_UPDATER);
                Object[] argValues = {this, out, Long.valueOf(documentLength)};
                Method method = incrementalUpdaterClass.getDeclaredMethod("appendIncrementalUpdate", Document.class, OutputStream.class, Long.TYPE);
                long appendedLength = ((Long) method.invoke(null, argValues)).longValue();
                return documentLength + appendedLength;
            } catch (Throwable e2) {
                logger.log(Level.FINE, "Could not call incremental updater.", e2);
            }
        }
        return documentLength;
    }

    public Image getPageImage(int pageNumber, int renderHintType, int pageBoundary, float userRotation, float userZoom) {
        Page page = this.catalog.getPageTree().getPage(pageNumber);
        page.init();
        PDimension sz = page.getSize(pageBoundary, userRotation, userZoom);
        int pageWidth = (int) sz.getWidth();
        int pageHeight = (int) sz.getHeight();
        BufferedImage image = new BufferedImage(pageWidth, pageHeight, 1);
        Graphics g2 = image.createGraphics();
        page.paint(g2, renderHintType, pageBoundary, userRotation, userZoom);
        g2.dispose();
        return image;
    }

    public PageText getPageText(int pageNumber) throws InterruptedException {
        PageTree pageTree = this.catalog.getPageTree();
        if (pageNumber >= 0 && pageNumber < pageTree.getNumberOfPages()) {
            Page pg = pageTree.getPage(pageNumber);
            return pg.getText();
        }
        return null;
    }

    public PageText getPageViewText(int pageNumber) {
        PageTree pageTree = this.catalog.getPageTree();
        if (pageNumber >= 0 && pageNumber < pageTree.getNumberOfPages()) {
            Page pg = pageTree.getPage(pageNumber);
            return pg.getViewText();
        }
        return null;
    }

    public SecurityManager getSecurityManager() {
        return this.library.securityManager;
    }

    public void setSecurityCallback(SecurityCallback securityCallback) {
        this.securityCallback = securityCallback;
    }

    public PInfo getInfo() {
        if (this.pTrailer == null) {
            return null;
        }
        return this.pTrailer.getInfo();
    }

    public List<Image> getPageImages(int pageNumber) {
        Page pg = this.catalog.getPageTree().getPage(pageNumber);
        pg.init();
        return pg.getImages();
    }

    public PageTree getPageTree() {
        if (this.catalog != null) {
            PageTree pageTree = this.catalog.getPageTree();
            if (pageTree != null) {
                pageTree.setWatermarkCallback(this.watermarkCallback);
            }
            return pageTree;
        }
        return null;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public static void setCachingEnabled(boolean cachingEnabled) {
        isCachingEnabled = cachingEnabled;
    }
}
