package controllers;

import models.poi.SheetModel;
import play.Play;
import play.mvc.Controller;
import service.ReportService;
import util.common.DateUtil;
import util.poi.ExportExcelUtil;
import util.poi.FTPUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class SyncCtrl extends Controller {


    /**
     * 电表数据同步测试
     */
    public static void testSync() {
        String excelPath = Play.configuration.getProperty("export.excel.path");
        String ftpPath = Play.configuration.getProperty("ftp.path");
        String fileName = "OrderData";
        Date endDate = DateUtil.getBeginOfDay(new Date());
        Date beginDate = DateUtil.getAfterDate(endDate, -1);

        fileName = fileName + DateUtil.dateToString(beginDate, "yyyyMMdd");

        List<SheetModel> sheetModelList = new ArrayList<>();
        sheetModelList.add(ReportService.loadSnapshot(beginDate,endDate));
        sheetModelList.add(ReportService.loadDayData(beginDate,endDate));
        sheetModelList.add(ReportService.loadCharge());
        sheetModelList.add(ReportService.loadWarn(beginDate,endDate));

        ExportExcelUtil.exportExcelMore(sheetModelList, fileName, excelPath);

        FTPUtil.transfer(ftpPath + fileName + ".xlsx", excelPath + fileName + ".xlsx");
        renderJSON(sheetModelList);
    }
}
