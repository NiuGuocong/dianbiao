package models.poi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hfl on 2017-4-13.
 *
 * Excel文件中的Sheet对象
 *
 */
public class SheetModel {

    private String sheetName;//该sheet页名称
    private String txtName;//txt文件名
    private List<ExcelHeaderModel> excelHeaderModelList = new ArrayList<>();//该sheet表头
    private List<Map<String, Object>> datas= new ArrayList<>();//该sheet的结果集

    public SheetModel() {
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<ExcelHeaderModel> getExcelHeaderModelList() {
        return excelHeaderModelList;
    }

    public void setExcelHeaderModelList(List<ExcelHeaderModel> excelHeaderModelList) {
        this.excelHeaderModelList = excelHeaderModelList;
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        this.datas = datas;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }
}
