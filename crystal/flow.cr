# adjacent list
class Dinic
  class Edge
    property :to, :cap, :rev

    def initialize(@to : Int32, @cap : Int32, @rev : Int32)
    end
  end

  getter :g
  @g : Array(Array(Edge))
  @level : Array(Int32)
  @next : Array(Int32)

  def initialize(size : Int32)
    @g = Array.new(size) { Array(Edge).new }
    @level = Array.new(size, -1)
    @next = Array.new(size, 0)
  end

  def add_edge(s : Int32, t : Int32, cap : Int32)
    @g[s] << Edge.new(t, cap, @g[t].size)
    @g[t] << Edge.new(s, 0, @g[s].size - 1)
  end

  private def bfs(s)
    @level.fill(-1)
    @level[s] = 0
    que = [s]
    @g.size.times do |i|
      break if i == que.size
      cur = que[i]
      @g[cur].each do |edge|
        if edge.cap > 0 && @level[edge.to] == -1
          @level[edge.to] = @level[cur] + 1
          que << edge.to
        end
      end
    end
  end

  private def dfs(pos : Int32, t : Int32, f : Int32)
    return f if pos == t
    while @next[pos] < @g[pos].size
      e = @g[pos][@next[pos]]
      if e.cap > 0 && @level[pos] < @level[e.to]
        v = dfs(e.to, t, {e.cap, f}.min)
        if v > 0
          e.cap -= v
          @g[e.to][e.rev].cap += v
          return v
        end
      end
      @next[pos] += 1
    end
    return 0
  end

  def calc(s : Int32, t : Int32) : Int32
    result = 0
    flow = 0
    while true
      bfs(s)
      if @level[t] == -1
        return flow
      end
      @next.fill(0)
      while true
        f = dfs(s, t, Int32::MAX)
        break if f <= 0
        flow += f
      end
    end
  end
end

# Hash dist table
class FordFulkerson
  @g : Array(Hash(Int32, Int32))

  def initialize(size : Int32)
    @g = Array.new(size) { Hash(Int32, Int32).new(0) }
  end

  def add_edge(s : Int32, t : Int32, cap : Int32)
    @g[s][t] = cap
  end

  def calc
    # assume node 0 is source, node size - 1 is sink
    n = @g.size
    result = 0
    prev = Array.new(n, 0)
    visited = Array.new(n, false)
    que = [] of Int32
    while true
      visited.fill(false)
      que.clear
      que << 0
      visited[0] = true
      found = false
      n.times do |i|
        break if i == que.size
        cur = que[i]
        map = @g[cur]
        n.times do |j|
          if !visited[j] && map[j] > 0
            prev[j] = cur
            visited[j] = true
            if j == n - 1
              found = true
              break
            end
            que << j
          end
        end
        break if found
      end
      break if !found
      pos = n - 1
      min_cap = 1 << 30
      while pos != 0
        p = prev[pos]
        min_cap = {min_cap, @g[p][pos]}.min
        pos = p
      end
      pos = n - 1
      while pos != 0
        p = prev[pos]
        @g[p][pos] -= min_cap
        @g[pos][p] += min_cap
        pos = p
      end
      result += min_cap
    end
    return result
  end
end

# adjacent list
class EdmondsCarp
  class Edge
    property :to, :cap, :rev

    def initialize(@to : Int32, @cap : Int32, @rev : Int32)
    end
  end

  getter :g
  @g : Array(Array(Edge))

  def initialize(size : Int32)
    @g = Array.new(size) { Array(Edge).new }
  end

  def add_edge(s : Int32, t : Int32, cap : Int32)
    @g[s] << Edge.new(t, cap, @g[t].size)
    @g[t] << Edge.new(s, 0, @g[s].size - 1)
  end

  def calc
    # assume node 0 is source, node size - 1 is sink
    n = @g.size
    result = 0
    path = Array(Edge?).new(n, nil)
    min_cap = Array.new(n, Int32::MAX)
    que = [] of Int32
    while true
      que.clear
      que << 0
      found = false
      n.times do |i|
        break if i == que.size
        cur = que[i]
        @g[cur].each do |edge|
          if !path[edge.to] && edge.cap > 0
            path[edge.to] = edge
            min_cap[edge.to] = {min_cap[cur], edge.cap}.min
            if edge.to == n - 1
              found = true
              break
            end
            que << edge.to
          end
        end
        break if found
      end
      break if !found
      pos = n - 1
      while pos != 0
        edge = path[pos].not_nil!
        rev = @g[edge.to][edge.rev]
        edge.cap -= min_cap[-1]
        rev.cap += min_cap[-1]
        pos = rev.to
      end
      result += min_cap[-1]
      que.each do |i|
        path[i] = nil
      end
      path[-1] = nil
    end
    return result
  end
end

class PrimalDual
  class Edge
    property :to, :cap, :cost, :rev

    def initialize(@to : Int32, @cap : Int32, @cost : Int64, @rev : Int32)
    end
  end

  getter :g
  @g : Array(Array(Edge))

  def initialize(size : Int32)
    @g = Array.new(size) { Array(Edge).new }
  end

  def add_edge(s : Int32, t : Int32, cost : Int64, cap : Int32)
    @g[s] << Edge.new(t, cap, cost, @g[t].size)
    @g[t] << Edge.new(s, 0, -cost, @g[s].size - 1)
  end

  def calc(flow : Int32, s : Int32, t : Int32)
    n = @g.size
    result = 0i64
    h = Array.new(n, 0i64)
    prev = Array(Edge?).new(n, nil)
    que = PriorityQueue(Tuple(Int64, Int32)).new(n)
    dist = Array.new(n, 0i64)
    while flow > 0
      dist.fill(Int64::MAX // 2)
      dist[s] = 0
      que.add({0i64, s})
      while que.size > 0
        cur = que.pop
        next if -cur[0] > dist[cur[1]]
        @g[cur[1]].each do |edge|
          next if edge.cap == 0
          n_cost = dist[cur[1]] + edge.cost + h[cur[1]] - h[edge.to]
          if n_cost < dist[edge.to]
            dist[edge.to] = n_cost
            que.add({-n_cost, edge.to})
            prev[edge.to] = edge
          end
        end
      end
      n.times do |i|
        h[i] += dist[i]
      end
      break if h[t] >= 0
      f = flow
      pos = t
      while pos != s
        pe = prev[pos].not_nil!
        f = {f, pe.cap}.min
        pos = @g[pos][pe.rev].to
      end
      pos = t
      while pos != s
        pe = prev[pos].not_nil!
        pe.cap -= f
        @g[pos][pe.rev].cap += f
        pos = @g[pos][pe.rev].to
      end
      result += h[t] * f
      flow -= f
    end
    return result
  end
end
