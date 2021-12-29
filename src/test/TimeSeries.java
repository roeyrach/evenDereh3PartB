package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class TimeSeries {

	private Vector<Vector<Double>> _array;
	private List<List<String>> _records;

	public TimeSeries(String csvFileName) {
		File file = new File(csvFileName);
		_records = new ArrayList<>();
		_array = new Vector<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				this._records.add(Arrays.asList(values));
			}
			for (int i = 1; i < this._records.size(); i++) {
				Vector<Double> temp_vector = new Vector<>();
				for (int j = 0; j < this._records.get(i).size(); j++) {
					temp_vector.add(Double.parseDouble(this._records.get(i).get(j)));
				}
				this._array.add(temp_vector);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector<Vector<Double>> getArray() {
		return this._array;
	}

	public float[] getCol(String str) {
		int i = 0;
		int size = this.getArray().size();
		float[] temp_vector1 = new float[size];
		for (i = 0; i < this._records.get(1).size(); i++) {
			if (this._records.get(0).get(i).equals(str)) {
				for (int j = 0; j < this._array.size(); j++) {
					temp_vector1[j] = (float) this._array.get(j).get(i).doubleValue();
				}
			}
		}
		return temp_vector1;
	}

	public float[] getColByNum(int num) {
		int i = 0;
		float[] temp_vector2 = new float[this._records.size()];
		for (i = 0; i < this._records.get(1).size(); i++) {
			for (int j = 0; j < this._array.size(); j++) {
				temp_vector2[j] = (float) (this._array.get(j).get(num).doubleValue());
			}
		}
		return temp_vector2;
	}

	public List<List<String>> get_records() {
		return _records;
	}
}