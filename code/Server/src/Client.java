import java.net.InetAddress;

public class Client {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [IPaddr=" + IPaddr + ", port=" + port + ", id=" + id
				+ "]";
	}
	private InetAddress IPaddr;
	private int port;
	private int id;

	public Client(InetAddress IP, int port, boolean isPlayer) {
		this.IPaddr = IP;
		this.port = port;
		this.id = -1;
	}
	public Client(InetAddress IP, int port, int id, boolean isPlayer) {
		this.IPaddr = IP;
		this.port = port;
		this.id = id;
	}

	/**
	 * @return the iPaddr
	 */
	public InetAddress getIPaddr() {
		return IPaddr;
	}

	/**
	 * @param iPaddr the iPaddr to set
	 */
	public void setIPaddr(InetAddress iPaddr) {
		IPaddr = iPaddr;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
