package edu.yonsei.util;

import java.util.ArrayList;
import java.util.Vector;

import edu.yonsei.classification.LibLinearSVM;
import edu.yonsei.classification.LingpipeLogisticRegression;
import edu.yonsei.classification.MalletNaiveBayes;
import edu.yonsei.preprocess.CoreNLPPreprocess;
import edu.yonsei.topic.MalletLDA;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class Document extends ArrayList<Sentence> {
	
	private static final long serialVersionUID = 5923438214189501337L;
	private String document, classification;
	private Vector<Double> features;
	private Topic topic;
	
	public Document(String d) throws Exception {
		document = d;
		classification = null;
		topic = null;
		features = new Vector<Double>();
	}
	
	public Document(String d, String c) throws Exception {
		this(d);
		classification = c;
	}
	
	public void preprocess() throws Exception {
		CoreNLPPreprocess cnlpp = new CoreNLPPreprocess("data/util/stopwords.txt");
		cnlpp.preprocess(this);
	}
	
	public Vector<Double> getFeatures() {
		return features;
	}
	
	public void setClassification(String c) {
		classification = c;
	}
	
	public String getClassification() {
		return classification;
	}
	
	public String getClassification(String model, String file) throws Exception {
		if(model.equals("naive")) {
			MalletNaiveBayes nb = new MalletNaiveBayes(file);
			classification = nb.getClassification(this);
		}
		else if(model.equals("svm")) {
			LibLinearSVM svm = new LibLinearSVM(file);
			classification = svm.getClassification(this);
		}
		else if(model.equals("logistic")) {
			LingpipeLogisticRegression lr = new LingpipeLogisticRegression(file);
			classification = lr.getClassification(this);
		}
		return classification;
	}
	
	public String getDocument() {
		return document;
	}
	
	public void setDocument(String d) {
		document = d;
	}
	
	public Topic getTopic() {
		if(topic == null) {
			System.err.println("No topic yet. Cannot initialize topic without a model.");
			return null;
		}
		return topic;
	}
	
	public Topic getTopic(String model) throws Exception {
		if(topic == null) {
			MalletLDA mlda = new MalletLDA(model);
			topic = mlda.getTopic(this);
		}
		return topic;
	}
	
	public void setFeatures(Vector<Double> f) {
		features = f;
	}

}
