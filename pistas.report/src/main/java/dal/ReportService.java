package dal;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import app.Piste;
import app.User;
import app.Weather;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ReportService {
	
	private DBCollection coll;
	
	
	
	public ReportService(DBCollection coll) {
		super();
		this.coll = coll;
	}


	public boolean save(User user, Piste piste, Weather weather) throws UnknownHostException{
		
		boolean ok = true;
		
		BasicDBObject report = new BasicDBObject();
		
		report.put("reporter", user.getName());
		report.put("date", new Date());
		
		BasicDBObject location = new BasicDBObject();
		location.put("coordinates", new double[]{piste.getLon(), piste.getLat()  });
		location.put("name", piste.getName());
		location.put("country", piste.getCountry());
		
		report.put("location", location);
		
		BasicDBObject conditions = new BasicDBObject();
		conditions.put("snow", weather.getSnow());
		conditions.put("wind", weather.getWind());
		conditions.put("temperature", weather.getTemperature());
		
		report.put("conditions", conditions);
		 
		
		coll.insert(report);
		
		
		return ok;
		
		
	}
	

	public DBCursor getClosestPistes(double longitude, double latitude, Date date) throws ParseException{
		DBObject fields = new BasicDBObject();
		//if we do not which to show _id we need to exclude it otherwise it will be presented by default
		fields.put("_id", 0);
		fields.put("conditions", 1);
		fields.put("location.name", 1);
		
		//near part 
		DBObject near = new BasicDBObject();
		near.put("$near", new double[]{ longitude, latitude });
		
		DBObject gte = new BasicDBObject("$gte", date);
		
		DBObject filter = new BasicDBObject();
		filter.put("location.coordinates", near);
		filter.put("date", gte);
		
		
		DBObject orderBy = new BasicDBObject();
		orderBy.put("location.temperature", -1);
		
		return coll.find(filter, fields).sort(orderBy);
		
	}
	
	
	public DBObject accurateReport( String reporter ){
		
		DBObject query = new BasicDBObject("reporter", reporter);
		DBObject accuracy = new BasicDBObject("accuracy", 1);
		DBObject update = new BasicDBObject( "$inc" , accuracy );
		
		return coll.findAndModify(query, update);
		
	}
	
	public DBObject skiablePistes(int minTemperature, Date day ) throws Exception{
		
		DBObject matchTemperature = new BasicDBObject("$match", new BasicDBObject( "conditions.temperature", new BasicDBObject("$gte", minTemperature  )  ) );
		DBObject matchWind = new BasicDBObject( "$match", new BasicDBObject("wind", "moderado") );
		DBObject groupBy = new BasicDBObject("_id", "$conditions.snow");
		groupBy.put("sum", new BasicDBObject( "$sum", 1 ) );
		DBObject group = new BasicDBObject("$group",  groupBy  );
		AggregationOutput ouptut = coll.aggregate(matchWind, matchTemperature, group);
		if (!ouptut.getCommandResult().ok()){
			throw new Exception(ouptut.getCommandResult().getErrorMessage());
		}
		return (DBObject) ouptut.getCommandResult().get("result");
		
	}
	
	
}
