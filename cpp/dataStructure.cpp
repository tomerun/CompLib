namespace segtree {

  int maxH[L_2][L_2]; 

  int getMaxY(int cur, int l, int r, int x, int y1, int y2) {
    if (r - l <= 1) return maxH[x][l];
    int m = (l + r) / 2;
    int ret = 0;
    if (y1 < m) {
      ret = max(ret, getMaxY(cur * 2 + 1, l, m, x, y1, min(y2, m)));
    }
    if (y2 > m) {
      ret = max(ret, getMaxY(cur * 2 + 2, m, r, x, max(y1, m), y2));
    }
    return ret;
  }

  int getMaxX(int cur, int l, int r, int x1, int x2, int y1, int y2) {
    if (r - l <= 1) return getMaxY(0, 0, L, cur, y1, y2);
    int m = (l + r) / 2;
    int ret = 0;
    if (x1 < m) {
      ret = max(ret, getMaxX(cur * 2 + 1, l, m, x1, min(x2, m), y1, y2));
    }
    if (x2 > m) {
      ret = max(ret, getMaxX(cur * 2 + 2, m, r, max(x1, m), x2, y1, y2));
    }
    return ret;
  }

  int getMax(int x1, int x2, int y1, int y2) {
    return getMaxX(0, 0, L, x1, x2, y1, y2);
  }

  void updateY(int cur, int l, int r, int x, int y1, int y2, int v) {
    if (r - l <= 1) {
      maxH[x][l] = max(maxH[x][l], v);
      return;
    }
    int m = (l + r) / 2;
    if (y1 < m) {
      updateY(cur * 2 + 1, l, m, x, y1, min(y2, m), v);
    }
    if (y2 > m) {
      updateY(cur * 2 + 2, m, r, x, max(y1, m), y2, v);
    }
  }

  void updateX(int cur, int l, int r, int x1, int x2, int y1, int y2, int v) {
    if (r - l <= 1) {
      updateY(0, 0, L, cur, y1, y2, v);
      return;
    }
    int m = (l + r) / 2;
    if (x1 < m) {
      updateX(cur * 2 + 1, l, m, x1, min(x2, m), y1, y2, v);
    }
    if (x2 > m) {
      updateX(cur * 2 + 2, m, r, max(x1, m), x2, y1, y2, v);
    }
  }

  void update(int x1, int x2, int y1, int y2, int v) {
    updateX(0, 0, L, x1, x2, y1, y2, v);
  }
}
