package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Package;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/UnpackerImpl.class */
public class UnpackerImpl extends TLGlobals implements Pack200.Unpacker {
    Object _nunp;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnpackerImpl.class.desiredAssertionStatus();
    }

    @Override // java.util.jar.Pack200.Unpacker
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.props.addListener(propertyChangeListener);
    }

    @Override // java.util.jar.Pack200.Unpacker
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.props.removeListener(propertyChangeListener);
    }

    @Override // java.util.jar.Pack200.Unpacker
    public SortedMap<String, String> properties() {
        return this.props;
    }

    public String toString() {
        return Utils.getVersionString();
    }

    @Override // java.util.jar.Pack200.Unpacker
    public synchronized void unpack(InputStream inputStream, JarOutputStream jarOutputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("null input");
        }
        if (jarOutputStream == null) {
            throw new NullPointerException("null output");
        }
        if (!$assertionsDisabled && Utils.currentInstance.get() != null) {
            throw new AssertionError();
        }
        boolean z2 = !this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone");
        try {
            Utils.currentInstance.set(this);
            if (z2) {
                Utils.changeDefaultTimeZoneToUtc();
            }
            int integer = this.props.getInteger("com.sun.java.util.jar.pack.verbose");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            if (Utils.isJarMagic(Utils.readMagic(bufferedInputStream))) {
                if (integer > 0) {
                    Utils.log.info("Copying unpacked JAR file...");
                }
                Utils.copyJarFile(new JarInputStream(bufferedInputStream), jarOutputStream);
            } else if (this.props.getBoolean("com.sun.java.util.jar.pack.disable.native")) {
                new DoUnpack().run(bufferedInputStream, jarOutputStream);
                bufferedInputStream.close();
                Utils.markJarFile(jarOutputStream);
            } else {
                try {
                    new NativeUnpack(this).run(bufferedInputStream, jarOutputStream);
                } catch (NoClassDefFoundError | UnsatisfiedLinkError e2) {
                    new DoUnpack().run(bufferedInputStream, jarOutputStream);
                }
                bufferedInputStream.close();
                Utils.markJarFile(jarOutputStream);
            }
        } finally {
            this._nunp = null;
            Utils.currentInstance.set(null);
            if (z2) {
                Utils.restoreDefaultTimeZone();
            }
        }
    }

    @Override // java.util.jar.Pack200.Unpacker
    public synchronized void unpack(File file, JarOutputStream jarOutputStream) throws IOException {
        if (file == null) {
            throw new NullPointerException("null input");
        }
        if (jarOutputStream == null) {
            throw new NullPointerException("null output");
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Throwable th = null;
        try {
            try {
                unpack(fileInputStream, jarOutputStream);
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                if (this.props.getBoolean("com.sun.java.util.jar.pack.unpack.remove.packfile")) {
                    file.delete();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (fileInputStream != null) {
                if (th != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th4;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/UnpackerImpl$DoUnpack.class */
    private class DoUnpack {
        final int verbose;
        final Package pkg;
        final boolean keepModtime;
        final boolean keepDeflateHint;
        final int modtime;
        final boolean deflateHint;
        final CRC32 crc;
        final ByteArrayOutputStream bufOut;
        final OutputStream crcOut;
        static final /* synthetic */ boolean $assertionsDisabled;

        private DoUnpack() {
            this.verbose = UnpackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.verbose");
            UnpackerImpl.this.props.setInteger(Pack200.Unpacker.PROGRESS, 0);
            this.pkg = new Package();
            this.keepModtime = "keep".equals(UnpackerImpl.this.props.getProperty("com.sun.java.util.jar.pack.unpack.modification.time", "keep"));
            this.keepDeflateHint = "keep".equals(UnpackerImpl.this.props.getProperty(Pack200.Unpacker.DEFLATE_HINT, "keep"));
            if (!this.keepModtime) {
                this.modtime = UnpackerImpl.this.props.getTime("com.sun.java.util.jar.pack.unpack.modification.time");
            } else {
                this.modtime = this.pkg.default_modtime;
            }
            this.deflateHint = this.keepDeflateHint ? false : UnpackerImpl.this.props.getBoolean(Pack200.Unpacker.DEFLATE_HINT);
            this.crc = new CRC32();
            this.bufOut = new ByteArrayOutputStream();
            this.crcOut = new CheckedOutputStream(this.bufOut, this.crc);
        }

        static {
            $assertionsDisabled = !UnpackerImpl.class.desiredAssertionStatus();
        }

        public void run(BufferedInputStream bufferedInputStream, JarOutputStream jarOutputStream) throws IOException {
            if (this.verbose > 0) {
                UnpackerImpl.this.props.list(System.out);
            }
            int i2 = 1;
            while (true) {
                unpackSegment(bufferedInputStream, jarOutputStream);
                if (Utils.isPackMagic(Utils.readMagic(bufferedInputStream))) {
                    if (this.verbose > 0) {
                        Utils.log.info("Finished segment #" + i2);
                    }
                    i2++;
                } else {
                    return;
                }
            }
        }

        private void unpackSegment(InputStream inputStream, JarOutputStream jarOutputStream) throws IOException {
            UnpackerImpl.this.props.setProperty(Pack200.Unpacker.PROGRESS, "0");
            new PackageReader(this.pkg, inputStream).read();
            if (UnpackerImpl.this.props.getBoolean("unpack.strip.debug")) {
                this.pkg.stripAttributeKind("Debug");
            }
            if (UnpackerImpl.this.props.getBoolean("unpack.strip.compile")) {
                this.pkg.stripAttributeKind("Compile");
            }
            UnpackerImpl.this.props.setProperty(Pack200.Unpacker.PROGRESS, "50");
            this.pkg.ensureAllClassFiles();
            HashSet hashSet = new HashSet(this.pkg.getClasses());
            for (Package.File file : this.pkg.getFiles()) {
                JarEntry jarEntry = new JarEntry(Utils.getJarEntryName(file.nameString));
                boolean z2 = this.keepDeflateHint ? ((file.options & 1) == 0 && (this.pkg.default_options & 32) == 0) ? false : true : this.deflateHint;
                boolean z3 = !z2;
                if (z3) {
                    this.crc.reset();
                }
                this.bufOut.reset();
                if (file.isClassStub()) {
                    Package.Class stubClass = file.getStubClass();
                    if (!$assertionsDisabled && stubClass == null) {
                        throw new AssertionError();
                    }
                    new ClassWriter(stubClass, z3 ? this.crcOut : this.bufOut).write();
                    hashSet.remove(stubClass);
                } else {
                    file.writeTo(z3 ? this.crcOut : this.bufOut);
                }
                jarEntry.setMethod(z2 ? 8 : 0);
                if (z3) {
                    if (this.verbose > 0) {
                        Utils.log.info("stored size=" + this.bufOut.size() + " and crc=" + this.crc.getValue());
                    }
                    jarEntry.setMethod(0);
                    jarEntry.setSize(this.bufOut.size());
                    jarEntry.setCrc(this.crc.getValue());
                }
                if (this.keepModtime) {
                    jarEntry.setTime(file.modtime);
                    jarEntry.setTime(file.modtime * 1000);
                } else {
                    jarEntry.setTime(this.modtime * 1000);
                }
                jarOutputStream.putNextEntry(jarEntry);
                this.bufOut.writeTo(jarOutputStream);
                jarOutputStream.closeEntry();
                if (this.verbose > 0) {
                    Utils.log.info("Writing " + Utils.zeString(jarEntry));
                }
            }
            if (!$assertionsDisabled && !hashSet.isEmpty()) {
                throw new AssertionError();
            }
            UnpackerImpl.this.props.setProperty(Pack200.Unpacker.PROGRESS, "100");
            this.pkg.reset();
        }
    }
}
