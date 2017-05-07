package edu.yonsei.test.data;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class NYTimesArticleCrawler {
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/nytimes_news_url.txt"));
		while(s.hasNext()) {
			PrintWriter printer = new PrintWriter(new FileOutputStream("data/corpus/nytimes_news_articles_2.txt", true));
			String url = s.nextLine();
			printer.println("URL: " + url + "\n");
			if(url.contains("aponline")) continue;
			if(url.contains("reuters")) continue;
			if(url.contains("legacy")) continue;
			while(true) {
				try {
					System.out.println(url);
					Document doc = Jsoup.connect(url).get();
					Elements texts = doc.getElementsByClass("story-body-text");
					for(Element text : texts)
						printer.println(text.text());					
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
				break;
			}
			printer.println();
			printer.close();
		}
		s.close();
	}

}
