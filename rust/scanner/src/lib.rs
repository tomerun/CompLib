use std::io::{self, BufReader, Read};
use std::str::FromStr;
use std::string::String;


#[cfg(test)]
mod tests {

	#[test]
	fn next_str() {
		let mut sc = ::Scanner::new(" a  xyz\nあ表ヶ!\t\r\n".as_bytes());
		assert_eq!(sc.next_str(), Some(String::from("a")));
		assert_eq!(sc.next_str(), Some(String::from("xyz")));
		assert_eq!(sc.next_str(), Some(String::from("あ表ヶ!")));
		assert_eq!(sc.next_str(), None{});
	}

	#[test]
	fn next_line() {
		let mut sc = ::Scanner::new(" a  xyz\nあ表ヶ!\t\r\n\n".as_bytes());
		assert_eq!(sc.next_line(), Some(String::from(" a  xyz")));
		assert_eq!(sc.next_line(), Some(String::from("あ表ヶ!\t\r")));
		assert_eq!(sc.next_line(), Some(String::from("")));
		assert_eq!(sc.next_line(), None{});
	}

	#[test]
	fn next_vec() {
		let mut sc = ::Scanner::new("1 4 9 16 25".as_bytes());
		assert_eq!(sc.next_vec::<i32>(4), vec![1, 4, 9, 16]);
	}

	#[test]
	fn next_i32() {
		let mut sc = ::Scanner::new("12 -3 0 -0 2147483647 -2147483648".as_bytes());
		assert_eq!(sc.next_i32(), Some(12));
		assert_eq!(sc.next_i32(), Some(-3));
		assert_eq!(sc.next_i32(), Some(0));
		assert_eq!(sc.next_i32(), Some(0));
		assert_eq!(sc.next_i32(), Some(2147483647));
		assert_eq!(sc.next_i32(), Some(-2147483648));
		assert_eq!(sc.next_i32(), None{});
	}

	#[test]
	fn next_i64() {
		let mut sc = ::Scanner::new("12 -3 0 -0 9223372036854775807 -9223372036854775808".as_bytes());
		assert_eq!(sc.next_i64(), Some(12i64));
		assert_eq!(sc.next_i64(), Some(-3i64));
		assert_eq!(sc.next_i64(), Some(0i64));
		assert_eq!(sc.next_i64(), Some(0i64));
		assert_eq!(sc.next_i64(), Some(9223372036854775807));
		assert_eq!(sc.next_i64(), Some(-9223372036854775808));
		assert_eq!(sc.next_i64(), None{});
	}

	#[test]
	fn next_f32() {
		let mut sc = ::Scanner::new("1 0 -0 0.0001 12345.678".as_bytes());
		assert_eq!(sc.next_f32(), Some(1.0));
		assert_eq!(sc.next_f32(), Some(0.0));
		assert_eq!(sc.next_f32(), Some(0.0));
		assert_eq!(sc.next_f32(), Some(0.0001));
		assert_eq!(sc.next_f32(), Some(12345.678));
		assert_eq!(sc.next_f32(), None{});
	}

	#[test]
	fn next_f64() {
		let mut sc = ::Scanner::new("1 0 -0 0.0001 9876543210.987".as_bytes());
		assert_eq!(sc.next_f64(), Some(1.0));
		assert_eq!(sc.next_f64(), Some(0.0));
		assert_eq!(sc.next_f64(), Some(0.0));
		assert_eq!(sc.next_f64(), Some(0.0001));
		assert_eq!(sc.next_f64(), Some(9876543210.987));
		assert_eq!(sc.next_f64(), None{});
	}

}

#[derive(Debug)]
pub struct Scanner<R> {
	reader: BufReader<R>
}

impl<R: io::Read> Scanner<R> {
	
	pub fn new(read: R) -> Scanner<R> {
		Scanner { reader: BufReader::new(read) }
	}

	pub fn next_str(&mut self) -> Option<String> {
		let mut buf = [0; 1];
		let size = self.reader.read(&mut buf).unwrap();
		if size == 0 {
			None{}
		} else {
			let first = self.skip_whitespace(&mut buf);
			if first.is_none() {
				return None{}
			}
			let mut v = vec![buf[0]];
			loop {
				let size = self.reader.read(&mut buf).unwrap();
				if size == 0 || buf[0].is_ascii_whitespace() {
					break;
				}
				v.push(buf[0]);
			}
			Some(String::from_utf8(v).unwrap())
		}
	}

	pub fn next_line(&mut self) -> Option<String> {
		let mut buf = [0; 1];
		let mut v :Vec<u8> = vec![];
		let mut size = self.reader.read(&mut buf).unwrap();
		if size == 0 {
			return None{}
		}
		loop {
			if size == 0 || char::from(buf[0]) == '\n' {
				return Some(String::from_utf8(v).unwrap())
			}
			v.push(buf[0]);
			size = self.reader.read(&mut buf).unwrap();
		}
	}

	pub fn next_vec<S: FromStr>(&mut self, size: u32) -> Vec<S> {
		let mut v: Vec<S> = vec![];
		for _ in 0..size {
			let token = self.next_str().unwrap();
			v.push(S::from_str(&token).ok().unwrap());
		}
		v
	}

	pub fn next_as<S: FromStr>(&mut self) -> Option<S> {
		if let Some(s) = self.next_str() {
			if let Ok(v) = S::from_str(&s) {
				return Some(v)
			}
		}
		None{}
	}

	pub fn next_i32(&mut self) -> Option<i32> {
		self.next_as::<i32>()
	}

	pub fn next_i64(&mut self) -> Option<i64> {
		self.next_as::<i64>()
	}

	pub fn next_f32(&mut self) -> Option<f32> {
		self.next_as::<f32>()
	}

	pub fn next_f64(&mut self) -> Option<f64> {
		self.next_as::<f64>()
	}

	fn skip_whitespace(&mut self, mut buf: &mut [u8]) -> Option<u8> {
		loop {
			if !buf[0].is_ascii_whitespace() {
				return Some(buf[0])
			}
			let size = self.reader.read(&mut buf).unwrap();
			if size == 0 {
				return None{}
			}
		}
	}
}
