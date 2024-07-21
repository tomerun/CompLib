#![allow(unused_imports)]
#![allow(dead_code)]
use std::collections::{HashMap, HashSet};
use std::io::{self, BufRead, BufReader, Read, Stdin};
use std::iter;
use std::mem;
use std::str::FromStr;
use std::string::String;

#[derive(Debug)]
pub struct Scanner<R> {
    reader: BufReader<R>,
}

#[cfg(test)]
mod tests {

    #[test]
    fn next_str() {
        let mut sc = ::Scanner::new(" a  xyz\nあ表ヶ!\t\r\n".as_bytes());
        assert_eq!(sc.next_str(), Some(String::from("a")));
        assert_eq!(sc.next_str(), Some(String::from("xyz")));
        assert_eq!(sc.next_str(), Some(String::from("あ表ヶ!")));
        assert_eq!(sc.next_str(), None {});
    }

    #[test]
    fn next_line() {
        let mut sc = ::Scanner::new(" a  xyz\nあ表ヶ!\t\r\n\n".as_bytes());
        assert_eq!(sc.next_line(), String::from(" a  xyz"));
        assert_eq!(sc.next_line(), String::from("あ表ヶ!\t\r"));
        assert_eq!(sc.next_line(), String::from(""));
        // assert_eq!(sc.next_line(), None {});
    }

    #[test]
    fn vec() {
        let mut sc = ::Scanner::new("1 4 9 16 25".as_bytes());
        assert_eq!(sc.vec::<i32>(4), vec![1, 4, 9, 16]);
    }

    #[test]
    fn i32() {
        let mut sc = ::Scanner::new("12 -3 0 -0 2147483647 -2147483648".as_bytes());
        assert_eq!(sc.i32(), 12);
        assert_eq!(sc.i32(), -3);
        assert_eq!(sc.i32(), 0);
        assert_eq!(sc.i32(), 0);
        assert_eq!(sc.i32(), 2147483647);
        assert_eq!(sc.i32(), -2147483648);
        // assert_eq!(sc.i32(), None {});
    }

    #[test]
    fn i64() {
        let mut sc = ::Scanner::new("12 -3 0 -0 9223372036854775807 -9223372036854775808".as_bytes());
        assert_eq!(sc.i64(), 12i64);
        assert_eq!(sc.i64(), -3i64);
        assert_eq!(sc.i64(), 0i64);
        assert_eq!(sc.i64(), 0i64);
        assert_eq!(sc.i64(), 9223372036854775807);
        assert_eq!(sc.i64(), -9223372036854775808);
        // assert_eq!(sc.i64(), None {});
    }

    #[test]
    fn f32() {
        let mut sc = ::Scanner::new("1 0 -0 0.0001 12345.678".as_bytes());
        assert_eq!(sc.f32(), 1.0);
        assert_eq!(sc.f32(), 0.0);
        assert_eq!(sc.f32(), 0.0);
        assert_eq!(sc.f32(), 0.0001);
        assert_eq!(sc.f32(), 12345.678);
        // assert_eq!(sc.f32(), None {});
    }

    #[test]
    fn f64() {
        let mut sc = ::Scanner::new("1 0 -0 0.0001 9876543210.987".as_bytes());
        assert_eq!(sc.f64(), 1.0);
        assert_eq!(sc.f64(), 0.0);
        assert_eq!(sc.f64(), 0.0);
        assert_eq!(sc.f64(), 0.0001);
        assert_eq!(sc.f64(), 9876543210.987);
        // assert_eq!(sc.f64(), None {});
    }
}

fn scanner() -> Scanner<Stdin> {
    Scanner::new(io::stdin())
}

impl<R: io::Read> Scanner<R> {
    pub fn new(read: R) -> Scanner<R> {
        Scanner {
            reader: BufReader::new(read),
        }
    }

    pub fn next_str(&mut self) -> Option<String> {
        let mut buf = [0; 1];
        let size = self.reader.read(&mut buf).unwrap();
        if size == 0 {
            None
        } else {
            self.skip_whitespace(&mut buf)?;
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

    pub fn next_line(&mut self) -> String {
        let mut line = String::new();
        self.reader.read_line(&mut line).unwrap();
        if let Some(s) = line.strip_suffix("\n") {
            s.to_string()
        } else {
            line
        }
    }

    pub fn vec<S: FromStr>(&mut self, size: i32) -> Vec<S> {
        let mut v: Vec<S> = vec![];
        for _ in 0..size {
            let token = self.next_str().unwrap();
            v.push(S::from_str(&token).ok().unwrap());
        }
        v
    }

    pub fn next_as<S: FromStr>(&mut self) -> Option<S> {
        let s = self.next_str()?;
        S::from_str(&s).ok()
    }

    pub fn str(&mut self) -> String {
        self.next_str().unwrap()
    }

    pub fn i32(&mut self) -> i32 {
        self.next_as::<i32>().unwrap()
    }

    pub fn u32(&mut self) -> u32 {
        self.next_as::<u32>().unwrap()
    }

    pub fn i64(&mut self) -> i64 {
        self.next_as::<i64>().unwrap()
    }

    pub fn u64(&mut self) -> u64 {
        self.next_as::<u64>().unwrap()
    }

    pub fn usize(&mut self) -> usize {
        self.next_as::<usize>().unwrap()
    }

    pub fn f32(&mut self) -> f32 {
        self.next_as::<f32>().unwrap()
    }

    pub fn f64(&mut self) -> f64 {
        self.next_as::<f64>().unwrap()
    }

    fn skip_whitespace(&mut self, mut buf: &mut [u8]) -> Option<u8> {
        loop {
            if !buf[0].is_ascii_whitespace() {
                return Some(buf[0]);
            }
            let size = self.reader.read(&mut buf).unwrap();
            if size == 0 {
                return None;
            }
        }
    }
}

fn yesno(b: bool) -> &'static str {
    if b {
        "Yes"
    } else {
        "No"
    }
}
