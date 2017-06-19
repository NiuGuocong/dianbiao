package service.job;
import models.Snapshot;
import play.jobs.Job;
import play.jobs.On;

import java.text.ParseException;


/**
 * Created by hfl on 2017-4-17.
 *
 * 快照定时抓取
 *
 */


@On(" 0 5 0/1 * * ? ")
public class SnapshotJob extends Job {

    public void doJob() throws ParseException {
        Snapshot.task();
        System.out.println("执行一个快照定时任务");
    }

}
