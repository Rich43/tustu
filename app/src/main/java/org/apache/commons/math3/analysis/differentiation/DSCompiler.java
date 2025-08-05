package org.apache.commons.math3.analysis.differentiation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/DSCompiler.class */
public class DSCompiler {
    private static AtomicReference<DSCompiler[][]> compilers = new AtomicReference<>(null);
    private final int parameters;
    private final int order;
    private final int[][] sizes;
    private final int[][] derivativesIndirection;
    private final int[] lowerIndirection;
    private final int[][][] multIndirection;
    private final int[][][] compIndirection;

    private DSCompiler(int parameters, int order, DSCompiler valueCompiler, DSCompiler derivativeCompiler) throws NumberIsTooLargeException {
        this.parameters = parameters;
        this.order = order;
        this.sizes = compileSizes(parameters, order, valueCompiler);
        this.derivativesIndirection = compileDerivativesIndirection(parameters, order, valueCompiler, derivativeCompiler);
        this.lowerIndirection = compileLowerIndirection(parameters, order, valueCompiler, derivativeCompiler);
        this.multIndirection = compileMultiplicationIndirection(parameters, order, valueCompiler, derivativeCompiler, this.lowerIndirection);
        this.compIndirection = compileCompositionIndirection(parameters, order, valueCompiler, derivativeCompiler, this.sizes, this.derivativesIndirection);
    }

    public static DSCompiler getCompiler(int parameters, int order) throws NumberIsTooLargeException {
        DSCompiler[][] cache = compilers.get();
        if (cache != null && cache.length > parameters && cache[parameters].length > order && cache[parameters][order] != null) {
            return cache[parameters][order];
        }
        int maxParameters = FastMath.max(parameters, cache == null ? 0 : cache.length);
        int maxOrder = FastMath.max(order, cache == null ? 0 : cache[0].length);
        DSCompiler[][] newCache = new DSCompiler[maxParameters + 1][maxOrder + 1];
        if (cache != null) {
            for (int i2 = 0; i2 < cache.length; i2++) {
                System.arraycopy(cache[i2], 0, newCache[i2], 0, cache[i2].length);
            }
        }
        for (int diag = 0; diag <= parameters + order; diag++) {
            int o2 = FastMath.max(0, diag - parameters);
            while (o2 <= FastMath.min(order, diag)) {
                int p2 = diag - o2;
                if (newCache[p2][o2] == null) {
                    DSCompiler valueCompiler = p2 == 0 ? null : newCache[p2 - 1][o2];
                    DSCompiler derivativeCompiler = o2 == 0 ? null : newCache[p2][o2 - 1];
                    newCache[p2][o2] = new DSCompiler(p2, o2, valueCompiler, derivativeCompiler);
                }
                o2++;
            }
        }
        compilers.compareAndSet(cache, newCache);
        return newCache[parameters][order];
    }

    private static int[][] compileSizes(int parameters, int order, DSCompiler valueCompiler) {
        int[][] sizes = new int[parameters + 1][order + 1];
        if (parameters == 0) {
            Arrays.fill(sizes[0], 1);
        } else {
            System.arraycopy(valueCompiler.sizes, 0, sizes, 0, parameters);
            sizes[parameters][0] = 1;
            for (int i2 = 0; i2 < order; i2++) {
                sizes[parameters][i2 + 1] = sizes[parameters][i2] + sizes[parameters - 1][i2 + 1];
            }
        }
        return sizes;
    }

    private static int[][] compileDerivativesIndirection(int parameters, int order, DSCompiler valueCompiler, DSCompiler derivativeCompiler) {
        if (parameters == 0 || order == 0) {
            return new int[1][parameters];
        }
        int vSize = valueCompiler.derivativesIndirection.length;
        int dSize = derivativeCompiler.derivativesIndirection.length;
        int[][] derivativesIndirection = new int[vSize + dSize][parameters];
        for (int i2 = 0; i2 < vSize; i2++) {
            System.arraycopy(valueCompiler.derivativesIndirection[i2], 0, derivativesIndirection[i2], 0, parameters - 1);
        }
        for (int i3 = 0; i3 < dSize; i3++) {
            System.arraycopy(derivativeCompiler.derivativesIndirection[i3], 0, derivativesIndirection[vSize + i3], 0, parameters);
            int[] iArr = derivativesIndirection[vSize + i3];
            int i4 = parameters - 1;
            iArr[i4] = iArr[i4] + 1;
        }
        return derivativesIndirection;
    }

