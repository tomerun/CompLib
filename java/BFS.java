    static CountBuf bfsBuf;
    static int N = 50;
    static SplittableRandom rnd = new SplittableRandom();
    static int[] DR = {1, 0, -1, 0};
    static int[] DC = {0, 1, 0, -1};
    static char[] DIR_CHARS = "DRUL".toCharArray();
    static int[][] backDir;

    static void bfs() {
        bfsBuf.clear();
        int[][] f = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (rnd.nextDouble() < 0.3) f[i][j] = 1;
            }
        }
        int sr = rnd.nextInt(N);
        int sc = rnd.nextInt(N);
        int gr = rnd.nextInt(N);
        int gc = rnd.nextInt(N);
        f[sr][sc] = 2;
        f[gr][gc] = 3;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(f[i][j]);
            }
            System.out.println();
        }
        IntDeque q = new IntDeque(N * N / 2);
        bfsBuf.set(sr, sc);
        q.push((sr << 8) | sc);
        while (q.size() > 0) {
            int cur = q.popFront();
            int cr = cur >> 8;
            int cc = cur & 0xFF;
            for (int i = 0; i < 4; i++) {
                int nr = cr + DR[i];
                int nc = cc + DC[i];
                if (nr == -1 || nr == N || nc == -1 || nc == N) continue;
                if (f[nr][nc] == 1) continue;
                if (bfsBuf.get(nr, nc)) continue;
                backDir[nr][nc] = i;
                if (nr == gr && nc == gc) {
                    IntDeque res = new IntDeque();
                    while (nr != sr || nc != sc) {
                        int d = backDir[nr][nc];
                        res.push(d);
                        nr -= DR[d];
                        nc -= DC[d];
                    }
                    for (int j = res.tail - 1; j >= res.head; j--) {
                        System.out.print(DIR_CHARS[res.buf[j]]);
                    }
                    System.out.println();
                    return;
                }
                bfsBuf.set(nr, nc);
                q.push((nr << 8) | nc);
            }
        }
        System.out.println("fail " + q.tail);
    }

    static final class IntDeque {
        int head, tail;
        int[] buf;

        IntDeque() {
            this(16);
        }

        IntDeque(int cap) {
            buf = new int[cap];
        }

        IntDeque(int preCap, int sufCap) {
            buf = new int[preCap + sufCap];
            head = tail = preCap;
        }

        void push(int v) {
            if (tail == buf.length) {
                int[] nbuf = new int[buf.length * 2];
                System.arraycopy(buf, head, nbuf, head, tail - head);
                buf = nbuf;
            }
            buf[tail++] = v;
        }

        void pushFront(int v) {
            if (head == 0) {
                int[] nbuf = new int[buf.length * 2];
                System.arraycopy(buf, head, nbuf, buf.length, tail - head);
                buf = nbuf;
            }
            buf[--head] = v;
        }

        int popFront() {
            return buf[head++];
        }

        int popBack() {
            return buf[--tail];
        }

        int[] extract() {
            return Arrays.copyOfRange(buf, head, tail);
        }

        int size() {
            return tail - head;
        }

        void clear() {
            tail = head;
        }
    }
