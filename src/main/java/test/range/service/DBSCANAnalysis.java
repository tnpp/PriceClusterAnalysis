package test.range.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import test.range.domain.ComputeRange;
import test.range.domain.Range;
import test.range.tools.Cluster;
import test.range.tools.Function;
import test.range.tools.Statistics;
import test.range.utils.ArrayUtils;

public class DBSCANAnalysis extends AbstractAnalysis {
	
	private static final int MIN_POINTS = 4;
	
	private final static double MIN_EPSILON = 0.1;
	
	private final static double MAX_EPSILON = 300.0;
	
	private double kernelEpsilon(double[] prices, double eta) {
		if(prices.length == 0.0) return 0.0;
		//　曲线右移动两个单位
		double x = Statistics.average(prices) - 2;
//		double epsilon = Function.sigmoid(MAX_EPSILON, 80.0, 0.01, x);
		double epsilon = Function.sigmoid(MAX_EPSILON, 80.0, 0.006, x) - eta;
		if(epsilon < MIN_EPSILON) epsilon = MIN_EPSILON;
		return epsilon;
	}
	
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
		int number = 0;
		double etaValue = ETA;
		int minPoints = MIN_POINTS;
		int maxMinPoints = prices.length / 10;
        List<ComputeRange> ranges = resultParser(Cluster.dbscan(prices, minPoints, kernelEpsilon(prices, etaValue)), prices.length);
		if(ranges.size() == 0) return null;
		double varCoe = ranges.get(0).getVarCoe();
		int fixedNumber = 0;
		double lastVarCoe = varCoe;
		while(varCoe > MAX_VAR_COE && MAX_CLUSTER_NUMBER > number) {
			etaValue += ETA_STEP;
			if(minPoints < maxMinPoints) ++minPoints;
			++number;
            List<ComputeRange> scanRange = resultParser(Cluster.dbscan(prices, minPoints, kernelEpsilon(prices, etaValue)), prices.length);
			if(scanRange == null || scanRange.size() == 0) break;
			ranges = scanRange;
			varCoe = ranges.get(0).getVarCoe();
			//System.out.println("DBSCAN 第 " + (number + 1) + " 次聚类，VARCOE = " + varCoe + ", 价格百分比 = " +  ranges.get(0).getRangePercent() + "， 下限: " + ranges.get(0).getInterval()[0] +  "， 上限: " + ranges.get(0).getInterval()[1]);
			//Log.logger.info("DBSCAN 第 " + (number + 1) + " 次聚类，VARCOE = " + varCoe + ", 价格百分比 = " +  ranges.get(0).getRangePercent() + "， 下限: " + ranges.get(0).getInterval()[0] +  "， 上限: " + ranges.get(0).getInterval()[1]);
			if(lastVarCoe == varCoe) ++fixedNumber;
			else fixedNumber = 0;
			lastVarCoe = varCoe;
			if(fixedNumber >= MAX_CLUSTER_FIXED_NUMBER) break;
		}
		return ranges;
		//return resultParser(Cluster.dbscan(prices, MIN_POINTS, kernelEpsilon(prices, 10.0)), prices.length);
	}

	@Override
    public List<ComputeRange> analysisList(List<Double> prices) {
		return analysisArray(ArrayUtils.list2array(prices));
		//return resultParser(Cluster.dbscan(prices, MIN_POINTS, kernelEpsilon(ArrayUtils.list2array(prices))), prices.size());
	}

    protected List<ComputeRange> analysisArray(double[] prices, double eta) {
		
		return resultParser(Cluster.dbscan(prices, MIN_POINTS, kernelEpsilon(prices, eta)), prices.length);
	}

    protected List<ComputeRange> analysisList(List<Double> prices, double eta) {
		return analysisArray(ArrayUtils.list2array(prices), eta);
	}

    @Override
    public List<Range> analysis(List<Double> prices) {
        List<ComputeRange> computeRanges = analysisList(prices);
        List<Range> ranges = new ArrayList<Range>();
        if (!CollectionUtils.isEmpty(computeRanges)) {
            for (ComputeRange cr : computeRanges) {
                Range r = new Range();
                r.setOutPercent(cr.getOutPercent());
                double min = cr.getInterval()[0];
                if (min == 0.0)
                    min = Statistics.getMin(cr.getPrices());
                if (min < 0.0 || min > cr.getMean())
                    min = 0.0;
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
}
