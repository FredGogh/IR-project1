package search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Highlight;
import utils.Index;
import utils.StopWordsFilter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

/**
 * Created by GaoShuo on 2022/10/31
 */

//布尔查询
public class BooleanSearch extends Index {
    //查询语句解析
//    //input example:"address:USA and author:Gary or not title:data"
//    public static String[][] query_parser(String query_text) throws Exception{
//        String[][] query = null;
//        String[] query_words = query_text.split(" ");
//        List<Integer> and_or_pos = null;
//        for (int i = 0;i < query_words.length; i++){
//            if(query_words[i].equalsIgnoreCase("and") || query_words[i].equalsIgnoreCase("or")){
//                and_or_pos.add(i);
//            }
//        }
//        return query;
//    }

    /**
     * 将逻辑项加入查询中
     * @param single_query      单个逻辑项查询内容
     * @param booleanBuilder    总querybuilder
     * @throws Exception
     */
    public static void query_input(List<String> single_query, BooleanQuery.Builder booleanBuilder) throws Exception {
        BooleanClause.Occur occur = BooleanClause.Occur.MUST;
        PhraseQuery.Builder querybuilder = new PhraseQuery.Builder();
        System.out.println(single_query);
        //布尔逻辑关系
        if(single_query.contains("[or]")){
            occur = BooleanClause.Occur.SHOULD;
            single_query.remove("[or]");
        }else if(single_query.contains("[and]")){
            occur = BooleanClause.Occur.MUST;
            single_query.remove("[and]");
        }else if(single_query.contains("[not]")){
            occur = BooleanClause.Occur.MUST_NOT;
            single_query.remove("[not]");
        }
//        System.out.println(single_query);
        String new_single_query = "";
        for(int i = 0;i < single_query.size();i++){
            new_single_query += single_query.get(i) + " ";
        }
        new_single_query = new_single_query.substring(0, new_single_query.length() - 1);
//        System.out.println("new query:" + new_single_query);
        String field = new_single_query.split(":")[0];
//        System.out.println(field);
        List<String> value = StopWordsFilter.StopWordsFilter(field,new_single_query.split(":")[1]);
        System.out.println("value: "+value.toString());
        for(int i = 0;i < value.size();i++){
            querybuilder.add(new Term(field,value.get(i)));
        }
        PhraseQuery query = querybuilder.build();
        booleanBuilder.add(new BooleanClause(query, occur));
//        //查询域与查询值
//        for(int i = 0;i < single_query.size();i++) {
////            if (single_query.get(i).matches("\\w*:[\\w\\s]*")) {
////                System.out.println("true");
//                querybuilder.add(new Term(field,value));
//                PhraseQuery query = querybuilder.build();
//                booleanBuilder.add(new BooleanClause(query, occur));
//                return;
////                TermQuery termQuery = new TermQuery(new Term(field, value));
////                BooleanClause booleanClause = new BooleanClause(termQuery, occur);
////                querybuilder.add(termQuery);
//            }else {
//                System.out.println("输入错误!");
//            }
//        }
    }


    /**
     * 布尔查询
     * @param input     查询内容
     * @return          查询结果
     * @throws Exception
     */
    public static List<List<String>> BooleanSearch(String input) throws Exception {
        /**
         * 布尔查询
         *             Args:
         *                 input:搜索内容
         *                          格式：[and] xxx [and] xxx [not] xxx
         */
        BooleanQuery.Builder booleanBuilder = new BooleanQuery.Builder();
        String[] query_words = input.toLowerCase().split(" ");
        List<String> single_query = new ArrayList<>();

        //查询
        for(int i = 0;i < query_words.length;i++){
            System.out.println(query_words[i]);
            if(!single_query.isEmpty()){
                if(query_words[i].equalsIgnoreCase("[and]") || query_words[i].equalsIgnoreCase("[or]") || query_words[i].equalsIgnoreCase("[not]") ){
                    //加入查询
                    query_input(single_query,booleanBuilder);
                    //清空查询
                    single_query.clear();
                    single_query.add(query_words[i]);
                    continue;
                }else{
                    single_query.add(query_words[i]);
                }
            }else{
                single_query.add(query_words[i]);
            }

        }
        if(!single_query.isEmpty()){
            query_input(single_query,booleanBuilder);
        }

        BooleanQuery booleanQuery = booleanBuilder.build();
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(booleanQuery, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //输出结果
        //title:book [and]
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
        System.out.println(scoreDocs.length);
//        for(int i = 0;i < scoreDocs.length;i++){
//            int doc = scoreDocs[i].doc;
//            Document document =indexSearcher.doc(doc);
//            fileNames.add(document.get("fileName"));
//            titles.add(document.get("title"));
//            authors.add(document.get("author"));
//            keywordss.add(document.get("keywords"));
//            abstracts.add(document.get("abstract"));
//            regions.add(document.get("region"));
//            times.add(document.get("time"));
//            institutions.add(document.get("institution"));
//            contents.add(document.get("content"));
//        }
        outputs.add(fileNames);
        outputs.add(titles);
        outputs.add(authors);
        outputs.add(keywordss);
        outputs.add(abstracts);
        outputs.add(regions);
        outputs.add(times);
        outputs.add(institutions);
        outputs.add(contents);

        //处理输入字符串：去除not条件的查询和条件标签[]
        ArrayList<String> reduced_inputs = new ArrayList<>(List.of(input.split("\\[")));
        String reduced_input = "";
        //去除not条件
        for(int i = 0;i < reduced_inputs.size();i++){
            if (reduced_inputs.get(i).startsWith("not")){
                reduced_inputs.remove(i);
            }
        }
        //去除条件标签[]
        for(int i = 0;i < reduced_inputs.size();i++){
            reduced_input += reduced_inputs.get(i).substring(reduced_inputs.get(i).indexOf(":")+1);
        }

        System.out.println(reduced_input);
        return Highlight.highlight(reduced_input, booleanQuery, indexSearcher, scoreDocs, outputs);
//        return outputs;
    }



    public static void main(String[] args){
        try {
            List<List<String>> output = BooleanSearch("[and] title:in Intralobar");
            if(output.get(0).size() != 0) {
                System.out.println("查询到" + output.get(0).size() + "篇");
                for(int i = 0;i < output.get(0).size();i++){
                    System.out.println(output.get(1).get(i));
                }
            }else {
                System.out.println("无相关结果");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
