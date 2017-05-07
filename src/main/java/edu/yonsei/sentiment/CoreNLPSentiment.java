package edu.yonsei.sentiment;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.yonsei.util.Sentence;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class CoreNLPSentiment {
	
	private StanfordCoreNLP pipeline;
	
	public CoreNLPSentiment() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}
	
	public double getSentiment(Sentence s) {
		String sent = s.getSentence();
		Annotation annotation = pipeline.process(sent);
		for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
			return RNNCoreAnnotations.getPredictedClass(tree);
		}
		return -1;
	}

}
