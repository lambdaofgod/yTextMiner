package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import edu.yonsei.util.Collection;
import edu.yonsei.util.Document;
import edu.yonsei.util.Topic;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LDAMain {
	
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
			if(count == 1000) break;
		}
		
		s.close();
		
		Collection collection = new Collection(list);
		collection.createLDA(10, 1000);
		
		for(int i=0; i<10; i++) {
			int idx = (int)(Math.random()*collection.size());
			Document document = collection.get(idx);
			System.out.println("Document: " + document.getDocument());
			Topic topic = document.getTopic("model/mallet/");
			System.out.println(topic);
		}
		
		System.out.println("TOPICS: ");
		Topic[] topics = collection.getTopics();
		for(int i=0; i<10; i++)
			System.out.println(topics[i]);
	}

}
