package service;

import models.poi.ExcelHeaderModel;
import models.poi.SheetModel;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.Play;
import play.db.jpa.JPA;
import util.common.DateUtil;
import util.poi.TXTUtil;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ReportService {


    /**
     * 电表快照
     *
     * @return
     */
    public static SheetModel loadSnapshot(Date beginDate, Date endDate) {
        String beginDateStr = DateUtil.dateToString(beginDate, "yyyy-MM-dd");
        String endDateStr = DateUtil.dateToString(endDate, "yyyy-MM-dd");
        SheetModel sheetModel = new SheetModel();
        sheetModel.setSheetName("电表快照");
        sheetModel.setTxtName("electricity_detail");
        sheetModel.setExcelHeaderModelList(new ArrayList<>());
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("laster_read_time", "抄读时间", 20));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("serial_number", "客户编号", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("meter_address", "表计地址"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("meter_type", "表计类型"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_1", "区域", 8));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_2", "公司", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_3", "楼层", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_4", "房间", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("user_name", "用户名", 10));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("home_address", "用户住址", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("beilv", "电量调整系数"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("total", "正向有功总电能"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("a", "A相电流", 10));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("b", "B相电流", 10));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("c", "C相电流", 10));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("aw", "A相有功功率(Kw)", 17));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("bw", "B相有功功率(Kw)", 17));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("cw", "C相有功功率(Kw)", 17));


        String snapshotSql = "select  " +
                " t3.read_hour as laster_read_time, " +
                " serial_number, " +
                " meter_address, " +
                " t2.text as meter_type, " +
                " level_1, " +
                " level_2, " +
                " level_3, " +
                " level_4, " +
                " user_name, " +
                " home_address, " +
                " beilv, " +
                " ifnull(t3.value,0) as total, " +
                " ifnull(t3.a,0) as a, " +
                " ifnull(t3.b,0) as b, " +
                " ifnull(t3.c,0) as c, " +
                " CONVERT(ifnull(t3.a,0) * 220 /1000,decimal(10,2)) as aw, " +
                " CONVERT(ifnull(t3.b,0) * 220 /1000,decimal(10,2)) as bw, " +
                " CONVERT(ifnull(t3.c,0) * 220 /1000,decimal(10,2)) as cw " +
                " from user_info t1 " +
                " left join meter_type t2 on t1.meter_type_id = t2.id " +
                " left join jhw_snapshots t3 on t1.id = t3.user_id" +
                " where t3.read_time>='%s' and t3.read_time<'%s'"+
                " order by t1.serial_number,t3.read_time";
        Query query = JPA.em().createNativeQuery(String.format(snapshotSql,beginDateStr,endDateStr));
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        sheetModel.setDatas(query.getResultList());

        return sheetModel;
    }

    /**
     * 每日电表账单
     *
     * @return
     */
    public static SheetModel loadDayData(Date beginDate, Date endDate) {
        String beginDateStr = DateUtil.dateToString(beginDate, "yyyy-MM-dd");
        String endDateStr = DateUtil.dateToString(endDate, "yyyy-MM-dd");
        String tableName = "user_datahour_" + DateUtil.dateToString(beginDate, "yyyyMM");

        SheetModel sheetModel = new SheetModel();
        sheetModel.setSheetName("每日电表账单");
        sheetModel.setTxtName("electricity_bill");
        sheetModel.setExcelHeaderModelList(new ArrayList<>());
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("read_date", "日期", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("serial_number", "客户编号", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("meter_address", "表计地址"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("meter_type", "表计类型"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_1", "区域", 8));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_2", "公司", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_3", "楼层", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("level_4", "房间", 5));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("user_name", "用户名", 10));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("home_address", "用户住址", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("beilv", "电量调整系数"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("kgzs", "开工指数", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("total_cha", "总用电量", 13));

        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t0_cha", "0-3时电量", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t0_price", "0-3时电价", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t0_amount", "0-3时总金额", 13));

        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t1_cha", "4-7时电量", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t1_price", "4-7时电价", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t1_amount", "4-7时总金额", 13));

        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t2_cha", "8-23时电量", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t2_price", "8-23时电价", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("t2_amount", "8-23时总金额", 13));

        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("total_amount", "总金额", 13));


        String sql = "select '%s' as read_date,serial_number," +
                " meter_address," +
                " t2.text as meter_type," +
                " level_1," +
                " level_2," +
                " level_3," +
                " level_4," +
                " user_name," +
                " home_address," +
                " beilv," +
                " t3.value as kgzs," +
                " CONVERT(ifnull(t4.total_cha,0),decimal(10,2)) as total_cha," +
                " CONVERT(ifnull(t4.t0_cha,0),decimal(10,2)) as t0_cha," +
                " CONVERT(ifnull(t4.t0_price,0),decimal(10,2)) as t0_price," +
                " CONVERT(ifnull(t4.t0_cha,0) * ifnull(t4.t0_price,0),decimal(10,2)) as t0_amount," +
                " CONVERT(ifnull(t4.t1_cha,0),decimal(10,2)) as t1_cha," +
                " CONVERT(ifnull(t4.t1_price,0),decimal(10,2)) as t1_price," +
                " CONVERT(ifnull(t4.t1_cha,0) * ifnull(t4.t1_price,0),decimal(10,2)) as t1_amount," +
                " CONVERT(ifnull(t4.t2_cha,0),decimal(10,2)) as t2_cha," +
                " CONVERT(ifnull(t4.t2_price,0),decimal(10,2)) as t2_price," +
                " CONVERT(ifnull(t4.t2_cha,0) * ifnull(t4.t2_price,0),decimal(10,2)) as t2_amount," +
                " CONVERT((ifnull(t4.t0_cha,0) * ifnull(t4.t0_price,0)) + (ifnull(t4.t1_cha,0) * ifnull(t4.t1_price,0)) + (ifnull(t4.t2_cha,0) * ifnull(t4.t2_price,0)),decimal(10,2)) as total_amount" +
                " from user_info t1" +
                " left join meter_type t2 on t1.meter_type_id = t2.id" +
                " left join jhw_kaigongzhishu t3 on t1.serial_number = t3.user_code" +
                " left join (" +
                " select t2.id," +
                " sum(ifnull(cha,0)) as total_cha," +
                " sum(case WHEN t5.type=0 then ifnull(t1.cha,0) else 0 end) as t0_cha," +
                " max(case WHEN t5.type=0 then t5.price else 0 end) as t0_price," +
                " sum(case WHEN t5.type=1 then ifnull(t1.cha,0) else 0 end) as t1_cha," +
                " max(case WHEN t5.type=1 then t5.price else 0 end) as t1_price," +
                " sum(case WHEN t5.type=2 then ifnull(t1.cha,0) else 0 end) as t2_cha," +
                " max(case WHEN t5.type=2 then t5.price else 0 end) as t2_price" +
                " from %s t1 " +
                " left join user_info t2 on t1.meter_terminal_id = t2.id" +
                " left join meter_item_data t3 on t1.meter_item_data_id = t3.id" +
                " left join meter_item t4 on t3.meter_item_id = t4.id" +
                " left join jhw_electric_charge t5 on t1.hour = t5.hour" +
                " where t1.read_time>='%s' and t1.read_time<'%s' and t4.id = 1" +
                " group by t2.id" +
                " ) t4 on t1.id = t4.id" +
                " order by t1.serial_number";

        Query dayQuery = JPA.em().createNativeQuery(String.format(sql, beginDateStr, tableName, beginDateStr, endDateStr));
        dayQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        sheetModel.setDatas(dayQuery.getResultList());
        return sheetModel;
    }

    /**
     *  电价参数表
     *
     * @return
     */
    public static SheetModel loadCharge() {
        SheetModel sheetModel = new SheetModel();
        sheetModel.setSheetName("电价参数表");
        sheetModel.setTxtName("electricity_rate");
        sheetModel.setExcelHeaderModelList(new ArrayList<>());
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("area", "区域", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("seasons", "季节", 9));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("hour", "小时"));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("price", "电价"));


        String sql = "select * from jhw_electric_charge order by area,hour";

        Query query = JPA.em().createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        sheetModel.setDatas(query.getResultList());
        return sheetModel;
    }

    /**
     *   报警信息
     *
     * @return
     */
    public static SheetModel loadWarn(Date beginDate, Date endDate) {
        String beginDateStr = DateUtil.dateToString(beginDate, "yyyy-MM-dd");
        String endDateStr = DateUtil.dateToString(endDate, "yyyy-MM-dd");
        String tableName = "user_datahour_" + DateUtil.dateToString(beginDate, "yyyyMM");

        SheetModel sheetModel = new SheetModel();
        sheetModel.setSheetName("报警信息");
        sheetModel.setTxtName("electricity_alarm");
        sheetModel.setExcelHeaderModelList(new ArrayList<>());
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("serial_number", "客户编号", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("user_name", "客户名称", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("warn_date", "时间", 13));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("warn_reason", "报警原因", 20));
        sheetModel.getExcelHeaderModelList().add(new ExcelHeaderModel("warn_reason", "报警原因分类", 20));


        String sql = "select t2.serial_number,t2.user_name,'%s' as warn_date,'无用电数据' warn_reason" +
                " from (" +
                " select t1.meter_terminal_id,sum(ifnull(cha,0)) cha" +
                " from %s t1" +
                " where read_time>='%s' and read_time <'%s'" +
                " group by t1.meter_terminal_id" +
                ") t1" +
                " Right join user_info t2 on t1.meter_terminal_id = t2.id" +
                " where t1.cha = 0 or t1.cha is null" +
                " order by t2.serial_number";

        Query query = JPA.em().createNativeQuery(String.format(sql, beginDateStr, tableName, beginDateStr, endDateStr));
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        sheetModel.setDatas(query.getResultList());
        return sheetModel;
    }


    public static  void  up(){

        String excelPath = Play.configuration.getProperty("export.excel.path");
        String ftpPath = Play.configuration.getProperty("ftp.path");

        Date endDate = DateUtil.getBeginOfDay(new Date());
        Date beginDate = DateUtil.getAfterDate(endDate, -1);

        SheetModel s1 = ReportService.loadSnapshot(beginDate,endDate);
        SheetModel s2 = ReportService.loadDayData(beginDate,endDate);
        SheetModel s3 = ReportService.loadCharge();
        SheetModel s4 = ReportService.loadWarn(beginDate,endDate);


        TXTUtil.up(s1);


    }
}
