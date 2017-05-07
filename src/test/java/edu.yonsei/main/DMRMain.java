package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.yonsei.topic.MalletDMR;
import edu.yonsei.util.Collection;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class DMRMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s1 = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		Scanner s2 = new Scanner(new FileReader("data/corpus/nytimes_news_json.txt"));
		
		List<String> documents = new ArrayList<String>();
		List<String> classes = new ArrayList<String>();
		
		int docCount = 0;
		while(s1.hasNext()) {
			String json = s2.nextLine();
			s2.nextLine();
			String[] dates = json.split("\"pub_date\":\"");
			
			for(int i=1; i<dates.length; i++) {
				String url = s1.nextLine();
				s1.nextLine();
				
				String docText = "";
				while(s1.hasNext()) {
					String text = s1.nextLine();
					if(text.isEmpty()) break;
					docText += text + " ";
				}
				
				if(docText.length() > 1000)
					docText = docText.substring(0, 1000);
				
				docCount++;
				documents.add(docText);
				
				String pubDate = dates[i].split("T")[0];
				classes.add(pubDate);
				
				System.out.println(pubDate + " " + docText);
				
				if(docCount == 5000) break;
			}
		
			if(docCount == 5000) break;
		}
		System.out.println();
		
		s1.close();
		s2.close();
		
		Collection collection = new Collection(documents, classes);
		collection.createDMR(10, 500, 100);
		
		MalletDMR dmr = new MalletDMR("model/mallet/");
		dmr.printParameters("parameters.txt");
		dmr.printTopics("topics.txt");
	}

}
