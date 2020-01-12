class MaxFlow
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
