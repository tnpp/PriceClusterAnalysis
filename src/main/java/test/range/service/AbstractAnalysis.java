package test.range.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import test.range.domain.ComputeRange;
import test.range.domain.Range;
import test.range.tools.Statistics;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class AbstractAnalysis {
	
	protected final static double ALPHA = 0.001;
	
	/**
	 * 期间价格数占总价格数百分比下限
	 */
	protected final static double MIN_PRICE_SIZE_PARSENT = 0.08;
	
	/**
	 * 每个区间最少需要的价格数
	 */
	protected final static int MIN_RANGE_PRICE_SIZE = 2;
	
	/**
	 * 最多可接受几个区间
	 */
	protected final static int MAX_RANGE_SIZE = 6;
	
	/**
	 * 去异常值时，标准差的倍数
	 */
	protected final static double THETA_MULTIPLE = 3.0;
	
	/**
	 * 区间上下限相同时，上下波动百分比
	 */
	protected final static double DISTURBANCE_PERCENT = 0.025;
	
	/**
	 * 聚类参数中，样本均值的初始缩小倍数
	 */
	protected final static double ETA = 5.0;
	
	/**
	 * 参数调整步长
	 */
	protected final static double ETA_STEP = 1.0;
	
	/**
	 * 能接受的变异系数上限
	 */
	protected final static double MAX_VAR_COE = 0.25;
	
	/**
	 * 最大聚类上限
	 */
	protected final static int MAX_CLUSTER_NUMBER = 100;
	
	/**
	 * 调整参数聚类连读不变退出次数
	 */
	protected final static int MAX_CLUSTER_FIXED_NUMBER = 3;
	
	protected final static class Log {
		public static Logger logger = Logger.getLogger("GAGENT-PRICE-RANGE");
		static {
			logger.setLevel(Level.SEVERE);
		}
	}
	
	/**
	 * 区间过滤
	 * @param range　区间样本
	 * @param size　总样本大小
	 * @return　true 保留，false 丢弃
	 */
	protected boolean rangeFilter(List<Double> range, int size) {
		if(range == null) return false;
		if(range.size() < MIN_RANGE_PRICE_SIZE) return false;
		if(range.size() < (size * MIN_PRICE_SIZE_PARSENT)) return false;
		return true;
	}
	
	/**
	 * 区间排序，按区间样本个数对区间进行排序，降序排列
	 * @param ranges　降序排列结果
	 */
	protected void rangeSort(List<ComputeRange> ranges) {
		Collections.sort(ranges, new Comparator<ComputeRange>() {
			@Override
			public int compare(ComputeRange r1, ComputeRange r2) {
				return r2.getPrices().size() - r1.getPrices().size();
			}
		});
	}
	
	/**
	 * 区间样本转为区间对象
	 * @param rangePrices 区间样本
	 * @param totalSize　总体样本数
	 * @return　区间
	 */
	protected ComputeRange toRange(List<Double> rangePrices, int totalSize) {
		if(rangePrices == null || rangePrices.size() < 1) return null;
//		List<Double> range = Statistics.grubbsHypothesis(rangePrices, ALPHA);
//		List<Double> range = Statistics.threeThetaHypothesis(rangePrices);
		List<Double> rangeValue = Statistics.thetaHypothesis(rangePrices, THETA_MULTIPLE);
		if(!rangeFilter(rangeValue, totalSize)) return null;
//		List<Double> outlier = Statistics.grubbsHypothesisOutlier(rangePrices, ALPHA);
//		List<Double> outlier = Statistics.threeThetaHypothesisOutlier(rangePrices);
		List<Double> outlier = Statistics.thetaHypothesisOutlier(rangePrices, THETA_MULTIPLE);
		double mean = Statistics.average(rangeValue);
		double standardDeviation = Statistics.sampleStandardDeviation(rangeValue);
		double[] quartile = Statistics.quartile(rangeValue);
		double[] interval = new double[2];
		if(standardDeviation != 0) {
			interval[0] = mean - standardDeviation;
			interval[1] = mean + standardDeviation;
		} else {
			interval[0] = mean - mean * DISTURBANCE_PERCENT;
			interval[1] = mean + mean * DISTURBANCE_PERCENT;
		}
		//区间下限小于等于0时取价格最小值
		if(interval[0] <= 0.0 && interval[1] >= 0.0) {
			double min = Statistics.getMin(rangeValue);
			if(min < mean && min > 0.0) interval[0] = min;
			else interval[0] = 0.0;
		}
		double variationCoefficient  = standardDeviation / mean;
		double outlierSize = 0.0;
		double rangePricesSize = 0.0;
		if(outlier != null) outlierSize = outlier.size();
		rangePricesSize = rangeValue.size();
		double rangePriceCountPercent = 100.0 * (double)rangeValue.size() / totalSize;
		double outlierPercent = 100.0 * (double)outlierSize / (rangePricesSize + outlierSize);
		return new ComputeRange(rangeValue, outlier, quartile,
						interval, mean, standardDeviation, 
						variationCoefficient, rangePriceCountPercent, 
						outlierPercent);
	}
	
	/**
	 * 区间合并，交叉区间合并
	 * @param ranges　合并后的结果
	 * @param range　待合并区间
	 * @param totalSize　总体样本数
	 */
	protected void rangeMarge(List<ComputeRange> ranges, ComputeRange range, int totalSize) {
		boolean crossFlag = false;
		if(range != null) {
			for(int i = 0; i < ranges.size(); ++i) {
				if(!(range.getInterval()[1] < ranges.get(i).getInterval()[0] || ranges.get(i).getInterval()[1] < range.getInterval()[0])) {
					System.out.println("区间：\t" + range.getInterval()[0] + "\t" + range.getInterval()[1] + "\t" + range.getPrices().size());
					System.out.println("已有区间：\t" + ranges.get(i).getInterval()[0] + "\t" + ranges.get(i).getInterval()[1] + "\t" + ranges.get(i).getPrices().size());
					List<Double> margePrice = new LinkedList<Double>();
					margePrice.addAll(range.getPrices());
					margePrice.addAll(range.getOutlier());
					margePrice.addAll(ranges.get(i).getPrices());
					margePrice.addAll(ranges.get(i).getOutlier());
					range = this.toRange(margePrice, totalSize);
					System.out.println("新区间：\t" + range.getInterval()[0] + "\t" + range.getInterval()[1] + "\t" + range.getPrices().size());
					ranges.remove(i);
					ranges.add(range);
					crossFlag = true;
					break;
				}
			}
		}
		if(!crossFlag) {
			ranges.add(range);
		}
	}
	abstract public List<ComputeRange> analysisList(List<Double> prices);
	abstract public List<Range> analysis(List<Double> prices);
}
