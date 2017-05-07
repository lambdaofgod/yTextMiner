package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import edu.yonsei.util.Collection;
import edu.yonsei.util.Document;
import edu.yonsei.util.Sentence;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class SentimentCoreNLPMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		Vector<String> list = new Vector<String>();
		int count = 0;
		while(s.hasNext()) {
			s.nextLine();
			s.nextLine();
			String text = "";
			while(s.hasNext()) {
				String line = s.nextLine();
				if(line.isEmpty()) break;
				text += line + " ";
			}
			list.add(text);
			count++;
			if(count == 10) break;
		}
		s.close();
		System.out.println("starting sentiment analysis");
		Collection collection = new Collection(list);
		for(int i=0; i<collection.size(); i++) {
			Document document = collection.get(i);
			document.preprocess();
			System.out.println("Document " + i);
			for(int j=0; j<document.size(); j++) {
				Sentence sentence = document.get(j);
				System.out.println("Sentence: " + sentence.getSentence());
				System.out.println("Sentiment (CoreNLP): " + sentence.getSentimentCoreNLP()); // 0 - negative, 4 - positive
				System.out.println();
			}
		}
		System.exit(0);
	}

}
