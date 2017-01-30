#include <vector>
#include <set>
#include <map>
#include <bitset>
#include <algorithm>
#include <iostream>
#include <cmath>
using namespace std;

typedef vector<int> vi;
typedef vi::iterator vii;

// bitset[i] == false when i is prime
template<int N>
bitset<N+1> primesSet(){
  bitset<N+1> bs;
  for(int i = 2; i <= N; ++i){
    if(bs[i]) continue;
    for(int j = i*2; j <= N; j += i){
      bs[j] = true;
    }
  }
  bs[1] = true;
  return bs;
}

template<int N>
vi primes(){
  bitset<N+1> bs;
  vi ret;
  for(int i = 2; i <= N; ++i){
    if(bs[i]) continue;
    ret.push_back(i);
    for(int j = i*2; j <= N; j += i){
      bs[j] = true;
    }
  }
  return ret;
}

