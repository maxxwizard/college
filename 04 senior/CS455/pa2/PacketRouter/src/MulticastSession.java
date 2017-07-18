import java.util.*;

public class MulticastSession
{
	int address; /* this session's address */
	int expirationTime; /* after this time passes, the multicast session will be destroyed */
	Integer source; /* address of the session starter */
	HashSet<Integer> members; /* list of addresses of the session's members */
	
	public MulticastSession(int address, int expirationTime, int source)
	{
		this.address = address;
		this.expirationTime = expirationTime;
		this.source = Integer.valueOf(source);
		members = new HashSet<Integer>();
		addMember(source);
	}
	
	public void addMember(int addr)
	{
		members.add(Integer.valueOf(addr));
	}
	
	public void removeMember(int addr)
	{
		members.remove(Integer.valueOf(addr));
	}
	
}
