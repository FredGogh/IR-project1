package utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by GaoShuo on 2022/10/26
 */

public class Index {
    public static final String index_dir = "D:\\codes\\information retrieval\\project1\\indexes";
    public static final String file_dir = "D:\\codes\\information retrieval\\project1\\files";

    //从xml文件中获取题目
    public static String getTitle(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        Element title = root.element("teiHeader").element("fileDesc").element("titleStmt").element("title");
        return title.asXML().replaceAll("<[^>]*>","");
    }

    //从xml文件中获取作者
    public static String getAuthor(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> author = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
        Iterator<?> iterator = author.iterator();
        String author_text = "";
        //遍历所有author标签
        while (iterator.hasNext()){
            Element next_author = (Element)iterator.next();
            Element persName = next_author.element("persName");
            if (persName != null) {
                //forename & middle name
                List<Element> forename = persName.elements("forename");
                for (int i = 0;i < forename.size();i++) {
                    author_text += forename.get(i).asXML().replaceAll("<[^>]*>", "") + " ";
                }
                //surname
                Element surname = persName.element("surname");
                if (surname != null) {
                    author_text += surname.asXML().replaceAll("<[^>]*>","") + ";";
                }
            }
        }
        if(!author_text.isEmpty()){
            return author_text;
        }else{
            return "unknown";
        }
    }


    //从xml文件中获取院校
    public static String getInstitution(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> authors = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
        Iterator<?> iterator = authors.iterator();
        String institution_text = "";
        //遍历所有author标签
        while (iterator.hasNext()){
            Element next_author = (Element)iterator.next();
            Element affiliation = next_author.element("affiliation");
            if (affiliation != null) {
                List<Element> org = affiliation.elements("orgName");
                if(org != null){
                    for(int i = 0;i < org.size();i++){
                        institution_text += org.get(i).asXML().replaceAll("<[^>]*>", "");
                        if(i != org.size() - 1){
                            institution_text +=  ",";
                        }
                    }
                    institution_text += "; ";
                }
            }
        }
        if(!institution_text.isEmpty()){
            return institution_text;
        }else{
            return "unknown";
        }
    }

    //从xml文件中获取时间
    public static String getTime(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        Element time = root.element("teiHeader").element("fileDesc").element("publicationStmt").element("date");
        if(time != null) {
            String time_text = time.asXML().replaceAll("<[^>]*>", "");
            return time_text;
        }else {
            return "unknown";
        }
    }

    //从xml文件中获取地区
    public static String getRegion(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> authors = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
        Iterator<?> iterator = authors.iterator();
        String region = "";
        //遍历所有author标签
        while(iterator.hasNext()){
            Element next_author = (Element)iterator.next();
            Element affiliation = next_author.element("affiliation");
            if(affiliation != null){
                Element address = affiliation.element("address");
                if(address != null){
                    if(address.element("settlement") != null){
                        region += address.element("settlement").getText() + ",";
                    }
                    if(address.element("region") != null){
                        region += address.element("region").getText() + ",";
                    }
                    if(address.element("country") != null){
                        region += address.element("country").getText() + ",";
                    }
                    if(!region.isEmpty()) {
                        region = region.substring(0, region.length() - 1);
                        region += ";";
                    }
                }
            }
        }
        if(region.length() != 0){
            return region;
        }else{
            return "unknown";
        }
    }

    //从xml文件中获取摘要
    public static String getAbstract(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        Element abstr = root.element("teiHeader").element("profileDesc").element("abstract");
        if(abstr != null){
            String abstr_text = "";
            abstr_text = abstr.asXML().replaceAll("<[^>]*>","");
            return abstr_text;
        }else{
            return "unknown";
        }
    }

    //从xml文件中获取关键词
    public static String getKeywords(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        Element keywords = root.element("teiHeader").element("profileDesc").element("textClass");
        String keywords_text = "";
        if(keywords != null) {
            List<Element> keywords_list = keywords.element("keywords").elements("term");
            for (int i = 0; i < keywords_list.size(); i++) {
                if (i != keywords_list.size() - 1) {
                    keywords_text += keywords_list.get(i).getText() + ",";
                } else {
                    keywords_text += keywords_list.get(i).getText();
                }
            }
        }
        if(keywords_text.length() != 0){
            return keywords_text;
        }else{
            return "unknown";
        }
    }

