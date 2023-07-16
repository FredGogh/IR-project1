package utils;

/**
 * Created by GaoShuo on 2022/10/26
 */

public class Docs {
    private String title;       //题目
    private String author;      //作者
    private String time;        //时间
    private String institution;  //院校
    private String abstr;       //摘要
    private String keywords;    //关键词
    private String content;     //正文
    private String region;      //地区
    private String hl_title;    //高亮题目
    private String hl_content;    //高亮正文
    private String docURL;
    private String image;   //图片链接
    private int totalDocs;

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getHl_title() {
        return hl_title;
    }
    public void setHl_title(String hl_title) {
        this.hl_title = hl_title;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstr() {
        return abstr;
    }
    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }

    public String getHl_content() {
        return hl_content;
    }
    public void setHl_content(String hl_content) {
        this.hl_content = hl_content;
    }

    public String getDocURL() {
        return docURL;
    }
    public void setDocURL(String docURL){
        this.docURL = docURL;
    }

    public int getTotalDocs(){
        return totalDocs;
    }
    public void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
    }

    public Docs(String image, String title, String author, String time, String institution, String abstr, String keywords, String content, String hl_content, String hl_title, String docURL){
        super();
        this.title = title;
        this.content = content;
        this.author = author;
        this.time = time;
        this.abstr = abstr;
        this.institution = institution;
        this.keywords = keywords;
        this.hl_title = hl_title;
        this.hl_content = hl_content;
        this.docURL = docURL;
        this.image = image;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
