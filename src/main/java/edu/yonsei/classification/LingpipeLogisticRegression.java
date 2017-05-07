package edu.yonsei.classification;

import java.io.IOException;
import java.util.Vector;

import com.aliasi.matrix.SparseFloatVector;
import com.aliasi.stats.LogisticRegression;

import edu.yonsei.util.Document;
import edu.yonsei.util.Serialization;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LingpipeLogisticRegression {
	
	private LogisticRegression model;
	
	public LingpipeLogisticRegression(String file) throws ClassNotFoundException, IOException {
		model = (LogisticRegression) Serialization.deserialize(file);
	}
	
	public String getClassification(Document d) {
		Vector<Double> features = d.getFeatures();
		int[] keys = new int[features.size()];
		float[] values = new float[features.size()];
		int dimensions = features.size();
		for(int j=0; j<features.size(); j++) {
			keys[j] = j;
			values[j] = features.get(j).floatValue();
		}
		com.aliasi.matrix.Vector v = new SparseFloatVector(keys, values, dimensions);
		double[] prob = model.classify(v);
		int index = -1;
		double max = 0;
		for(int i=0; i<prob.length; i++) {
			if(max < prob[i]) {
				max = prob[i];
				index = i;
			}
		}
		return "" + index;
	}

}
