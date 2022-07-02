def z_algo(p : Array(T), t : Array(T)) forall T
  # find p in t
  # z[i] := prefix match length starting at (p+(null)+t)[i]
  ret = [] of Int32
  return ret if p.size > t.size
  s = p + [T.zero] + t
  z = Array.new(s.size, 0)
  left = 0
  right = 0
  1.upto(s.size - 1) do |i|
    if i >= right
      left = i
      right = i
      while right < s.size && s[right] == s[right - left]
        right += 1
      end
      z[i] = right - left
    else
      k = i - left
      if z[k] < right - i
        z[i] = z[k]
      else
        left = i
        while right < s.size && s[right] == s[right - left]
          right += 1
        end
        z[i] = right - left
      end
    end
    if p.size < i && z[i] == p.size
      ret << i - p.size - 1
    end
  end
  return ret
end
