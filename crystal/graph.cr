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
