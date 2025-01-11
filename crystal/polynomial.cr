class PolynomialCoef(T)
  def initialize(@coefs : Array(T))
  end

  def evaluate_at(x : T) : T
    v = @coefs[-1]
    (@coefs.size - 2).downto(0) do |i|
      v = v * x + @coefs[i]
    end
    return v
  end

  def order : Int32
    return @coefs.size - 1
  end

  def to_s(io)
    io << @coefs
  end
end

class PolynomialPoints(T)
  @order : Int32

  def initialize(@xs : Array(T), @ys : Array(T), order : Int32? = nil)
    @order = order.nil? ? @xs.size - 1 : order
  end

  def interpolate : PolynomialCoef(T)
    # Lagrange Interpolation

    # Π(x - x_i)
    num = Array.new(@order + 2, T.zero)
    num[0] = -@xs[0]
    num[1] = T.multiplicative_identity
    1.upto(@order) do |i|
      num[i + 1] = num[i]
      i.downto(1) do |j|
        num[j] = num[j - 1] - num[j] * @xs[i]
      end
      num[0] *= -@xs[i]
    end

    coefs = Array.new(@order + 1, T.zero)
    (@order + 1).times do |i|
      den = T.multiplicative_identity
      (@order + 1).times do |j|
        if i != j
          den *= (@xs[i] - @xs[j])
        end
      end
      # Π(x - x_j) / (x - x_i)
      poly = [num[@order + 1]]
      @order.downto(1) do |j|
        poly << num[j] + poly[-1] * @xs[i]
      end
      (@order + 1).times do |j|
        coefs[j] += @ys[i] * poly[@order - j] / den # TODO:整数の場合
      end
    end
    return PolynomialCoef.new(coefs)
  end

  def to_s(io)
    @xs.size.times do |i|
      io << "[#{@xs[i]}, #{@ys[i]}] "
    end
    io
  end
end

# sample
# poly = PolynomialCoef(Float64).new([1.0, -3.0, 2.0, -1.0, 0.0, 3.0, 0.0, -3.0, 1.0, 1.0, 2.0])
# xs = Array.new(poly.order + 1) { |i| i.to_f64 }
# ys = xs.map { |x| poly.evaluate_at(x) }
# poly2 = PolynomialPoints(Float64).new(xs, ys)
# poly3 = poly2.interpolate
# puts poly
# puts poly2
# puts poly3
