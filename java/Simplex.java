import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class Simplex {

  static final double EPS = 1e-12;
  int d;
  double[][] a;
  double[] b;
  double[] c; // maximization
  int[] varI;
  double v;
  BitSet base;
  boolean[] nonnegative;
  ArrayList<LowerConstraint> constraints = new ArrayList<>();

  Simplex(int dimension) {
    this.d = dimension;
    this.nonnegative = new boolean[d];
  }

  void setTarget(double[] c) {
    if (c.length != this.d) {
      throw new RuntimeException("illegal dimension");
    }
    this.c = c.clone();
  }

  void addLowerConstraint(double[] coef, double rhs) {
    this.constraints.add(new LowerConstraint(coef.clone(), rhs));
  }

  void addUpperConstraint(double[] coef, double rhs) {
    double[] coefNegated = new double[d];
    for (int i = 0; i < d; i++) {
      coefNegated[i] = -coef[i];
    }
    this.constraints.add(new LowerConstraint(coefNegated, -rhs));
  }

  void addEqualityConstraint(double[] coef, double rhs) {
    addLowerConstraint(coef, rhs);
    addUpperConstraint(coef, rhs);
  }

  void setNonnegativity(int idx) {
    this.nonnegative[idx] = true;
  }

  Result solve() {
    convertToStandardSlackForm();
    debugPrint();
    Result.Type resType = initialize();
    if (resType != Result.Type.POSSIBLE) return new Result(resType);
    Result res = optimize();
    if (res.type != Result.Type.POSSIBLE) return res;
    Result ret = new Result(res.targetValue, new double[d]);
    for (int i = 0, e = 0; i < d; i++) {
      ret.variables[i] = res.variables[i];
      if (!nonnegative[i]) {
        ret.variables[i] -= res.variables[d + e];
        ++e;
      }
    }
    return ret;
  }

  private void convertToStandardSlackForm() {
    int extra = 0;
    for (int i = 0; i < c.length; i++) {
      if (!nonnegative[i]) ++extra;
    }
    double[] oldC = c;
    int n = oldC.length;
    int m = constraints.size();
    c = new double[n + extra + m];
    System.arraycopy(oldC, 0, c, 0, n);
    for (int i = 0, e = 0; i < n; i++) {
      if (!nonnegative[i]) {
        c[n + e] -= oldC[i];
        ++e;
      }
    }
    v = 0;
    base = new BitSet(n + extra + m);
    base.set(n + extra, base.size());
    a = new double[m][n + extra + m];
    b = new double[m];
    varI = new int[m];
    for (int i = 0; i < m; i++) {
      LowerConstraint constraint = constraints.get(i);
      varI[i] = n + extra + i;
      for (int j = 0, e = 0; j < n; j++) {
        a[i][j] = constraint.coef[j];
        if (!nonnegative[j]) {
          a[i][n + e] = -constraint.coef[j];
          ++e;
        }
      }
      b[i] = constraint.rhs;
    }
  }

  private Result.Type initialize() {
    int minBIdx = 0;
    for (int i = 1; i < b.length; i++) {
      if (b[i] < b[minBIdx]) minBIdx = i;
    }
    if (b[minBIdx] >= 0) return Result.Type.POSSIBLE;
    Simplex aux = new Simplex(this.c.length + 1);
    aux.a = new double[this.a.length][this.c.length + 1];
    for (int i = 0; i < this.a.length; i++) {
      System.arraycopy(this.a[i], 0, aux.a[i], 0, this.c.length);
      aux.a[i][this.c.length] = -1;
    }
    aux.b = this.b.clone();
    aux.c = new double[this.c.length + 1];
    aux.c[this.c.length] = -1;
    aux.base = new BitSet(aux.c.length);
    aux.base.set(this.c.length - this.a.length, this.c.length);
    aux.varI = this.varI.clone();
    System.err.println("aux:");
    aux.debugPrint();
    aux.pivot(this.c.length, minBIdx);
    aux.debugPrint();
    Result auxRes = aux.optimize();
    System.err.println("auxRes:" + auxRes);
    if (auxRes.type == Result.Type.POSSIBLE && Math.abs(auxRes.targetValue) < EPS) {
      // restructure
      this.base.clear();
      for (int i = 0; i < aux.a.length; i++) {
        int baseI = aux.varI[i];
        if (baseI == aux.c.length - 1) continue;
        this.base.set(baseI);
        System.arraycopy(aux.a[i], 0, this.a[i], 0, this.a[i].length);
        this.b[i] = aux.b[i];
        this.varI[i] = aux.varI[i];
        double mul = this.c[baseI];
        this.c[baseI] = 0;
        for (int j = 0; j < this.c.length; j++) {
          this.c[j] -= mul * aux.a[i][j];
        }
        this.v += mul * aux.b[i];
      }
      for (int i = 0; i < this.a.length; i++) {
        for (int j = 0; j < this.a[i].length; j++) {
          if (Math.abs(this.a[i][j]) < EPS) this.a[i][j] = 0;
        }
        if (Math.abs(this.b[i]) < EPS) this.b[i] = 0;
      }
      for (int i = 0; i < this.c.length; i++) {
        if (Math.abs(this.c[i]) < EPS) this.c[i] = 0;
      }
      return Result.Type.POSSIBLE;
    }
    return Result.Type.IMPOSSIBLE;
  }

  private Result optimize() {
    System.err.println("optimize");
    debugPrint();
    while (true) {
      int entering = -1;
      for (int i = 0; i < c.length; i++) {
        if (base.get(i)) continue;
        if (c[i] <= 0) continue;
        double increase = Double.MAX_VALUE;
        int leavingIdx = -1;
        for (int j = 0; j < a.length; j++) {
          if (a[j][i] > 0) {
            double curIncrease = b[j] / a[j][i];
            if (curIncrease < increase) {
              increase = curIncrease;
              leavingIdx = j;
            }
          }
        }
        if (leavingIdx < 0) {
          return new Result(Result.Type.UNBOUNDED);
        }
        pivot(i, leavingIdx);
        debugPrint();
        entering = i;
        break;
      }
      if (entering == -1) break;
    }
    double[] variables = new double[c.length];
    for (int i = 0; i < b.length; i++) {
      variables[varI[i]] = b[i];
    }
    return new Result(v, variables);
  }

  private void pivot(int entering, int leavingIdx) {
    int leaving = varI[leavingIdx];
    System.err.println("pivot:" + entering + " " + leaving);
    varI[leavingIdx] = entering;
    a[leavingIdx][leaving] = 1;
    double mul = 1.0 / a[leavingIdx][entering];
    for (int i = 0; i < a[leavingIdx].length; i++) {
      a[leavingIdx][i] *= mul;
    }
    a[leavingIdx][entering] = 0;
    b[leavingIdx] *= mul;
    for (int i = 0; i < a.length; i++) {
      if (i == leavingIdx) continue;
      double coef = a[i][entering];
      for (int j = 0; j < a[i].length; j++) {
        a[i][j] -= coef * a[leavingIdx][j];
      }
      a[i][entering] = 0;
      b[i] -= coef * b[leavingIdx];
    }
    double coef = c[entering];
    for (int i = 0; i < c.length; i++) {
      c[i] += -coef * a[leavingIdx][i];
    }
    c[entering] = 0;
    v += coef * b[leavingIdx];
    base.set(entering);
    base.clear(leaving);
  }

  private void debugPrint() {
    System.err.println("a:");
    for (double[] aa : a) {
      System.err.println(Arrays.toString(aa));
    }
    System.err.println("b:" + Arrays.toString(b));
    System.err.println("c:" + Arrays.toString(c) + " v:" + v);
    System.err.println("varI:" + Arrays.toString(varI));
    System.err.println();
  }

  static class Result {
    enum Type {POSSIBLE, IMPOSSIBLE, UNBOUNDED}

    Type type;
    double targetValue;
    double[] variables;

    Result(Type type) {
      this.type = type;
    }

    Result(double target, double[] variables) {
      this.type = Type.POSSIBLE;
      this.targetValue = target;
      this.variables = variables;
    }

    @Override
    public String toString() {
      if (type != Type.POSSIBLE) return "type:" + type;
      return "type:" + type + "\nans:" + targetValue + "\nvariables:" + Arrays.toString(variables);
    }
  }

  static class LowerConstraint {
    double[] coef;
    double rhs;

    LowerConstraint(double[] coef, double rhs) {
      this.coef = coef;
      this.rhs = rhs;
    }
  }

  public static void main(String[] args) {
    Simplex simplex = new Simplex(3);
    simplex.setTarget(new double[]{-1, -1, -1});
    simplex.addUpperConstraint(new double[]{2, 7.5, 3}, 10000);
    simplex.addUpperConstraint(new double[]{20, 5, 10}, 30000);
    simplex.setNonnegativity(0);
    simplex.setNonnegativity(1);
    simplex.setNonnegativity(2);
    Result res = simplex.solve();
    System.err.println(res);
  }
}
