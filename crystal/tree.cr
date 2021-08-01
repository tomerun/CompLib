class LCA
  @g : Array(Array(Int32))
  @depth : Array(Int32)
  @parent : Array(Array(Int32))

  def initialize(n, es)
    @g = Array.new(n) { [] of Int32 }
    es.each do |e|
      @g[e[0]] << e[1]
      @g[e[1]] << e[0]
    end
    @depth = Array.new(n, -1)
    @parent = Array.new(Math.log2(n).ceil.to_i) { [-1] * (n + 1) }
    q = [0]
    @depth[0] = 0
    n.times do |i|
      cur = q[i]
      @g[cur].each do |adj|
        next if @depth[adj] != -1
        q << adj
        @parent[0][adj] = cur
        @depth[adj] = @depth[cur] + 1
      end
    end
    (@parent.size - 1).times do |i|
      n.times do |j|
        @parent[i + 1][j] = @parent[i][@parent[i][j]]
      end
    end
  end

  def lca(p, q)
    if @depth[p] > @depth[q]
      p, q = q, p
    end
    diff = @depth[q] - @depth[p]
    while diff != 0
      q = @parent[diff.trailing_zeros_count][q]
      diff &= diff - 1
    end
    return p if p == q
    (@parent.size - 1).downto(0) do |i|
      if @parent[i][p] != @parent[i][q]
        p = @parent[i][p]
        q = @parent[i][q]
      end
    end
    return @parent[0][p]
  end
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
