package test;

public class StatLib {
	// simple average
	public static float avg(float[] x){
		float sum = 0;
		for(int i = 0; i < x.length; i++){
			sum += x[i];
		}
		float avg1 = (sum/x.length);
		return avg1;
	}
	// returns the variance of X and Y
	public static float var(float[] x){
		float sum = 0;
		float average = avg(x);
		for(int i = 0; i < x.length; i++){
			sum += Math.pow((x[i] - average),2);
		}
		float final_var = sum/x.length;
		return final_var;
	}
	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){

		float sum = 0;

		for(int i = 0; i < x.length ; i++) {
			sum = sum +  (x[i] - avg(x)) * (y[i] - avg(y));
		}
		float covariance = (sum / (x.length));
		return covariance;
	}
	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float root_x = (float)Math.sqrt(var(x));
		float root_y = (float)Math.sqrt(var(y));
		float final_pear = cov(x,y) / (root_x * root_y);
		return final_pear;
	}
	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float sum_x = 0;
		float[] _x = new float[points.length];
		float sum_y = 0;
		float[] _y = new float[points.length];
		for(int i = 0; i < points.length; i++){
			sum_x += points[i].x;
			_x[i] = points[i].x;
			sum_y += points[i].y;
			_y[i] = points[i].y;
		}
		float a = cov(_x,_y)/var(_x);
		float b = (sum_y/points.length) - a*(sum_x/points.length);
		Line final_line = new Line(a, b);
		return final_line;

	}
	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line equation = linear_reg(points);
		float distance = Math.abs(equation.f(p.x) - p.y);
		return distance;
	}
	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float distance = Math.abs(l.f(p.x) - p.y);
		return distance;
	}
}