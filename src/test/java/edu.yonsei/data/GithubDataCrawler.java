package edu.yonsei.test.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class GithubDataCrawler {
	
	public static void main(String[] args) throws Exception {
		String[] queries = {"java", "python", "php", "javascript", "perl",
							"assembly", "ruby", "pascal", "swift", "matlab"};
		for(String query : queries) {
			String id = "24de45c1fced6e331091";
			String secret = "fada1bc2d4e8829dab867ff890ccd858504ad22c";
			for(int i=1; i<=10; i++) {
				System.out.println(query + " page " + i);
				PrintWriter printer = new PrintWriter(new FileOutputStream("github_url.txt", true));
				PrintWriter jsonprinter = new PrintWriter(new FileOutputStream("github_json.txt", true));
				try {
					String url = "https://api.github.com/search/repositories?"
							+ "client_id=" + id + "&client_secret=" + secret + "&"
							+ "q=" + query + "&sort=stars&order=desc&"
							+ "page=" + i + "&per_page=100";
					
					URL uri = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(uri.openStream());
					FileOutputStream fos = new FileOutputStream("temp.txt");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
					Scanner s = new Scanner(new File("temp.txt"));
					String line = "";
					while(s.hasNext()) line += s.nextLine();
					s.close();
					
					jsonprinter.println(line + "\n");
					
					String[] split = line.split("\"html_url\":");
					for(int j=2; j<split.length; j+=2) {
						String[] split2 = split[j].split(",");
						String data = split2[0].trim().substring(1, split2[0].length()-1);
						data = data.replaceAll("\\\\", "");
						printer.println(data);
						
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					break;
				}
				printer.close();
				jsonprinter.close();
			}
		}
	}

}
