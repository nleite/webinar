package dal;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.Piste;
import app.User;
import app.Weather;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ReportServiceTest {

	MongoClient m;
	DB db;
	
	@Before
	public void setUp() throws Exception{
		m = new MongoClient("nair.local");
		m.dropDatabase("esqui");
		db = m.getDB("esqui");
		
	}
	
	@After
	public void tearClassDown() throws Exception{
//		db.dropDatabase();
	}
	
	
	@Test
	public void testSave() throws UnknownHostException {
		User user = new User("wingman", "192.168.1.1", "facebook");
		Piste piste = new Piste("formigal", -0.364158, 42.774031, "ESP");
		Weather weather = new Weather("polvo", "moderate", -2);
		
		
		DBCollection coll = db.getCollection("informes");
		ReportService service = new ReportService(coll);
		
		boolean expected = true;
		boolean actual = service.save(user, piste, weather);
		
		assertEquals(expected, actual);
		
		
		
		BasicDBObject report2 = (BasicDBObject) coll.findOne();
		
		System.out.println(report2);
		
	}

	
	public void testGetClosestPistes() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = sdf.parse("2013/02/12 15:00:00");
		long longitude = (long) -0.364158;
		long latitude = (long) 42.774031;
		DBCollection coll = db.getCollection("informes");
		ReportService service = new ReportService(coll);
		boolean actual = service.getClosestPistes(longitude, latitude, now).count() > 0;
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testAccuracy(){
		DBCollection coll = db.getCollection("informes");
		BasicDBObject test = new BasicDBObject("reporter", "wingman");
		
		
		coll.insert(test);
		
		ReportService service = new ReportService(coll);
		String name = "wingman";
		DBObject result = service.accurateReport(name);
		
		System.out.println(result);
		int actual = (Integer) coll.findOne(test).get("accuracy");
		int expected = 1;
		
		assertEquals(expected, actual);
		
		
	}
	
}
