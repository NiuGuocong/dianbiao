package service;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.JPA;

import javax.persistence.Query;

/**
 * Created by hfl on 2017-6-15.
 */
public class TransferDataService {

    public void test(){
         String sql = "select * from jh_factory";
         Query query = JPA.em("other").createNativeQuery(sql);
         query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
         System.out.println(query.getResultList());
    }
}
