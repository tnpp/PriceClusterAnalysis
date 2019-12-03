package test.range.domain;

import java.io.Serializable;

public class Range implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double min;
	
	private Double max;
	
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

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		if(min < 0.0) this.min = 0.0;
		else this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		if(max < 0.0) this.max = 0.0;
		else this.max = max;
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
	public String toString() {
		return "outPercent: " + outPercent +"\n" +
		       "min: " + min  +"\n" +
		       "varCoe: " + varCoe  +"\n" +
		       "max: " + max  +"\n" +
		       "rangePercent: " + rangePercent  +"\n" +
		       "stdDev: " + stdDev  +"\n" +
		       "mean: " + mean;
	}
}
