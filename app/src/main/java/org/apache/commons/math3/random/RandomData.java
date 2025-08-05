package org.apache.commons.math3.random;

import java.util.Collection;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/RandomData.class */
public interface RandomData {
    String nextHexString(int i2) throws NotStrictlyPositiveException;

    int nextInt(int i2, int i3) throws NumberIsTooLargeException;

    long nextLong(long j2, long j3) throws NumberIsTooLargeException;

    String nextSecureHexString(int i2) throws NotStrictlyPositiveException;

    int nextSecureInt(int i2, int i3) throws NumberIsTooLargeException;

    long nextSecureLong(long j2, long j3) throws NumberIsTooLargeException;

    long nextPoisson(double d2) throws NotStrictlyPositiveException;

    double nextGaussian(double d2, double d3) throws NotStrictlyPositiveException;

    double nextExponential(double d2) throws NotStrictlyPositiveException;

    double nextUniform(double d2, double d3) throws NotANumberException, NotFiniteNumberException, NumberIsTooLargeException;

    double nextUniform(double d2, double d3, boolean z2) throws NotANumberException, NotFiniteNumberException, NumberIsTooLargeException;

    int[] nextPermutation(int i2, int i3) throws NotStrictlyPositiveException, NumberIsTooLargeException;

    Object[] nextSample(Collection<?> collection, int i2) throws NotStrictlyPositiveException, NumberIsTooLargeException;
}
