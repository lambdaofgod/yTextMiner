package edu.yonsei.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class TwitterDataCrawler {
	
	public static void main(String[] args) throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("1LBnwWfU94eNxefvuQdnAFDpn")
		  .setOAuthConsumerSecret("mUZnVo2EOSShVhIJYT2aTsGGFk365YeacSFftKmCmcw2TFP13z")
		  .setOAuthAccessToken("735483974252789761-8dSqqZR9X3iR6MK7xKCMqRzkkW3jeIh")
		  .setOAuthAccessTokenSecret("gdmHuBr3G38UVjDmPo8NLQVIR2UAABvUPJKegFmwWMGa9");
		final long start = System.currentTimeMillis();
		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				PrintWriter printer;
				long end = System.currentTimeMillis();
				if(end - start >= 3600000*4) System.exit(0);
				try {
					printer = new PrintWriter(new FileOutputStream("streamtweets.txt", true));
					String text = status.getText();
					if(text.startsWith("RT @")) {
						printer.close();
						return;
					}
					printer.println(text);
					System.out.println(text);
					printer.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);
		twitterStream.sample("en");
	}

}
