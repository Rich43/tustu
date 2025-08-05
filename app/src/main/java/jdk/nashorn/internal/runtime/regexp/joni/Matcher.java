package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/Matcher.class */
public abstract class Matcher extends IntHolder {
    protected final Regex regex;
    protected final char[] chars;
    protected final int str;
    protected final int end;
    protected int msaStart;
    protected int msaOptions;
    protected final Region msaRegion;
    protected int msaBestLen;
    protected int msaBestS;
    protected int msaBegin;
    protected int msaEnd;
    int low;
    int high;

    protected abstract int matchAt(int i2, int i3, int i4);

    public Matcher(Regex regex, char[] chars) {
        this(regex, chars, 0, chars.length);
    }

    public Matcher(Regex regex, char[] chars, int p2, int end) {
        this.regex = regex;
        this.chars = chars;
        this.str = p2;
        this.end = end;
        this.msaRegion = regex.numMem == 0 ? null : new Region(regex.numMem + 1);
    }

    public final Region getRegion() {
        return this.msaRegion;
    }

    public final int getBegin() {
        return this.msaBegin;
    }

    public final int getEnd() {
        return this.msaEnd;
    }

    protected final void msaInit(int option, int start) {
        this.msaOptions = option;
        this.msaStart = start;
        this.msaBestLen = -1;
    }

    public final int match(int at2, int range, int option) {
        msaInit(option, at2);
        int prev = EncodingHelper.prevCharHead(this.str, at2);
        return matchAt(range, at2, prev);
    }

