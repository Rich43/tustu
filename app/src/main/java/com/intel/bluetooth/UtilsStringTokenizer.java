package com.intel.bluetooth;

import java.util.NoSuchElementException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/UtilsStringTokenizer.class */
class UtilsStringTokenizer {
    private int currentPosition = 0;
    private int newPosition;
    private String str;
    private String delimiter;

    public UtilsStringTokenizer(String str, String delimiter) {
        this.str = str;
        this.delimiter = delimiter;
        nextPosition();
    }

    public boolean hasMoreTokens() {
        return this.newPosition != -1 && this.currentPosition < this.newPosition;
    }

    private void nextPosition() {
        this.newPosition = this.str.indexOf(this.delimiter, this.currentPosition);
        if (this.newPosition == -1) {
            this.newPosition = this.str.length();
        } else if (this.newPosition == this.currentPosition) {
            this.currentPosition++;
            nextPosition();
        }
    }

    public String nextToken() throws NoSuchElementException {
        if (!hasMoreTokens()) {
            throw new NoSuchElementException();
        }
        String next = this.str.substring(this.currentPosition, this.newPosition);
        this.currentPosition = this.newPosition + 1;
        nextPosition();
        return next;
    }
}
