package edu.yonsei.test.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import edu.yonsei.classification.feature.TFIDF;
import edu.yonsei.util.Collection;

public class TFIDFMain {
	
	public TFIDFMain()
	{
	}
	
	public void writeResults(String fileName) throws Exception
	{
		Scanner s1 = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		Scanner s2 = new Scanner(new FileReader("data/corpus/nytimes_news_json.txt"));
		
		List<String> documents = new ArrayList<String>();
		List<String> classes = new ArrayList<String>();
		
		TreeMap<String, Integer> classIndexMap = new TreeMap<String, Integer>();
		TreeMap<Integer, String> indexClassMap = new TreeMap<Integer, String>();
		int docCount = 0, classCount = 0;
		while(s1.hasNext()) {
			String url = s1.nextLine();
			s1.nextLine();
			
			String docText = "";
			while(s1.hasNext()) {
				String text = s1.nextLine();
				if(text.isEmpty()) break;
				docText += text + " ";
			}
			
			if(docText.length() > 100)
				docText = docText.substring(0, 100);
			
			String json = s2.nextLine();
			s2.nextLine();
			
			String section = json.split("\"section_name\":\"")[1].split("\"")[0];
			if(!classIndexMap.containsKey(section)) {
				classIndexMap.put(section, classCount);
				indexClassMap.put(classCount++, section);
			}
			int sectionIndex = classIndexMap.get(section);
			
			System.out.println("doc " + docCount + ": " + section);
			
			documents.add(docText);
			classes.add(sectionIndex+"");
			docCount++;
			
			if(docCount == 100) break;
		}
		System.out.println();
		
		s1.close();
		s2.close();
		
		System.out.println("classes: " + classIndexMap);
		
		Collection collection = new Collection(documents, classes);
		collection.preprocess();
		
		System.out.print("extracting features...");
		collection.getTfIdf();
		System.out.println("done");
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			
			TFIDF tfidf = collection.getTfIdf();
			
			double[][] matrix = tfidf.getTfIdfMatrix();
			String[] wordVector = tfidf.getWordVector();
			for(int i=0; i<docCount; i++) {
				System.out.printf("%-10s", "doc " + i);
				writer.print(collection.getClassLabel(i) + " ");
				 
				for(int j=0; j < matrix[0].length; ++j) {
					if (j != matrix[0].length-1) {
						writer.print(j + ":" + matrix[i][j] + " ");
					} else {
						writer.print(j + ":" + matrix[i][j]);
					}
				}
				
				if (i < docCount-1) {
					writer.println();
				}
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		
		List<String> documents = new ArrayList<String>();
		int docCount = 0;
		while(s.hasNext()) {
			String url = s.nextLine();
			s.nextLine();
			
			String docText = "";
			while(s.hasNext()) {
				String text = s.nextLine();
				if(text.isEmpty()) break;
				docText += text + " ";
			}
			
			if(docText.length() > 500)
				docText = docText.substring(0, 500);
			
			System.out.println("doc " + docCount + ": " + url);
			
			documents.add(docText);
			docCount++;
			
			if(docCount == 10) break;
		}
		System.out.println();
		
		Collection collection = new Collection(documents);
		collection.preprocess();
		TFIDF tfidf = collection.getTfIdf();
		TFIDFMain main = new TFIDFMain();
		main.writeResults("tfidf_matrix.txt");
		
		double[][] matrix = tfidf.getTfIdfMatrix();
		String[] wordVector = tfidf.getWordVector();
		
		System.out.print("         ");
		for(int i=0; i<100; i+=10)
			System.out.printf("%11s", wordVector[i]);
		System.out.println();
		
		for(int i=0; i<docCount; i++) {
			System.out.printf("%-10s", "doc " + i);
			for(int j=0; j<100; j+=10)
				System.out.printf("%.8f ", matrix[i][j]);
			System.out.println();
		}
		
		s.close();
	}

}