    private boolean forwardSearchRange(char[] ch, int string, int e2, int s2, int range, IntHolder lowPrev) {
        int p2;
        int pprev = -1;
        int p3 = s2;
        if (this.regex.dMin > 0) {
            p3 += this.regex.dMin;
        }
        while (true) {
            p2 = this.regex.searchAlgorithm.search(this.regex, ch, p3, e2, range);
            if (p2 != -1 && p2 < range) {
                if (p2 - this.regex.dMin < s2) {
                    pprev = p2;
                    p3 = p2 + 1;
                } else if (this.regex.subAnchor != 0) {
                    switch (this.regex.subAnchor) {
                        case 2:
                            if (p2 != string) {
                                int prev = EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, p2);
                                if (!EncodingHelper.isNewLine(ch, prev, e2)) {
                                    pprev = p2;
                                    p3 = p2 + 1;
                                    break;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        case 32:
                            if (p2 != e2 && !EncodingHelper.isNewLine(ch, p2, e2)) {
                                pprev = p2;
                                p3 = p2 + 1;
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                }
            } else {
                return false;
            }
        }
        if (this.regex.dMax == 0) {
            this.low = p2;
            if (lowPrev != null) {
                if (this.low > s2) {
                    lowPrev.value = EncodingHelper.prevCharHead(s2, p2);
                } else {
                    lowPrev.value = EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, p2);
                }
            }
        } else if (this.regex.dMax != Integer.MAX_VALUE) {
            this.low = p2 - this.regex.dMax;
            if (this.low > s2) {
                this.low = EncodingHelper.rightAdjustCharHeadWithPrev(this.low, lowPrev);
                if (lowPrev != null && lowPrev.value == -1) {
                    lowPrev.value = EncodingHelper.prevCharHead(pprev != -1 ? pprev : s2, this.low);
                }
            } else if (lowPrev != null) {
                lowPrev.value = EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, this.low);
            }
        }
        this.high = p2 - this.regex.dMin;
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00af, code lost:
    
        if (r10.regex.dMax == Integer.MAX_VALUE) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b2, code lost:
    
        r10.low = r0 - r10.regex.dMax;
        r10.high = r0 - r10.regex.dMin;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ce, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:?, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean backwardSearchRange(char[] r11, int r12, int r13, int r14, int r15, int r16) {
        /*
            r10 = this;
            r0 = r15
            r17 = r0
            r0 = r17
            r1 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r1 = r1.regex
            int r1 = r1.dMin
            int r0 = r0 + r1
            r17 = r0
            r0 = r14
            r18 = r0
        L14:
            r0 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r0 = r0.regex
            jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm r0 = r0.searchAlgorithm
            r1 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r1 = r1.regex
            r2 = r11
            r3 = r17
            r4 = r16
            r5 = r13
            r6 = r18
            r7 = r14
            r8 = r17
            int r0 = r0.searchBackward(r1, r2, r3, r4, r5, r6, r7, r8)
            r18 = r0
            r0 = r18
            r1 = -1
            if (r0 == r1) goto Ld0
            r0 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r0 = r0.regex
            int r0 = r0.subAnchor
            if (r0 == 0) goto La6
            r0 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r0 = r0.regex
            int r0 = r0.subAnchor
            switch(r0) {
                case 2: goto L60;
                case 32: goto L82;
                default: goto La6;
            }
        L60:
            r0 = r18
            r1 = r12
            if (r0 == r1) goto La6
            r0 = r12
            r1 = r18
            int r0 = jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper.prevCharHead(r0, r1)
            r19 = r0
            r0 = r11
            r1 = r19
            r2 = r13
            boolean r0 = jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper.isNewLine(r0, r1, r2)
            if (r0 != 0) goto L7f
            r0 = r19
            r18 = r0
            goto L14
        L7f:
            goto La6
        L82:
            r0 = r18
            r1 = r13
            if (r0 != r1) goto L8b
            goto La6
        L8b:
            r0 = r11
            r1 = r18
            r2 = r13
            boolean r0 = jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper.isNewLine(r0, r1, r2)
            if (r0 != 0) goto La6
            r0 = r16
            r1 = r18
            int r0 = jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper.prevCharHead(r0, r1)
            r18 = r0
            r0 = r18
            r1 = -1
            if (r0 != r1) goto L14
            r0 = 0
            return r0
        La6:
            r0 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r0 = r0.regex
            int r0 = r0.dMax
            r1 = 2147483647(0x7fffffff, float:NaN)
            if (r0 == r1) goto Lce
            r0 = r10
            r1 = r18
            r2 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r2 = r2.regex
            int r2 = r2.dMax
            int r1 = r1 - r2
            r0.low = r1
            r0 = r10
            r1 = r18
            r2 = r10
            jdk.nashorn.internal.runtime.regexp.joni.Regex r2 = r2.regex
            int r2 = r2.dMin
            int r1 = r1 - r2
            r0.high = r1
        Lce:
            r0 = 1
            return r0
        Ld0:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.regexp.joni.Matcher.backwardSearchRange(char[], int, int, int, int, int):boolean");
    }

    private boolean matchCheck(int upperRange, int s2, int prev) {
        if (matchAt(this.end, s2, prev) != -1 && !Option.isFindLongest(this.regex.options)) {
            return true;
        }
        return false;
    }

    public final int search(int startp, int rangep, int option) {
        int adjrange;
        int prev;
        int start = startp;
        int range = rangep;
        if (start > this.end || start < this.str) {
            return -1;
        }
        if (this.regex.anchor != 0 && this.str < this.end) {
            if ((this.regex.anchor & 4) != 0) {
                if (range > start) {
                    range = start + 1;
                } else {
                    range = start;
                }
            } else if ((this.regex.anchor & 1) != 0) {
                if (range > start) {
                    if (start != this.str) {
                        return -1;
                    }
                    range = this.str + 1;
                } else if (range <= this.str) {
                    start = this.str;
                    range = this.str;
                } else {
                    return -1;
                }
            } else if ((this.regex.anchor & 8) != 0) {
                int minSemiEnd = this.end;
                if (endBuf(start, range, minSemiEnd, minSemiEnd)) {
                    return -1;
                }
            } else if ((this.regex.anchor & 16) != 0) {
                int preEnd = EncodingHelper.stepBack(this.str, this.end, 1);
                int maxSemiEnd = this.end;
                if (EncodingHelper.isNewLine(this.chars, preEnd, this.end)) {
                    if (preEnd > this.str && start <= preEnd && endBuf(start, range, preEnd, maxSemiEnd)) {
                        return -1;
                    }
                } else if (endBuf(start, range, this.end, maxSemiEnd)) {
                    return -1;
                }
            } else if ((this.regex.anchor & 32768) != 0) {
                if (range > start) {
                    range = start + 1;
                } else {
                    range = start;
                }
            }
        } else if (this.str == this.end) {
            if (this.regex.thresholdLength == 0) {
                int start2 = this.str;
                msaInit(option, start2);
                if (matchCheck(this.end, start2, -1)) {
                    return match(start2);
                }
                return mismatch();
            }
            return -1;
        }
        msaInit(option, start);
        int s2 = start;
        if (range > start) {
            if (s2 > this.str) {
                prev = EncodingHelper.prevCharHead(this.str, s2);
            } else {
                prev = 0;
            }
            if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
                int schRange = range;
                if (this.regex.dMax != 0) {
                    if (this.regex.dMax == Integer.MAX_VALUE) {
                        schRange = this.end;
                    } else {
                        schRange += this.regex.dMax;
                        if (schRange > this.end) {
                            schRange = this.end;
                        }
                    }
                }
                if (this.end - start < this.regex.thresholdLength) {
                    return mismatch();
                }
                if (this.regex.dMax != Integer.MAX_VALUE) {
                    while (forwardSearchRange(this.chars, this.str, this.end, s2, schRange, this)) {
                        if (s2 < this.low) {
                            s2 = this.low;
                            prev = this.value;
                        }
                        while (s2 <= this.high) {
                            if (matchCheck(range, s2, prev)) {
                                return match(s2);
                            }
                            prev = s2;
                            s2++;
                        }
                        if (s2 >= range) {
                        }
                    }
                    return mismatch();
                }
                if (!forwardSearchRange(this.chars, this.str, this.end, s2, schRange, null)) {
                    return mismatch();
                }
                if ((this.regex.anchor & 16384) != 0) {
                    while (!matchCheck(range, s2, prev)) {
                        prev = s2;
                        s2++;
                        if (s2 >= range) {
                            return mismatch();
                        }
                    }
                    return match(s2);
                }
            }
            while (!matchCheck(range, s2, prev)) {
                prev = s2;
                s2++;
                if (s2 >= range) {
                    if (s2 == range && matchCheck(range, s2, prev)) {
                        return match(s2);
                    }
                }
            }
            return match(s2);
        }
        if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
            if (range < this.end) {
                adjrange = range;
            } else {
                adjrange = this.end;
            }
            if (this.regex.dMax != Integer.MAX_VALUE && this.end - range >= this.regex.thresholdLength) {
                do {
                    int schStart = s2 + this.regex.dMax;
                    if (schStart > this.end) {
                        schStart = this.end;
                    }
                    if (!backwardSearchRange(this.chars, this.str, this.end, schStart, range, adjrange)) {
                        return mismatch();
                    }
                    if (s2 > this.high) {
                        s2 = this.high;
                    }
                    while (s2 != -1 && s2 >= this.low) {
                        int prev2 = EncodingHelper.prevCharHead(this.str, s2);
                        if (matchCheck(start, s2, prev2)) {
                            return match(s2);
                        }
                        s2 = prev2;
                    }
                } while (s2 >= range);
                return mismatch();
            }
            if (this.end - range < this.regex.thresholdLength) {
                return mismatch();
            }
            int schStart2 = s2;
            if (this.regex.dMax != 0) {
                if (this.regex.dMax == Integer.MAX_VALUE) {
                    schStart2 = this.end;
                } else {
                    schStart2 += this.regex.dMax;
                    if (schStart2 > this.end) {
                        schStart2 = this.end;
                    }
                }
            }
            if (!backwardSearchRange(this.chars, this.str, this.end, schStart2, range, adjrange)) {
                return mismatch();
            }
        }
        do {
            int prev3 = EncodingHelper.prevCharHead(this.str, s2);
            if (matchCheck(start, s2, prev3)) {
                return match(s2);
            }
            s2 = prev3;
        } while (s2 >= range);
        return mismatch();
    }

    private boolean endBuf(int startp, int rangep, int minSemiEnd, int maxSemiEnd) {
        int start = startp;
        int range = rangep;
        if (maxSemiEnd - this.str < this.regex.anchorDmin) {
            return true;
        }
        if (range > start) {
            if (minSemiEnd - start > this.regex.anchorDmax) {
                start = minSemiEnd - this.regex.anchorDmax;
                if (start >= this.end) {
                    start = EncodingHelper.prevCharHead(this.str, this.end);
                }
            }
            if (maxSemiEnd - (range - 1) < this.regex.anchorDmin) {
                range = (maxSemiEnd - this.regex.anchorDmin) + 1;
            }
            if (start >= range) {
                return true;
            }
            return false;
        }
        if (minSemiEnd - range > this.regex.anchorDmax) {
            range = minSemiEnd - this.regex.anchorDmax;
        }
        if (maxSemiEnd - start < this.regex.anchorDmin) {
            start = maxSemiEnd - this.regex.anchorDmin;
        }
        if (range > start) {
            return true;
        }
        return false;
    }

    private int match(int s2) {
        return s2 - this.str;
    }

    private int mismatch() {
        if (this.msaBestLen >= 0) {
            int s2 = this.msaBestS;
            return match(s2);
        }
        return -1;
    }
}
