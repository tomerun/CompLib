  static class SCC {

    int N;
    ArrayList<ArrayList<Integer>> child = new ArrayList<>();
    ArrayList<ArrayList<Integer>> parent = new ArrayList<>();
    ArrayList<ArrayList<Integer>> groupToOrig = new ArrayList<>();
    ArrayList<ArrayList<Integer>> groupChild = new ArrayList<>();
    int[] idx;
    int[] group;

    void addEdge(int from, int to) {
      while (child.size() <= from) {
        child.add(new ArrayList<>());
      }
      while (parent.size() <= to) {
        parent.add(new ArrayList<>());
      }
      child.get(from).add(to);
      parent.get(to).add(from);
    }

    void calc() {
      while (child.size() < parent.size()) {
        child.add(new ArrayList<>());
      }
      while (parent.size() < child.size()) {
        parent.add(new ArrayList<>());
      }
      N = child.size();
      dfs();
      revdfs();
    }

    void dfs() {
      idx = new int[N];
      boolean[] used = new boolean[N];
      int pos = 0;
      for (int i = 0; i < N; ++i) {
        if (used[i]) continue;
        ArrayList<Integer> buf = new ArrayList<>();
        used[i] = true;
        buf.add(i);
        while (!buf.isEmpty()) {
          int cur = buf.get(buf.size() - 1);
          buf.remove(buf.size() - 1);
          if (cur < 0) {
            idx[pos++] = -cur - 1;
            continue;
          }
          buf.add(-cur - 1);
          for (int c : child.get(cur)) {
            if (used[c]) continue;
            used[c] = true;
            buf.add(c);
          }
        }
      }
    }

    void revdfs() {
      int gi = 0;
      group = new int[N];
      boolean[] used = new boolean[N];
      for (int i = N - 1; i >= 0; --i) {
        int id = idx[i];
        if (used[id]) continue;
        ArrayList<Integer> buf = new ArrayList<>();
        used[id] = true;
        buf.add(id);
        for (int j = 0; j < buf.size(); j++) {
          int cur = buf.get(j);
          group[cur] = gi;
          for (int p : parent.get(cur)) {
            if (used[p]) continue;
            used[p] = true;
            buf.add(p);
          }
        }
        ++gi;
        groupToOrig.add(buf);
      }
      for (int i = 0; i < gi; i++) {
        HashSet<Integer> gc = new HashSet<>();
        for (int n : groupToOrig.get(i)) {
          for (int c : child.get(n)) {
            gc.add(group[c]);
          }
        }
        groupChild.add(new ArrayList<>(gc));
      }
    }
  }


class SCC {

	static int N;
	static boolean[][] g;
	static int ii;
	static int[] idx;
	static int[] ord;
	static boolean[] used;

	public static void main(String[] args) {
		N = graph.length;
		g = new boolean[N][N];
		ii = 0;
		idx = new int[N];
		ord = new int[N];
		used = new boolean[N];
		Arrays.fill(idx, -1);
		for (int i = 0; i < N; ++i) {
			if (!used[i]) dfs(i);
		}
		int M = 0;
		used = new boolean[N];
		for (int i = N - 1; i >= 0; --i) {
			int id = idx[i];
			if (!used[id]) revdfs(id, M++);
		}
	}

	static void dfs(int cur) {
		used[cur] = true;
		for (int i = 0; i < N; ++i) {
			if (used[i]) continue;
			if (!g[cur][i]) continue;
			dfs(i);
		}
		idx[ii++] = cur;
	}

	static void revdfs(int cur, int o) {
		ord[cur] = o;
		used[cur] = true;
		for (int i = 0; i < N; ++i) {
			if (used[i]) continue;
			if (!g[i][cur]) continue;
			revdfs(i, o);
		}
	}

}

