package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.Scanner;

import edu.yonsei.util.Sentence;
import edu.yonsei.util.Token;

public class StemAndStopMain {

	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/twitter_stream.txt"));
		
		for(int i=0; i<10; i++) {
			String text = s.nextLine();
				
			Sentence sent = new Sentence(text);
			sent.preprocess();

			System.out.println("Sentence # " + (i+1));
			System.out.println(sent.getSentence());
			System.out.println("After stemming");
			for(Token token : sent)
				System.out.print(token.getStem() + " "); 
			System.out.println();
			System.out.println("After lemmatization");
			for(Token token : sent)
				System.out.print(token.getLemma() + " ");
			System.out.println();
			System.out.println();
		}
		
		s.close();
	}

}
