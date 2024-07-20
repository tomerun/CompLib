w, n = read_line.split.map(&.to_i)
sd = SqrtDecomposition(Bucket, Int32).new(w + 1, 300)
n.times do
  l, r = read_line.split.map(&.to_i)
  prev = sd.query(l, r)
  puts prev + 1
  sd.set(l, r, prev + 1)
end

class Bucket
  def initialize(pos, len)
    @ar = Array(Int32).new(len, 0)
    @all = 0
    @top = 0
  end

  def set_edge(lo, hi, v)
    @top = {@top, v}.max
    lo.upto(hi) do |i|
      @ar[i] = {@ar[i], v}.max
    end
  end

  def set_all(v)
    @all = {@all, v}.max
    @top = {@top, v}.max
  end

  def query_edge(lo, hi, v)
    v = {@all, v}.max
    lo.upto(hi) do |i|
      v = {@ar[i], v}.max
    end
    return v
  end

  def query_all(v)
    return {@top, v}.max
  end
end

class SqrtDecomposition(Bucket, Value)
  def initialize(@size : Int32, @bucket_len : Int32)
    @bucket = Array(Bucket).new((@size + @bucket_len - 1) // @bucket_len) { |i| Bucket.new(i, @bucket_len) }
  end

  def set(lo : Int32, hi : Int32, v : Value)
    # inclusive
    lo_b, lo_i = lo.divmod(@bucket_len)
    hi_b, hi_i = hi.divmod(@bucket_len)
    if lo_b == hi_b
      @bucket[lo_b].set_edge(lo_i, hi_i, v)
    else
      if lo_i != 0
        @bucket[lo_b].set_edge(lo_i, @bucket_len - 1, v)
        lo_b += 1
      end
      if hi_i != @bucket_len - 1
        @bucket[hi_b].set_edge(0, hi_i, v)
        hi_b -= 1
      end
      lo_b.upto(hi_b) do |bi|
        @bucket[bi].set_all(v)
      end
    end
  end

  def query(lo : Int32, hi : Int32) : Value
    lo_b, lo_i = lo.divmod(@bucket_len)
    hi_b, hi_i = hi.divmod(@bucket_len)
    ret = Value.zero
    if lo_b == hi_b
      ret = @bucket[lo_b].query_edge(lo_i, hi_i, ret)
    else
      if lo_i != 0
        ret = @bucket[lo_b].query_edge(lo_i, @bucket_len - 1, ret)
        lo_b += 1
      end
      if hi_i != @bucket_len - 1
        ret = @bucket[hi_b].query_edge(0, hi_i, ret)
        hi_b -= 1
      end
      lo_b.upto(hi_b) do |bi|
        ret = @bucket[bi].query_all(ret)
      end
    end
    return ret
  end
end
