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
