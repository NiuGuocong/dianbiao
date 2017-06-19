package util.poi;

import models.poi.ExcelHeaderModel;
import models.poi.SheetModel;
import play.Play;
import util.common.DateUtil;
import util.common.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hfl on 2017-4-18.
 * excel数据写入到txt工具类
 */
public class TXTUtil {

    static String excelPath = Play.configuration.getProperty("export.excel.path");
    static String ftpPath = Play.configuration.getProperty("ftp.path");


    /**
     * 数据写入到本地的方法
     *
     * @param filePath   文件目录如: "/JHuaW/JRongB/shouxinjieduan/history_data"
     * @param sheetModel
     */
    public static void write(String filePath, SheetModel sheetModel) {

        List<ExcelHeaderModel> excelHeaderModelList = sheetModel.getExcelHeaderModelList();   //表头
        int len = sheetModel.getExcelHeaderModelList().size();  //列数
        List<Map<String, Object>> datas = sheetModel.getDatas(); //每页sheet数据集合
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            if (excelHeaderModelList != null && len > 0) {

                //File file = createFile("G:/excel",sheetModel.getTxtName());
                File file = createFile(filePath, sheetModel.getTxtName());
                fw = new FileWriter(file.getAbsoluteFile());
                bw = new BufferedWriter(fw);

                ExcelHeaderModel model = null;
                //遍历每条数据
                for (int j = 0; j < datas.size(); j++) {
                    StringBuilder builder = new StringBuilder();
                    Map<String, Object> data = datas.get(j);  //每条数据
                    for (int i = 0; i < len; i++) {
                        model = excelHeaderModelList.get(i);
                        String content = model.getColumn();  //每个字段名称
                        String val = data.get(content) != null ? StringUtil.initValue(data.get(content)) : "null";   //每个单元格数据
                        if (null == val || "".equals(val.trim()) || "null".equals(val.trim())) { //空数据用空格代替
                            builder.append(" ");
                        } else {
                            builder.append(val);
                        }
                        if (i < len - 1) builder.append("@@@");//最后一个单元格数据后面不用加分隔符号
                    }
                    String strLine = builder.toString();
                    bw.write(strLine);
                    bw.newLine(); //换行
                }
            }
            bw.close();
            System.out.println("导出txt完成");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //本地新建文件夹和文件
    public static File createFile(String filePath, String name) throws IOException {
        File file = new File(filePath + File.separator + name + ".txt");
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) fileParent.mkdirs(); //没有文件目录就创建
        file.createNewFile();
        return file;
    }


    //上传方法
    public static void up(SheetModel s) {

        try {
            TXTUtil.write(excelPath, s);

            String datePath = DateUtil.dateToString(DateUtil.getAfterDate(new Date(), -1), "yyyy-MM-dd");
            String originfilename = excelPath + s.getTxtName() + ".txt";

            String upPath = ftpPath + datePath + "/";  //拼接ftp上的文件夹
            String pathFile = upPath + s.getTxtName() + ".txt"; //ftp上的文件夹和文件名
            //判断是否存在，如果存在就先删除
            try {
                FTPUtil.remove(pathFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //上传
            FTPUtil.createDir(upPath);
            FTPUtil.transfer(pathFile, originfilename);
        } catch (Exception e) {
            System.out.println("任务没有执行成功");
        }
    }


}
