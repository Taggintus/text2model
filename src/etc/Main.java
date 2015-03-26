package etc;

import java.io.FileNotFoundException;

public class Main {
	
	private static Test tt = new Test();
	private static String text = "The postman delivers the letter and then steals them. Afterwards he got fired.";
	
	public static void main(String[] args) throws FileNotFoundException{
		tt.testing(text);
	}

}
