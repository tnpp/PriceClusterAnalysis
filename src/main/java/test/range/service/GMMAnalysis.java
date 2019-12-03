package test.range.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import test.range.domain.ComputeRange;
import test.range.domain.Range;
import test.range.tools.Cluster;
import test.range.tools.Statistics;
import test.range.utils.ArrayUtils;
import test.range.utils.DateUtils;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class GMMAnalysis extends AbstractAnalysis {
	
	/**
	 * 对聚类结果进行解析，并生成区间
	 * @param maps　Cluster result
	 * @param totalSize 总体样本数
	 * @return　区间结果
	 */
	private List<ComputeRange> resultParser(Map<Integer, List<Double>> maps, int totalSize) {
		List<ComputeRange> ranges = new LinkedList<ComputeRange>();
		for(Map.Entry<Integer, List<Double>> entry : maps.entrySet()) {
			Collections.sort(entry.getValue());
			ComputeRange range = toRange(entry.getValue(), totalSize);
			if(range != null) rangeMarge(ranges, range, totalSize);
		}
		if(ranges != null) rangeSort(ranges);
		return ranges;
	}
	
	protected List<ComputeRange> analysisArray(double[] prices) {
		return resultParser(Cluster.gmm(prices, MAX_RANGE_SIZE), prices.length);
	}
	@Override
	public List<ComputeRange> analysisList(List<Double> prices) {
		return analysisArray(ArrayUtils.list2array(prices));
		//return resultParser(Cluster.gmm(prices, MAX_RANGE_SIZE), prices.size());
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

	protected List<ComputeRange> analysisArray(double[] prices, double eta) {
		return resultParser(Cluster.gmm(prices, (int)eta), prices.length);
	}

	protected List<ComputeRange> analysisList(List<Double> prices, double eta) {
		return analysisArray(ArrayUtils.list2array(prices));
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
    	GMMAnalysis analysis = new GMMAnalysis();
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
    	analysis = new GMMAnalysis();
    	range = analysis.analysisList(dList);
    	d2 = new Date();
    	System.out.println(range.size() + ", cost(s): " + DateUtils.compareToSecond(d1, d2));
    	range.forEach((r)->System.out.println(r));
    }

}
