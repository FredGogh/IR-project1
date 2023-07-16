package search;

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
import java.util.Random;

/**
 * Created by GaoShuo on 2022/11/11
 */

/**
 *  TODO:1.确定前端最后输出内容，删除outputs中多余的返回属性以提高搜索速度
 *       2.完善多种搜索在全部搜索域上的搜索（可尝试封装）
 *       3.处理停用词查询不到问题
 */

public class WildCardSearch extends Index {

    /**
     * 通配符查询
     * @param field     查询域
     * @param input     查询内容
     * @return          查询结果
     * @throws Exception
     */
    public static List<List<String>> WildCardSearch(String field, String input) throws Exception{
        //查询
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        WildcardQuery wildcardQuery = new WildcardQuery(new Term(field, input.toLowerCase()));
        TopDocs topDocs = indexSearcher.search(wildcardQuery, 1000);
        ScoreDoc[] scoreDocs =topDocs.scoreDocs;
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

        //配合正则表达式进行文本高亮
        return Highlight.highlight(input, wildcardQuery, indexSearcher, scoreDocs, outputs);
    }

    public static void main(String[] args){
        try {
            List<List<String>> output = WildCardSearch("title","g*d");
            System.out.println("查询到" + output.get(0).size() + "篇");
            Random random = new Random();
            int random_num = random.nextInt(output.get(0).size());
            System.out.println(output.get(8).get(random_num));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
