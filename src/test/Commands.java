package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Commands {

	// Default IO interface
	public interface DefaultIO {
		public String readText();

		public void write(String text);

		public float readVal();

		public void write(float val);

		// you may add default methods here

		default void upLoadFile(String csvFile) {

		}

		default List<String> downLoadS() {
			List<String> lines = new ArrayList<>();
			while (true) {
				String line = new String(this.readText());
				if (line.equals("done")) {
					break;
				}
				lines.add(line);
			}
			return lines;
		}

		default TimeSeries downLoadFileTS(String csvFileName) {

			boolean flag = true;
			try {
				PrintWriter file = new PrintWriter(csvFileName);
				while (flag) {
					String str = this.readText();
					String str2 = new String("done").intern();
					String str3 = str.intern();
					if (str3 == str2) {
						flag = false;
						break;
					}
					file.print(str);
					file.print("\n");
				}
				file.close();


			} catch (IOException e) {
				e.printStackTrace();
			}
			TimeSeries anomaly = new TimeSeries(csvFileName);
			return anomaly;
		}

	}

	// the default IO to be used in all commands
	DefaultIO dio;

	public Commands(DefaultIO dio) {
		this.dio = dio;
	}

	// you may add other helper classes here


	// the shared state of all commands
	private class SharedState {
		// implement here whatever you need
		TimeSeries train;
		TimeSeries test;
		SimpleAnomalyDetector sad = new SimpleAnomalyDetector();

	}

	private SharedState sharedState = new SharedState();


	// Command abstract class
	public abstract class Command {
		protected String description;

		public Command(String description) {
			this.description = description;
		}

		public abstract void execute();
	}

	// Command class for example:
	public class ExampleCommand extends Command {

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}

	public class CommandNumber1 extends Command {

		public CommandNumber1() {
			super("upload a time series csv file");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.");
			dio.write("\n");
			sharedState.train = dio.downLoadFileTS("anomalyTrain.csv");
			dio.write("Upload complete.");
			dio.write("\n");
			dio.write("Please upload your local test CSV file.");
			dio.write("\n");
			sharedState.test = dio.downLoadFileTS("anomalyTest.csv");
			dio.write("Upload complete.");
			dio.write("\n");
		}
	}

	public class CommandNumber2 extends Command {

		public CommandNumber2() {
			super("algorithm settings");
		}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is " + sharedState.sad.getThresholdCorr());
			dio.write("\n");
			while (true) {
				dio.write("Type a new threshold");
				dio.write("\n");
				float th = Float.parseFloat(dio.readText());
				if (th > 0 && th < 1) {
					sharedState.sad.setThresholdCorr(th);
					break;
				} else {
					dio.write("Please choose a value between 0 and 1.\n");
				}
			}
		}
	}

	public class CommandNumber3 extends Command {

		public CommandNumber3() {
			super("detect anomalies");
		}

		@Override
		public void execute() {
			sharedState.sad.learnNormal(sharedState.train);
			sharedState.sad.detect(sharedState.test);
			dio.write("anomaly detection complete.\n");
		}
	}

	public class CommandNumber4 extends Command {

		public CommandNumber4() {
			super("display results");
		}

		@Override
		public void execute() {
			for (int i = 0; i < sharedState.sad.getListOfExp().size(); i++) {
				Integer number = (int) sharedState.sad.getListOfExp().get(i).timeStep;
				String tmp = number.toString();
				dio.write(tmp);
				dio.write("\t ");
				dio.write(sharedState.sad.getListOfExp().get(i).description);
				dio.write("\n");
			}
			dio.write("Done.");
			dio.write("\n");
		}
	}

	public class CommandNumber5 extends Command {

		public CommandNumber5() {
			super("upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local anomalies file.\n");
			float tpr;
			float far;
			float FP = 0;
			float TP = 0;
			int P = 0;
			int sum = 0;
			List<String> listOfExp = dio.downLoadS();
			dio.write("Upload complete.");
			dio.write("\n");
			Hashtable<String, List<AnomalyReport>> ulist = new Hashtable<>();
			List<String> decNames = new ArrayList<>();
			int k = 0;
			for (int i = 0; i < sharedState.sad.getListOfExp().size(); i++) {
				String tmp = sharedState.sad.getListOfExp().get(i).description;
				if (!ulist.containsKey(tmp)) {
					List<AnomalyReport> newListAR = new ArrayList<>();
					decNames.add(k, tmp);
					k++;
					newListAR.add(sharedState.sad.getListOfExp().get(i));
					ulist.put(tmp, newListAR);
					continue;
				}
				if (ulist.containsKey(tmp)) {
					if (sharedState.sad.getListOfExp().get(i).timeStep - 1 == sharedState.sad.getListOfExp().get(i - 1).timeStep) {
						ulist.get(tmp).add(sharedState.sad.getListOfExp().get(i));
					}
				}
			}
			int n = sharedState.test.getArray().size();
			for (int i = 0; i < listOfExp.size(); i++) {
				Integer first, second;
				String[] values = listOfExp.get(i).split(",");
				first = Integer.parseInt(values[0]);
				second = Integer.parseInt(values[1]);
				sum -= ((second.intValue() - first.intValue()) + 1);
				P++;
				for (int j = 0; j < ulist.size(); j++) {
					if (second.intValue() - (ulist.get(decNames.get(j)).get(0).timeStep) >= 0 &&
							(ulist.get(decNames.get(j)).get(ulist.size()).timeStep) - first.intValue() >= 0) {
						TP++;
					}
				}
			}
			FP = ulist.size() - TP;
			int N = n + sum;
			tpr = (float) ((int) ((TP / P) * 1000f)) / 1000f;
			far = (float) ((int) ((FP / N) * 1000) / 1000.0f);
			dio.write("True Positive Rate: ");
			dio.write(tpr);
			dio.write("\n");
			dio.write("False Positive Rate: ");
			dio.write(far);
			dio.write("\n");

		}
	}

	public class CommandNumber6 extends Command {

		public CommandNumber6() {
			super("exit");
		}

		@Override
		public void execute() {
			dio.write("bye");
		}
	}
	// implement here all others commands

}