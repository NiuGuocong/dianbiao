package service.job;
import models.poi.SheetModel;
import play.jobs.Job;
import play.jobs.On;
import service.ReportService;
import util.common.DateUtil;
import util.poi.TXTUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by hfl on 2017-4-18.
 *
 * 向ftp上传递数据
 *
 */
@On(" 0 10 0 1/1 * ? ")
public class FTPJob  extends Job{


    public void doJob() throws ParseException {
        up();
        System.out.println("执行一个上传FTP定时任务");
    }


    public static  void  up(){

        Date endDate = DateUtil.getBeginOfDay(new Date());
        Date beginDate = DateUtil.getAfterDate(endDate, -1);

        SheetModel s1 = ReportService.loadSnapshot(beginDate,endDate);
        SheetModel s2 = ReportService.loadDayData(beginDate,endDate);
        SheetModel s3 = ReportService.loadCharge();
        SheetModel s4 = ReportService.loadWarn(beginDate,endDate);

        TXTUtil.up(s1);
        TXTUtil.up(s2);
        TXTUtil.up(s3);
        TXTUtil.up(s4);
    }
}
