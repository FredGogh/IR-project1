package utils;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.search.suggest.SortedInputIterator;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 *      Created by GaoShuo on 2022/11/15
 */

//联想
public class Suggest {
    public static void suggest_index() throws IOException {
        RAMDirectory indexDir = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(indexDir, analyzer);

        // 创建索引,根据InputIterator的具体实现决定数据源以及创建索引的规则
//        suggester.build();
    }

    public static void suggest(AnalyzingInfixSuggester suggester, String field, String input){

    }

}
