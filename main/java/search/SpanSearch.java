package search;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
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

//跨度查询
public class SpanSearch {
    /**
     * 跨度查询
     * @param field         查询域
     * @param input_start   起始内容
     * @param input_end     结束内容
     * @param slop          相隔位数
     * @param order         是否排序
     * @return              查询结果
     * @throws Exception
     */
    public static List<List<String>> SpanSearch(String field, String input_start, String input_end, int slop, boolean order) throws Exception {
        Directory directory = FSDirectory.open(Paths.get(Index.index_dir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        SpanQuery queryStart = new SpanTermQuery(new Term(field, input_start.toLowerCase()));
        SpanQuery queryEnd = new SpanTermQuery(new Term(field, input_end.toLowerCase()));
        SpanQuery spanNearQuery = new SpanNearQuery(new SpanQuery[]{queryStart, queryEnd}, slop, order);
        TopDocs topDocs = indexSearcher.search(spanNearQuery, 50);
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

        String input = input_start + "[a-zA-Z\\s]*" + input_end;
        return Highlight.highlight(input, spanNearQuery, indexSearcher, scoreDocs, outputs);
    }

    public static void main(String[] args) throws Exception {
        List<List<String>> outputs = SpanSearch("title","Words","dealing",2,false);
        Random random = new Random();
        int random_num = random.nextInt(outputs.get(0).size());
        System.out.println(outputs.get(8).get(random_num));
    }
}
