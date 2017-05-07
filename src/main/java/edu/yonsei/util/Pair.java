package edu.yonsei.util;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class Pair<L extends Comparable<L>, R extends Comparable<R>> implements Comparable<Pair<L, R>>{
	
	public L left;
	public R right;
	
	public Pair(L l, R r) {
		left = l;
		right = r;
	}
	
	public int compareTo(Pair<L, R> p) {
		if(!left.equals(p.left)) return left.compareTo(p.left);
		else return right.compareTo(p.right);
	}

}
