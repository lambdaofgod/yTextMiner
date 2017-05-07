package edu.yonsei.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;

import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TargetStringToFeatures;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.DMRTopicModel;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.matrix.SparseFloatVector;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.stats.RegressionPrior;

import edu.yonsei.classification.feature.TFIDF;

public class Collection extends ArrayList<Document> {
	
	private static final long serialVersionUID = -283788963082278261L;
	private Topic[] topics;

	public Collection(List<String> list) throws Exception {
		topics = null;
		for(String str : list) {
			Document d = new Document(str);
			add(d);
		}
	}
	
	public Collection(List<String> docs, List<String> classes) throws Exception {
		for(int i=0; i<docs.size(); i++) {
			Document d = new Document(docs.get(i), classes.get(i));
			add(d);
		}
	}
	
	public TFIDF getTfIdf(int noOfFeatures) {
		TFIDF tfidf = new TFIDF(this, noOfFeatures);
		return tfidf;
	}
	
	public TFIDF getTfIdf() {
		TFIDF tfidf = new TFIDF(this, -1);
		return tfidf;
	}
	
	public String getClassLabel(int index)
	{
		Document doc = get(index);
		
		return doc.getClassification();
	}
	
	public void preprocess() throws Exception {
		for(int i=0; i<size(); i++) {
			System.out.println("preprocessing document " + i);
			get(i).preprocess();
		}
	}
	
	public void svm(String modelFile) throws IOException {
		Dataset data = new DefaultDataset();
		for(int i=0; i<size(); i++) {
			Vector<Double> featureList = get(i).getFeatures();
			double[] featureArray = new double[featureList.size()];
			for(int j=0; j<featureList.size(); j++)
				featureArray[j] = featureList.get(j);
			Instance instance = new DenseInstance(featureArray, get(i).getClassification());
			data.add(instance);
		}
		
		Classifier classifier = new LibSVM();
		classifier.buildClassifier(data);
		
		Serialization.serialize(classifier, modelFile);
	}
	
	public void logisticRegression(String modelFile) throws IOException {
		com.aliasi.matrix.Vector[] xs = new com.aliasi.matrix.Vector[size()];
		int[] cs = new int[size()];
		RegressionPrior prior = RegressionPrior.gaussian(1.0, true);
		AnnealingSchedule annealingSchedule = AnnealingSchedule.exponential(0.00025,0.999);
		double minImprovement = 0.000000001;
        int minEpochs = 100;
        int maxEpochs = 20000;
		for(int i=0; i<size(); i++) {
			Document d = get(i);
			Vector<Double> features = d.getFeatures();
			int[] keys = new int[features.size()];
			float[] values = new float[features.size()];
			int dimensions = features.size();
			for(int j=0; j<features.size(); j++) {
				keys[j] = j;
				values[j] = features.get(j).floatValue();
			}
			xs[i] = new SparseFloatVector(keys, values, dimensions);
			cs[i] = (int)(Double.parseDouble(d.getClassification()));
		}
		
		LogisticRegression lrmodel = LogisticRegression.estimate(xs, cs, prior, annealingSchedule, null, minImprovement, minEpochs, maxEpochs);
		Serialization.serialize(lrmodel, modelFile);
	}
	
	public void divideTrainTest(Collection train, Collection test, double ratio) {
		int noTrain = (int)(size()*ratio);
		for(int i=0; i<noTrain; i++)
			train.add(get(i));
		for(int i=noTrain; i<size(); i++)
			test.add(get(i));
	}
	
	@SuppressWarnings("deprecation")
	public void createDMR(int noTopics, int iteration, int interval) throws Exception {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new TargetStringToFeatures());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));
		pipeList.add(new TokenSequenceLowercase());
		pipeList.add(new TokenSequenceRemoveStopwords(new File("data/util/stopwords.txt"), "UTF-8", false, false, false));
		pipeList.add(new TokenSequence2FeatureSequence());
		
		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		
		ArrayList<cc.mallet.types.Instance> instanceBuffer = new ArrayList<cc.mallet.types.Instance>();
		
		for(int i=0; i<size(); i++) {
			String doc = get(i).getDocument();
			String feature = get(i).getClassification();
			
			instanceBuffer.add(new cc.mallet.types.Instance(doc, feature, i+"", null));
		}
		
		instances.addThruPipe(instanceBuffer.iterator());
		
		DMRTopicModel model = new DMRTopicModel(noTopics);
		
		model.setTopicDisplay(interval, 50);
		model.setNumIterations(iteration);
		model.setOptimizeInterval(iteration/10);
		
		model.addInstances(instances);
		model.estimate();
		
		model.write(new File("model/mallet/dmr.model"));
		Serialization.serialize(instances, "model/mallet/instances.model");
		
		topics = new Topic[noTopics];
		IDSorter[] topicSortedWords = model.getSortedTopicWords(0);
		ArrayList<IDSorter> tsw = new ArrayList<IDSorter>();
		for(IDSorter ids : topicSortedWords)
			tsw.add(ids);
		for(int i=0; i<noTopics; i++)
			topics[i] = new Topic(i, model.getAlphabet(), tsw.iterator());
	}
	
	public void createLDA(int noTopics, int iteration) throws Exception {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));
		pipeList.add(new TokenSequenceRemoveStopwords(new File("data/util/stopwords.txt"), "UTF-8", false, false, false));
		pipeList.add(new TokenSequence2FeatureSequence());
		
		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		
		String[] array = new String[size()];
		for(int i=0; i<size(); i++)
			array[i] = get(i).getDocument();
		
		instances.addThruPipe(new StringArrayIterator(array));
		
		ParallelTopicModel model = new ParallelTopicModel(noTopics, 1.0, 0.01);
		
		model.addInstances(instances);
		model.setNumThreads(2);
		model.setNumIterations(iteration);
		model.estimate();
		
		model.write(new File("model/mallet/lda.model"));
		Serialization.serialize(instances, "model/mallet/instances.model");
		
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		topics = new Topic[noTopics];
		for(int i=0; i<noTopics; i++)
			topics[i] = new Topic(i, model.getAlphabet(), topicSortedWords.get(i).iterator());
	}
	
	public void createLingpipeSentimentModel() throws Exception {
		Vector<String> comment = new Vector<String>();
		Vector<String> polarity = new Vector<String>();
		for(int i=0; i<size(); i++) {
			Document doc = get(i);
			comment.add(doc.getDocument());
			polarity.add(doc.getClassification());
		}
		String[] mCategories = {"0.0", "1.0"};
		DynamicLMClassifier<NGramProcessLM> mClassifier = DynamicLMClassifier.createNGramProcess(mCategories, 8);
		for(int i=0; i<size(); i++) {
			String document = comment.get(i);
			Classification classification = new Classification("" + polarity.get(i));
			Classified<CharSequence> classified = new Classified<CharSequence>(document, classification);
			mClassifier.handle(classified);
		}
		FileOutputStream fos = new FileOutputStream("model/lingpipe/sentiment.model");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		mClassifier.compileTo(oos);
		oos.close();
		fos.close();
	}
	
	public Topic[] getTopics() {
		return topics;
	}

}
