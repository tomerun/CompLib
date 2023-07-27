class SCC
  getter :groups # Array(Array(Int32)) トポロジカルソートされた強連結成分のリスト

  def initialize(g : Array(Array(Int32)))
    stack = [] of Int32
    visited = Array.new(g.size, false)
    g.size.times do |i|
      dfs(g, stack, visited, i)
    end
    visited.fill(false)
    g_rev = Array.new(g.size) { [] of Int32 }
    g.size.times do |v|
      g[v].each do |u|
        g_rev[u] << v
      end
    end
    @groups = [] of Array(Int32)
    stack.reverse.each do |i|
      next if visited[i]
      group = [] of Int32
      dfs_rev(g_rev, group, visited, i)
      groups << group
    end
  end

  def dfs(g, stack, visited, cur)
    return if visited[cur]
    visited[cur] = true
    g[cur].each do |c|
      dfs(g, stack, visited, c)
    end
    stack << cur
  end

  def dfs_rev(g_rev, group, visited, cur)
    return if visited[cur]
    visited[cur] = true
    g_rev[cur].each do |c|
      dfs_rev(g_rev, group, visited, c)
    end
    group << cur
  end
end
