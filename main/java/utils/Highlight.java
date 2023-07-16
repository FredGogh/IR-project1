package utils;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class Highlight {
    private static Formatter formatter;

    /**
     * 动态摘要和文本高亮
     * @param input             查询内容
     * @param query             查询
     * @param indexSearcher     索引查询器
     * @param scoreDocs         结果文档
     * @param outputs           高亮前的输出结果
     * @return                  高亮后的输出结果
     * @throws Exception
     */
    public static List<List<String>> highlight(String input, Query query, IndexSearcher indexSearcher, ScoreDoc[] scoreDocs , List<List<String>> outputs) throws Exception{

        System.out.println("start highlighting...");
        List<String> fields = Arrays.asList("fileName","title","author","keywords","abstract","region","time","institution","content");
        //文本高亮
        for(int i = 0;i < scoreDocs.length;i++){
            int doc = scoreDocs[i].doc;
            Document document = indexSearcher.doc(doc);
//            System.out.println(scoreDocs[i].score);
//            System.out.println("document"+i+":");
            for(int j = 0;j < fields.size();j++){
                String field = fields.get(j);
//                System.out.println("j: "+field);
                //输出文件名
                if(j == 0){
                    outputs.get(0).add(document.get("fileName"));
//                    System.out.println("field: "+field);
                    continue;
                }
                //拆分文本
                Fragmenter fragmenter = new SimpleFragmenter(400);
                TokenStream tokenStream = (new StandardAnalyzer()).tokenStream(field, new StringReader(document.get(field)));
                QueryScorer fragmentScore = new QueryScorer(query);
                if(field == "title"){
                    //前端输出为蓝色
                    formatter = new SimpleHTMLFormatter("</font><font color='red'>", "</font><font color='blue'>");
                }else{
                    formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
                }
                Highlighter highlighter = new Highlighter(formatter, fragmentScore);
                highlighter.setTextFragmenter(fragmenter);
                String highlight_string = highlighter.getBestFragment(tokenStream, document.get(field));
//                System.out.println(highlight_string);
                //highlight_string为null的情况
                if(highlight_string == null){
                    //查询所在位置
                    int term_position = document.get(field).toLowerCase().indexOf(input);
//                    System.out.println("term_position: "  + term_position);
                    //如果该搜索域无查询文本
                    if(term_position == -1){
                        outputs.get(j).add(document.get(field));
//                        System.out.println("field: "+field);
                        continue;
                    }else{
                        //查询内容长度
                        int term_length = input.length();
//                        System.out.println("term_length: "  + term_length);
//                        System.out.println(input);
                        //搜索域原文长度
                        int content_length = document.get(field).length();
//                        System.out.println("content_length: "  + content_length);
                        if (content_length < term_position + 401) {
                            //动态摘要超出文本长度
                            //动态摘要文本
                            String dynamic_abstract = document.get(field).substring(term_position, content_length);
//                            System.out.println("dynamic_abstract: "+dynamic_abstract.length());
                            //关键词在动态摘要中的位置
                            int term_start_position = dynamic_abstract.toLowerCase().indexOf(input);
                            highlight_string = "<font color='red'>" + dynamic_abstract.substring(term_start_position, term_length)
                                    + "</font>" + dynamic_abstract.substring(term_length);
                        } else {
                            //动态摘要未超出文本长度
                            String real_highlight_string = document.get(field).substring(term_position, term_position + 200);
                            //查询在动态摘要中的位置
                            int term_start_position = real_highlight_string.toLowerCase().indexOf(input);
                            highlight_string = "<font color='red'>" + real_highlight_string.substring(term_start_position, term_length)
                                    + "</font>" + real_highlight_string.substring(term_length);
                        }
                    }
                }
                //输出结果
                switch (field){
                    case "title":
                    {
                        outputs.get(j).add("<font color='blue'>" + highlight_string + "</font>");
                        break;
                    }
                    case "author":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                    case "content":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                    case "region":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                    case "time":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                    case "institution":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }case "keywords":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                    case "abstract":
                    {
                        outputs.get(j).add(highlight_string);
                        break;
                    }
                }
            }
        }
        System.out.println("end highlighting.");
        return outputs;
    }
}
