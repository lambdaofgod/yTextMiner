package edu.yonsei.sentiment;

import java.io.File;

import com.aliasi.classify.Classification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.stats.MultivariateEstimator;
import com.aliasi.util.AbstractExternalizable;

import edu.yonsei.util.Sentence;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LingpipeSentiment {
	
	private LMClassifier<NGramProcessLM, MultivariateEstimator> mClassifier;
	
	@SuppressWarnings("unchecked")
	public LingpipeSentiment(String model) throws Exception {
		mClassifier = (LMClassifier<NGramProcessLM, MultivariateEstimator>) AbstractExternalizable.readObject(new File(model));
	}
	
	public double getSentiment(Sentence s) {
		String sentence = s.getSentence();
		Classification classification = mClassifier.classify(sentence);
		return Double.parseDouble(classification.bestCategory());
	}

}
