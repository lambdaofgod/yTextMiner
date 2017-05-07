package edu.yonsei.test.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

public class LibSVMClassificationMain {

	public static void main(String[] args) throws Exception
	{
		
		 Classifier clas = new LibSVM();
		 Dataset dataForClassification =  FileHandler.loadSparseDataset(new File("tfidf_matrix.txt"), 0, " ",":");
			
	     clas.buildClassifier(dataForClassification);
	        
	     CrossValidation cv = new CrossValidation(clas);
	     /* 5-fold CV with fixed random generator */
	     Map<Object, PerformanceMeasure> p = cv.crossValidation(dataForClassification, 5, new Random(1));
	     Map<Object, PerformanceMeasure> q = cv.crossValidation(dataForClassification, 5, new Random(1));
	     Map<Object, PerformanceMeasure> r = cv.crossValidation(dataForClassification, 5, new Random(25));
	     
	     System.out.println("Accuracy=" + p.get("1").getAccuracy());
	     System.out.println("Accuracy=" + q.get("2").getAccuracy());
	     System.out.println("Accuracy=" + r.get("3").getAccuracy());
	     System.out.println(p);
	     System.out.println(q);
	     System.out.println(r);
	     
	     for(Instance inst : dataForClassification) {
	    	 System.out.println(inst.classValue() + " " + clas.classify(inst));
	     }
	}
}
