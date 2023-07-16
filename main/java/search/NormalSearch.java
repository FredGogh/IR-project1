package search;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Highlight;
import utils.Index;
import utils.StopWordsFilter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *      Created by GaoShuo on 2022/11/6
 */


//普通查询
public class NormalSearch {

    /**
     * 在全部搜索域上进行普通查询
     * @param input     查询内容
     * @return          查询结果
     * @throws Exception
     */
    public static List<List<String>> NormalSearch_all(String input) throws Exception{
        //查询
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        BooleanQuery.Builder multi_builer = new BooleanQuery.Builder();
        //查询结果
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

        List<String> fields = Arrays.asList("fileName","title","author","keywords","abstract","region","time","institution","content");
        for(int j = 1;j < fields.size();j++){
            PhraseQuery.Builder querybuilder = new PhraseQuery.Builder();
            List<String> queries = StopWordsFilter.StopWordsFilter(fields.get(j),input);
            for(int i = 0;i < queries.size();i++){
                for(int k = 0;k < queries.size();k++){
                    querybuilder.add(new Term(fields.get(j),queries.get(k)));
                }
            }
            PhraseQuery phraseQuery = querybuilder.build();
            multi_builer.add(phraseQuery, BooleanClause.Occur.SHOULD);
        }
        BooleanQuery booleanQuery = multi_builer.build();
        TopDocs topDocs = indexSearcher.search(booleanQuery, 1000);
        ScoreDoc[] scoreDocs =topDocs.scoreDocs;
        System.out.println(scoreDocs.length);   //查询数量
        //文本高亮
//        for(int i = 0;i < scoreDocs.length;i++){
//            for(int j = 0;j < fields.size();j++){
//                String field = fields.get(j);
//                int doc = scoreDocs[i].doc;
//                Document document = indexSearcher.doc(doc);
//                //输出文件名
//                if(j == 0){
//                    outputs.get(0).add(document.get("fileName"));
////                    System.out.println("field: fileName");
//                    continue;
//                }
//                //拆分文本
//                Fragmenter fragmenter = new SimpleFragmenter(200);
//                TokenStream tokenStream = (new StandardAnalyzer()).tokenStream(field, new StringReader(document.get(field)));
//                QueryScorer fragmentScore = new QueryScorer(booleanQuery);
//                if(field == "title"){
//                    //前端输出为蓝色
//                    formatter = new SimpleHTMLFormatter("</font><font color='red'>", "</font><font color='blue'>");
//                }else{
//                    formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
//                }
//                Highlighter highlighter = new Highlighter(formatter, fragmentScore);
//                highlighter.setTextFragmenter(fragmenter);
//                String highlight_string = highlighter.getBestFragment(tokenStream, document.get(field));
////                System.out.println(highlight_string);
//                //highlight_string为null的情况
//                if(highlight_string == null){
//                    //查询所在位置
//                    int term_position = document.get(field).toLowerCase().indexOf(query[0].toString());
//                    //如果该搜索域无查询文本
//                    if(term_position == -1){
//                        outputs.get(j).add(document.get(field));
////                        System.out.println("field: "+field);
//                        continue;
//                    }else{
//                        //查询内容长度
//                        int term_length = query[0].toString().length();
//                        //搜索域原文长度
//                        int content_length = document.get(field).length();
//                        if (content_length < term_position + 201) {
//                            //动态摘要超出文本长度
//                            String real_highlight_string = document.get(field).substring(term_position, content_length - 1);
//                            //关键词在动态摘要中的位置
//                            int term_start_position = real_highlight_string.toLowerCase().indexOf(query[0].toString());
//                            highlight_string = "<font color='red'>" + real_highlight_string.substring(term_start_position, term_length)
//                                    + "</font>" + real_highlight_string.substring(term_length);
//                        } else {
//                            //动态摘要未超出文本长度
//                            String real_highlight_string = document.get(field).substring(term_position, term_position + 200);
//                            int term_start_position = real_highlight_string.toLowerCase().indexOf(query[0].toString());
//                            highlight_string = "<font color='red'>" + real_highlight_string.substring(term_start_position, term_length)
//                                    + "</font>" + real_highlight_string.substring(term_length);
//                        }
//                    }
//                }
//                //输出结果
//                if(field == "title"){
//                    //将标题中的相应部分显示为高亮
//                    outputs.get(j).add("<font color='blue'>" + highlight_string + "</font>");
////                    System.out.println("input from field:title\tfield: "+field);
//                }else if(field == "author"){
//                    //将作者中的相应部分显示为高亮
//                    outputs.get(j).add(highlight_string);
////                    System.out.println("input from field:author\tfield: "+field);
//                }else if(field == "content"){
//                    //将正文中的相应部分显示为高亮
//                    outputs.get(j).add(highlight_string);
////                    System.out.println("input from field:content\tfield: "+field);
//                }else{
//                    outputs.get(j).add(document.get(field));
////                    System.out.println("input from field:else\tfield: "+field);
//                }
//            }
//        }

        return Highlight.highlight(input, booleanQuery, indexSearcher, scoreDocs, outputs);
    }

    /**
     * 在特定搜索域上进行普通查询
     * @param field     搜索域
     * @param input     搜索内容
     * @return          搜索结果
     * @throws Exception
     */
    public static List<List<String>> NormalSearch_specific(String field, String input) throws Exception{
        //查询
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        PhraseQuery.Builder querybuilder = new PhraseQuery.Builder();
//        String[] query = input.split(" ");
        List<String> query = StopWordsFilter.StopWordsFilter(field, input);
        for (int i = 0;i < query.size();i++){
            querybuilder.add(new Term(field,query.get(i)));
            System.out.println(query.get(i));
        }
        PhraseQuery phraseQuery = querybuilder.build();
        TopDocs topDocs = indexSearcher.search(phraseQuery, 1000);
        ScoreDoc[] scoreDocs =topDocs.scoreDocs;
        System.out.println(scoreDocs.length);

        //查询结果
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

        //输出
        outputs.add(fileNames);
        outputs.add(titles);
        outputs.add(authors);
        outputs.add(keywordss);
        outputs.add(abstracts);
        outputs.add(regions);
        outputs.add(times);
        outputs.add(institutions);
        outputs.add(contents);
        return Highlight.highlight(input, phraseQuery, indexSearcher,scoreDocs, outputs);
//        return outputs;
    }

    /**
     * 普通查询
     * @param field     搜索域
     * @param input     搜索内容
     * @return          搜索结果
     * @throws Exception
     */
    public static List<List<String>> NormalSearch(String field, String input) throws Exception{
        /**
         * 普通查询
         *             Args:
         *                 field:搜索域
         *                 input:搜索内容
         */
        if(field.equals("all")){
            //在全部搜索域上查询
            System.out.println("all");
            return NormalSearch_all(input.toLowerCase());
        }else{
            //在特定搜索域上查询
            System.out.println("specific");
            return NormalSearch_specific(field,input.toLowerCase());
        }
    }


    public static void main(String[] args) throws Exception {
        List<List<String>> output = NormalSearch("all","Science");
        Random random = new Random();
        int random_num = random.nextInt(output.get(0).size());
        System.out.println(random_num);
        System.out.println("filename: "+output.get(0).get(0));
        System.out.println("title: "+output.get(1).get(0));
        System.out.println("author: "+output.get(2).get(0));
        System.out.println("institution: "+output.get(7).get(0));
        System.out.println("region: "+output.get(5).get(0));
        System.out.println("content: "+output.get(8).get(0));
//        for (int i = 0;i < output.get(0).size();i++){
//            System.out.println("title: "+output.get(1).get(i));
//        }
    }
}
