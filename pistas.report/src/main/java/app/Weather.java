package app;

public class Weather {
	
	
	
	private String snow;
	private String wind;
	private int temperature;
	
	public Weather(String snow, String wind, int temperature) {
		super();
		this.snow = snow;
		this.wind = wind;
		this.temperature = temperature;
	}
	public String getSnow() {
		return snow;
	}
	public void setSnow(String snow) {
		this.snow = snow;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	
	

}
