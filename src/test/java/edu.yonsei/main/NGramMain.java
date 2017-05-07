package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;

import edu.yonsei.util.Sentence;

public class NGramMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		
		String url = s.nextLine();
		s.nextLine();
		while(true) {
			String text = s.nextLine();
			if(text.equals("")) break;
			
			Sentence sent = new Sentence(text);
			sent.preprocess();

			int n = 2;
			
			System.out.println("Sentence");
			System.out.println(sent.getSentence());
			System.out.println(n + "-grams");
			System.out.println(sent.getNGrams(n));
			System.out.println();
		}
		
		s.close();
	}

}
