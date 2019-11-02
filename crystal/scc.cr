stack = [] of Int32
visited = Array.new(n, false)
scc_dfs(g, stack, visited, 0)
visited.fill(false)
groups = [] of Array(Int32)
stack.reverse.each do |i|
  next if visited[i]
  group = [] of Int32
  scc_dfs_rev(g_rev, group, visited, i)
  groups << group
end

def scc_dfs(g, stack, visited, cur)
  return if visited[cur]
  visited[cur] = true
  g[cur].each do |c|
    scc_dfs(g, stack, visited, c)
  end
  stack << cur
end

def scc_dfs_rev(g_rev, list, visited, cur)
  return if visited[cur]
  visited[cur] = true
  g_rev[cur].each do |c|
    scc_dfs_rev(g_rev, list, visited, c)
  end
  list << cur
end
