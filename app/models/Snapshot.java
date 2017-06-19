package models;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.db.jpa.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hfl on 2017-4-17.
 *
 * 快照对象
 *
 */
@Entity
@Table(name = "jhw_snapshots")
public class Snapshot extends Model {


    @Column(name = "read_time")
    public Date readTime;

    //业务名称
    @Column(name = "read_hour")
    public String readHour;


    //业务名称
    @Column(name = "value")
    public double val;

    //业务名称
    @Column(name = "user_id")
    public String userId;

    @Column(name = "a")
    public double a;

    @Column(name = "b")
    public double b;


    @Column(name = "c")
    public double c;

    //查询快照信息
    public static List<Map<String,Object>> getALLInfo(){
        Query query = Model.em().createNativeQuery(QSQL);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //查询SQL
    public static String QSQL = "SELECT  " +
            "        NOW() AS read_time, " +
            "      date_format(NOW(), '%Y-%c-%d %H' ) as read_hour, " +
            "        t1.id as user_id," +
            "        max( CASE WHEN t2.meter_item_id = 1 THEN laster_read_value ELSE 0 END ) AS value, " +
            "        max( CASE WHEN t2.meter_item_id = 8 THEN laster_read_value ELSE 0 END ) AS a, " +
            "        max( CASE WHEN t2.meter_item_id = 9 THEN laster_read_value ELSE 0 END ) AS b, " +
            "        max( CASE WHEN t2.meter_item_id = 10 THEN laster_read_value ELSE 0 END ) AS c " +
            "    FROM" +
            "        user_info t1" +
            "    LEFT JOIN meter_item_data t2 ON t1.id = t2.meter_terminal_id " +
            "    LEFT JOIN meter_item t3 ON t2.meter_item_id = t3.id " +
            "    GROUP BY" +
            "        t1.id," +
            "        t1.serial_number," +
            "        t1.user_name";

    //根据条件删除SQL
    public static String DSQL = "delete from jhw_snapshots where read_hour = date_format(NOW(), '%Y-%c-%d %H' )";



    @Transactional
    public static void task() throws ParseException {
        List<Map<String,Object>> list = Snapshot.getALLInfo();
        if(list!=null && list.size()>0){
            int count = Model.em().createNativeQuery(DSQL).executeUpdate();//先删除此记录
            System.out.println("删除个数=="+count);
            for (Map<String,Object> map:list){
                Snapshot snapshots = new Snapshot();
                if(map.get("read_time")!=null){
                    Timestamp time = (Timestamp) map.get("read_time");
                    Date t = new Date(time.getTime());
                    System.out.println(time);
                    snapshots.readTime = new Date(time.getTime());
                }
                snapshots.readHour = (String)map.get("read_hour");
                snapshots.userId =  (String)map.get("user_id");
                snapshots.val =  (Double)map.get("value");
                snapshots.a =  (Double)map.get("a");
                snapshots.b =  (Double)map.get("b");
                snapshots.c =  (Double)map.get("c");
                snapshots.save();
                System.out.println("插入成功");
            }
        }
    }



}
