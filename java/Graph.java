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

