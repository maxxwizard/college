import java.io.*;

public class FileWrite {
	BufferedWriter out;
	
	public FileWrite(String fileName)
	{
		// Create file
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out = new BufferedWriter(fstream);
	}
	
	public void writeAndClose(String s)
	{
		try {
			String[] lines = s.split("#");
			for (String l : lines)
			{
				out.write(l);
				out.newLine();
			}
			this.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void close()
	{
		try {
			out.close(); // Close the output stream
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}