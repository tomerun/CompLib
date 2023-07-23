class Mo
  include Enumerable(Tuple(Int32, Int32, Int32))

  def initialize(@n : Int32, @bucket_size : Int32)
    @bucket = Array(Array(Int32)).new((@n + @bucket_size - 1) // @bucket_size) { [] of Int32 }
    @qs = Array(Tuple(Int32, Int32)).new
  end

  def add_query(l : Int32, r : Int32)
    @bucket[l // @bucket_size] << @qs.size
    @qs << {l, r}
  end

  def build
    @bucket.size.times do |i|
      @bucket[i].sort_by! { |qi| @qs[qi][1] }
      if i % 2 != 0
        @bucket[i].reverse!
      end
    end
  end

  def each
    @bucket.each do |b|
      b.each do |qi|
        yield ({@qs[qi][0], @qs[qi][1], qi})
      end
    end
  end
end
