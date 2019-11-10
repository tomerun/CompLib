require 'open3'

NUM_THREADS = 2
NUM_TESTS = 40
Result = Struct.new(:seed, :score, :err)

q_s = Queue.new
q_r = Queue.new
NUM_TESTS.times { |i| q_s << (100 + i) } 
q_s	<< -1
q_s << -1
results = Array.new(NUM_TESTS)

NUM_THREADS.times do
	Thread.new do
		while true
			seed = q_s.pop
			break if seed < 0
			so, se, st = Open3.capture3("java Main LOACL < data/testCase_#{seed}.txt")
			q_r << Result.new(seed, so.empty? ? -1 : so.to_i, se)
		end
	end
end

receiver = Thread.new do
	pos = 0
	sum = 0
	while pos < NUM_TESTS
		res = q_r.pop
		results[res.seed - 100] = res
		while pos <= NUM_TESTS && results[pos]
			puts sprintf('seed:%3d score:%d', results[pos].seed, results[pos].score)
			puts results[pos].err if !results[pos].err.empty?
			sum += results[pos].score
			pos += 1
		end
	end
	puts "ave:#{sum / NUM_TESTS}"
end

receiver.join
