	static class Treap<T extends Comparable<T>> {

		Random rand = new Random();
		Node root = null;

		void add(T v) {
			Node cur = new Node(v, null);
			if (this.root == null) {
				this.root = cur;
				return;
			}
			Node parent = null;
			Node ref = this.root;
			while (ref != null) {
				int comp = v.compareTo(ref.val);
				if (comp == 0) {
					return;
				}
				parent = ref;
				if (comp < 0) {
					ref = ref.left;
					if (ref == null) {
						cur.parent = parent;
						parent.left = cur;
						break;
					}
				} else {
					ref = ref.right;
					if (ref == null) {
						cur.parent = parent;
						parent.right = cur;
						break;
					}
				}
			}
			while (cur.parent != null && cur.priority > cur.parent.priority) {
				Node p = cur.parent;
				Node pp = p.parent;
				if (p.left == cur) { // clockwise
					p.left = cur.right;
					if (cur.right != null) {
						cur.right.parent = p;
					}
					cur.right = p;
				} else { // anticlockwise
					p.right = cur.left;
					if (cur.left != null) {
						cur.left.parent = p;
					}
					cur.left = p;
				}
				if (pp != null) {
					if (pp.left == p) {
						pp.left = cur;
					} else {
						pp.right = cur;
					}
				}
				p.parent = cur;
				cur.parent = pp;
				if (cur.parent == null) {
					this.root = cur;
				}
			}
		}

		void print() {
			this.root.print(0);
		}

		class Node {
			T val;
			double priority;
			Node parent, left, right;
			int total = 0;

			Node(T val, Node parent) {
				this.val = val;
				this.parent = parent;
				this.priority = rand.nextDouble();
			}

			void print(int depth) {
				if (this.left != null) {
					this.left.print(depth + 1);
				} else {
					for (int i = 0; i < depth + 1; ++i) {
						System.out.print("  ");
					}
					System.out.println("(null)");
				}
				for (int i = 0; i < depth; ++i) {
					System.out.print("  ");
				}
				System.out.println(val + " " + priority);
				if (this.right != null) {
					this.right.print(depth + 1);
				} else {
					for (int i = 0; i < depth + 1; ++i) {
						System.out.print("  ");
					}
					System.out.println("(null)");
				}
			}
		}
	}
