	static class BiMatching {
		ArrayList<ArrayList<Integer>> left = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> right = new ArrayList<ArrayList<Integer>>();
		int[] l2r, r2l;

		BiMatching(int n, int m) {
			l2r = new int[n];
			r2l = new int[m];
			Arrays.fill(l2r, -1);
			Arrays.fill(r2l, -1);
			for (int i = 0; i < n; ++i) {
				left.add(new ArrayList<Integer>());
			}
			for (int i = 0; i < m; ++i) {
				right.add(new ArrayList<Integer>());
			}
		}

		void addEdge(int ln, int rn) {
			left.get(ln).add(rn);
			right.get(rn).add(ln);
		}

		int matching() {
			int res = 0;
			for (int i = 0; i < left.size(); ++i) {
				if (l2r[i] != -1) continue;
				boolean[] visited = new boolean[right.size()];
				for (int j = 0; j < left.get(i).size(); ++j) {
					if (dfs(left.get(i).get(j), visited)) {
						l2r[i] = left.get(i).get(j);
						r2l[left.get(i).get(j)] = i;
						++res;
						break;
					}
				}
			}
			return res;
		}

		boolean dfs(int rn, boolean[] visited) {
			int l = r2l[rn];
			if (l == -1) {
				return true;
			}
			visited[rn] = true;
			for (int i = 0; i < left.get(l).size(); ++i) {
				int r = left.get(l).get(i);
				if (visited[r]) continue;
				if (dfs(r, visited)) {
					r2l[r] = l;
					l2r[l] = r;
					return true;
				}
			}
			return false;
		}
	}


	static class MaxFlow {

		int[][] g;
		int[][] res;

		MaxFlow(int[][] g) {
			this.g = g;
			this.res = new int[g.length][];
			for (int i = 0; i < g.length; ++i) {
				this.res[i] = g[i].clone();
			}
		}

		int calc(int src, int sink) {
			int result = 0;
			int[] prev = new int[g.length];
			while (true) {
				Arrays.fill(prev, -1);
				boolean[] visited = new boolean[g.length];
				Queue<Integer> q = new LinkedList<Integer>();
				q.add(src);
				visited[src] = true;
				OUT: while (!q.isEmpty()) {
					int cur = q.poll();
					for (int i = 0; i < g.length; ++i) {
						if (!visited[i] && res[cur][i] > 0) {
							prev[i] = cur;
							visited[i] = true;
							if (i == sink) {
								break OUT;
							}
							q.add(i);
						}
					}
				}
				if (!visited[sink]) {
					break;
				}
				int pos = sink;
				int pathCap = Integer.MAX_VALUE;
				while (pos != src) {
					int p = prev[pos];
					pathCap = Math.min(pathCap, res[p][pos]);
					pos = p;
				}
				pos = sink;
				while (pos != src) {
					int p = prev[pos];
					res[p][pos] -= pathCap;
					res[pos][p] += pathCap;
					pos = p;
				}
				result += pathCap;
			}
			return result;
		}
	}

	// adjacency array
	static class MinCostFlow {
		int size;
		int[][] cost;
		int[][] cap;

		MinCostFlow(int size) {
			this.size = size;
			cost = new int[size][size];
			cap = new int[size][size];
		}

		void addEdge(int from, int to, int c, int capacity) {
			cost[from][to] = c;
			cap[from][to] = capacity;
			cost[to][from] = -c;
		}

		int calc(int flow) {
			final int INF = 1 << 25;
			int total = 0;
			int[] h = new int[size];
			Arrays.fill(h, 0);
			int[] prev = new int[size];
			while (flow > 0) {
				int[] dist = new int[size];
				Arrays.fill(dist, INF);
				dist[0] = 0;
				PriorityQueue<State> q = new PriorityQueue<State>();
				q.add(new State(0, dist[0]));
				while (!q.isEmpty()) {
					State st = q.poll();
					if (st.cost > dist[st.v]) continue;
					for (int i = 0; i < size; ++i) {
						if (cap[st.v][i] == 0) continue;
						int nCost = dist[st.v] + cost[st.v][i] + h[st.v] - h[i];
						if (dist[i] > nCost) {
							dist[i] = nCost;
							q.add(new State(i, nCost));
							prev[i] = st.v;
						}
					}
				}
				if (dist[size - 1] == INF) break;
				for (int i = 0; i < size; ++i) {
					h[i] += dist[i];
				}
				int f = Integer.MAX_VALUE;
				for (int pos = size - 1; pos != 0; pos = prev[pos]) {
					f = Math.min(f, cap[prev[pos]][pos]);
				}
				for (int pos = size - 1; pos != 0; pos = prev[pos]) {
					cap[prev[pos]][pos] -= f;
					cap[pos][prev[pos]] += f;
				}
				total += h[size - 1] * f;
				flow -= f;
			}
			return total;
		}

		static class State implements Comparable<State> {
			int v;
			int cost;

			public State(int v, int cost) {
				this.v = v;
				this.cost = cost;
			}

			public int compareTo(State o) {
				return this.cost - o.cost;
			}
		}
	}

	// explicit edge
	static class MinCostFlow {
		int size;
		ArrayList<ArrayList<Edge>> g = new ArrayList<>();

		static class Edge {
			int from, to, cost, cap;
			Edge rev;

			public Edge(int from, int to, int cost, int cap) {
				this.from = from;
				this.to = to;
				this.cost = cost;
				this.cap = cap;
			}
		}

		MinCostFlow(int size) {
			this.size = size;
			for (int i = 0; i < size; ++i) {
				g.add(new ArrayList<Edge>());
			}
		}

		void addEdge(int from, int to, int c, int capacity) {
			Edge e1 = new Edge(from, to, c, capacity);
			Edge e2 = new Edge(to, from, -c, 0);
			e1.rev = e2;
			e2.rev = e1;
			g.get(from).add(e1);
			g.get(to).add(e2);
		}

		int calc(int flow) {
			final int INF = 1 << 25;
			int total = 0;
			int[] h = new int[size];
			Arrays.fill(h, 0);
			Edge[] prev = new Edge[size];
			while (flow > 0) {
				int[] dist = new int[size];
				Arrays.fill(dist, INF);
				dist[0] = 0;
				PriorityQueue<State> q = new PriorityQueue<State>();
				q.add(new State(0, dist[0]));
				while (!q.isEmpty()) {
					State st = q.poll();
					if (st.cost > dist[st.v]) continue;
					for (Edge e : g.get(st.v)) {
						if (e.cap == 0) continue;
						int nCost = dist[st.v] + e.cost + h[st.v] - h[e.to];
						if (dist[e.to] > nCost) {
							dist[e.to] = nCost;
							q.add(new State(e.to, nCost));
							prev[e.to] = e;
						}
					}
				}
				if (dist[size - 1] == INF) return -1;
				for (int i = 0; i < size; ++i) {
					h[i] += dist[i];
				}
				int f = Integer.MAX_VALUE;
				for (int pos = size - 1; pos != 0; pos = prev[pos].from) {
					f = Math.min(f, prev[pos].cap);
				}
				for (int pos = size - 1; pos != 0; pos = prev[pos].from) {
					prev[pos].cap -= f;
					prev[pos].rev.cap += f;
				}
				total += h[size - 1] * f;
				flow -= f;
			}
			return total;
		}

		static class State implements Comparable<State> {
			int v;
			int cost;

			public State(int v, int cost) {
				this.v = v;
				this.cost = cost;
			}

			public int compareTo(State o) {
				return this.cost - o.cost;
			}
		}
	}


