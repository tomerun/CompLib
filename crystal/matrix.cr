class Matrix(T)
  @a : Array(Array(T))
  getter :a, :d1, :d2

  def initialize(n : Int32, m : Int32)
    @a = Array.new(n) { Array.new(m, T.zero) }
    @d1 = n
    @d2 = m
  end

  def [](idx : Int32)
    return @a[idx]
  end

  def mul(other : Matrix(T))
    if @d2 != other.d1
      raise ArgumentError.new
    end
    d3 = other.d2
    tmp = Array.new(d3) { Array.new(@d2, T.zero) }
    d3.times do |i|
      tr1 = tmp[i]
      @d2.times do |j|
        tr1[j] = other[j][i]
      end
    end
    ret = Array.new(@d1) { Array.new(d3, T.zero) }
    @d1.times do |i|
      row1 = @a[i]
      ret1 = ret[i]
      d3.times do |j|
        sum = T.zero
        row2 = tmp[j]
        @d2.times do |k|
          sum += row1[k] * row2[k]
        end
        ret1[j] = sum
      end
    end
    @a = ret
  end
end

class AndXor
  property :v

  def initialize(v : UInt32)
    @v = v
  end

  def self.zero
    return self.new(0u32)
  end

  def +(other : AndXor)
    return self.class.new(@v ^ other.v)
  end

  def *(other : AndXor)
    return self.class.new(@v & other.v)
  end
end
