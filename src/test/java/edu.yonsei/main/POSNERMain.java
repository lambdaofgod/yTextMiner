package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;

import edu.yonsei.util.Sentence;
import edu.yonsei.util.Token;

public class POSNERMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		
		String url = s.nextLine();
		s.nextLine();
		while(true) {
			String text = s.nextLine();
			if(text.equals("")) break;
			
			Sentence sent = new Sentence(text);
			sent.preprocess();
			
			System.out.println("Sentence");
			System.out.println(sent.getSentence());
			System.out.println("Sentence with POS");
			for(Token token : sent)
				System.out.print(token.getToken() + "|" + token.getPOS() + " ");
			System.out.println();
			System.out.println();
		}
		
		s.close();
	}

}
