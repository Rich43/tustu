package com.sun.webkit.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/webkit/network/FormDataElement.class */
abstract class FormDataElement {
    private InputStream inputStream;

    protected abstract InputStream createInputStream() throws IOException;

    protected abstract long doGetSize();

    FormDataElement() {
    }

    void open() throws IOException {
        this.inputStream = createInputStream();
    }

    long getSize() {
        if (this.inputStream == null) {
            throw new IllegalStateException();
        }
        return doGetSize();
    }

    InputStream getInputStream() {
        if (this.inputStream == null) {
            throw new IllegalStateException();
        }
        return this.inputStream;
    }

    void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
            this.inputStream = null;
        }
    }

    private static FormDataElement fwkCreateFromByteArray(byte[] byteArray) {
        return new ByteArrayElement(byteArray);
    }

    private static FormDataElement fwkCreateFromFile(String fileName) {
        return new FileElement(fileName);
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/FormDataElement$ByteArrayElement.class */
    private static final class ByteArrayElement extends FormDataElement {
        private final byte[] byteArray;

        private ByteArrayElement(byte[] byteArray) {
            this.byteArray = byteArray;
        }

        @Override // com.sun.webkit.network.FormDataElement
        protected InputStream createInputStream() {
            return new ByteArrayInputStream(this.byteArray);
        }

        @Override // com.sun.webkit.network.FormDataElement
        protected long doGetSize() {
            return this.byteArray.length;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/FormDataElement$FileElement.class */
    private static final class FileElement extends FormDataElement {
        private final File file;

        private FileElement(String filename) {
            this.file = new File(filename);
        }

        @Override // com.sun.webkit.network.FormDataElement
        protected InputStream createInputStream() throws IOException {
            return new BufferedInputStream(new FileInputStream(this.file));
        }

        @Override // com.sun.webkit.network.FormDataElement
        protected long doGetSize() {
            return this.file.length();
        }
    }
}
