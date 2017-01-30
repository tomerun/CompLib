
	static class Segment {
		int x1, y1, x2, y2;

		public Segment(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	static boolean cross(Segment s1, Segment s2) {
		long v1 = (s1.x1 - s1.x2) * (s2.y1 - s1.y1) + (s1.y1 - s1.y2) * (s1.x1 - s2.x1);
		long v2 = (s1.x1 - s1.x2) * (s2.y2 - s1.y1) + (s1.y1 - s1.y2) * (s1.x1 - s2.x2);
		long v3 = (s2.x1 - s2.x2) * (s1.y1 - s2.y1) + (s2.y1 - s2.y2) * (s2.x1 - s1.x1);
		long v4 = (s2.x1 - s2.x2) * (s1.y2 - s2.y1) + (s2.y1 - s2.y2) * (s2.x1 - s1.x2);
		return v1 * v2 < 0 && v3 * v4 < 0;
	}

	static boolean cross(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		long v1 = (x1 - x2) * (y3 - y1) + (y1 - y2) * (x1 - x3);
		long v2 = (x1 - x2) * (y4 - y1) + (y1 - y2) * (x1 - x4);
		long v3 = (x3 - x4) * (y1 - y3) + (y3 - y4) * (x3 - x1);
		long v4 = (x3 - x4) * (y2 - y3) + (y3 - y4) * (x3 - x2);
		// on the line?
		if (v3 == 0 && v4 == 0) {
			return Math.min(x1, x2) <= x3 && x3 <= Math.max(x1, x2) && Math.min(y1, y2) <= y3 && y3 <= Math.max(y1, y2);
		}
		return v1 * v2 <= 0 && v3 * v4 <= 0;
	}

	// caliper method
	static long farestPair(ArrayList<Point> convex) {
		if (convex.size() == 1) return 0;
		if (convex.size() == 2) {
			f1 = convex.get(0).idx;
			f2 = convex.get(1).idx;
			return dist2(convex.get(0), convex.get(1));
		}
		int i = 0;
		int j = 0;
		for (int k = 0; k < convex.size(); k++) {
			if (convex.get(k).x < convex.get(i).x) i = k;
			if (convex.get(k).x > convex.get(j).x) j = k;
		}
		long ret = 0;
		int si = i;
		int sj = j;
		int count = 0;
		while (i != sj || j != si) {
			if (count > 2 * convex.size()) break; // TODO:??
			++count;
			long d = dist2(convex.get(i), convex.get(j));
			if (ret < d) {
				ret = d;
				f1 = convex.get(i).idx;
				f2 = convex.get(j).idx;
			}
			if (ccw(convex.get(i), convex.get((i + 1) % convex.size()), convex.get(j), convex.get((j + 1) % convex.size())) < 0) {
				i = (i + 1) % convex.size();
			} else {
				j = (j + 1) % convex.size();
			}
		}
		return ret;
	}

	static ArrayList<Point> convexHull(Point[] ps) {
		Arrays.sort(ps);
		ArrayList<Point> ret = new ArrayList<>();
		for (int i = 0; i < ps.length; i++) {
			while (ret.size() > 1 && ccw(ret.get(ret.size() - 2), ret.get(ret.size() - 1), ps[i]) <= 0) {
				ret.remove(ret.size() - 1);
			}
			ret.add(ps[i]);
		}
		final int lowerSize = ret.size();
		for (int i = ps.length - 2; i >= 0; i--) {
			while (ret.size() > lowerSize && ccw(ret.get(ret.size() - 2), ret.get(ret.size() - 1), ps[i]) <= 0) {
				ret.remove(ret.size() - 1);
			}
			ret.add(ps[i]);
		}
		return ret;
	}

	static long dist2(Point p1, Point p2) {
		return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
	}

	// returns positive value if p1 -> p2 -> p3 is counterclockwise
	static long ccw(Point p1, Point p2, Point p3) {
		long dx1 = p2.x - p1.x;
		long dy1 = p2.y - p1.y;
		long dx2 = p3.x - p2.x;
		long dy2 = p3.y - p2.y;
		return dx1 * dy2 - dx2 * dy1;
	}

	// returns positive value if p1 -> p2 with p3 -> p4 is counterclockwise
	static long ccw(Point p1, Point p2, Point p3, Point p4) {
		long dx1 = p2.x - p1.x;
		long dy1 = p2.y - p1.y;
		long dx2 = p4.x - p3.x;
		long dy2 = p4.y - p3.y;
		return dx1 * dy2 - dx2 * dy1;
	}
	static class Point implements Comparable<Point> {
		int idx;
		long x, y;

		public Point(int idx, long x, long y) {
			this.idx = idx;
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point o) {
			if (this.x != o.x) return Long.compare(this.x, o.x);
			return Long.compare(this.y, o.y);
		}
	}

