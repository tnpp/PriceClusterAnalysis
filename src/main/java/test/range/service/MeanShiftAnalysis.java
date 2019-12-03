package test.range.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import test.range.domain.ComputeRange;
import test.range.domain.Range;
import test.range.domain.Term;
import test.range.tools.Cluster;
import test.range.tools.Statistics;
import test.range.utils.ArrayUtils;
import test.range.utils.DateUtils;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class MeanShiftAnalysis  extends AbstractAnalysis {
	
	/**
	 * 带宽下限
	 */
	private final static double MIN_BANDWIDTH = 0.1;
	
	/**
	 * 带宽上限
	 */
	private final static double MAX_BANDWIDTH = 150.0;
	
	/**
	 * 动态生成带宽
	 * @param prices
	 * @param eta
	 * @return
	 */
	private double kernelBandwidth(double[] prices, double eta) {
		if(prices.length == 0.0) return 0.0;
		double bandwidth = Statistics.average(prices) / eta;
		if(bandwidth < MIN_BANDWIDTH) bandwidth = MIN_BANDWIDTH;
		if(bandwidth > MAX_BANDWIDTH) bandwidth = MAX_BANDWIDTH;
		return bandwidth;
	}
	
	/**
	 * 聚类结果解析，生成价格区间
	 * @param terms
	 * @param totalSize
	 * @return
	 */
	private List<ComputeRange> resultParser(List<Term> terms, int totalSize) {
		List<ComputeRange> ranges = new LinkedList<ComputeRange>();
		for(Term term : terms) {
			double[] arrayPrices = term.getPoints().getMatrix().getData();
			List<Double> prices = ArrayUtils.array2list(arrayPrices);
			Collections.sort(prices);
			ComputeRange range = toRange(prices, totalSize);
			if(range != null) rangeMarge(ranges, range, totalSize);
		}
		if(ranges != null) rangeSort(ranges);
		for(int i = MAX_RANGE_SIZE; i < ranges.size(); ++i) {
			ranges.remove(i);
		}
		return ranges;
	}
	
	protected List<ComputeRange> analysisArray(double[] prices) {
		int number = 0;
		double etaValue = ETA;
		List<ComputeRange>  ranges = resultParser(Cluster.meanshift(prices, kernelBandwidth(prices, etaValue)), prices.length);
		if(ranges.size() == 0) return null;
		double varCoe = ranges.get(0).getVarCoe();
		int fixedNumber = 0;
		double lastVarCoe = varCoe;
		while(varCoe > MAX_VAR_COE && MAX_CLUSTER_NUMBER > number) {
			etaValue += ETA_STEP;
			++number;
			List<ComputeRange> mfRanges = resultParser(Cluster.meanshift(prices, kernelBandwidth(prices, etaValue)), prices.length);
			if(mfRanges == null || mfRanges.size() == 0) break;
			ranges = mfRanges;
			varCoe = ranges.get(0).getVarCoe();
			// System.out.println("MeanShift 第 " + (number + 1) + " 次聚类，VARCOE = " + varCoe + ", 价格百分比 = " +  ranges.get(0).getRangePercent() + "， 下限: " + ranges.get(0).getInterval()[0] +  "， 上限: " + ranges.get(0).getInterval()[1]);
			// Log.logger.info("MeanShift 第 " + (number + 1) + " 次聚类，VARCOE = " + varCoe + ", 价格百分比 = " +  ranges.get(0).getRangePercent() + "， 下限: " + ranges.get(0).getInterval()[0] +  "， 上限: " + ranges.get(0).getInterval()[1]);
			if(lastVarCoe == varCoe) ++fixedNumber;
			else fixedNumber = 0;
			lastVarCoe = varCoe;
			if(fixedNumber >= MAX_CLUSTER_FIXED_NUMBER) break;
		}
		return ranges;
	}

	@Override
	public List<ComputeRange> analysisList(List<Double> prices) {
		return analysisArray(ArrayUtils.list2array(prices));
		//return resultParser(Cluster.meanshift(prices, kernelBandwidth(ArrayUtils.list2array(prices), 5.0)), prices.size());
	}
	
	protected List<ComputeRange> analysisArray(double[] prices, double eta) {
		return resultParser(Cluster.meanshift(prices, kernelBandwidth(prices, eta)), prices.length);
	}
	
	protected List<ComputeRange> analysisList(List<Double> prices, double eta) {
		return analysisArray(ArrayUtils.list2array(prices), eta);
	}
	@Override
	public List<Range> analysis(List<Double> prices) {
		List<ComputeRange> computeRanges = analysisList(prices);
		List<Range> ranges = new ArrayList<Range>();
		if (!CollectionUtils.isEmpty(computeRanges)) {
			for (ComputeRange cr:computeRanges) {
				Range r = new Range();
				r.setOutPercent(cr.getOutPercent());
				double min = cr.getInterval()[0];
				if(min == 0.0) min = Statistics.getMin(cr.getPrices());
				if(min < 0.0 || min > cr.getMean()) min = 0.0;
				r.setMin(min);
				r.setMax(cr.getInterval()[1]);
	            r.setVarCoe(cr.getVarCoe());
	            r.setRangePercent(cr.getRangePercent());
	            r.setStdDev(cr.getStdDev());
	            r.setMean(cr.getMean());
				ranges.add(r);
			}
		}
		return ranges;
	}
    public static void main(String[] args) throws Exception {
    	List<Double> list = Arrays.asList(new Double(1.1),new Double(1.2),new Double(1.3),new Double(1.4),new Double(1.44)
    			,new Double(1.43),new Double(1.42),new Double(1.8),new Double(1.9),new Double(1.49),new Double(1.441),new Double(1.442),
    			new Double(100.1),new Double(100.2),new Double(100.3),new Double(1.4),new Double(100.44)
    			,new Double(100.43),new Double(100.42),new Double(100.8),new Double(100.9),new Double(100.49),new Double(100.441),new Double(100.442),
    			new Double(600.1),new Double(600.2),new Double(600.3),new Double(1200.4),new Double(1200.44)
    			,new Double(1200.43),new Double(1200.42),new Double(1200.8),new Double(1200),new Double(1200.49),new Double(112.441)
    			,new Double(1201.442),new Double(1201.43),new Double(1201.42),new Double(1201.8),new Double(1201),new Double(1201.49),new Double(111.441),new Double(1201.442)
    			,new Double(1202.43),new Double(1202.42),new Double(1202.8),new Double(1202),new Double(1202.49),new Double(1112.441)
    			,new Double(1203.442),new Double(1203.43),new Double(1203.42),new Double(1203.8),new Double(1201),new Double(1201.49),new Double(1211.441),new Double(1201.442)
    			,new Double(1203.13),new Double(1210.42),new Double(1209.8),new Double(1209),new Double(1208.49),new Double(1212.441)
    			,new Double(1201.442),new Double(1201.43),new Double(1201.42),new Double(1201.8),new Double(1201),new Double(1201.22),new Double(111.441),new Double(1211.442)
    			,new Double(1202.43),new Double(1202.42),new Double(1202.8),new Double(1202),new Double(1202.49),new Double(1112.441)
    			,new Double(1203.042),new Double(1203.99),new Double(1211.142),new Double(1213.8),new Double(1203),new Double(1202.4),new Double(1211.1),new Double(1210.042)
    			);
    	
    	
    	List<Double> dList = new ArrayList<Double>();
    	dList.addAll(list);
    	for (int i=0; i<500;i++) {
    		double d = 30 * Math.random();
    		dList.add(d);
    	}
    	Date d1 = new Date();
    	System.out.println(dList.size());
    	MeanShiftAnalysis analysis = new MeanShiftAnalysis();
    	List<ComputeRange> range = analysis.analysisList(dList);
    	Date d2 = new Date();
    	System.out.println(range.size() + ", cost(s): " + DateUtils.compareToSecond(d1, d2));
    	range.forEach((r)->System.out.println(r));
    	
        System.out.println("------------------------------------");
    	for (int i=0; i<500;i++) {
    		double d = 30 * Math.random();
    		dList.add(d);
    	}
    	d1 = new Date();
    	System.out.println(dList.size());
    	analysis = new MeanShiftAnalysis();
    	range = analysis.analysisList(dList);
    	d2 = new Date();
    	System.out.println(range.size() + ", cost(s): " + DateUtils.compareToSecond(d1, d2));
        range.forEach((r) -> System.out.println(r));
    }
}
