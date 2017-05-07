package edu.yonsei.classification.feature;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import edu.yonsei.util.Collection;
import edu.yonsei.util.Document;
import edu.yonsei.util.Pair;
import edu.yonsei.util.Sentence;
import edu.yonsei.util.Token;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class TFIDF {

	private int numOfWords;
	private double[] idfVector;
	private double[][] tfIdfMatrix, tfMatrix;
	private String[] wordVector;
	private int[] docLength;
	
	public TFIDF(Collection docs, int noOfFeatures) {
		HashMap<String, Integer> mapWordToIdx = new HashMap<String, Integer>();
		int nextIdx = 0;
		for(Document doc : docs)
			for(Sentence sent : doc)
				for(int i=0; i<sent.size(); i++) {
					Token tok = sent.get(i);
					if(tok.isStopword()) continue;
					String lemma = tok.getLemma().toLowerCase();
					lemma = lemma.replaceAll("[^a-zA-Z0-9]", "");
					if(lemma.isEmpty()) continue;
					if(!mapWordToIdx.containsKey(lemma))
						mapWordToIdx.put(lemma, nextIdx++);
				}
		
		numOfWords = mapWordToIdx.size();
		
		wordVector = new String[numOfWords];
		for(String word : mapWordToIdx.keySet()) {
			int wordIdx = mapWordToIdx.get(word);
			wordVector[wordIdx] = word;
		}
		
		int[] docCountVector = new int[numOfWords];
		docLength = new int[docs.size()];
		int[] lastDocWordVector = new int[numOfWords];
		for(int wordIdx=0; wordIdx<numOfWords; wordIdx++)
			lastDocWordVector[wordIdx] = -1;
		for(int docIdx=0; docIdx<docs.size(); docIdx++) {
			Document doc = docs.get(docIdx);
			int count = 0;
			for(Sentence sent : doc)
				for(int i=0; i<sent.size(); i++) {
					Token tok = sent.get(i);
					if(tok.isStopword()) continue;
					count++;
					String lemma = tok.getLemma().toLowerCase();
					lemma = lemma.replaceAll("[^a-zA-Z0-9]", "");
					if(lemma.isEmpty()) continue;
					int wordIdx = mapWordToIdx.get(lemma);
					if(lastDocWordVector[wordIdx] < docIdx) {
						lastDocWordVector[wordIdx] = docIdx;
						docCountVector[wordIdx]++;
					}
				}
			docLength[docIdx] = count;
		}
		
		idfVector = new double[numOfWords];
		Vector<Pair<Double, Integer>> top = new Vector<Pair<Double, Integer>>();
		for(int wordIdx=0; wordIdx<numOfWords; wordIdx++) {
			idfVector[wordIdx] = Math.log10(1 + (double) docs.size() / docCountVector[wordIdx]);
			top.add(new Pair<Double, Integer>(-idfVector[wordIdx], wordIdx));
		}
		Collections.sort(top);
		
		HashSet<Integer> featureIndex = new HashSet<Integer>();
		int featureCount = 0;
		for(int i=0; i<numOfWords; i++) {
			featureIndex.add(top.get(i).right);
			featureCount++;
			if(noOfFeatures != -1 && featureCount == noOfFeatures) break;
		}
		
		tfMatrix = new double[docs.size()][];
		for(int docIdx=0; docIdx<docs.size(); docIdx++)
			tfMatrix[docIdx] = new double[numOfWords];
		for(int docIdx=0; docIdx<docs.size(); docIdx++) {
			Document doc = docs.get(docIdx);
			for(Sentence sent : doc)
				for(int i=0; i<sent.size(); i++) {
					Token tok = sent.get(i);
					if(tok.isStopword()) continue;
					String lemma = tok.getLemma().toLowerCase();
					lemma = lemma.replaceAll("[^a-zA-Z0-9]", "");
					if(lemma.isEmpty()) continue;
					String word = lemma;
					int wordIdx = mapWordToIdx.get(word);
					tfMatrix[docIdx][wordIdx] = tfMatrix[docIdx][wordIdx] + 1;
				}
		}
		for(int docIdx=0; docIdx<docs.size(); docIdx++)
			for(int wordIdx=0; wordIdx<numOfWords; wordIdx++)
				tfMatrix[docIdx][wordIdx] = tfMatrix[docIdx][wordIdx] / docLength[docIdx];
		
		tfIdfMatrix = new double[docs.size()][];
		for(int docIdx=0; docIdx<docs.size(); docIdx++)
			tfIdfMatrix[docIdx] = new double[numOfWords];
		
		for(int docIdx=0; docIdx<docs.size(); docIdx++) {
			Vector<Double> features = new Vector<Double>();
			for(int wordIdx=0; wordIdx<numOfWords; wordIdx++) {
				tfIdfMatrix[docIdx][wordIdx] = tfMatrix[docIdx][wordIdx] * idfVector[wordIdx];
				if(featureIndex.contains(wordIdx))
					features.add(tfIdfMatrix[docIdx][wordIdx]);
			}
			docs.get(docIdx).setFeatures(features);
		}
	}
	
	public double[][] getTfIdfMatrix() {
		return tfIdfMatrix;
	}
	
	public String[] getWordVector() {
		return wordVector;
	}
	
}
