package edu.yonsei.test.main;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import edu.yonsei.util.Collection;
import edu.yonsei.util.Document;
import edu.yonsei.util.Sentence;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class SentimentLingpipeTrainMain {
	
	public static void main(String[] args) throws Exception {
		Vector<String> docs = new Vector<String>();
		Vector<String> classes = new Vector<String>();
		File dir = new File("data/util/txt_sentoken/neg");
		for(File file : dir.listFiles()) {
			if(file.isDirectory()) continue;
			Scanner s = new Scanner(file);
			String doc = "";
			while(s.hasNext())
				doc += s.nextLine() + " ";
			s.close();
			docs.add(doc);
			classes.add("0.0");
		}
		dir = new File("data/util/txt_sentoken/pos");
		for(File file : dir.listFiles()) {
			if(file.isDirectory()) continue;
			Scanner s = new Scanner(file);
			String doc = "";
			while(s.hasNext())
				doc += s.nextLine() + " ";
			s.close();
			docs.add(doc);
			classes.add("1.0");
		}
		Collection collection = new Collection(docs, classes);
		collection.createLingpipeSentimentModel();
		System.out.println("training end");
		for(int i=0; i<10; i++) {
			int idx = (int)(Math.random()*collection.size());
			Document doc = collection.get(idx);
			doc.setDocument(doc.getDocument().substring(0, 500));
			System.out.println("Document " + idx + ", Sentiment: " + doc.getClassification());
			doc.preprocess();
			for(int j=0; j<Math.min(doc.size(), 5); j++) {
				Sentence sent = doc.get(j);
				System.out.println("Sentence: " + sent.getSentence());
				System.out.println("Sentiment: " + sent.getSentimentLingpipe("model/lingpipe/sentiment.model"));
				System.out.println();
			}
		}
	}

}
