	static void calcParentPow2() {
		int logN = 1;
		while ((1 << logN) < N) {
			++logN;
		}
		parent2 = new int[N][logN];
		for (int[] a : parent2) {
			Arrays.fill(a, -1);
		}
		for (int i = 0; i < N; ++i) {
			parent2[i][0] = parent[i];
		}
		for (int i = 1; (1 << i) < N; ++i) {
			for (int j = 0; j < N; ++j) {
				if (parent2[j][i - 1] != -1) {
					parent2[j][i] = parent2[parent2[j][i - 1]][i - 1];
				}
			}
		}
	}

	static int lca(int a, int b) {
		if (depth[a] < depth[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		int log = 1;
		while ((1 << log) <= depth[a]) {
			++log;
		}
		--log;
		for (int i = log; i >= 0; --i) {
			if (depth[a] - (1 << i) >= depth[b]) {
				a = parent2[a][i];
			}
		}
		if (a == b) {
			return a;
		}
		for (int i = log; i >= 0; --i) {
			if (parent2[a][i] != -1 && parent2[a][i] != parent2[b][i]) {
				a = parent2[a][i];
				b = parent2[b][i];
			}
		}
		return parent[a];
	}

	static class BIT {
		long[] v;

		BIT(int size) {
			int len = 1;
			while (len < size) {
				len *= 2;
			}
			this.v = new long[len + 1];
		}

		long cumulativeSum(int index) {
			long ret = 0;
			while (index > 0) {
				ret += v[index];
				ret %= MOD;
				index &= index - 1;
			}
			return ret;
		}

		// inclusive, 1-indexed
		long sum(int l, int r) {
			long ret = cumulativeSum(r) - cumulativeSum(l - 1);
			if (ret < 0) ret += MOD;
			return ret;
		}

		void add(int index, long val) {
			while (index < v.length) {
				v[index] += val;
				v[index] %= MOD;
				if (v[index] < 0) v[index] += MOD;
				index += (index & -index);
			}
		}

		void set(int index, long val) {
			long old = sum(index, index);
			add(index, val - old);
		}
	}

  static class PersistentBIT {
    int len;
    Node root;

    static class Node {
      long v;
      Node left, right;

      Node() {
      }

      Node(Node o) {
        if (o != null) {
          this.v = o.v;
          this.left = o.left;
          this.right = o.right;
        }
      }
    }

    PersistentBIT(int size) {
      this.len = 1;
      while (this.len <= size) {
        this.len *= 2;
      }
      this.root = new Node();
    }

    PersistentBIT(PersistentBIT o) {
      this.len = o.len;
      this.root = new Node(o.root);
    }

    // inclusive, 1-indexed
    long cumulativeSum(int index) {
      long ret = 0;
      int bit = this.len;
      Node node = this.root;
      while (node != null && bit > 0) {
        if ((index & bit) != 0) {
          node = node.right;
          if (node != null) ret += node.v;
        } else {
          node = node.left;
        }
        bit >>= 1;
      }
      return ret;
    }

    // inclusive, 1-indexed
    long sum(int l, int r) {
      return cumulativeSum(r) - cumulativeSum(l - 1);
    }

    PersistentBIT add(int index, long val) {
      PersistentBIT ret = new PersistentBIT(this);
      int bit = ret.len;
      Node node = ret.root;
      while (bit > 0) {
        if ((index & bit) != 0) {
          Node child = new Node(node.right);
          child.v += val;
          node.right = child;
          node = child;
        } else {
          Node child = new Node(node.left);
          node.left = child;
          node = child;
        }
        bit >>= 1;
      }
      return ret;
    }

    PersistentBIT set(int index, long val) {
      long old = sum(index, index);
      return add(index, val - old);
    }
  }

  static class SegTree {

    int size;
    long[] tree;

    SegTree(int size) {
      this.size = size;
      int l = 1;
      while (l < size * 2) {
        l *= 2;
      }
      tree = new long[l];
    }

    void add(int pos, long v) {
      add(0, 0, size, pos, v);
    }

    private void add(int idx, int left, int right, int pos, long v) {
      tree[idx] += v;
      tree[idx] %= MOD;
      if (left + 1 == right) {
        return;
      }
      int mid = (left + right) / 2;
      if (pos < mid) add(idx * 2 + 1, left, mid, pos, v);
      if (mid <= pos) add(idx * 2 + 2, mid, right, pos, v);
    }

    void addNonRec(int pos, long v) {
      int idx = 0;
      int left = 0;
      int right = size;
      while (true) {
        tree[idx] += v;
        tree[idx] %= MOD;
        if (left + 1 == right) {
          return;
        }
        int mid = (left + right) / 2;
        if (pos < mid) {
          idx = idx * 2 + 1;
          right = mid;
        } else {
          idx = idx * 2 + 2;
          left = mid;
        }
      }
    }

    long get(int lo, int hi) {
      return get(0, 0, size, lo, hi);
    }

    private long get(int idx, int left, int right, int lo, int hi) {
      if (hi <= left || right <= lo) return 0;
      if (lo <= left && right <= hi) {
        return tree[idx];
      }
      long ret = 0;
      int mid = (left + right) / 2;
      if (lo < mid) ret += get(idx * 2 + 1, left, mid, lo, hi);
      if (mid <= hi) ret += get(idx * 2 + 2, mid, right, lo, hi);
      return ret % MOD;
    }
  }

	static class PersistentSegtree {
		int lo, hi; // [lo, hi)
		PersistentSegtree left, right;
		int count;

		PersistentSegtree(int lo, int hi) {
			this.lo = lo;
			this.hi = hi;
		}

		PersistentSegtree(PersistentSegtree o) {
			this.lo = o.lo;
			this.hi = o.hi;
			this.left = o.left;
			this.right = o.right;
			this.count = o.count;
		}

		PersistentSegtree add(int v) {
			if (this.lo + 1 == this.hi) {
				PersistentSegtree ret = new PersistentSegtree(this);
				ret.count++;
				return ret;
			}
			int mid = (this.lo + this.hi) / 2;
			PersistentSegtree ret = new PersistentSegtree(this);
			if (v < mid) {
				PersistentSegtree nc;
				if (this.left == null) {
					nc = new PersistentSegtree(this.lo, mid);
					nc = nc.add(v);
				} else {
					nc = this.left.add(v);
				}
				ret.left = nc;
			} else {
				PersistentSegtree nc;
				if (this.right == null) {
					nc = new PersistentSegtree(mid, this.hi);
					nc = nc.add(v);
				} else {
					nc = this.right.add(v);
				}
				ret.right = nc;
			}
			ret.count++;
			return ret;
		}

		int count(int min, int max) {
			if (min <= this.lo && this.hi <= max) return this.count;
			int mid = (this.lo + this.hi) / 2;
			int ret = 0;
			if (min < mid && this.left != null) {
				ret += this.left.count(min, max);
			}
			if (mid < max && this.right != null) {
				ret += this.right.count(min, max);
			}
			return ret;
		}

		int rank(int k) {
			if (this.lo + 1 == this.hi) return this.lo;
			if (this.left != null && k <= this.left.count) {
				return this.left.rank(k);
			}
			return this.right.rank(k - (this.left == null ? 0 : this.left.count));
		}
	}

  static class DelayedSegTree {
    int size;
    Node[] tree;

    public DelayedSegTree(int size) {
      this.size = 1;
      while (this.size < size * 2) {
        this.size *= 2;
      }
      tree = new Node[this.size];
      for (int i = 0; i < this.size; i++) {
        tree[i] = new Node();
      }
    }

    void add(int l, int r, int c2, int c5) {
      addRec(0, 0, size / 2, l, r, c2, c5);
    }

    void addRec(int idx, int left, int right, int l, int r, int c2, int c5) {
      Node node = tree[idx];
      if (node.invalid && idx * 2 + 2 < tree.length) {
        tree[idx * 2 + 1].setInvalid();
        tree[idx * 2 + 2].setInvalid();
      }
      node.invalid = false;
      if (l <= left && right <= r) {
        node.sum2 += (long) c2 * (right - left);
        node.sum5 += (long) c5 * (right - left);
        node.each2 += c2;
        node.each5 += c5;
        return;
      }
      propagate(idx, right - left);
      int mid = (left + right) / 2;
      if (l < mid) addRec(idx * 2 + 1, left, mid, l, r, c2, c5);
      if (mid < r) addRec(idx * 2 + 2, mid, right, l, r, c2, c5);
      node.sum2 = tree[idx * 2 + 1].sum2 + tree[idx * 2 + 2].sum2;
      node.sum5 = tree[idx * 2 + 1].sum5 + tree[idx * 2 + 2].sum5;
    }

    void setRange(int l, int r, int start) {
      setRangeRec(0, 0, size / 2, l, r, start);
    }

    void setRangeRec(int idx, int left, int right, int l, int r, int start) {
      Node node = tree[idx];
      if (node.invalid && idx * 2 + 2 < tree.length) {
        tree[idx * 2 + 1].setInvalid();
        tree[idx * 2 + 2].setInvalid();
      }
      node.invalid = false;
      if (l <= left && right <= r) {
        node.invalid = true;
        node.sum2 = node.sum5 = node.each2 = node.each5 = 0;
        node.setRange(start + left - l, right - left);
        return;
      }
      propagate(idx, right - left);
      int mid = (left + right) / 2;
      if (l < mid) setRangeRec(idx * 2 + 1, left, mid, l, r, start);
      if (mid < r) setRangeRec(idx * 2 + 2, mid, right, l, r, start);
      node.sum2 = tree[idx * 2 + 1].sum2 + tree[idx * 2 + 2].sum2;
      node.sum5 = tree[idx * 2 + 1].sum5 + tree[idx * 2 + 2].sum5;
    }

    void get(int l, int r, long[] ret) {
      getRec(0, 0, size / 2, l, r, ret);
    }

    void getRec(int idx, int left, int right, int l, int r, long[] ret) {
      Node node = tree[idx];
      if (node.invalid && idx * 2 + 2 < tree.length) {
        tree[idx * 2 + 1].setInvalid();
        tree[idx * 2 + 2].setInvalid();
      }
      node.invalid = false;
      if (l <= left && right <= r) {
        ret[0] += node.sum2;
        ret[1] += node.sum5;
        return;
      }
      propagate(idx, right - left);
      int mid = (left + right) / 2;
      if (l < mid) getRec(idx * 2 + 1, left, mid, l, r, ret);
      if (mid < r) getRec(idx * 2 + 2, mid, right, l, r, ret);
      node.sum2 = tree[idx * 2 + 1].sum2 + tree[idx * 2 + 2].sum2;
      node.sum5 = tree[idx * 2 + 1].sum5 + tree[idx * 2 + 2].sum5;
    }

    void propagate(int idx, int len) {
      if (idx * 2 + 2 >= size) return;
      Node node = tree[idx];
      for (int i = 0; i < 2; i++) {
        tree[idx * 2 + i + 1].each2 += node.each2;
        tree[idx * 2 + i + 1].each5 += node.each5;
        tree[idx * 2 + i + 1].sum2 += (long) node.each2 * len / 2;
        tree[idx * 2 + i + 1].sum5 += (long) node.each5 * len / 2;
      }
      node.each2 = node.each5 = 0;
      if (node.rangeStart != 0) {
        tree[idx * 2 + 1].setRange(node.rangeStart, len / 2);
        tree[idx * 2 + 2].setRange(node.rangeStart + len / 2, len / 2);
        node.rangeStart = 0;
      }
    }

    static class Node {
      long sum2, sum5;
      int each2, each5;
      int rangeStart;
      boolean invalid;

      void setInvalid() {
        invalid = true;
        sum2 = sum5 = each2 = each5 = rangeStart = 0;
      }

      void setRange(int start, int len) {
        rangeStart = start;
        sum2 += count2[start + len - 1] - count2[start - 1];
        sum5 += count5[start + len - 1] - count5[start - 1];
      }
    }
  }


	static class RBST<T extends Comparable<T>> {
		Random rnd = new Random();
		Node root;

		void insert(T v) {
			root = insert(root, v);
		}

		// return new root
		Node insert(Node node, T v) {
			if (node == null) return new Node(v);
			if (rnd.nextInt(node.size + 1) == 0) return insertRoot(node, v);
			if (v.compareTo(node.val) < 0) {
				node.left = insert(node.left, v);
			} else {
				node.right = insert(node.right, v);
			}
			node.fixSize();
			return node;
		}

		Node insertRoot(Node node, T v) {
			if (node == null) return new Node(v);
			if (v.compareTo(node.val) < 0) {
				node.left = insertRoot(node.left, v);
				return rotateRight(node);
			} else {
				node.right = insertRoot(node.right, v);
				return rotateLeft(node);
			}
		}

		Node rotateRight(Node node) {
			Node top = node.left;
			Node mid = node.left.right;
			top.right = node;
			node.left = mid;
			node.fixSize();
			top.fixSize();
			return top;
		}

		Node rotateLeft(Node node) {
			Node top = node.right;
			Node mid = node.right.left;
			top.left = node;
			node.right = mid;
			node.fixSize();
			top.fixSize();
			return top;
		}

		Node find(T v) {
			if (root == null) return null;
			Node cur = root;
			while (cur != null) {
				int compare = v.compareTo(cur.val);
				if (compare == 0) return cur;
				if (compare < 0) {
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			}
			return null;
		}

		int rank(T v) {
			if (root == null) return 0;
			Node cur = root;
			int ret = 0;
			while (cur != null) {
				int compare = v.compareTo(cur.val);
				if (compare == 0) {
					if (cur.left != null) ret += cur.left.size
					return ret;
				}
				if (compare < 0) {
					cur = cur.left;
				} else {
					if (cur.left != null) ret += cur.left.size;
					ret += 1;
					cur = cur.right;
				}
			}
			return ret;
		}

		void remove(T v) {
			root = remove(root, v);
		}

		Node remove(Node node, T v) {
			if (node == null) throw new RuntimeException("not found");
			int compare = v.compareTo(node.val);
			if (compare == 0) {
				return join(node.left, node.right);
			}
			if (compare < 0) {
				node.left = remove(node.left, v);
			} else {
				node.right = remove(node.right, v);
			}
			node.fixSize();
			return node;
		}

		Node join(Node left, Node right) {
			if (left == null) return right;
			if (right == null) return left;
			if (rnd.nextInt(left.size + right.size) < left.size) {
				left.right = join(left.right, right);
				left.fixSize();
				return left;
			} else {
				right.left = join(left, right.left);
				right.fixSize();
				return right;
			}
		}

		int size() {
			return root == null ? 0 : root.size;
		}

		int height() {
			return root == null ? 0 : root.height();
		}

		class Node {
			T val;
			Node left, right;
			int size;

			Node(T v) {
				this.val = v;
				this.size = 1;
			}

			int height() {
				int lh = left == null ? 0 : left.height();
				int rh = right == null ? 0 : right.height();
				return Math.max(lh, rh) + 1;
			}

			void fixSize() {
				this.size = 1 + (left == null ? 0 : left.size) + (right == null ? 0 : right.size);
			}

			void print(int level) {
				if (left != null) left.print(level + 1);
				for (int i = 0; i < level * 2; ++i) {
					System.out.print(" ");
				}
				System.out.println(val);
				if (right != null) right.print(level + 1);
			}

		}
	}
