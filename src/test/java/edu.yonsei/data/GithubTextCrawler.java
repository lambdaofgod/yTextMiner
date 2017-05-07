package edu.yonsei.test.data;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class GithubTextCrawler {

	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("github_url.txt"));
		while(s.hasNext()) {
			PrintWriter printer = new PrintWriter(new FileOutputStream("github_text.txt", true));
			String url = s.nextLine();
			printer.println("URL: " + url + "\n");
			try {
				System.out.println(url);
				Document doc = Jsoup.connect(url).get();
				String text = doc.getElementsByClass("markdown-body").text();
				printer.println(text);
			}
			catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			printer.println();
			printer.close();
		}
		s.close();
	}
	
}
