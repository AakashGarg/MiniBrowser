package browser;

import java.net.*;
import java.io.*;

public class URLConnectionReader{
	static String inputLine = "";
	
	public static void main(String... s) throws IOException{
		URL google = new URL("http://www.google.com");
		URLConnection gc = google.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(gc.getInputStream()));
		while(inputLine!= null)
		{	inputLine = br.readLine();
			System.out.println(inputLine);
		}
		br.close();
	}
}