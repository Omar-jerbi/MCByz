

public class Histogram {

	   private final double[] freq;   // freq[i] = # occurences of value i
	    private double max;            // max frequency of any value

	    // Create a new histogram. 
	    public Histogram(int n) {
	        freq = new double[n];
	    }

	    // Add one occurrence of the value i. 
	    public void addDataPoint(int i) {
	        freq[i]++; 
	        if (freq[i] > max) max = freq[i]; 
	    } 

	    // draw (and scale) the histogram.
	    public void draw() {
	        StdDraw.setYscale(-1, max + 1);  // to leave a little border
	        StdStats.plotBars(freq);
	    }
	 
	    
	    
	    // See Program 2.2.6.
	    public static void main(String[] args) {
	        int n = 3;       // number of coins  //valori da 0 a 3
//	        int trials = 2000;// number of trials

	        // create the histogram
	        Histogram histogram = new Histogram(n+1);
//	        for (int t = 0; t < trials; t++) {
//	            histogram.addDataPoint(Bernoulli.binomial(n));
//	        }
	        
	        
	        
	        histogram.addDataPoint(1);
	        histogram.addDataPoint(0);
	        histogram.addDataPoint(0);
	        histogram.addDataPoint(0);
	        histogram.addDataPoint(0);
	        histogram.addDataPoint(2);
	        histogram.addDataPoint(3);
//	        histogram.addDataPoint(4);  //esplode

	        
	        
	        
	        // display using standard draw
	        StdDraw.setCanvasSize(500, 100);
	        histogram.draw();
	    } 

}
