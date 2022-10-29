#include <vector>
#include <iostream>

using namespace std;

struct IndexSet {

  vector<int> que;
  vector<int> pos;

  IndexSet(int n) : pos(n, -1) {}

  void add(int v) {
    assert(pos[v] == -1);
    pos[v] = que.size();
    que.push_back(v);
  }

  void remove(int v) {
    assert(pos[v] != -1);
    int p = pos[v];
    int b = que.back();
    que[p] = b;
    que.pop_back();
    pos[b] = p;
    pos[v] = -1;
  }

  int pop_random(XorShift& rnd) {
    assert(size() > 0);
    int i = rnd.nextUInt(que.size());
    int ret = que[i];
    remove(ret);
    return ret;
  }

  void clear() {
    for (int v : que) {
      pos[v] = -1;
    }
    que.clear();
  }

  bool exists(int v) const {
    return pos[v] != -1;
  }

  int size() const {
    return que.size();
  }
};


int main() {
  auto is = IndexSet(10);
  is.add(1);
  is.add(2);
  is.add(3);
  is.add(4);
  for (int i = 1; i <= 5; ++i) {
    cout << i << ":" << is.exists(i) << endl;
  }
  is.remove(4);
  for (int i = 1; i <= 5; ++i) {
    cout << i << ":" << is.exists(i) << endl;
  }
  is.remove(3);
  for (int i = 1; i <= 5; ++i) {
    cout << i << ":" << is.exists(i) << endl;
  }
  is.remove(2);
  for (int i = 1; i <= 5; ++i) {
    cout << i << ":" << is.exists(i) << endl;
  }
  is.remove(1);
  for (int i = 1; i <= 5; ++i) {
    cout << i << ":" << is.exists(i) << endl;
  }
}