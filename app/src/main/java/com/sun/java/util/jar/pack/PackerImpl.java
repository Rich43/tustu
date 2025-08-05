package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ClassReader;
import com.sun.java.util.jar.pack.Package;
import com.sun.java.util.jar.pack.Package.Class;
import com.sun.java.util.jar.pack.Package.File;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Pack200;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/PackerImpl.class */
public class PackerImpl extends TLGlobals implements Pack200.Packer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PackerImpl.class.desiredAssertionStatus();
    }

    @Override // java.util.jar.Pack200.Packer
    public SortedMap<String, String> properties() {
        return this.props;
    }

    @Override // java.util.jar.Pack200.Packer
    public synchronized void pack(JarFile jarFile, OutputStream outputStream) throws IOException {
        if (!$assertionsDisabled && Utils.currentInstance.get() != null) {
            throw new AssertionError();
        }
        boolean z2 = !this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone");
        try {
            Utils.currentInstance.set(this);
            if (z2) {
                Utils.changeDefaultTimeZoneToUtc();
            }
            if ("0".equals(this.props.getProperty(Pack200.Packer.EFFORT))) {
                Utils.copyJarFile(jarFile, outputStream);
            } else {
                new DoPack().run(jarFile, outputStream);
            }
            Utils.currentInstance.set(null);
            if (z2) {
                Utils.restoreDefaultTimeZone();
            }
            jarFile.close();
        } catch (Throwable th) {
            Utils.currentInstance.set(null);
            if (z2) {
                Utils.restoreDefaultTimeZone();
            }
            jarFile.close();
            throw th;
        }
    }

    @Override // java.util.jar.Pack200.Packer
    public synchronized void pack(JarInputStream jarInputStream, OutputStream outputStream) throws IOException {
        if (!$assertionsDisabled && Utils.currentInstance.get() != null) {
            throw new AssertionError();
        }
        boolean z2 = !this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone");
        try {
            Utils.currentInstance.set(this);
            if (z2) {
                Utils.changeDefaultTimeZoneToUtc();
            }
            if ("0".equals(this.props.getProperty(Pack200.Packer.EFFORT))) {
                Utils.copyJarFile(jarInputStream, outputStream);
            } else {
                new DoPack().run(jarInputStream, outputStream);
            }
            Utils.currentInstance.set(null);
            if (z2) {
                Utils.restoreDefaultTimeZone();
            }
            jarInputStream.close();
        } catch (Throwable th) {
            Utils.currentInstance.set(null);
            if (z2) {
                Utils.restoreDefaultTimeZone();
            }
            jarInputStream.close();
            throw th;
        }
    }

    @Override // java.util.jar.Pack200.Packer
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.props.addListener(propertyChangeListener);
    }

    @Override // java.util.jar.Pack200.Packer
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.props.removeListener(propertyChangeListener);
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/PackerImpl$DoPack.class */
    private class DoPack {
        final int verbose;
        final Package pkg;
        final String unknownAttrCommand;
        final String classFormatCommand;
        final Map<Attribute.Layout, Attribute> attrDefs;
        final Map<Attribute.Layout, String> attrCommands;
        final boolean keepFileOrder;
        final boolean keepClassOrder;
        final boolean keepModtime;
        final boolean latestModtime;
        final boolean keepDeflateHint;
        long totalOutputSize;
        int segmentCount;
        long segmentTotalSize;
        long segmentSize;
        final long segmentLimit;
        final List<String> passFiles;
        private int nread;
        static final /* synthetic */ boolean $assertionsDisabled;

        /*  JADX ERROR: Failed to decode insn: 0x0076: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        void run(java.util.jar.JarInputStream r7, java.io.OutputStream r8) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 275
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackerImpl.DoPack.run(java.util.jar.JarInputStream, java.io.OutputStream):void");
        }

        /*  JADX ERROR: Failed to decode insn: 0x0074: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        void run(java.util.jar.JarFile r7, java.io.OutputStream r8) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 357
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackerImpl.DoPack.run(java.util.jar.JarFile, java.io.OutputStream):void");
        }

        private DoPack() {
            long j2;
            int time;
            this.verbose = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.verbose");
            PackerImpl.this.props.setInteger(Pack200.Packer.PROGRESS, 0);
            if (this.verbose > 0) {
                Utils.log.info(PackerImpl.this.props.toString());
            }
            this.pkg = new Package(Package.Version.makeVersion(PackerImpl.this.props, "min.class"), Package.Version.makeVersion(PackerImpl.this.props, "max.class"), Package.Version.makeVersion(PackerImpl.this.props, "package"));
            String property = PackerImpl.this.props.getProperty(Pack200.Packer.UNKNOWN_ATTRIBUTE, Pack200.Packer.PASS);
            if (!Pack200.Packer.STRIP.equals(property) && !Pack200.Packer.PASS.equals(property) && !Pack200.Packer.ERROR.equals(property)) {
                throw new RuntimeException("Bad option: pack.unknown.attribute = " + property);
            }
            this.unknownAttrCommand = property.intern();
            String property2 = PackerImpl.this.props.getProperty("com.sun.java.util.jar.pack.class.format.error", Pack200.Packer.PASS);
            if (!Pack200.Packer.PASS.equals(property2) && !Pack200.Packer.ERROR.equals(property2)) {
                throw new RuntimeException("Bad option: com.sun.java.util.jar.pack.class.format.error = " + property2);
            }
            this.classFormatCommand = property2.intern();
            HashMap map = new HashMap();
            HashMap map2 = new HashMap();
            String[] strArr = {Pack200.Packer.CLASS_ATTRIBUTE_PFX, Pack200.Packer.FIELD_ATTRIBUTE_PFX, Pack200.Packer.METHOD_ATTRIBUTE_PFX, Pack200.Packer.CODE_ATTRIBUTE_PFX};
            int[] iArr = {0, 1, 2, 3};
            for (int i2 = 0; i2 < iArr.length; i2++) {
                String str = strArr[i2];
                for (String str2 : PackerImpl.this.props.prefixMap(str).keySet()) {
                    if (!$assertionsDisabled && !str2.startsWith(str)) {
                        throw new AssertionError();
                    }
                    String strSubstring = str2.substring(str.length());
                    String property3 = PackerImpl.this.props.getProperty(str2);
                    Attribute.Layout layoutKeyForLookup = Attribute.keyForLookup(iArr[i2], strSubstring);
                    if (Pack200.Packer.STRIP.equals(property3) || Pack200.Packer.PASS.equals(property3) || Pack200.Packer.ERROR.equals(property3)) {
                        map2.put(layoutKeyForLookup, property3.intern());
                    } else {
                        Attribute.define(map, iArr[i2], strSubstring, property3);
                        if (this.verbose > 1) {
                            Utils.log.fine("Added layout for " + Constants.ATTR_CONTEXT_NAME[i2] + " attribute " + strSubstring + " = " + property3);
                        }
                        if (!$assertionsDisabled && !map.containsKey(layoutKeyForLookup)) {
                            throw new AssertionError();
                        }
                    }
                }
            }
            this.attrDefs = map.isEmpty() ? null : map;
            this.attrCommands = map2.isEmpty() ? null : map2;
            this.keepFileOrder = PackerImpl.this.props.getBoolean(Pack200.Packer.KEEP_FILE_ORDER);
            this.keepClassOrder = PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.keep.class.order");
            this.keepModtime = "keep".equals(PackerImpl.this.props.getProperty(Pack200.Packer.MODIFICATION_TIME));
            this.latestModtime = Pack200.Packer.LATEST.equals(PackerImpl.this.props.getProperty(Pack200.Packer.MODIFICATION_TIME));
            this.keepDeflateHint = "keep".equals(PackerImpl.this.props.getProperty(Pack200.Packer.DEFLATE_HINT));
            if (!this.keepModtime && !this.latestModtime && (time = PackerImpl.this.props.getTime(Pack200.Packer.MODIFICATION_TIME)) != 0) {
                this.pkg.default_modtime = time;
            }
            if (!this.keepDeflateHint && PackerImpl.this.props.getBoolean(Pack200.Packer.DEFLATE_HINT)) {
                this.pkg.default_options |= 32;
            }
            this.totalOutputSize = 0L;
            this.segmentCount = 0;
            this.segmentTotalSize = 0L;
            this.segmentSize = 0L;
            if (PackerImpl.this.props.getProperty(Pack200.Packer.SEGMENT_LIMIT, "").equals("")) {
                j2 = -1;
            } else {
                j2 = PackerImpl.this.props.getLong(Pack200.Packer.SEGMENT_LIMIT);
            }
            long jMax = Math.max(-1L, Math.min(2147483647L, j2));
            this.segmentLimit = jMax == -1 ? Long.MAX_VALUE : jMax;
            this.passFiles = PackerImpl.this.props.getProperties(Pack200.Packer.PASS_FILE_PFX);
            ListIterator<String> listIterator = this.passFiles.listIterator();
            while (listIterator.hasNext()) {
                String next = listIterator.next();
                if (next == null) {
                    listIterator.remove();
                } else {
                    String jarEntryName = Utils.getJarEntryName(next);
                    listIterator.set(jarEntryName.endsWith("/") ? jarEntryName.substring(0, jarEntryName.length() - 1) : jarEntryName);
                }
            }
            if (this.verbose > 0) {
                Utils.log.info("passFiles = " + ((Object) this.passFiles));
            }
            int integer = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.archive.options");
            if (integer != 0) {
                this.pkg.default_options |= integer;
            }
            this.nread = 0;
        }

        static {
            $assertionsDisabled = !PackerImpl.class.desiredAssertionStatus();
        }

        boolean isClassFile(String str) {
            if (!str.endsWith(".class")) {
                return false;
            }
            String strSubstring = str;
            while (true) {
                String str2 = strSubstring;
                if (this.passFiles.contains(str2)) {
                    return false;
                }
                int iLastIndexOf = str2.lastIndexOf(47);
                if (iLastIndexOf >= 0) {
                    strSubstring = str2.substring(0, iLastIndexOf);
                } else {
                    return true;
                }
            }
        }

        boolean isMetaInfFile(String str) {
            return str.startsWith("/META-INF") || str.startsWith("META-INF");
        }

        private void makeNextPackage() {
            this.pkg.reset();
        }

        /* loaded from: rt.jar:com/sun/java/util/jar/pack/PackerImpl$DoPack$InFile.class */
        final class InFile {
            final String name;
            final JarFile jf;
            final JarEntry je;

            /* renamed from: f, reason: collision with root package name */
            final File f11850f;
            int modtime;
            int options;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !PackerImpl.class.desiredAssertionStatus();
            }

            InFile(String str) {
                this.modtime = 0;
                this.name = Utils.getJarEntryName(str);
                this.f11850f = new File(str);
                this.jf = null;
                this.je = null;
                int modtime = getModtime(this.f11850f.lastModified());
                if (DoPack.this.keepModtime && modtime != 0) {
                    this.modtime = modtime;
                } else if (DoPack.this.latestModtime && modtime > DoPack.this.pkg.default_modtime) {
                    DoPack.this.pkg.default_modtime = modtime;
                }
            }

            InFile(JarFile jarFile, JarEntry jarEntry) {
                this.modtime = 0;
                this.name = Utils.getJarEntryName(jarEntry.getName());
                this.f11850f = null;
                this.jf = jarFile;
                this.je = jarEntry;
                int modtime = getModtime(jarEntry.getTime());
                if (DoPack.this.keepModtime && modtime != 0) {
                    this.modtime = modtime;
                } else if (DoPack.this.latestModtime && modtime > DoPack.this.pkg.default_modtime) {
                    DoPack.this.pkg.default_modtime = modtime;
                }
                if (DoPack.this.keepDeflateHint && jarEntry.getMethod() == 8) {
                    this.options |= 1;
                }
            }

            InFile(DoPack doPack, JarEntry jarEntry) {
                this(null, jarEntry);
            }

            long getInputLength() {
                long size = this.je != null ? this.je.getSize() : this.f11850f.length();
                if ($assertionsDisabled || size >= 0) {
                    return Math.max(0L, size) + this.name.length() + 5;
                }
                throw new AssertionError((Object) (((Object) this) + ".len=" + size));
            }

            int getModtime(long j2) {
                long j3 = (j2 + 500) / 1000;
                if (((int) j3) == j3) {
                    return (int) j3;
                }
                Utils.log.warning("overflow in modtime for " + ((Object) this.f11850f));
                return 0;
            }

            void copyTo(Package.File file) {
                if (this.modtime != 0) {
                    file.modtime = this.modtime;
                }
                file.options |= this.options;
            }

            InputStream getInputStream() throws IOException {
                if (this.jf != null) {
                    return this.jf.getInputStream(this.je);
                }
                return new FileInputStream(this.f11850f);
            }

            public String toString() {
                return this.name;
            }
        }

        private void noteRead(InFile inFile) {
            this.nread++;
            if (this.verbose > 2) {
                Utils.log.fine("...read " + inFile.name);
            }
            if (this.verbose > 0 && this.nread % 1000 == 0) {
                Utils.log.info("Have read " + this.nread + " files...");
            }
        }

        Package.File readClass(String str, InputStream inputStream) throws IOException {
            Package r2 = this.pkg;
            r2.getClass();
            Package.Class r0 = r2.new Class(str);
            ClassReader classReader = new ClassReader(r0, new BufferedInputStream(inputStream));
            classReader.setAttrDefs(this.attrDefs);
            classReader.setAttrCommands(this.attrCommands);
            classReader.unknownAttrCommand = this.unknownAttrCommand;
            try {
                classReader.read();
                this.pkg.addClass(r0);
                return r0.file;
            } catch (IOException e2) {
                if (e2 instanceof Attribute.FormatException) {
                    Attribute.FormatException formatException = (Attribute.FormatException) e2;
                    if (formatException.layout.equals(Pack200.Packer.PASS)) {
                        Utils.log.info(formatException.toString());
                        Utils.log.warning("Passing class file uncompressed due to unrecognized attribute: " + str);
                        return null;
                    }
                } else if (e2 instanceof ClassReader.ClassFormatException) {
                    ClassReader.ClassFormatException classFormatException = (ClassReader.ClassFormatException) e2;
                    if (this.classFormatCommand.equals(Pack200.Packer.PASS)) {
                        Utils.log.info(classFormatException.toString());
                        Utils.log.warning("Passing class file uncompressed due to unknown class format: " + str);
                        return null;
                    }
                }
                throw e2;
            }
        }

        Package.File readFile(String str, InputStream inputStream) throws IOException {
            Package r2 = this.pkg;
            r2.getClass();
            Package.File file = r2.new File(str);
            file.readFrom(inputStream);
            if (file.isDirectory() && file.getFileLength() != 0) {
                throw new IllegalArgumentException("Non-empty directory: " + ((Object) file.getFileName()));
            }
            return file;
        }

        void flushPartial(OutputStream outputStream, int i2) throws IOException {
            if (this.pkg.files.isEmpty() && this.pkg.classes.isEmpty()) {
                return;
            }
            flushPackage(outputStream, Math.max(1, i2));
            PackerImpl.this.props.setInteger(Pack200.Packer.PROGRESS, 25);
            makeNextPackage();
            this.segmentCount++;
            this.segmentTotalSize += this.segmentSize;
            this.segmentSize = 0L;
        }

        void flushAll(OutputStream outputStream) throws IOException {
            PackerImpl.this.props.setInteger(Pack200.Packer.PROGRESS, 50);
            flushPackage(outputStream, 0);
            outputStream.flush();
            PackerImpl.this.props.setInteger(Pack200.Packer.PROGRESS, 100);
            this.segmentCount++;
            this.segmentTotalSize += this.segmentSize;
            this.segmentSize = 0L;
            if (this.verbose > 0 && this.segmentCount > 1) {
                Utils.log.info("Transmitted " + this.segmentTotalSize + " input bytes in " + this.segmentCount + " segments totaling " + this.totalOutputSize + " bytes");
            }
        }

        void flushPackage(OutputStream outputStream, int i2) throws IOException {
            int size = this.pkg.files.size();
            if (!this.keepFileOrder) {
                if (this.verbose > 1) {
                    Utils.log.fine("Reordering files.");
                }
                this.pkg.reorderFiles(this.keepClassOrder, true);
            } else {
                if (!$assertionsDisabled && !this.pkg.files.containsAll(this.pkg.getClassStubs())) {
                    throw new AssertionError();
                }
                ArrayList<Package.File> arrayList = this.pkg.files;
                if (!$assertionsDisabled) {
                    ArrayList<Package.File> arrayList2 = new ArrayList<>(this.pkg.files);
                    arrayList = arrayList2;
                    if (!arrayList2.retainAll(this.pkg.getClassStubs())) {
                    }
                }
                if (!$assertionsDisabled && !arrayList.equals(this.pkg.getClassStubs())) {
                    throw new AssertionError();
                }
            }
            this.pkg.trimStubs();
            if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.debug")) {
                this.pkg.stripAttributeKind("Debug");
            }
            if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.compile")) {
                this.pkg.stripAttributeKind("Compile");
            }
            if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.constants")) {
                this.pkg.stripAttributeKind("Constant");
            }
            if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.exceptions")) {
                this.pkg.stripAttributeKind("Exceptions");
            }
            if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.innerclasses")) {
                this.pkg.stripAttributeKind("InnerClasses");
            }
            PackageWriter packageWriter = new PackageWriter(this.pkg, outputStream);
            packageWriter.archiveNextCount = i2;
            packageWriter.write();
            outputStream.flush();
            if (this.verbose > 0) {
                long j2 = packageWriter.archiveSize0 + packageWriter.archiveSize1;
                this.totalOutputSize += j2;
                Utils.log.info("Transmitted " + size + " files of " + this.segmentSize + " input bytes in a segment of " + j2 + " bytes");
            }
        }

        List<InFile> scanJar(JarFile jarFile) throws IOException {
            ArrayList arrayList = new ArrayList();
            try {
                Iterator it = Collections.list(jarFile.entries()).iterator();
                while (it.hasNext()) {
                    JarEntry jarEntry = (JarEntry) it.next();
                    InFile inFile = new InFile(jarFile, jarEntry);
                    if (!$assertionsDisabled && jarEntry.isDirectory() != inFile.name.endsWith("/")) {
                        throw new AssertionError();
                    }
                    arrayList.add(inFile);
                }
                return arrayList;
            } catch (IllegalStateException e2) {
                throw new IOException(e2.getLocalizedMessage(), e2);
            }
        }
    }
}
