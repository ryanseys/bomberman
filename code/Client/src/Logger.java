import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Logger {
	private PrintWriter out;

	Logger(String filename) {
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void append(String line) {
		out.println(line);
		out.flush();
	}
}
