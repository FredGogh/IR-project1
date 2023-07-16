package search;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Highlight;
import utils.Index;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *      Created by GaoShuo on 2022/11/11
 */

//模糊查询
public class FuzzySearch {
    /**
     * 模糊查询
     * @param field     查询域
     * @param input     查询内容
     * @return          查询结果
     * @throws Exception
     */
    public static List<List<String>> FuzzySearch(String field, String input) throws Exception {
        //查询
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(field, input.toLowerCase()));
        TopDocs topDocs = indexSearcher.search(fuzzyQuery, 1000);
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

        return Highlight.highlight(input, fuzzyQuery, indexSearcher, scoreDocs, outputs);
    }


    public static void main(String[] args) throws Exception {
        List<List<String>> outputs = FuzzySearch("title","usa");
        System.out.println("查询到" + outputs.get(0).size() + "篇");
        Random random = new Random();
        int random_num = random.nextInt(outputs.get(0).size());
        System.out.println(outputs.get(1).get(random_num));
    }
}
