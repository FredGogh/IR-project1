package utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.ArrayList;
import java.util.List;

public class StopWordsFilter {
    /**
     * 去停用词
     * @param str       输入内容
     * @return          对应词项列表
     * @throws Exception
     */
    public static List StopWordsFilter(String field, String str) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream(field, str);
        CharTermAttribute charTermAttribute = (CharTermAttribute)tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        List<String> queryList = new ArrayList();

        while(tokenStream.incrementToken()) {
            queryList.add(charTermAttribute.toString());
        }

        tokenStream.close();
        return queryList;
    }
}
