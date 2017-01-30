	static class Dice {
		int[] val = new int[6]; // top, north, east, south, west, bottom

		Dice(int top, int north, int east) {
			val[0] = top;
			val[1] = north;
			val[2] = east;
			val[3] = 7 - north;
			val[4] = 7 - east;
			val[5] = 7 - top;
		}

		Dice(int top, int north) {
			this(1, 3, 2);
			if (top == bottom()) {
				rollE();
				rollE();
			} else if (top == north()) {
				rollS();
			} else if (top == east()) {
				rollW();
			} else if (top == south()) {
				rollN();
			} else if (top == west()) {
				rollE();
			}
			while (north != north()) {
				rollClockwise();
			}
		}

		Dice(int[] val) {
			if (val[0] + val[5] != 7 || val[1] + val[3] != 7 || val[2] + val[4] != 7) throw new RuntimeException();
			for (int i = 0; i < 6; ++i) {
				this.val[i] = val[i];
			}
		}

		void rollN() {
			int tmp = north();
			this.val[1] = this.val[0];
			this.val[0] = this.val[3];
			this.val[3] = this.val[5];
			this.val[5] = tmp;
		}

		void rollS() {
			int tmp = south();
			this.val[3] = this.val[0];
			this.val[0] = this.val[1];
			this.val[1] = this.val[5];
			this.val[5] = tmp;
		}

		void rollE() {
			int tmp = east();
			this.val[2] = this.val[0];
			this.val[0] = this.val[4];
			this.val[4] = this.val[5];
			this.val[5] = tmp;
		}

		void rollW() {
			int tmp = west();
			this.val[4] = this.val[0];
			this.val[0] = this.val[2];
			this.val[2] = this.val[5];
			this.val[5] = tmp;
		}

		void rollClockwise() {
			int tmp = south();
			this.val[3] = this.val[2];
			this.val[2] = this.val[1];
			this.val[1] = this.val[4];
			this.val[4] = tmp;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Dice)) return false;
			return Arrays.equals(this.val, ((Dice) o).val);
		}

		public int hashCode() {
			return Arrays.hashCode(this.val);
		}

		public Dice clone() {
			return new Dice(this.val);
		}

		int top() {
			return this.val[0];
		}

		int bottom() {
			return this.val[5];
		}

		int north() {
			return this.val[1];
		}

		int south() {
			return this.val[3];
		}

		int east() {
			return this.val[2];
		}

		int west() {
			return this.val[4];
		}

		public String toString() {
			String ret = "   " + top() + "   \n";
			ret += north() + " " + east() + " " + south() + " " + west() + "\n";
			ret += "   " + bottom() + "   ";
			return ret;
		}
	}
