package util.poi;

import models.poi.ExcelHeaderModel;
import models.poi.SheetModel;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by hfl on 2017-4-10.
 *
 * 数据库表导出到本地文件工具类
 */
public class ExportExcelUtil {


    /**
     * 多个sheet页导出
     *
     * @param sheetModelList
     * @param exportExcelName
     * @param targetPath
     */
    public static void exportExcelMore(List<SheetModel> sheetModelList, String exportExcelName, String targetPath) {

        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 生成表格中非标题栏的样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);//背景色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成表格中非标题栏的字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName("Microsoft Yahei");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 设置表格标题栏的样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置标题栏字体
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBold(true);
        titleFont.setColor(HSSFColor.WHITE.index);
        titleFont.setFontName("Microsoft Yahei");
        // 把字体应用到当前的样式
        titleStyle.setFont(titleFont);

        //遍历添加sheet名 标题头 数据
        for (int i = 0, len = sheetModelList.size(); i < len; i++) {
            SheetModel sheetmodel = sheetModelList.get(i);
            String sheetName = sheetmodel.getSheetName();
            List<ExcelHeaderModel> headerList = sheetmodel.getExcelHeaderModelList();
            List<Map<String, Object>> dataList = sheetmodel.getDatas();
            // 生成一个表格
            XSSFSheet sheet = workbook.createSheet(sheetName);

            // 产生表格标题行
            XSSFRow row = sheet.createRow(0);
            for (short j = 0; j < headerList.size(); j++) {
                ExcelHeaderModel excelHeaderModel = headerList.get(j);
                XSSFCell cell = row.createCell(j);
                cell.setCellStyle(titleStyle);
                XSSFRichTextString text = new XSSFRichTextString(excelHeaderModel.getTitle());
                cell.setCellValue(text);
                sheet.setColumnWidth(j, excelHeaderModel.getWidth() * 256);
            }
            Iterator<Map<String, Object>> it = dataList.iterator();
            int index = 0;
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                Map<String, Object> data = it.next();
                int k = 0;
                String value = null;

                for (short j = 0; j < headerList.size(); j++) {
                    ExcelHeaderModel excelHeaderModel = headerList.get(j);
                    for (String key : data.keySet()) {
                        XSSFCell cell = row.createCell(k);
                        if (excelHeaderModel.getColumn().equalsIgnoreCase(key)) { //标题和数据对应的条件判断
                            value = data.get(key) + "";
                            cell.setCellStyle(style);
                            XSSFRichTextString text = new XSSFRichTextString("null".equalsIgnoreCase(value) ? "" : value);
                            cell.setCellValue(text);
                            break;
                        }
                    }
                    k++;
                }
            }
        }

        OutputStream out = null;
        try {
            String tmpPath = targetPath + exportExcelName + ".xlsx";
            out = new FileOutputStream(tmpPath);
            workbook.write(out);
            System.out.println("数据库表导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
