package org.apache.commons.math3.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/ValueServer.class */
public class ValueServer {
    public static final int DIGEST_MODE = 0;
    public static final int REPLAY_MODE = 1;
    public static final int UNIFORM_MODE = 2;
    public static final int EXPONENTIAL_MODE = 3;
    public static final int GAUSSIAN_MODE = 4;
    public static final int CONSTANT_MODE = 5;
    private int mode;
    private URL valuesFileURL;
    private double mu;
    private double sigma;
    private EmpiricalDistribution empiricalDistribution;
    private BufferedReader filePointer;
    private final RandomDataGenerator randomData;

    public ValueServer() {
        this.mode = 5;
        this.valuesFileURL = null;
        this.mu = 0.0d;
        this.sigma = 0.0d;
        this.empiricalDistribution = null;
        this.filePointer = null;
        this.randomData = new RandomDataGenerator();
    }

    @Deprecated
    public ValueServer(RandomDataImpl randomData) {
        this.mode = 5;
        this.valuesFileURL = null;
        this.mu = 0.0d;
        this.sigma = 0.0d;
        this.empiricalDistribution = null;
        this.filePointer = null;
        this.randomData = randomData.getDelegate();
    }

    public ValueServer(RandomGenerator generator) {
        this.mode = 5;
        this.valuesFileURL = null;
        this.mu = 0.0d;
        this.sigma = 0.0d;
        this.empiricalDistribution = null;
        this.filePointer = null;
        this.randomData = new RandomDataGenerator(generator);
    }

    public double getNext() throws MathIllegalStateException, IOException, MathIllegalArgumentException {
        switch (this.mode) {
            case 0:
                return getNextDigest();
            case 1:
                return getNextReplay();
            case 2:
                return getNextUniform();
            case 3:
                return getNextExponential();
            case 4:
                return getNextGaussian();
            case 5:
                return this.mu;
            default:
                throw new MathIllegalStateException(LocalizedFormats.UNKNOWN_MODE, Integer.valueOf(this.mode), "DIGEST_MODE", 0, "REPLAY_MODE", 1, "UNIFORM_MODE", 2, "EXPONENTIAL_MODE", 3, "GAUSSIAN_MODE", 4, "CONSTANT_MODE", 5);
        }
    }

    public void fill(double[] values) throws MathIllegalStateException, IOException, MathIllegalArgumentException {
        for (int i2 = 0; i2 < values.length; i2++) {
            values[i2] = getNext();
        }
    }

    public double[] fill(int length) throws MathIllegalStateException, IOException, MathIllegalArgumentException {
        double[] out = new double[length];
        for (int i2 = 0; i2 < length; i2++) {
            out[i2] = getNext();
        }
        return out;
    }

    public void computeDistribution() throws NullArgumentException, ZeroException, IOException {
        computeDistribution(1000);
    }

    public void computeDistribution(int binCount) throws NullArgumentException, ZeroException, IOException {
        this.empiricalDistribution = new EmpiricalDistribution(binCount, this.randomData.getRandomGenerator());
        this.empiricalDistribution.load(this.valuesFileURL);
        this.mu = this.empiricalDistribution.getSampleStats().getMean();
        this.sigma = this.empiricalDistribution.getSampleStats().getStandardDeviation();
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public URL getValuesFileURL() {
        return this.valuesFileURL;
    }

    public void setValuesFileURL(String url) throws MalformedURLException {
        this.valuesFileURL = new URL(url);
    }

    public void setValuesFileURL(URL url) {
        this.valuesFileURL = url;
    }

    public EmpiricalDistribution getEmpiricalDistribution() {
        return this.empiricalDistribution;
    }

    public void resetReplayFile() throws IOException {
        if (this.filePointer != null) {
            try {
                this.filePointer.close();
                this.filePointer = null;
            } catch (IOException e2) {
            }
        }
        this.filePointer = new BufferedReader(new InputStreamReader(this.valuesFileURL.openStream(), "UTF-8"));
    }

    public void closeReplayFile() throws IOException {
        if (this.filePointer != null) {
            this.filePointer.close();
            this.filePointer = null;
        }
    }

    public double getMu() {
        return this.mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public double getSigma() {
        return this.sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void reSeed(long seed) {
        this.randomData.reSeed(seed);
    }

    private double getNextDigest() throws MathIllegalStateException {
        if (this.empiricalDistribution == null || this.empiricalDistribution.getBinStats().size() == 0) {
            throw new MathIllegalStateException(LocalizedFormats.DIGEST_NOT_INITIALIZED, new Object[0]);
        }
        return this.empiricalDistribution.getNextValue();
    }

    private double getNextReplay() throws MathIllegalStateException, IOException {
        if (this.filePointer == null) {
            resetReplayFile();
        }
        String line = this.filePointer.readLine();
        String str = line;
        if (line == null) {
            closeReplayFile();
            resetReplayFile();
            String line2 = this.filePointer.readLine();
            str = line2;
            if (line2 == null) {
                throw new MathIllegalStateException(LocalizedFormats.URL_CONTAINS_NO_DATA, this.valuesFileURL);
            }
        }
        return Double.parseDouble(str);
    }

    private double getNextUniform() throws MathIllegalArgumentException {
        return this.randomData.nextUniform(0.0d, 2.0d * this.mu);
    }

    private double getNextExponential() throws MathIllegalArgumentException {
        return this.randomData.nextExponential(this.mu);
    }

    private double getNextGaussian() throws MathIllegalArgumentException {
        return this.randomData.nextGaussian(this.mu, this.sigma);
    }
}
