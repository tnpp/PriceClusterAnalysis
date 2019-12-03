package test.range.domain;

import java.util.List;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class ComputeRange {
	
	/**
	 * 区间内正常价格
	 */
	private List<Double> prices;
	
	/**
	 * 区间内异常价格
	 */
	private List<Double> outlier;
	
	/**
	 * 四分位数
	 */
	private double[] quartile;
	
	/**
	 * 价格区间
	 */
	private double[] interval;
	
	/**
	 * 区间平均值
	 */
	private Double mean;
	
	/**
	 * 区间标准差　standardDeviation
	 */
	private Double stdDev;
	
	/**
	 * 变异系数　variationCoefficient
	 */
	private Double varCoe;
	
	/**
	 * 区间价格数百分比　rangePriceCountPercent
	 */
	private Double rangePercent;
	
	/**
	 * 异常价格百分比　outlierPercent
	 */
	private Double outPercent;
	
	public ComputeRange() {};
	
	public ComputeRange(List<Double> prices, List<Double> outlier, double[] quartile, double[] interval, Double mean, Double stdDev, Double varCoe, double rangePercent, Double outPercent) {
		this.prices = prices;
		this.outlier = outlier;
		this.quartile = quartile;
		this.interval = interval;
		this.mean = mean;
		this.stdDev = stdDev;
		this.varCoe = varCoe;
		this.rangePercent = rangePercent;
		this.outPercent = outPercent;
	}
	
	public List<Double> getPrices() {
		return prices;
	}

	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}

	public List<Double> getOutlier() {
		return outlier;
	}

	public void setOutlier(List<Double> outlier) {
		this.outlier = outlier;
	}

	public double[] getQuartile() {
		return quartile;
	}

	public void setQuartile(double[] quartile) {
		this.quartile = quartile;
	}

	public double[] getInterval() {
		return interval;
	}

	public void setInterval(double[] interval) {
		this.interval = interval;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getStdDev() {
		return stdDev;
	}

	public void setStdDev(Double stdDev) {
		this.stdDev = stdDev;
	}

	public Double getVarCoe() {
		return varCoe;
	}

	public void setVarCoe(Double varCoe) {
		this.varCoe = varCoe;
	}

	public Double getRangePercent() {
		return rangePercent;
	}

	public void setRangePercent(Double rangePercent) {
		this.rangePercent = rangePercent;
	}

	public Double getOutPercent() {
		return outPercent;
	}

	public void setOutPercent(Double outPercent) {
		this.outPercent = outPercent;
	}

	@Override
	public boolean equals(Object obj) {
		ComputeRange range = (ComputeRange)obj;
		//只需要判断平均值和样本标准差
		if(mean.equals(range.getMean()) && stdDev.equals(range.getStdDev())) return true;
		return false;
	}

	@Override
	public String toString() {
		String qstring = "null", istring = "null";
		if(quartile != null) 
			qstring = "quartile: Q1=" + quartile[0] + ", Q2=" + quartile[1] + ", Q3=" + quartile[2];
		if(quartile != null) 
			istring = "interval: min=" + interval[0] + ", max=" + interval[1];
		return "\nprices: " + prices + "\n" +
				"outlier: " + outlier + "\n" +
				qstring + "\n" +
				istring + "\n" +
				"mean: " + mean + "\n" +
				"stdDev: " + stdDev + "\n" +
				"varCoe: " + varCoe + "\n" + 
				"rangePercent: " + rangePercent + "\n" + 
				"outPercent: " + outPercent + "\n";
	}
	
}
