package org.apache.commons.math3.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/SobolSequenceGenerator.class */
public class SobolSequenceGenerator implements RandomVectorGenerator {
    private static final int BITS = 52;
    private static final double SCALE = FastMath.pow(2.0d, 52);
    private static final int MAX_DIMENSION = 1000;
    private static final String RESOURCE_NAME = "/assets/org/apache/commons/math3/random/new-joe-kuo-6.1000";
    private static final String FILE_CHARSET = "US-ASCII";
    private final int dimension;
    private int count = 0;
    private final long[][] direction;

    /* renamed from: x, reason: collision with root package name */
    private final long[] f13086x;

    public SobolSequenceGenerator(int dimension) throws OutOfRangeException {
        if (dimension < 1 || dimension > 1000) {
            throw new OutOfRangeException(Integer.valueOf(dimension), 1, 1000);
        }
        InputStream is = getClass().getResourceAsStream(RESOURCE_NAME);
        if (is == null) {
            throw new MathInternalError();
        }
        this.dimension = dimension;
        this.direction = new long[dimension][53];
        this.f13086x = new long[dimension];
        try {
            try {
                initFromStream(is);
            } catch (IOException e2) {
                throw new MathInternalError();
            } catch (MathParseException e3) {
                throw new MathInternalError();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e4) {
            }
        }
    }

    public SobolSequenceGenerator(int dimension, InputStream is) throws NotStrictlyPositiveException, MathParseException, IOException {
        if (dimension < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(dimension));
        }
        this.dimension = dimension;
        this.direction = new long[dimension][53];
        this.f13086x = new long[dimension];
        int lastDimension = initFromStream(is);
        if (lastDimension < dimension) {
            throw new OutOfRangeException(Integer.valueOf(dimension), 1, Integer.valueOf(lastDimension));
        }
    }

    private int initFromStream(InputStream is) throws MathParseException, IOException {
        for (int i2 = 1; i2 <= 52; i2++) {
            this.direction[0][i2] = 1 << (52 - i2);
        }
        Charset charset = Charset.forName(FILE_CHARSET);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        int dim = -1;
        try {
            reader.readLine();
            int lineNumber = 2;
            int index = 1;
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    StringTokenizer st = new StringTokenizer(line, " ");
                    try {
                        dim = Integer.parseInt(st.nextToken());
                        if (dim >= 2 && dim <= this.dimension) {
                            int s2 = Integer.parseInt(st.nextToken());
                            int a2 = Integer.parseInt(st.nextToken());
                            int[] m2 = new int[s2 + 1];
                            for (int i3 = 1; i3 <= s2; i3++) {
                                m2[i3] = Integer.parseInt(st.nextToken());
                            }
                            int i4 = index;
                            index++;
                            initDirectionVector(i4, a2, m2);
                        }
                        if (dim > this.dimension) {
                            return dim;
                        }
                        lineNumber++;
                    } catch (NumberFormatException e2) {
                        throw new MathParseException(line, lineNumber);
                    } catch (NoSuchElementException e3) {
                        throw new MathParseException(line, lineNumber);
                    }
                } else {
                    reader.close();
                    return dim;
                }
            }
        } finally {
            reader.close();
        }
    }

    private void initDirectionVector(int d2, int a2, int[] m2) {
        int s2 = m2.length - 1;
        for (int i2 = 1; i2 <= s2; i2++) {
            this.direction[d2][i2] = m2[i2] << (52 - i2);
        }
        for (int i3 = s2 + 1; i3 <= 52; i3++) {
            this.direction[d2][i3] = this.direction[d2][i3 - s2] ^ (this.direction[d2][i3 - s2] >> s2);
            for (int k2 = 1; k2 <= s2 - 1; k2++) {
                long[] jArr = this.direction[d2];
                int i4 = i3;
                jArr[i4] = jArr[i4] ^ (((a2 >> ((s2 - 1) - k2)) & 1) * this.direction[d2][i3 - k2]);
            }
        }
    }

    @Override // org.apache.commons.math3.random.RandomVectorGenerator
    public double[] nextVector() {
        double[] v2 = new double[this.dimension];
        if (this.count == 0) {
            this.count++;
            return v2;
        }
        int c2 = 1;
        int value = this.count - 1;
        while ((value & 1) == 1) {
            value >>= 1;
            c2++;
        }
        for (int i2 = 0; i2 < this.dimension; i2++) {
            long[] jArr = this.f13086x;
            int i3 = i2;
            jArr[i3] = jArr[i3] ^ this.direction[i2][c2];
            v2[i2] = this.f13086x[i2] / SCALE;
        }
        this.count++;
        return v2;
    }

    public double[] skipTo(int index) throws NotPositiveException {
        if (index == 0) {
            Arrays.fill(this.f13086x, 0L);
        } else {
            int i2 = index - 1;
            long grayCode = i2 ^ (i2 >> 1);
            for (int j2 = 0; j2 < this.dimension; j2++) {
                long result = 0;
                for (int k2 = 1; k2 <= 52; k2++) {
                    long shift = grayCode >> (k2 - 1);
                    if (shift == 0) {
                        break;
                    }
                    long ik = shift & 1;
                    result ^= ik * this.direction[j2][k2];
                }
                this.f13086x[j2] = result;
            }
        }
        this.count = index;
        return nextVector();
    }

    public int getNextIndex() {
        return this.count;
    }
}
