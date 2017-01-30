import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prime {

	public static BitSet primes(int max) {
		BitSet primeSet = new BitSet(max + 1);
		if (max < 2) {
			return primeSet;
		}
		int limit = (int) Math.sqrt(max + 1);
		primeSet.set(2);
		for (int i = 3; i < max + 1; i += 2) {
			primeSet.set(i);
		}
		for (int i = 3; i <= limit; i += 2) {
			if (!primeSet.get(i)) {
				continue;
			}
			for (int j = i * i; j < max; j += i) {
				primeSet.clear(j);
			}
		}
		return primeSet;
	}

	public static ArrayList<Integer> primes(int max) {
		if (max < 2) {
			return new ArrayList<Integer>();
		}
		BitSet primeSet = new BitSet(max / 2);
		primeSet.set(1, max / 2);
		int limit = (int) Math.sqrt(max);
		for (int i = 3; i <= limit; i += 2) {
			if (!primeSet.get(i / 2)) {
				continue;
			}
			for (int j = i * i; j < max; j += i * 2) {
				primeSet.clear(j / 2);
			}
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
		for (int i = primeSet.nextSetBit(0); i >= 0; i = primeSet.nextSetBit(i + 1)) {
			list.add(i * 2 + 1);
		}
		return list;
	}

	public static Map<Integer, Integer> factorize(int i) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int n = i;
		int index = 0;
		while (n > 1) {
			if (n % primeAry[index] != 0) {
				index++;
				continue;
			}
			n = n / primeAry[index];
			if (map.containsKey(primeAry[index])) {
				map.put(primeAry[index], map.get(primeAry[index]) + 1);
			} else {
				map.put(primeAry[index], 1);
			}
		}
		return map;
	}

	public static Map<Integer, Integer> factorize(BigInteger i) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		BigInteger[] primeBIs = new BigInteger[primeAry.length];
		for (int j = 0; j < primeAry.length; j++) {
			primeBIs[j] = BigInteger.valueOf((primeAry[j]));
		}
		BigInteger n = i;
		int index = 0;
		while (n.compareTo(BigInteger.ONE) > 0) {
			if (n.remainder(primeBIs[index]).signum() != 0) {
				index++;
				continue;
			}
			n = n.divide(primeBIs[index]);
			if (map.containsKey(primeAry[index])) {
				map.put(primeAry[index], map.get(primeAry[index]) + 1);
			} else {
				map.put(primeAry[index], 1);
			}
		}
		return map;
	}

}
