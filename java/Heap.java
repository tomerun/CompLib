	/* min heap */
	static class SkewHeap<T extends Comparable<T>> {

		Node<T> root;

		void add(T v) {
			Node<T> nn = new Node<T>(v);
			this.root = meld(this.root, nn);
		}

		T pop() {
			T ret = this.root.v;
			Node<T> l = this.root.left;
			Node<T> r = this.root.right;
			this.root = meld(l, r);
			return ret;
		}

		void meld(SkewHeap<T> o) {
			this.root = meld(this.root, o.root);
		}

		static <T extends Comparable<T>> Node<T> meld(Node<T> l, Node<T> r) {
			if (l == null) return r;
			if (r == null) return l;
			if (l.v.compareTo(r.v) > 0) {
				Node<T> tmp = l;
				l = r;
				r = tmp;
			}
			Node<T> nl = meld(l.right, r);
			l.right = l.left;
			l.left = nl;
			return l;
		}

		void print() {
			if (this.root == null) {
				System.out.println("empty heap");
				return;
			}
			System.out.println("heap:");
			this.root.print(0);
		}

		static class Node<T extends Comparable<T>> {
			Node<T> left, right;
			T v;

			Node(T v) {
				this.v = v;
			}

			void print(int level) {
				if (this.left != null) this.left.print(level + 1);
				for (int i = 0; i < level; ++i) {
					System.out.print("  ");
				}
				System.out.println(this.v);
				if (this.right != null) this.right.print(level + 1);
			}
		}

	}


	static class RadixHeap {
		private ArrayList<ArrayList<Long>> buf = new ArrayList<>();
		private long last = 0;
		private int size = 0;

		RadixHeap() {
			for (int i = 0; i <= 64; ++i) {
				buf.add(new ArrayList<Long>());
			}
		}

		void push(long v) {
			assert (last <= v);
			++size;
			buf.get(bsr(v ^ last)).add(v);
		}

		long pop() {
			if (buf.get(0).isEmpty()) {
				int pos = 1;
				while (buf.get(pos).isEmpty()) {
					++pos;
				}
				long nextLast = Long.MAX_VALUE;
				for (int i = 0; i < buf.get(pos).size(); ++i) {
					nextLast = Math.min(nextLast, buf.get(pos).get(i));
				}
				for (long move : buf.get(pos)) {
					buf.get(bsr(move ^ nextLast)).add(move);
				}
				last = nextLast;
				buf.get(pos).clear();
			}
			--size;
			buf.get(0).remove(buf.get(0).size() - 1);
			return last;
		}

		int size() {
			return this.size;
		}

		static int bsr(long v) {
			return 64 - Long.numberOfLeadingZeros(v);
		}
	}

