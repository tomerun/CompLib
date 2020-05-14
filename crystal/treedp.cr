n = read_line.to_i
g = Array.new(n) { [] of Int32 }
(n - 1).times do
  a, b = read_line.split.map(&.to_i)
  g[a - 1] << b - 1
  g[b - 1] << a - 1
end
q = [0]
parent = Array.new(n, -1)
n.times do |i|
  cur = q[i]
  g[cur].each do |adj|
    next if parent[cur] == adj
    parent[adj] = cur
    q << adj
  end
end

# bottom -> top
(n - 1).downto(0) do |i|
  cur = q[i]
  par = parent[cur]
  g[cur].each do |adj|
    next if par == adj
    # TODO
  end
end

# top -> bottom
n.times do |i|
  cur = q[i]
  adjs = g[cur]
  right = Array.new(adjs.size + 1, 1i64)
  par = parent[cur]
  (adjs.size - 1).downto(1) do |j|
    adj = adjs[j]
    if par == adj
      right[j] = right[j + 1]
      next
    end
    # TODO right[j] = ...
  end
  adjs.size.times do |j|
    adj = adjs[j]
    next if par == adj
    # TODO
  end
end
