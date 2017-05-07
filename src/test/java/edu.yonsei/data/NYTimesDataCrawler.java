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
public class NYTimesDataCrawler {

	public static void main(String[] args) throws Exception {
		int day = 30, month = 6, year = 2016;
		System.out.println(day);
		int count = 0;
		int page = 4;
		int[] days = {31,29,31,30,31,30,31,31,30,31,30,31};
		while(true) {
			if(year == 0) break;
			for(int i=page; i<100; i++) {
				PrintWriter printer = new PrintWriter(new FileOutputStream("data/corpus/nytimes_news_url.txt", true));
				PrintWriter jsonprinter = new PrintWriter(new FileOutputStream("data/corpus/nytimes_news_json.txt", true));
				try {
					System.out.println("page " + i + " of " + month + "/" + day);

					String url = "https://api.nytimes.com";
					url += "/svc/search/v2/articlesearch.json?";
					// c1e42d50a88a4d09a8b175b7bfe61d19
					// bdeff9a845cd4482a71779d04544be2e
					// 4c5f45e3d5a945dfa5b2e814299d49bf
					url += "api-key=bdeff9a845cd4482a71779d04544be2e&";
					url += "end_date=" + year + "" + (month<10?"0":"") + month + "" + (day<10?"0":"") + day + "&";
					url += "begin_date=" + year + "" + (month<10?"0":"") + month + "" + (day<10?"0":"") + day + "&";
					url += "fq=type_of_material:(\"News\")+AND+source:(\"The+New+York+Times\")&";
					url += "page=" + i;
					
					URL uri = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(uri.openStream());
					FileOutputStream fos = new FileOutputStream("data/temp.txt");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
					Scanner s = new Scanner(new File("data/temp.txt"));
					String line = "";
					while(s.hasNext()) line += s.nextLine();
					s.close();
					count++;
					System.out.println("COUNT: " + count);
					
					if(line.contains("\"docs\":[]")) break;
					
					jsonprinter.println(line + "\n");
					
					String[] split = line.split("\"web_url\":");
					for(int j=1; j<split.length; j++) {
						String[] split2 = split[j].split(",");
						String data = split2[0].trim().substring(1, split2[0].length()-1);
						data = data.replaceAll("\\\\", "");
						if(data.contains("aponline")) continue;
						if(data.contains("reuters")) continue;
						printer.println(data);
						System.out.println(data);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					year = 0;
					break;
				}
				printer.close();
				jsonprinter.close();
			}
			page = 0;
			day--;
			if(day == 0) {
				month--;
				if(month == 0) {
					month = 12;
					year--;
				}
				day = days[month-1];
			}
		}
	}

}
