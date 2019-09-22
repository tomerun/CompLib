  static class FastScanner {
    Reader input;

    FastScanner() {
      this(System.in);
    }

    FastScanner(InputStream stream) {
      this.input = new BufferedReader(new InputStreamReader(stream));
    }

    int nextInt() {
      return (int) nextLong();
    }

    long nextLong() {
      try {
        int sign = 1;
        int b = input.read();
        while ((b < '0' || '9' < b) && b != '-' && b != '+') {
          b = input.read();
        }
        if (b == '-') {
          sign = -1;
          b = input.read();
        } else if (b == '+') {
          b = input.read();
        }
        long ret = b - '0';
        while (true) {
          b = input.read();
          if (b < '0' || '9' < b) return ret * sign;
          ret *= 10;
          ret += b - '0';
        }
      } catch (IOException e) {
        e.printStackTrace();
        return -1;
      }
    }

    double nextDouble() {
      try {
        double sign = 1;
        int b = input.read();
        while ((b < '0' || '9' < b) && b != '-' && b != '+') {
          b = input.read();
        }
        if (b == '-') {
          sign = -1;
          b = input.read();
        } else if (b == '+') {
          b = input.read();
        }
        double ret = b - '0';
        while (true) {
          b = input.read();
          if (b < '0' || '9' < b) break;
          ret *= 10;
          ret += b - '0';
        }
        if (b != '.') return sign * ret;
        double div = 1;
        b = input.read();
        while ('0' <= b && b <= '9') {
          ret *= 10;
          ret += b - '0';
          div *= 10;
          b = input.read();
        }
        return sign * ret / div;
      } catch (IOException e) {
        e.printStackTrace();
        return Double.NaN;
      }
    }

    char nextChar() {
      try {
        int b = input.read();
        while (Character.isWhitespace(b)) {
          b = input.read();
        }
        return (char) b;
      } catch (IOException e) {
        e.printStackTrace();
        return 0;
      }
    }

    String nextStr() {
      try {
        StringBuilder sb = new StringBuilder();
        int b = input.read();
        while (Character.isWhitespace(b)) {
          b = input.read();
        }
        while (b != -1 && !Character.isWhitespace(b)) {
          sb.append((char) b);
          b = input.read();
        }
        return sb.toString();
      } catch (IOException e) {
        e.printStackTrace();
        return "";
      }
    }

  }
