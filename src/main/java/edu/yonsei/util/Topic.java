package edu.yonsei.util;

import java.util.Iterator;
import java.util.Vector;

import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class Topic {

	private int id;
	private Vector<Pair<String, Double>> wordProbPair;
	
	public Topic(int topic, Alphabet dataAlphabet, Iterator<IDSorter> iterator) {
		id = topic;
		wordProbPair = new Vector<Pair<String, Double>>();
		while(iterator.hasNext()) {
			IDSorter idCountPair = iterator.next();
			wordProbPair.add(new Pair<String, Double>(dataAlphabet.lookupObject(idCountPair.getID()).toString(), idCountPair.getWeight()));
		}
	}
	
	public String toString() {
		String print = id + ": "; 
		for(int i=0; i<10; i++) {
			Pair<String, Double> p = wordProbPair.get(i);
			print += String.format("%s (%.2f) ", p.left, p.right);
		}
		return print;
	}
}
