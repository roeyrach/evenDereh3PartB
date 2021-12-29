package test;

import java.util.ArrayList;
import java.util.List;

import static test.StatLib.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {

	private static float thresholdCorr = (float) 0.9;
	public List<CorrelatedFeatures> listOfPairs;
	public List<AnomalyReport> listOfExp;

	@Override
	public void learnNormal(TimeSeries ts) {
		listOfPairs = new ArrayList<>();
		float max = 0;
		for (int i = 0; i < ts.get_records().get(0).size(); i++) {
			for (int j = i + 1; j < ts.get_records().get(0).size(); j++) {
				int c = (-1);
				float p = Math.abs(pearson(ts.getColByNum(i), ts.getColByNum(j)));
				if (p > max) {
					max = p;
					c = j;
				}
				if (p > getThresholdCorr() && p <= max) {
					c = j;
				}
				if (c != (-1) && max > getThresholdCorr()) {
					Point[] points = new Point[ts.getArray().size()];
					for (int k = 0; k < ts.getArray().size(); k++) {
						points[k] = new Point((float) ts.getArray().get(k).get(i).doubleValue()
								, (float) ts.getArray().get(k).get(c).doubleValue());
					}
					Line line = linear_reg(points);
					float thresholdTemp = (maxDis(line, points));
					CorrelatedFeatures pairs = new CorrelatedFeatures(ts.get_records().get(0).get(i),
							ts.get_records().get(0).get(c), p, line, (float) (thresholdTemp * (1.1)));
					listOfPairs.add(pairs);
				}
			}
		}
	}

	public static float maxDis(Line line, Point[] points) {
		float max_dis = 0;
		for (int i = 0; i < points.length; i++) {
			float dis = Math.abs(points[i].y - line.f(points[i].x)); //dev(points[i], line);
			if (dis > max_dis) {
				max_dis = dis;
			}
		}
		return max_dis;
	}

	public float getThresholdCorr() {
		return thresholdCorr;
	}

	public void setThresholdCorr(float thresholdCorr) {
		this.thresholdCorr = thresholdCorr;
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		listOfExp = new ArrayList<>();
		for (CorrelatedFeatures c : this.listOfPairs) {
			Point[] points = getArrayP(ts.getCol(c.feature1), ts.getCol(c.feature2));
			Line line = c.lin_reg;
			float tempDis = 0;
			for (int i = 0; i < points.length; i++) {
				tempDis = dev(points[i], line);
				if (tempDis > c.threshold) {
					String str = c.feature1 + "-" + c.feature2;
					AnomalyReport AR = new AnomalyReport(str, i + 1);
					listOfExp.add(AR);
				}
			}
		}
		return listOfExp;
	}

	public List<CorrelatedFeatures> getListOfPairs() {
		return listOfPairs;
	}

	public List<AnomalyReport> getListOfExp() {
		return listOfExp;
	}

	public static Point[] getArrayP(float[] col1, float[] col2) {
		int size = col1.length;
		Point[] Array = new Point[size];
		for (int i = 0; i < col1.length; i++) {
			Array[i] = new Point(col1[i], col2[i]);
		}
		return Array;
	}

	public List<CorrelatedFeatures> getNormalModel() {
		return this.listOfPairs;
	}
}