macro debug(msg)
  {% if flag?(:trace) %}
    STDERR.puts({{msg}})
  {% end %}
end

macro debugf(format_string, *args)
  {% if flag?(:trace) %}
    STDERR.printf({{format_string}}, {{*args}})
  {% end %}
end

def crash(msg, caller_line = __LINE__)
  puts "[ERROR] line #{caller_line}: #{msg}"
  exit
end

macro assert(cond, msg = "", caller_line = __LINE__)
  {% if flag?(:local) %}
    if !({{cond}})
      crash({{msg}}, {{caller_line}})
    end
  {% end %}
end

class XorShift
  TO_DOUBLE = 0.5 / (1u64 << 63)
  @gauss : Float64

  def initialize(@x = 123456789u64)
    @gauss = INF.to_f64
  end

  def next_int
    @x ^= @x << 13
    @x ^= @x >> 17
    @x ^= @x << 5
    return @x
  end

  def next_int(m)
    return next_int % m
  end

  def next_double
    return TO_DOUBLE * next_int
  end

  def gaussian
    if @gauss == INF.to_f64
      # Box-Muller
      theta = 2.0 * Math::PI * next_double
      rho = Math.sqrt(-2 * Math.log(1.0 - next_double))
      @gauss = rho * Math.cos(theta)
      return rho * Math.sin(theta)
    else
      ret = @gauss
      @gauss = INF.to_f64
      return ret
    end
  end
end

class StopWatch
  def initialize
    @start_at = Hash(String, Int64).new
    @sum = Hash(String, Int64).new(0i64)
  end

  def start(name)
    {% if flag?(:local) %}
      @start_at[name] = Time.utc.to_unix_ms
    {% end %}
  end

  def stop(name)
    {% if flag?(:local) %}
      @sum[name] += Time.utc.to_unix_ms - @start_at[name]
    {% end %}
  end

  def to_s(io)
    {% if flag?(:local) %}
      io << @sum
    {% end %}
  end
end

class Counter
  def initialize
    @hist = [] of Int32
  end

  def add(i)
    while @hist.size <= i
      @hist << 0
    end
    @hist[i] += 1
  end

  def to_s(io)
    io << "counter:\n"
    ((@hist.size + 9) // 10).times do |i|
      io << @hist[((i * 10)...(i * 10 + 10))]
      io << "\n"
    end
  end
end

class IndexSet
  getter :upper

  def initialize(@upper : Int32)
    @que = [] of Int32
    @pos = Array(Int32).new(@upper, -1)
  end

  def add(v)
    if @pos[v] == -1
      @pos[v] = @que.size
      @que << v
    end
  end

  def remove(v)
    p = @pos[v]
    b = @que[-1]
    @que[p] = b
    @que.pop
    @pos[b] = p
    @pos[v] = -1
  end

  def get_random
    @que[RND.rand(@que.size)]
  end

  def size
    @que.size
  end

  def to_s(io)
    @que.to_s(io)
  end
end
