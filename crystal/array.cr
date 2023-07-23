def maximal_rectangles(height : Array(Int32), &block : Int32, Int32, Int32 ->)
  stack = [{-1, 0}]
  height.size.times do |i|
    h = height[i]
    while !stack.empty? && stack[-1][1] >= h
      _, ch = stack.pop
      if !stack.empty?
        yield stack[-1][0] + 1, i - 1, ch
      end
    end
    stack << {i, h}
  end
  while stack.size > 1
    yield stack[-2][0] + 1, height.size - 1, stack[-1][1]
    stack.pop
  end
end
