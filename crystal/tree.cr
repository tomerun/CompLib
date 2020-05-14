def parents_doubling(parent : Array(Int32))
  n = parent.size
  parents = Array(Array(Int32)).new
  parents << parent + [-1]
  parents_size = 1
  while parents_size < n
    pn = Array.new(n + 1, -1)
    prev = parents[-1]
    n.times do |i|
      pn[i] = prev[prev[i]]
    end
    parents << pn
    parents_size *= 2
  end
  return parents
end

def lca(parents : Array(Array(Int32)), u, v, du, dv)
  if du > dv
    u, v = v, u
    du, dv = dv, du
  end
  if du != dv
    diff = dv - du
    parents.size.times do |i|
      break if (1 >> i) > diff * 2
      if (diff & (1 << i)) != 0
        v = parents[i][v]
      end
    end
  end
  return u if u == v
  (parents.size - 1).downto(0) do |i|
    if parents[i][u] != parents[i][v]
      u = parents[i][u]
      v = parents[i][v]
    end
  end
  return parents[0][u]
end

class EulerTour
  getter :tour, :down, :up, :parent, :depth

  def initialize(graph)
    n = graph.size
    @tour = Array(Int32).new(2 * n, -1)
    @down = Array(Int32).new(n, -1)
    @up = Array(Int32).new(n, -1)
    @parent = Array(Int32).new(n, -1)
    @depth = Array(Int32).new(n, -1)
    dfs(graph, 0, 0, 0)
  end

  def dfs(graph, cur, pos, depth)
    @down[cur] = pos
    @tour[pos] = cur
    @depth[cur] = depth
    pos += 1
    graph[cur].each do |adj|
      next if @parent[cur] == adj
      @parent[adj] = cur
      pos = dfs(graph, adj, pos, depth + 1)
      @tour[pos] = cur
      pos += 1
    end
    @up[cur] = pos
    return pos
  end

  def to_s(io)
    io << @tour.to_s << "\n" << @down.to_s << "\n" << @up.to_s
  end
end
