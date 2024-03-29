#include <vector>
#include <map>
#include <set>
#include <list>
#include <iostream>
#include <sstream>
#include <fstream>
#include <algorithm>
#include <numeric>
#include <utility>
#include <iterator>
#include <cstdio>
#include <cmath>
#include <cstring>
//#include "myutil.hpp"

using namespace std;
typedef long long ll;
typedef vector<int> vi;
const int INF = 1000000000;
int n, k;

class BiMatching{

  vector<vi> left_;
  vector<vi> right_;
  vi l2r;
  vi r2l;

  bool dfs(int cur, vector<bool>& visited){
    if(r2l[cur] == -1){
      return true;
    }
    visited[cur] = true;
    int l = r2l[cur];
    for(size_t i = 0; i < left_[l].size(); ++i){
      int r = left_[l][i];
      if(visited[r]) continue;
      if(dfs(r, visited)){
      r2l[r] = l;
      l2r[l] = r;
      visited[cur] = false;
      return true;
      }
    }
    visited[cur] = false;
    return false;
  }

  void dfs_cover(int cur, vector<bool>& visited_l, vector<bool>& visited_r) {
    if (visited_l[cur]) return;
    visited_l[cur] = true;
    for (int adj : left_[cur]) {
      if (adj == l2r[cur]) continue;
      if (visited_r[adj]) continue;
      visited_r[adj] = true;
      if (r2l[adj] != -1) {
        dfs_cover(r2l[adj], visited_l, visited_r);
      }
    }
  }

public:
  BiMatching(int n, int m) : left_(n), right_(m), l2r(n, -1), r2l(m, -1){ }

  void add_edge(int l, int r){
    left_[l].push_back(r);
    right_[r].push_back(l);
  }

  int matching(){
    int res = 0;
    vector<bool> visited(right_.size());
    for(size_t i = 0; i < left_.size(); ++i){
      if(l2r[i] != -1) continue;
      for(size_t j = 0; j < left_[i].size(); ++j){
        if(dfs(left_[i][j], visited)){
          l2r[i] = left_[i][j];
          r2l[left_[i][j]] = i;
          ++res;
          break;
        }
      }
    }
    return res;
  }

  pair<vector<bool>, vector<bool>> point_cover() {
    vector<bool> visited_l(left_.size(), false);
    vector<bool> visited_r(right_.size(), false);
    for (int i = 0; i < left_.size(); ++i) {
      if (l2r[i] == -1) {
        dfs_cover(i, visited_l, visited_r);
      }
    }
    for (int i = 0; i < left_.size(); ++i) {
      visited_l[i] = !visited_l[i];
    }
    return make_pair(visited_l, visited_r);
  }
};

int main(){
  cin >> n >> k;
  BiMatching bm(n,n);
  for(int i = 0; i < k; ++i){
    int r,c;
    scanf("%d%d", &r, &c);
    bm.add_edge(r-1, c-1);
  }
  cout << bm.matching() << endl;
}
