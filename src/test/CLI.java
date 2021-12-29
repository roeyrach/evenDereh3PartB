package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;

	public CLI(DefaultIO dio) {
		this.dio = dio;
		c = new Commands(dio);
		commands = new ArrayList<>();
		// example: commands.add(c.new ExampleCommand());
		// implement
		commands.add(c.new CommandNumber1());
		commands.add(c.new CommandNumber2());
		commands.add(c.new CommandNumber3());
		commands.add(c.new CommandNumber4());
		commands.add(c.new CommandNumber5());
		commands.add(c.new CommandNumber6());
	}

	public void start() {
		// implement
		while (true) {

			dio.write("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
					"3. detect anomalies\n" +
					"4. display results\n" +
					"5. upload anomalies and analyze results\n" +
					"6. exit\n");

			int number = Integer.parseInt(dio.readText());
			commands.get(number - 1).execute();
			if(number == 6){
				break;
			}
		}

	}
}