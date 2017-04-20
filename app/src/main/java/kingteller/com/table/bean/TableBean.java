package kingteller.com.table.bean;

/**
 * Created by Administrator on 17-3-29.
 */

public class TableBean {
    private String title;
    private String content;

    public TableBean() {
    }

    public TableBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
