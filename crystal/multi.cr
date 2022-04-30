require "option_parser"

struct Result
  getter :seed, :score

  def initialize(@seed : Int32, @score : Int64)
  end
end

num_test = 20
num_worker = 4
ch = Channel(Result).new

OptionParser.parse do |parser|
  parser.on("-t NUM_TEST", "--tests=NUM_TEST", "Number of test cases(default:#{num_test})") do |v|
    num_test = v.to_i
  end
  parser.on("-w NUM_WORKER", "--workers=NUM_WORKER", "Number of workers(default:#{num_worker})") do |v|
    num_worker = v.to_i
  end
  parser.on("-h", "--help", "Show this help") do
    puts parser
    exit
  end
end

num_worker.times do |i|
  spawn do
    i.step(to: num_test - 1, by: num_worker) do |j|
      seed = sprintf("%02d", j)
      output = IO::Memory.new(1000)
      error = IO::Memory.new(1000)
      File.open("input/#{seed}.txt") do |f|
        Process.run("./main", input: f, output: output, error: error)
      end
      error.rewind
      error.gets_to_end.scan(/final_score:(\d+)/) do |m|
        ch.send(Result.new(j, m[1].to_i64))
      end
      output.rewind
      File.open("output/#{seed}.txt", "w") do |f|
        f << output.gets_to_end
      end
    end
  end
end

scores = Array.new(num_test, -1i64)
ri = 0
num_test.times do |i|
  res = ch.receive
  scores[res.seed] = res.score
  while ri < scores.size && scores[ri] != -1
    printf "seed:%02d score:%d\n", ri, scores[ri]
    ri += 1
  end
end
printf "ave:%d\n", scores.sum // num_test
