package app;

public class User {
	
	private String name;
	private String ip;
	private String channel;
	public User(String name, String ip, String channel) {
		super();
		this.name = name;
		this.ip = ip;
		this.channel = channel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
