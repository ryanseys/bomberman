import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Logger {
	private String filename;

	Logger(String filename) {
		this.filename = filename;
	}

	public void append(String line) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
		    out.println(line);
		}
		catch (IOException e) {
			// fail
		}
	}
}
