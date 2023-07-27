def radial_sort(ps : Array(Tuple(Int, Int)), center : Tuple(Int, Int)) : Array(Int32)
  right = [] of Int32
  left = [] of Int32
  top = [] of Int32
  bottom = [] of Int32
  ps.size.times do |i|
    p = ps[i]
    next if p == center
    dy = p[0] - center[0]
    dx = p[1] - center[1]
    if dx == 0
      (dy > 0 ? top : bottom) << i
    else
      (dx > 0 ? right : left) << i
    end
  end
  right.sort! do |i0, i1|
    dy0 = ps[i0][0] - center[0]
    dx0 = ps[i0][1] - center[1]
    dy1 = ps[i1][0] - center[0]
    dx1 = ps[i1][1] - center[1]
    # dy0 / dx0 < dy1 / dx1
    dy0 * dx1 <=> dy1 * dx0
  end
  left.sort! do |i0, i1|
    dy0 = ps[i0][0] - center[0]
    dx0 = ps[i0][1] - center[1]
    dy1 = ps[i1][0] - center[0]
    dx1 = ps[i1][1] - center[1]
    # dy0 / dx0 < dy1 / dx1
    dy0 * dx1 <=> dy1 * dx0
  end
  return right + top + left + bottom
end