    private static int[] compileLowerIndirection(int parameters, int order, DSCompiler valueCompiler, DSCompiler derivativeCompiler) {
        if (parameters == 0 || order <= 1) {
            return new int[]{0};
        }
        int vSize = valueCompiler.lowerIndirection.length;
        int dSize = derivativeCompiler.lowerIndirection.length;
        int[] lowerIndirection = new int[vSize + dSize];
        System.arraycopy(valueCompiler.lowerIndirection, 0, lowerIndirection, 0, vSize);
        for (int i2 = 0; i2 < dSize; i2++) {
            lowerIndirection[vSize + i2] = valueCompiler.getSize() + derivativeCompiler.lowerIndirection[i2];
        }
        return lowerIndirection;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [int[][], int[][][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v2, types: [int[][], int[][][]] */
    private static int[][][] compileMultiplicationIndirection(int parameters, int order, DSCompiler valueCompiler, DSCompiler derivativeCompiler, int[] lowerIndirection) {
        if (parameters == 0 || order == 0) {
            return new int[][]{new int[]{new int[]{1, 0, 0}}};
        }
        int vSize = valueCompiler.multIndirection.length;
        int dSize = derivativeCompiler.multIndirection.length;
        ?? r0 = new int[vSize + dSize][];
        System.arraycopy(valueCompiler.multIndirection, 0, r0, 0, vSize);
        for (int i2 = 0; i2 < dSize; i2++) {
            int[][] dRow = derivativeCompiler.multIndirection[i2];
            List<int[]> row = new ArrayList<>(dRow.length * 2);
            for (int j2 = 0; j2 < dRow.length; j2++) {
                row.add(new int[]{dRow[j2][0], lowerIndirection[dRow[j2][1]], vSize + dRow[j2][2]});
                row.add(new int[]{dRow[j2][0], vSize + dRow[j2][1], lowerIndirection[dRow[j2][2]]});
            }
            List<int[]> combined = new ArrayList<>(row.size());
            for (int j3 = 0; j3 < row.size(); j3++) {
                int[] termJ = row.get(j3);
                if (termJ[0] > 0) {
                    for (int k2 = j3 + 1; k2 < row.size(); k2++) {
                        int[] termK = row.get(k2);
                        if (termJ[1] == termK[1] && termJ[2] == termK[2]) {
                            termJ[0] = termJ[0] + termK[0];
                            termK[0] = 0;
                        }
                    }
                    combined.add(termJ);
                }
            }
            r0[vSize + i2] = (int[][]) combined.toArray(new int[combined.size()]);
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [int[][], int[][][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v2, types: [int[][], int[][][]] */
    private static int[][][] compileCompositionIndirection(int parameters, int order, DSCompiler valueCompiler, DSCompiler derivativeCompiler, int[][] sizes, int[][] derivativesIndirection) throws NumberIsTooLargeException {
        if (parameters == 0 || order == 0) {
            return new int[][]{new int[]{new int[]{1, 0}}};
        }
        int vSize = valueCompiler.compIndirection.length;
        int dSize = derivativeCompiler.compIndirection.length;
        ?? r0 = new int[vSize + dSize][];
        System.arraycopy(valueCompiler.compIndirection, 0, r0, 0, vSize);
        for (int i2 = 0; i2 < dSize; i2++) {
            List<int[]> row = new ArrayList<>();
            int[][] arr$ = derivativeCompiler.compIndirection[i2];
            for (int[] term : arr$) {
                int[] derivedTermF = new int[term.length + 1];
                derivedTermF[0] = term[0];
                derivedTermF[1] = term[1] + 1;
                int[] orders = new int[parameters];
                orders[parameters - 1] = 1;
                derivedTermF[term.length] = getPartialDerivativeIndex(parameters, order, sizes, orders);
                for (int j2 = 2; j2 < term.length; j2++) {
                    derivedTermF[j2] = convertIndex(term[j2], parameters, derivativeCompiler.derivativesIndirection, parameters, order, sizes);
                }
                Arrays.sort(derivedTermF, 2, derivedTermF.length);
                row.add(derivedTermF);
                for (int l2 = 2; l2 < term.length; l2++) {
                    int[] derivedTermG = new int[term.length];
                    derivedTermG[0] = term[0];
                    derivedTermG[1] = term[1];
                    for (int j3 = 2; j3 < term.length; j3++) {
                        derivedTermG[j3] = convertIndex(term[j3], parameters, derivativeCompiler.derivativesIndirection, parameters, order, sizes);
                        if (j3 == l2) {
                            System.arraycopy(derivativesIndirection[derivedTermG[j3]], 0, orders, 0, parameters);
                            int i3 = parameters - 1;
                            orders[i3] = orders[i3] + 1;
                            derivedTermG[j3] = getPartialDerivativeIndex(parameters, order, sizes, orders);
                        }
                    }
                    Arrays.sort(derivedTermG, 2, derivedTermG.length);
                    row.add(derivedTermG);
                }
            }
            List<int[]> combined = new ArrayList<>(row.size());
            for (int j4 = 0; j4 < row.size(); j4++) {
                int[] termJ = row.get(j4);
                if (termJ[0] > 0) {
                    for (int k2 = j4 + 1; k2 < row.size(); k2++) {
                        int[] termK = row.get(k2);
                        boolean equals = termJ.length == termK.length;
                        for (int l3 = 1; equals && l3 < termJ.length; l3++) {
                            equals &= termJ[l3] == termK[l3];
                        }
                        if (equals) {
                            termJ[0] = termJ[0] + termK[0];
                            termK[0] = 0;
                        }
                    }
                    combined.add(termJ);
                }
            }
            r0[vSize + i2] = (int[][]) combined.toArray(new int[combined.size()]);
        }
        return r0;
    }

    public int getPartialDerivativeIndex(int... orders) throws DimensionMismatchException, NumberIsTooLargeException {
        if (orders.length != getFreeParameters()) {
            throw new DimensionMismatchException(orders.length, getFreeParameters());
        }
        return getPartialDerivativeIndex(this.parameters, this.order, this.sizes, orders);
    }

    private static int getPartialDerivativeIndex(int parameters, int order, int[][] sizes, int... orders) throws NumberIsTooLargeException {
        int index = 0;
        int m2 = order;
        int ordersSum = 0;
        for (int i2 = parameters - 1; i2 >= 0; i2--) {
            int derivativeOrder = orders[i2];
            ordersSum += derivativeOrder;
            if (ordersSum > order) {
                throw new NumberIsTooLargeException(Integer.valueOf(ordersSum), Integer.valueOf(order), true);
            }
            while (true) {
                int i3 = derivativeOrder;
                derivativeOrder--;
                if (i3 > 0) {
                    int i4 = m2;
                    m2--;
                    index += sizes[i2][i4];
                }
            }
        }
        return index;
    }

    private static int convertIndex(int index, int srcP, int[][] srcDerivativesIndirection, int destP, int destO, int[][] destSizes) throws NumberIsTooLargeException {
        int[] orders = new int[destP];
        System.arraycopy(srcDerivativesIndirection[index], 0, orders, 0, FastMath.min(srcP, destP));
        return getPartialDerivativeIndex(destP, destO, destSizes, orders);
    }

    public int[] getPartialDerivativeOrders(int index) {
        return this.derivativesIndirection[index];
    }

    public int getFreeParameters() {
        return this.parameters;
    }

    public int getOrder() {
        return this.order;
    }

    public int getSize() {
        return this.sizes[this.parameters][this.order];
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < getSize(); i2++) {
            result[resultOffset + i2] = MathArrays.linearCombination(a1, c1[offset1 + i2], a2, c2[offset2 + i2]);
        }
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double a3, double[] c3, int offset3, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < getSize(); i2++) {
            result[resultOffset + i2] = MathArrays.linearCombination(a1, c1[offset1 + i2], a2, c2[offset2 + i2], a3, c3[offset3 + i2]);
        }
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double a3, double[] c3, int offset3, double a4, double[] c4, int offset4, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < getSize(); i2++) {
            result[resultOffset + i2] = MathArrays.linearCombination(a1, c1[offset1 + i2], a2, c2[offset2 + i2], a3, c3[offset3 + i2], a4, c4[offset4 + i2]);
        }
    }

    public void add(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < getSize(); i2++) {
            result[resultOffset + i2] = lhs[lhsOffset + i2] + rhs[rhsOffset + i2];
        }
    }

    public void subtract(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < getSize(); i2++) {
            result[resultOffset + i2] = lhs[lhsOffset + i2] - rhs[rhsOffset + i2];
        }
    }

    public void multiply(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < this.multIndirection.length; i2++) {
            int[][] mappingI = this.multIndirection[i2];
            double r2 = 0.0d;
            for (int j2 = 0; j2 < mappingI.length; j2++) {
                r2 += mappingI[j2][0] * lhs[lhsOffset + mappingI[j2][1]] * rhs[rhsOffset + mappingI[j2][2]];
            }
            result[resultOffset + i2] = r2;
        }
    }

    public void divide(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        double[] reciprocal = new double[getSize()];
        pow(rhs, lhsOffset, -1, reciprocal, 0);
        multiply(lhs, lhsOffset, reciprocal, 0, result, resultOffset);
    }

    public void remainder(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        double rem = FastMath.IEEEremainder(lhs[lhsOffset], rhs[rhsOffset]);
        double k2 = FastMath.rint((lhs[lhsOffset] - rem) / rhs[rhsOffset]);
        result[resultOffset] = rem;
        for (int i2 = 1; i2 < getSize(); i2++) {
            result[resultOffset + i2] = lhs[lhsOffset + i2] - (k2 * rhs[rhsOffset + i2]);
        }
    }

    public void pow(double a2, double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        if (a2 != 0.0d) {
            function[0] = FastMath.pow(a2, operand[operandOffset]);
            double lnA = FastMath.log(a2);
            for (int i2 = 1; i2 < function.length; i2++) {
                function[i2] = lnA * function[i2 - 1];
            }
        } else if (operand[operandOffset] == 0.0d) {
            function[0] = 1.0d;
            double infinity = Double.POSITIVE_INFINITY;
            for (int i3 = 1; i3 < function.length; i3++) {
                infinity = -infinity;
                function[i3] = infinity;
            }
        } else if (operand[operandOffset] < 0.0d) {
            Arrays.fill(function, Double.NaN);
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void pow(double[] operand, int operandOffset, double p2, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double xk = FastMath.pow(operand[operandOffset], p2 - this.order);
        for (int i2 = this.order; i2 > 0; i2--) {
            function[i2] = xk;
            xk *= operand[operandOffset];
        }
        function[0] = xk;
        double coefficient = p2;
        for (int i3 = 1; i3 <= this.order; i3++) {
            int i4 = i3;
            function[i4] = function[i4] * coefficient;
            coefficient *= p2 - i3;
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void pow(double[] operand, int operandOffset, int n2, double[] result, int resultOffset) {
        if (n2 == 0) {
            result[resultOffset] = 1.0d;
            Arrays.fill(result, resultOffset + 1, resultOffset + getSize(), 0.0d);
            return;
        }
        double[] function = new double[1 + this.order];
        if (n2 > 0) {
            int maxOrder = FastMath.min(this.order, n2);
            double xk = FastMath.pow(operand[operandOffset], n2 - maxOrder);
            for (int i2 = maxOrder; i2 > 0; i2--) {
                function[i2] = xk;
                xk *= operand[operandOffset];
            }
            function[0] = xk;
        } else {
            double inv = 1.0d / operand[operandOffset];
            double xk2 = FastMath.pow(inv, -n2);
            for (int i3 = 0; i3 <= this.order; i3++) {
                function[i3] = xk2;
                xk2 *= inv;
            }
        }
        double coefficient = n2;
        for (int i4 = 1; i4 <= this.order; i4++) {
            int i5 = i4;
            function[i5] = function[i5] * coefficient;
            coefficient *= n2 - i4;
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void pow(double[] x2, int xOffset, double[] y2, int yOffset, double[] result, int resultOffset) {
        double[] logX = new double[getSize()];
        log(x2, xOffset, logX, 0);
        double[] yLogX = new double[getSize()];
        multiply(logX, 0, y2, yOffset, yLogX, 0);
        exp(yLogX, 0, result, resultOffset);
    }

    public void rootN(double[] operand, int operandOffset, int n2, double[] result, int resultOffset) {
        double xk;
        double[] function = new double[1 + this.order];
        if (n2 == 2) {
            function[0] = FastMath.sqrt(operand[operandOffset]);
            xk = 0.5d / function[0];
        } else if (n2 == 3) {
            function[0] = FastMath.cbrt(operand[operandOffset]);
            xk = 1.0d / ((3.0d * function[0]) * function[0]);
        } else {
            function[0] = FastMath.pow(operand[operandOffset], 1.0d / n2);
            xk = 1.0d / (n2 * FastMath.pow(function[0], n2 - 1));
        }
        double nReciprocal = 1.0d / n2;
        double xReciprocal = 1.0d / operand[operandOffset];
        for (int i2 = 1; i2 <= this.order; i2++) {
            function[i2] = xk;
            xk *= xReciprocal * (nReciprocal - i2);
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void exp(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        Arrays.fill(function, FastMath.exp(operand[operandOffset]));
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void expm1(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.expm1(operand[operandOffset]);
        Arrays.fill(function, 1, 1 + this.order, FastMath.exp(operand[operandOffset]));
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.log(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / operand[operandOffset];
            double xk = inv;
            for (int i2 = 1; i2 <= this.order; i2++) {
                function[i2] = xk;
                xk *= (-i2) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log1p(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.log1p(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / (1.0d + operand[operandOffset]);
            double xk = inv;
            for (int i2 = 1; i2 <= this.order; i2++) {
                function[i2] = xk;
                xk *= (-i2) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log10(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.log10(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / operand[operandOffset];
            double xk = inv / FastMath.log(10.0d);
            for (int i2 = 1; i2 <= this.order; i2++) {
                function[i2] = xk;
                xk *= (-i2) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void cos(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.cos(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = -FastMath.sin(operand[operandOffset]);
            for (int i2 = 2; i2 <= this.order; i2++) {
                function[i2] = -function[i2 - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void sin(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.sin(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.cos(operand[operandOffset]);
            for (int i2 = 2; i2 <= this.order; i2++) {
                function[i2] = -function[i2 - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void tan(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double t2 = FastMath.tan(operand[operandOffset]);
        function[0] = t2;
        if (this.order > 0) {
            double[] p2 = new double[this.order + 2];
            p2[1] = 1.0d;
            double t22 = t2 * t2;
            for (int n2 = 1; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 + 1] = n2 * p2[n2];
                for (int k2 = n2 + 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * t22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) + ((k2 - 3) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= t2;
                }
                function[n2] = v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void acos(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.acos(x2);
        if (this.order > 0) {
            double[] p2 = new double[this.order];
            p2[0] = -1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (1.0d - x22);
            double coeff = FastMath.sqrt(f2);
            function[1] = coeff * p2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 - 1] = (n2 - 1) * p2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) + (((2 * n2) - k2) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void asin(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.asin(x2);
        if (this.order > 0) {
            double[] p2 = new double[this.order];
            p2[0] = 1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (1.0d - x22);
            double coeff = FastMath.sqrt(f2);
            function[1] = coeff * p2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 - 1] = (n2 - 1) * p2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) + (((2 * n2) - k2) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atan(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.atan(x2);
        if (this.order > 0) {
            double[] q2 = new double[this.order];
            q2[0] = 1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (1.0d + x22);
            double coeff = f2;
            function[1] = coeff * q2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                q2[n2 - 1] = (-n2) * q2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + q2[k2];
                    if (k2 > 2) {
                        q2[k2 - 2] = ((k2 - 1) * q2[k2 - 1]) + (((k2 - 1) - (2 * n2)) * q2[k2 - 3]);
                    } else if (k2 == 2) {
                        q2[0] = q2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atan2(double[] y2, int yOffset, double[] x2, int xOffset, double[] result, int resultOffset) {
        double[] tmp1 = new double[getSize()];
        multiply(x2, xOffset, x2, xOffset, tmp1, 0);
        double[] tmp2 = new double[getSize()];
        multiply(y2, yOffset, y2, yOffset, tmp2, 0);
        add(tmp1, 0, tmp2, 0, tmp2, 0);
        rootN(tmp2, 0, 2, tmp1, 0);
        if (x2[xOffset] >= 0.0d) {
            add(tmp1, 0, x2, xOffset, tmp2, 0);
            divide(y2, yOffset, tmp2, 0, tmp1, 0);
            atan(tmp1, 0, tmp2, 0);
            for (int i2 = 0; i2 < tmp2.length; i2++) {
                result[resultOffset + i2] = 2.0d * tmp2[i2];
            }
        } else {
            subtract(tmp1, 0, x2, xOffset, tmp2, 0);
            divide(y2, yOffset, tmp2, 0, tmp1, 0);
            atan(tmp1, 0, tmp2, 0);
            result[resultOffset] = (tmp2[0] <= 0.0d ? -3.141592653589793d : 3.141592653589793d) - (2.0d * tmp2[0]);
            for (int i3 = 1; i3 < tmp2.length; i3++) {
                result[resultOffset + i3] = (-2.0d) * tmp2[i3];
            }
        }
        result[resultOffset] = FastMath.atan2(y2[yOffset], x2[xOffset]);
    }

    public void cosh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.cosh(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.sinh(operand[operandOffset]);
            for (int i2 = 2; i2 <= this.order; i2++) {
                function[i2] = function[i2 - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void sinh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        function[0] = FastMath.sinh(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.cosh(operand[operandOffset]);
            for (int i2 = 2; i2 <= this.order; i2++) {
                function[i2] = function[i2 - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void tanh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double t2 = FastMath.tanh(operand[operandOffset]);
        function[0] = t2;
        if (this.order > 0) {
            double[] p2 = new double[this.order + 2];
            p2[1] = 1.0d;
            double t22 = t2 * t2;
            for (int n2 = 1; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 + 1] = (-n2) * p2[n2];
                for (int k2 = n2 + 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * t22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) - ((k2 - 3) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= t2;
                }
                function[n2] = v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void acosh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.acosh(x2);
        if (this.order > 0) {
            double[] p2 = new double[this.order];
            p2[0] = 1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (x22 - 1.0d);
            double coeff = FastMath.sqrt(f2);
            function[1] = coeff * p2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 - 1] = (1 - n2) * p2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((1 - k2) * p2[k2 - 1]) + ((k2 - (2 * n2)) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = -p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void asinh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.asinh(x2);
        if (this.order > 0) {
            double[] p2 = new double[this.order];
            p2[0] = 1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (1.0d + x22);
            double coeff = FastMath.sqrt(f2);
            function[1] = coeff * p2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                p2[n2 - 1] = (1 - n2) * p2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) + ((k2 - (2 * n2)) * p2[k2 - 3]);
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atanh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[1 + this.order];
        double x2 = operand[operandOffset];
        function[0] = FastMath.atanh(x2);
        if (this.order > 0) {
            double[] q2 = new double[this.order];
            q2[0] = 1.0d;
            double x22 = x2 * x2;
            double f2 = 1.0d / (1.0d - x22);
            double coeff = f2;
            function[1] = coeff * q2[0];
            for (int n2 = 2; n2 <= this.order; n2++) {
                double v2 = 0.0d;
                q2[n2 - 1] = n2 * q2[n2 - 2];
                for (int k2 = n2 - 1; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * x22) + q2[k2];
                    if (k2 > 2) {
                        q2[k2 - 2] = ((k2 - 1) * q2[k2 - 1]) + ((((2 * n2) - k2) + 1) * q2[k2 - 3]);
                    } else if (k2 == 2) {
                        q2[0] = q2[1];
                    }
                }
                if ((n2 & 1) == 0) {
                    v2 *= x2;
                }
                coeff *= f2;
                function[n2] = coeff * v2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void compose(double[] operand, int operandOffset, double[] f2, double[] result, int resultOffset) {
        for (int i2 = 0; i2 < this.compIndirection.length; i2++) {
            int[][] mappingI = this.compIndirection[i2];
            double r2 = 0.0d;
            for (int[] mappingIJ : mappingI) {
                double product = mappingIJ[0] * f2[mappingIJ[1]];
                for (int k2 = 2; k2 < mappingIJ.length; k2++) {
                    product *= operand[operandOffset + mappingIJ[k2]];
                }
                r2 += product;
            }
            result[resultOffset + i2] = r2;
        }
    }

    public double taylor(double[] ds, int dsOffset, double... delta) throws MathArithmeticException {
        double value = 0.0d;
        for (int i2 = getSize() - 1; i2 >= 0; i2--) {
            int[] orders = getPartialDerivativeOrders(i2);
            double term = ds[dsOffset + i2];
            for (int k2 = 0; k2 < orders.length; k2++) {
                if (orders[k2] > 0) {
                    try {
                        term *= FastMath.pow(delta[k2], orders[k2]) / CombinatoricsUtils.factorial(orders[k2]);
                    } catch (NotPositiveException e2) {
                        throw new MathInternalError(e2);
                    }
                }
            }
            value += term;
        }
        return value;
    }

    public void checkCompatibility(DSCompiler compiler) throws DimensionMismatchException {
        if (this.parameters != compiler.parameters) {
            throw new DimensionMismatchException(this.parameters, compiler.parameters);
        }
        if (this.order != compiler.order) {
            throw new DimensionMismatchException(this.order, compiler.order);
        }
    }
}
