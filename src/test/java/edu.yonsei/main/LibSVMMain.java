package edu.yonsei.test.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import edu.yonsei.classification.LibLinearSVM;
import edu.yonsei.classification.LingpipeLogisticRegression;
import edu.yonsei.util.Collection;

public class LibSVMMain {
	
	public static void main(String[] args) throws Exception {
		Scanner s1 = new Scanner(new FileReader("data/corpus/nytimes_news_articles.txt"));
		Scanner s2 = new Scanner(new FileReader("data/corpus/nytimes_news_json.txt"));
		
		List<String> documents = new ArrayList<String>();
		List<String> classes = new ArrayList<String>();
		
		int docCount = 0;
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
			System.out.println(section);
			if(!(section.equals("Fashion & Style") || section.equals("Technology"))) continue;
			
			System.out.println("doc " + docCount + ": " + section);
			
			documents.add(docText);
			classes.add(section);
			docCount++;
			
			if(docCount == 100) break;
		}
		System.out.println();
		
		s1.close();
		s2.close();
		
		Collection collection = new Collection(documents, classes);
		collection.preprocess();
		
		System.out.print("extracting features...");
		collection.getTfIdf();
		System.out.println("done");
		
		Collection train = new Collection(new ArrayList<String>(), new ArrayList<String>());
		Collection test = new Collection(new ArrayList<String>(), new ArrayList<String>());
		collection.divideTrainTest(train, test, 0.9);
		
		System.out.print("training svm...");
		train.svm("model/libsvm/svm.model");
		System.out.println("done");
		
		LibLinearSVM svm = new LibLinearSVM("model/libsvm/svm.model");
		for(int i=0; i<test.size(); i++) {
			String actual = test.get(i).getClassification();
			String predicted = svm.getClassification(collection.get(i));
			System.out.println("Document " + i + ": actual -> " + actual + ", predicted -> " + predicted);
		}
	}

}
