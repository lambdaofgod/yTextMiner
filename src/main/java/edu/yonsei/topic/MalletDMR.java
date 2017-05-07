package edu.yonsei.topic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Vector;

import cc.mallet.topics.DMRTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import edu.yonsei.util.Collection;
import edu.yonsei.util.Document;
import edu.yonsei.util.Pair;
import edu.yonsei.util.Serialization;
import edu.yonsei.util.Topic;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class MalletDMR {
	
	private DMRTopicModel model;
	
	public MalletDMR(Collection collection, int noTopics, int iteration, int interval) throws Exception {
		collection.createDMR(noTopics, iteration, interval);
		model = (DMRTopicModel) DMRTopicModel.read(new File("model/mallet/dmr.model"));
	}
	
	public MalletDMR(String folder) throws Exception {
		model = (DMRTopicModel) DMRTopicModel.read(new File(folder + "/dmr.model"));
	}
	
	public void printParameters(String file) throws Exception {
		model.writeParameters(new File(file));
	}
	
	public void printTopics(String file) throws Exception {
		model.printTopWords(new File(file), 20, false);
	}

}
