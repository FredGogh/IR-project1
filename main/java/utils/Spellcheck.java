package utils;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Index;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.join;

/**
 *      Created by GaoShuo on 2022/11/15
 */
public class Spellcheck {
    public static String words_index_dir = "D:\\codes\\information retrieval\\project1\\words_indexes";
    public static String words_dir = "D:\\codes\\information retrieval\\project1\\words.txt";
    public static int suggestionsNumber = 5;

    //构建字典

    /**
     * 构建字典
     * 统计文集中的所有单词
     * @throws Exception
     */
    public static void build_dictionary() throws Exception {
        ArrayList<File> fileList = Index.getFiles(Index.file_dir);

        //使用集合去重
        Set text_set = new HashSet();
        if (fileList.size() > 0){
            System.out.println("start processing...");
            for (int i = 0;i < fileList.size();i++){
                System.out.println("file " + i);
                File file = fileList.get(i);
                String fulltext = "";
                fulltext = Index.getAbstract(file) + Index.getContent(file) + Index.getTitle(file);

                String[] fulltexts = fulltext.split(" ");

//                //去重
//                List list = Arrays.asList(fulltexts);
//                Set text_set = new HashSet(list);
//                fulltexts =(String [])text_set.toArray(new String[0]);

                for(int j = 0; j < fulltexts.length; j++){
                    if(!fulltexts[j].startsWith("\\-") && fulltexts[j].matches("[a-zA-Z]*") && fulltexts[j].length() > 1){
//                        real_text += "\n" + fulltexts[j];
//                        System.out.println(fulltexts[j]);
                        text_set.add(fulltexts[j].toLowerCase());
                    }
                }
            }
            System.out.println("finish getting files.");
        }

        System.out.println("start writing...");
        FileWriter fileWriter = new FileWriter(words_dir);
        String[] real_texts =(String [])text_set.toArray(new String[0]);
        for(int i = 0;i < real_texts.length;i++){
            fileWriter.write(real_texts[i] + "\n");

        }
        System.out.println("finish writing.");
    }


    /**
     * 拼写检查
     * @param input     输入内容
     * @return          纠错建议（若拼写正确则返回null）
     * @throws Exception
     */
    public static String[] suggest(String input) throws Exception{
        //检查拼写是否正确
        File file = new File(words_dir);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuffer stringBuffer = new StringBuffer();
        String text = null;
        while((text = bufferedReader.readLine()) != null){
            stringBuffer.append(text);
        }
        if(stringBuffer.toString().contains(input.toLowerCase())){
            //拼写正确
            System.out.println("spell right");
           return null;
        }else {
            System.out.println("spell wrong");
            Directory directory = FSDirectory.open(Paths.get(words_index_dir));
            SpellChecker spellChecker = new SpellChecker(directory);
            //建立索引
//            spellChecker.indexDictionary(new PlainTextDictionary(Path.of(words_dir)),new IndexWriterConfig(new StandardAnalyzer()),true);
            //查询
            String[] suggestions = spellChecker.suggestSimilar(input, suggestionsNumber);
            System.out.println("3");
            if (suggestions!=null && suggestions.length>0) {
                for (String word : suggestions) {
                    System.out.println("did you mean:" + word);
                }
            } else {
                System.out.println("no suggestion of this word:"+input);
            }

            return suggestions;
        }
    }

    public static void main(String[] args) throws Exception {
        String[] suggestions = suggest("abandan");
    }
}
