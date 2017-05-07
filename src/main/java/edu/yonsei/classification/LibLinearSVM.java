package edu.yonsei.classification;

import java.util.Vector;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import edu.yonsei.util.Document;
import edu.yonsei.util.Serialization;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LibLinearSVM {
	
	private Classifier model;
	
	public LibLinearSVM(String file) throws Exception {
		model = (Classifier) Serialization.deserialize(file);
	}
	
	public String getClassification(Document d) {
		Vector<Double> featureList = d.getFeatures();
		double[] featureArray = new double[featureList.size()];
		for(int j=0; j<featureList.size(); j++)
			featureArray[j] = featureList.get(j);
		Instance instance = new DenseInstance(featureArray, d.getClassification());
		return (String) model.classify(instance);
	}

}
