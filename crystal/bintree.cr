class RBST(T)
  class Node(T)
    @left : Node(T) | Nil
    @right : Node(T) | Nil
    property :left, :right
    getter :val, :size

    def initialize(@val : T)
      @left = nil
      @right = nil
      @size = 1
    end

    def height
      if left = @left
        lh = left.height
      else
        lh = 0
      end
      if right = @right
        rh = right.height
      else
        rh = 0
      end
      return {lh, rh}.max + 1
    end

    def fix_size
      @size = 1
      if left = @left
        @size += left.size
      end
      if right = @right
        @size += right.size
      end
    end

    def to_s(io : IO)
      to_s(io, 0)
    end

    def to_s(io : IO, level : Int32)
      if left = @left
        left.to_s(io, level + 1)
      end
      io << " " * level * 2 << @val << "\n"
      if right = @right
        right.to_s(io, level + 1)
      end
    end
  end

  @root : Node(T) | Nil

  def initialize
    @root = nil
    @rnd = Random.new
  end

  def insert(v : T)
    @root = insert(@root, v)
  end

  def insert(node : Node(T) | Nil, v : T) : Node(T)
    return Node(T).new(v) if !node
    if @rnd.rand(node.size + 1) == 0
      return insert_root(node, v)
    end
    if v < node.val
      node.left = insert(node.left, v)
    else
      node.right = insert(node.right, v)
    end
    node.fix_size
    return node
  end

  def insert_root(node : Node(T) | Nil, v : T) : Node(T)
    return Node(T).new(v) if !node
    if v < node.val
      node.left = insert_root(node.left, v)
      return rotate_right(node)
    else
      node.right = insert_root(node.right, v)
      return rotate_left(node)
    end
  end

  def rotate_right(node : Node(T)) : Node(T)
    top = node.left.not_nil!
    mid = top.right
    top.right = node
    node.left = mid
    node.fix_size
    top.fix_size
    return top
  end

  def rotate_left(node : Node(T)) : Node(T)
    top = node.right.not_nil!
    mid = top.left
    top.left = node
    node.right = mid
    node.fix_size
    top.fix_size
    return top
  end

  def clear
    @root = nil
  end

  def remove(v : T) : Bool
    new_root, found = remove(@root, v)
    if found
      @root = new_root
      return true
    else
      return false
    end
  end

  def remove(node : Node(T) | Nil, v : T) : Tuple(Node(T) | Nil, Bool)
    return {nil, false} if !node
    if v == node.val
      return {meld(node.left, node.right), true}
    elsif v < node.val
      new_child, found = remove(node.left, v)
      if found
        node.left = new_child
        node.fix_size
      end
    else
      new_child, found = remove(node.right, v)
      if found
        node.right = new_child
        node.fix_size
      end
    end
    return {node, found}
  end

  def meld(left : Node(T) | Nil, right : Node(T) | Nil) : Node(T) | Nil
    return right if !left
    return left if !right
    if @rnd.rand(left.size + right.size) < left.size
      left.right = meld(left.right, right)
      left.fix_size
      return left
    else
      right.left = meld(left, right.left)
      right.fix_size
      return right
    end
  end

  def find(v : T) : Node(T) | Nil
    cur = @root
    while cur
      if v == cur.val
        return cur
      elsif v < cur.val
        cur = cur.left
      else
        cur = cur.right
      end
    end
    return nil
  end

  def nth(n : Int32) : T
    # 0-indexed
    cur = @root
    while cur
      ln = cur.left
      lc = ln ? ln.size : 0
      if lc > n
        cur = cur.left
      elsif lc < n
        cur = cur.right
        n -= lc + 1
      else
        break
      end
    end
    if cur
      return cur.val
    else
      raise Exception.new("cannot fine")
    end
  end

  def rank(v : T) : Int32
    # 0-indexed, not count equivalent values
    cur = @root
    ret = 0
    while cur
      if v <= cur.val
        cur = cur.left
      else
        if left = cur.left
          ret += left.size
        end
        ret += 1
        cur = cur.right
      end
    end
    return ret
  end

  def lower(v : T) : T | Nil
    # largest value smaller than v
    cur = @root
    ret = nil
    while cur
      if cur.val < v
        ret = cur.val
        cur = cur.right
      else
        cur = cur.left
      end
    end
    return ret
  end

  def upper(v : T) : T | Nil
    # smallest value larger than v
    cur = @root
    ret = nil
    while cur
      if cur.val > v
        ret = cur.val
        cur = cur.left
      else
        cur = cur.right
      end
    end
    return ret
  end

  def size
    if root = @root
      return root.size
    else
      return 0
    end
  end

  def height
    if root = @root
      root.height
    else
      0
    end
  end

  def to_s(io : IO)
    if root = @root
      root.to_s(io, 0)
    else
      io << "nil\n"
    end
  end
end

# rnd = Random.new(2)
# 100000.times do
#   rbst = RBST(Int32).new
#   upper = rnd.rand(10000) + 2
#   axis = upper // 2
#   values = Array(Int32).new(rnd.rand(100) + 2) { rnd.rand(upper) }.uniq
#   small, large = values.partition { |v| v < axis }
#   # STDERR.puts("upper:#{upper} axis:#{axis} small:#{small} large:#{large} values:#{values}")
#   values.each { |v| rbst.insert(v) }
#   if rbst.rank(axis) != small.size
#     raise Exception.new("expect:#{small.size} actual:#{rbst.rank(axis)}")
#   end
#   if rbst.lower(axis) != small.empty? ? nil : small.max
#     raise Exception.new("expect:#{small.max} actual:#{rbst.lower(axis)}")
#   end
#   if rbst.upper(axis - 1) != large.empty? ? nil : large.min
#     raise Exception.new("expect:#{large.min} actual:#{rbst.upper(axis)}")
#   end
#   (small.size // 2).times do |i|
#     if !rbst.remove(small[i])
#       raise Exception.new("remove failed")
#     end
#   end
#   if rbst.rank(axis) != small.size - small.size // 2
#     raise Exception.new("expect:#{small.size - small.size // 2} actual:#{rbst.rank(axis)}")
#   end
#   large.each { |v| rbst.remove(v) }
#   (small.size // 2).upto(small.size - 1) do |i|
#     if !rbst.remove(small[i])
#       raise Exception.new("remove failed")
#     end
#   end
#   if rbst.rank(axis) != 0
#     raise Exception.new("expect:0 actual:#{rbst.rank(axis)}")
#   end
#   if rbst.size != 0
#     raise Exception.new("expect:0 actual:#{rbst.size}")
#   end
# end
