package edu.yonsei.util;

import edu.yonsei.preprocess.CoreNLPPreprocess;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class Token {
	
	private String token, lemma, pos, ner, stem;
	private boolean isStop;
	
	public Token(String t, String l, String p, String n, String s, boolean i) {
		token = t;
		lemma = l;
		pos = p;
		ner = n;
		stem = s;
		isStop = i;
	}
	
	public void preprocess() throws Exception {
		CoreNLPPreprocess cnlpp = new CoreNLPPreprocess("data/util/stopwords.txt");
		cnlpp.preprocess(this);
	}
	
	public Token(String t) throws Exception {
		token = t;
		preprocess();
	}
	
	public String getStem() {
		return stem;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public String getPOS() {
		return pos;
	}
	
	public String getNER() {
		return ner;
	}
	
	public boolean isStopword() {
		return isStop;
	}
	
	public void setLemma(String l) {
		lemma = l;
	}
	
	public void setPOS(String p) {
		pos = p;
	}
	
	public void setNER(String n) {
		ner = n;
	}
	
	public void setStop(boolean s) {
		isStop = s;
	}

}
