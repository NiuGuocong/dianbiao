package models.poi;

/**
 * Created by hfl on 2017-4-12.
 *
 * Excel某列表头对象
 *
 */
public class ExcelHeaderModel {

    private String column;//表头某列对应的字段
    private String title; //表头某列的标题
    private Integer width;

    public ExcelHeaderModel() {
    }

    public ExcelHeaderModel(String column, String title) {
        this.column = column;
        this.title = title;
        this.width = 15;
    }

    public ExcelHeaderModel(String column, String title,Integer width) {
        this.column = column;
        this.title = title;
        this.width = width;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
