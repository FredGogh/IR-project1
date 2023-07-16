package search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Highlight;
import utils.Index;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 *      Created by GaoShuo on 2022/11/15
 */

//多域查询
public class MultiSearch {

    /**
     * 普通多域查询
     * @param fields    查询域
     * @param input     查询内容
     * @return      查询结果
     * @throws Exception
     */
    public static List<List<String>> MultiSearch(String[] fields, String input) throws Exception {
        BooleanQuery.Builder multibuilder = new BooleanQuery.Builder();

        for(int i = 0;i < fields.length;i++){
            PhraseQuery.Builder querybuilder = new PhraseQuery.Builder();
            querybuilder.add(new Term(fields[i], input.toLowerCase()));
            PhraseQuery query = querybuilder.build();
            multibuilder.add(query, BooleanClause.Occur.SHOULD);
        }
        BooleanQuery multiquery = multibuilder.build();
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(multiquery, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<List<String>> outputs = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<String> keywordss = new ArrayList<>();
        List<String> abstracts = new ArrayList<>();
        List<String> regions = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<String> institutions = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        outputs.add(fileNames);
        outputs.add(titles);
        outputs.add(authors);
        outputs.add(keywordss);
        outputs.add(abstracts);
        outputs.add(regions);
        outputs.add(times);
        outputs.add(institutions);
        outputs.add(contents);

        return Highlight.highlight(input, multiquery, indexSearcher, scoreDocs, outputs);
    }


    /**
     * 加权多域查询
     * @param fields    查询域
     * @param weights   权重
     * @param input     查询内容
     * @return      查询结果
     * @throws Exception
     */
    public static List<List<String>> WeightedMultiSearch(String[] fields, Float[] weights, String input) throws Exception {
        if(fields.length != weights.length){
            System.out.println("输入错误");
            return null;
        }
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();
        Map<String, Float> map = new HashMap();
        for (int i = 0;i < fields.length;i++){
            map.put(fields[i],weights[i]);
        }
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer, map);
        Query query = queryParser.parse(input);
        TopDocs topDocs = indexSearcher.search(query, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        System.out.println(scoreDocs.length);
        List<List<String>> outputs = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<String> keywordss = new ArrayList<>();
        List<String> abstracts = new ArrayList<>();
        List<String> regions = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<String> institutions = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        outputs.add(fileNames);
        outputs.add(titles);
        outputs.add(authors);
        outputs.add(keywordss);
        outputs.add(abstracts);
        outputs.add(regions);
        outputs.add(times);
        outputs.add(institutions);
        outputs.add(contents);

        return Highlight.highlight(input, query, indexSearcher, scoreDocs, outputs);
    }


    public static void main(String[] args) throws Exception {
        List<List<String>> output1 = WeightedMultiSearch(new String[]{"title","region","institutions"},new Float[]{0.9f,0.05f,0.05f}, "USA");
        List<List<String>> output2 = MultiSearch(new String[]{"title","region","institutions"}, "Relaxation in Intralobar");
        System.out.println("查询到" + output1.get(0).size() + "篇");
        Random random = new Random();
        int random_num = random.nextInt(output1.get(0).size());
        System.out.println("filename1: "+output1.get(0).get(0));
        System.out.println("title1: "+output1.get(1).get(0));
        System.out.println("region1: "+output1.get(5).get(0));
        System.out.println("institutions1: "+output1.get(7).get(0));
        System.out.println("filename2: "+output2.get(0).get(0));
        System.out.println("title2: "+output2.get(1).get(0));
        System.out.println("region2: "+output2.get(5).get(0));
        System.out.println("institutions2: "+output2.get(7).get(0));
    }
}