    //从xml文件中获取正文
    public static String getContent(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(file);
        Element root = document.getRootElement();
        Element texts = root.element("text").element("body");
        String texts_string = "";
        texts_string = texts_string + texts.asXML().replaceAll("<[^>]*>", "");
//        for(int i = 0;i < texts.size();i++){
//            texts_string += texts.get(i).element("head").attribute("n").getValue() + " " + texts.get(i).element("head").getText() + "\n";
//            texts_string += texts.get(i).elementText("p") + "\n";
//        }
//        String fullText = "";
//        if (fullText != null) {
//            Pattern p = Pattern.compile("\\t|\\n");
//            Matcher m = p.matcher(fullText);
//            fullText = m.replaceAll("");
//        }
//        return fullText;
        if(texts_string.length() != 0){
            return texts_string;
        }else{
            return "unknown";
        }
    }


    /**
     * 建立索引
     * @param dataPath      数据路径
     * @param indexPath     索引路径
     */
    public static void create_index(String dataPath,String indexPath){
        try {
            //创建IndexWriter对象
            Directory directory = FSDirectory.open(Paths.get(indexPath));           //存储路径
            Analyzer analyzer = new StandardAnalyzer();                             //分析器
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);  //IndexWriterConfig
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);            //IndexWriter

            //获得文件
            ArrayList<File> fileList = getFiles(dataPath);
            if(fileList.size()>0){
                for(int i = 0;i < fileList.size();i++){
                    File file = fileList.get(i);
                    System.out.println(i);
                    indexWriter.addDocument(fileToDocument(file));
                }
                System.out.println("index successfully created!");
                indexWriter.close();
            }else{
                System.out.println("no file found!");
                indexWriter.close();
            }
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 将文件存入document中
     * @param file      文件
     * @return          document
     * @throws IOException
     * @throws DocumentException
     */
    public static Document fileToDocument (File file) throws IOException, DocumentException {
        Document document = new Document();

        document.add(new StringField("fileName", file.getName(), Field.Store.YES));
//        System.out.println(file.getName());
        document.add(new StringField("filepath", file.getPath(), Field.Store.YES));
//        System.out.println(file.getPath());
        document.add(new TextField("title", getTitle(file), Field.Store.YES));
        System.out.println(getTitle(file));
        document.add(new TextField("author", getAuthor(file), Field.Store.YES));
//        System.out.println(getAuthor(file));
        document.add(new TextField("time", getTime(file), Field.Store.YES));
//        System.out.println(getTime(file));
        document.add(new TextField("institution", getInstitution(file), Field.Store.YES));
//        System.out.println(getInstitution(file));
        document.add(new TextField("abstract", getAbstract(file), Field.Store.YES));
        System.out.println(getAbstract(file));
        document.add(new TextField("content", getContent(file), Field.Store.YES));
//        System.out.println(getContent(file));
        document.add(new TextField("keywords", getKeywords(file), Field.Store.YES));
//        System.out.println(getKeywords(file));
        document.add(new TextField("region", getRegion(file), Field.Store.YES));
//        System.out.println(getRegion(file));

        return document;
    }

    //获取文件
    public static ArrayList<File> getFiles(String path) throws DocumentException {
        File file = new File(path);
        File[] files = file.listFiles();
        ArrayList<File> arrayList = new ArrayList(Arrays.asList(files));
//        try {
            Random random = new Random();
            int i = random.nextInt(files.length);
//            int i = 431;
//            int i = 942;
//            int i = 785;
//            System.out.println("i：" + i);
//            System.out.println("xml name：" + arrayList.get(i).getName());
//            System.out.println("title: "+getTitle(arrayList.get(i)));
//            System.out.println("author: "+getAuthor(arrayList.get(i)));
//            System.out.println("time: "+getTime(arrayList.get(i)));
//            System.out.println("institution: "+getInstitution(arrayList.get(i)));
//            System.out.println("region：" + getRegion(arrayList.get(i)));
//            System.out.println("abstract：" + getAbstract(arrayList.get(i)));
//            System.out.println("keywords：" + getKeywords(arrayList.get(i)));
//            System.out.println("content：" + getContent(arrayList.get(i)));
//        } catch (DocumentException e) {
//            throw new RuntimeException(e);
//        }
        return arrayList;
    }

    public static void main(String[] args) {
        Index index = new Index();
        try {
            index.create_index(file_dir,index_dir);
//            getFiles(file_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
