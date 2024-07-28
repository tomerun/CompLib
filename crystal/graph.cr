def lowlink(g : Array(Array(Int32))) : Tuple(Set(Int32), Array(Tuple(Int32, Int32)))
  ord = Array.new(g.size, -1)
  low = Array.new(g.size, g.size)
  cnt_child = Array.new(g.size, 0)
  articulations = Set(Int32).new
  bridges = [] of Tuple(Int32, Int32)
  dfs = uninitialized Int32, Int32, Int32 -> Int32
  dfs = ->(cur : Int32, idx : Int32, prev : Int32) do
    ord[cur] = idx
    low[cur] = idx
    idx += 1
    g[cur].each do |adj|
      next if adj == prev
      if ord[adj] == -1
        idx = dfs.call(adj, idx, cur)
        cnt_child[cur] += 1
        low[cur] = {low[cur], low[adj]}.min
        if ord[cur] < low[adj]
          bridges << {cur, adj}
        end
        if cur != 0 && ord[cur] <= low[adj]
          articulations << cur
        end
      else
        low[cur] = {low[cur], ord[adj]}.min
      end
    end
    return idx
  end
  dfs.call(0, 0, -1)
  if cnt_child[0] > 1
    articulations << 0
  end
  return articulations, bridges
end

def minimum_steiner_tree(g, terminal)
  n = g.size
  k = terminal.size
  # dp[S][v] := vとterminalの部分集合Sを連結ににする最小コスト
  dp = Array.new(1 << k) { Array.new(n, INF) }
  k.times do |i|
    dp[1 << i][terminal[i]] = 0
  end
  1.upto((1 << k) - 1) do |i|
    n.times do |v|
      s = i
      while s != 0
        s = (s - 1) & i
        dp[i][v] = {dp[i][v], dp[s][v] + dp[i - s][v]}.min
      end
    end
    q = PriorityQueue(Tuple(Int64, Int32)).new(n)
    dist = Array.new(n, INF)
    n.times do |v|
      dist[v] = dp[i][v]
      q.add({-dist[v], v})
    end
    ord = [] of Int32
    while q.size > 0
      cd, cv = q.pop
      cd *= -1
      next if dist[cv] < cd
      ord << cv
      g[cv].each do |nv, cost|
        nd = cd + cost
        if nd < dist[nv]
          dist[nv] = nd
          q.add({-nd, nv})
        end
      end
    end
    ord.each do |v|
      g[v].each do |e, c|
        dp[i][v] = {dp[i][v], dp[i][e] + c}.min
      end
    end
  end
  return dp
end
