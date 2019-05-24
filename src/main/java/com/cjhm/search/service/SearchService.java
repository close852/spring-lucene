package com.cjhm.search.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
@Service
public class SearchService {

	String idxDir = "D:\\git\\repository\\lucenedbms";
	Analyzer analyzer = new StandardAnalyzer();
	
	public List<Document> search(String queryString) throws IOException, ParseException {
		File file = new File(idxDir);
		Directory directory = FSDirectory.open(file.toPath());

		List<Document> docList = new ArrayList<>();
		String field="title";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(file.toPath()));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		QueryParser parser = new QueryParser(field, analyzer);
		parser.setAllowLeadingWildcard(true);
		Query query = parser.parse(queryString+"*");
		
		TopDocs hits = searcher.search(query, 10);
		System.out.println("hits.scoreDocs : "+hits.scoreDocs.length);
		for(ScoreDoc sDoc : hits.scoreDocs) {
			Document _doc = searcher.doc(sDoc.doc);
			docList.add(_doc);
		}
		directory.close();
//		analyzer.close();
		return docList;
	}
	public List<Document> search(String field,String queryString) throws IOException, ParseException {
		File file = new File(idxDir);
		Directory directory = FSDirectory.open(file.toPath());
		
		List<Document> docList = new ArrayList<>();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(file.toPath()));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		QueryParser parser = new QueryParser(field.toLowerCase(), analyzer);
		parser.setAllowLeadingWildcard(true);
		Query query = parser.parse(queryString+"*");
		
		TopDocs hits = searcher.search(query, 10);
		System.out.println("hits.scoreDocs : "+hits.scoreDocs.length);
		for(ScoreDoc sDoc : hits.scoreDocs) {
			Document _doc = searcher.doc(sDoc.doc);
			docList.add(_doc);
		}
		directory.close();
//		analyzer.close();
		return docList;
	}
	public <T> void  indexing(List<T> list) throws IOException, IllegalArgumentException, IllegalAccessException {
		File file = new File(idxDir);
		Directory directory = FSDirectory.open(file.toPath());

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, indexWriterConfig);

//		writer.deleteAll();

		for (T t : list) {
			Document doc = new Document();
			List<Field> fields = getAllFields(t.getClass());
			for(Field f : fields) {
				String fieldId = f.getName();
				doc.add(new TextField(fieldId.toLowerCase(), getData(t, f), Store.YES));
			}
//			System.out.println("doc 출력 : " + doc);
			writer.addDocument(doc);
		}
		System.out.println("DBMS Indexing - adding Total Count : " + list.size());
		writer.commit();
		writer.close();
		directory.close();
	}
	
	private <T>String getData(T t,Field field) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		Object o = field.get(t);
//		System.out.println(o);
		return nullable(o);
	}

	private <T> List<Field> getAllFields(Class clazz) {
		List<Field> fields = new ArrayList<Field>();

		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		Class superClazz = clazz.getSuperclass();
		if (superClazz != null) {
			fields.addAll(getAllFields(superClazz));
		}

		return fields;
	}

	private static String nullable(Object content) {
		return content == null ? "" : String.valueOf(content);
	}

}
