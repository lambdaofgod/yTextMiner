package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;

import edu.yonsei.util.Document;
import edu.yonsei.util.Sentence;

public class ParsingMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/github_text.txt"));
		
		String url = s.nextLine();
		s.nextLine();
		while(true) {
			String text = s.nextLine();
			if(text.startsWith("URL:")) break;
			if(text.isEmpty()) continue;
			
			Document doc = new Document(text);
			doc.preprocess();
			
			for(int i=0; i<doc.size(); i++) {
				Sentence sent = doc.get(i);
				
				System.out.println("Sentence " + (i+1));
				System.out.println(sent.getSentence());
				System.out.println("Dependencies");
				for(String dependency : sent.getDependencies())
					System.out.println(dependency);
				System.out.println();
			}
		}
		
		s.close();
	}

}
