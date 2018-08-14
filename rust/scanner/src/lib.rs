use std::io;
use std::io::Read;
use std::io::BufReader;
use std::fmt;
use std::option;
use std::string::String;


#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }

    #[test]
    fn next_str() {
    	let mut sc = ::Scanner::new(" a  xyz\nあ表ヶ!\t\r\n".as_bytes());
      assert_eq!(sc.next_str(), Some(String::from("a")));
      assert_eq!(sc.next_str(), Some(String::from("xyz")));
      assert_eq!(sc.next_str(), Some(String::from("あ表ヶ!")));
      assert_eq!(sc.next_str(), None{});
    }

    #[test]
    fn next_int() {
    	let sc = ::Scanner::new("12 -3 0 -0 2147483647, -2147483648".as_bytes());
    	// eprintln!("{:?}", sc.reader);
      // assert_eq!(sc.next_i32(), 12);
      // assert_eq!(sc.next_i32(), -3);
      // assert_eq!(sc.next_i32(), 0);
      // assert_eq!(sc.next_i32(), 0);
      // assert_eq!(sc.next_i32(), 2147483647);
      // assert_eq!(sc.next_i32(), -2147483648);
    }

}

#[derive(Debug)]
pub struct Scanner<R> {
	reader: BufReader<R>
}

impl<R: io::Read + fmt::Debug> Scanner<R> {
	
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

	pub fn next_i32(&self) -> i32 {
	    12
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
