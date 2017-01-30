import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/* min heap */
public class FibHeap<T extends Comparable<T>> {

	T MIN_INFINITY;
	Node<T> min;
	int size;

	void add(Node<T> nn) {
		if (this.min == null) {
			this.min = nn;
			nn.right = nn;
			nn.left = nn;
		} else {
			Node<T> l = this.min;
			Node<T> r = this.min.right;
			l.right = nn;
			nn.left = l;
			r.left = nn;
			nn.right = r;
			if (this.min.v.compareTo(nn.v) > 0) this.min = nn;
		}
		nn.degree = 0;
		nn.parent = null;
		nn.mark = false;
		this.size++;
	}

	static <T extends Comparable<T>> FibHeap<T> union(FibHeap<T> h1, FibHeap<T> h2) {
		if (h1.size == 0) return h2;
		if (h2.size == 0) return h1;
		FibHeap<T> ret = new FibHeap<>();
		ret.min = h1.min.v.compareTo(h2.min.v) < 0 ? h1.min : h2.min;
		ret.size = h1.size + h2.size;
		connect(h1.min, h2.min);
		return ret;
	}

	Node<T> pop() {
		if (this.min == null) return null;
		Node<T> ret = this.min;
		Node<T> child = this.min.child;
		if (child != null) {
			do {
				child.parent = null;
				child = child.right;
			} while (child != this.min.child);
			connect(this.min, child);
		}
		if (this.min.right == this.min) {
			this.min = null;
		} else {
			remove(this.min);
			this.min = this.min.right;
			consolidate();
		}
		this.size--;
		return ret;
	}

	private void consolidate() {
		ArrayList<Node<T>> list = new ArrayList<>();
		list.add(null);
		for (int i = 1; i <= this.size; i *= 2) {
			list.add(null);
		}
		ArrayList<Node<T>> rootList = new ArrayList<>();
		Node<T> rootListElem = this.min;
		do {
			rootList.add(rootListElem);
			rootListElem = rootListElem.right;
		} while (rootListElem != this.min);

		for (Node<T> w : rootList) {
			Node<T> x = w;
			int d = x.degree;
			while (list.get(d) != null) {
				Node<T> y = list.get(d);
				if (x.v.compareTo(y.v) > 0) {
					Node<T> tmp = x;
					x = y;
					y = tmp;
				}
				remove(y);
				y.right = y.left = y;
				if (x.child == null) {
					x.child = y;
				} else {
					connect(x.child, y);
				}
				x.degree++;
				y.parent = x;
				y.mark = false;
				list.set(d, null);
				++d;
			}
			list.set(d, x);
		}
		this.min = null;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) == null) continue;
			if (this.min == null || this.min.v.compareTo(list.get(i).v) > 0) this.min = list.get(i);
		}
	}

	void decreaseKey(Node<T> x, T newVal) {
		if (x.v.compareTo(newVal) < 0) {
			throw new RuntimeException("new value is greater than current value");
		}
		x.v = newVal;
		Node<T> y = x.parent;
		if (y != null && x.v.compareTo(y.v) < 0) {
			cut(x);
			cascadingCut(y);
		}
		if (x.v.compareTo(this.min.v) < 0) {
			this.min = x;
		}
	}

	private void cut(Node<T> x) {
		remove(x);
		insert(this.min, x);
		x.parent = null;
		x.mark = false;
	}

	private void cascadingCut(Node<T> y) {
		Node<T> z = y.parent;
		if (z == null) return;
		if (y.mark) {
			cut(y);
			cascadingCut(z);
		} else {
			y.mark = true;
		}
	}

	void delete(Node<T> x) {
		decreaseKey(x, MIN_INFINITY);
		pop();
	}

	private static <T extends Comparable<T>> void connect(Node<T> n1, Node<T> n2) {
		Node<T> l1 = n1;
		Node<T> r1 = l1.right;
		Node<T> l2 = n2;
		Node<T> r2 = l2.right;
		l1.right = r2;
		r2.left = l1;
		r1.left = l2;
		l2.right = r1;
	}

	private static <T extends Comparable<T>> void remove(Node<T> n) {
		Node<T> p = n.parent;
		if (p != null) {
			if (p.child == n) {
				p.child = n.right == n ? null : n.right;
			}
			p.degree--;
		}
		n.right.left = n.left;
		n.left.right = n.right;
	}

	private static <T extends Comparable<T>> void insert(Node<T> pos, Node<T> add) {
		Node<T> next = pos.right;
		pos.right = add;
		add.left = pos;
		add.right = next;
		next.left = add;
	}

	void print() {
		if (this.min == null) {
			System.out.println("empty heap");
			return;
		}
		System.out.println("heap:");
		Node<T> c = this.min;
		do {
			c.print(1);
			c = c.right;
		} while (c != this.min);
	}

	public static void main(String[] args) {
		FibHeap<Integer> heap = new FibHeap<>();
		heap.MIN_INFINITY = Integer.MIN_VALUE;
		Random rnd = new Random(42);
		final int SIZE = 100;
		ArrayList<Node<Integer>> v = new ArrayList<>();
		for (int i = 0; i < SIZE; ++i) {
			v.add(new Node<Integer>(rnd.nextInt(10000)));
		}
		for (int i = 0; i < SIZE; ++i) {
			heap.add(v.get(i));
		}
		Collections.shuffle(v);
		heap.print();
		for (int i = 0; i < SIZE; ++i) {
			System.out.println("delete:" + v.get(i).v);
			heap.delete(v.get(i));
			heap.print();
		}
	}

	static class Node<T extends Comparable<T>> {
		Node<T> parent, child, left, right;
		int degree;
		boolean mark;
		T v;

		Node(T v) {
			this.v = v;
		}

		void print(int level) {
			for (int i = 0; i < level; ++i) {
				System.out.print("  ");
			}
			System.out.println(this.v + " " + (this.mark ? "T" : "F"));
			if (this.child == null) return;
			Node<T> c = this.child;
			do {
				c.print(level + 1);
				c = c.right;
			} while (c != this.child);
		}
	}

}
