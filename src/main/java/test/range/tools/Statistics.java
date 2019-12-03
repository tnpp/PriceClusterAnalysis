package test.range.tools;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import test.range.utils.ArrayUtils;

/**
 * 数理统计相关量计算，如期望、方差、协方差、相关系数、余弦夹角、假设检验等
 * 
 * @author  Zhao Shiyu
 * 
 * copy from: https://github.com/zhaoshiyu/SEANLP/blob/master/src/main/java/cn/edu/kmust/math/Statistics.java
 * project: https://github.com/zhaoshiyu/SEANLP
 * 
 * copyright: Zhao Shiyu
 *
 */
public class Statistics {
	
	/**
	 * 求和 sum(X)
	 * @param doubleArray
	 * @return
	 */
	public static double sum(double[] doubleArray) {
		double sum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			sum += doubleArray[i];
		}
		return sum;
	}
	
	public static double sum(List<Double> doubleList) {
		double sum = 0;
		for (int i = 0; i < doubleList.size(); i++) {
			sum += doubleList.get(i);
		}
		return sum;
	}
	
	/**
	 * <p> 
	 * average 
	 * </p>
	 * <p> 
	 * 平均值 avg(X)
	 * </p>
	 * @param doubleArray
	 * @return
	 */
	public static double average(double[] doubleArray) {
		return sum(doubleArray) / doubleArray.length;
	}
	
	public static double average(List<Double> doubleList) {
		return sum(doubleList) / doubleList.size();
	}
	
	/**
	 * <p> 
	 * squareSum 
	 * </p>
	 * <p> 
	 * 每个数据平方后在求和 sum(X^2)
	 * </p>
	 * @param doubleArray
	 * @return
	 */
	public static double squareSum(double[] doubleArray) {
		double squareSum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			squareSum += Math.pow(doubleArray[i], 2);
		}
		return squareSum;
	}
	
	public static double squareSum(List<Double> doubleList) {
		double squareSum = 0;
		for (int i = 0; i < doubleList.size(); i++) {
			squareSum += Math.pow(doubleList.get(i), 2);
		}
		return squareSum;
	}
	
	/**
	 * 
	 * <p> 
	 * expectation 
	 * </p>
	 * <p> 
	 * 求数学期望 E(X) = sum(X)/n
	 * </p>
	 * @param doubleArray
	 * @return double
	 *
	 */
	public static double expectation(double[] doubleArray) {
		
		return sum(doubleArray) / doubleArray.length;
	}
	
	public static double expectation(List<Double> doubleList) {
		
		return sum(doubleList) / doubleList.size();
	}
	
	/**
	 * 
	 * <p> 
	 * variance
	 * </p>
	 * <p> 
	 * 求方差: D(X) = sum([X-E(X)]^2)/n = {[sum(X^2)]-n*[E(X)]^2}/n =  [sum(X^2)]/n-[E(X)]^2
	 * </p>
	 * @param doubleArray
	 * @return double
	 *
	 */
	public static double variance(double[] doubleArray) {
		
		return squareSum(doubleArray) / doubleArray.length - (Math.pow(expectation(doubleArray), 2));
	}
	
	public static double variance(List<Double> doubleList) {
		
		return squareSum(doubleList) / doubleList.size() - (Math.pow(expectation(doubleList), 2));
	}
	
	public static double sampleVariance(double[] doubleArray) {
		int len = doubleArray.length;
		if(len < 2) return 0.0;
		double deviationSquareSum = 0;
		double e = expectation(doubleArray);
		for (int i = 0; i < len; i++) {
			deviationSquareSum += Math.pow(doubleArray[i] - e, 2);
		}
		
		return deviationSquareSum / (len - 1);
	}
	
	public static double sampleVariance(List<Double> doubleList) {
		int len = doubleList.size();
		if(len < 2) return 0.0;
		double deviationSquareSum = 0;
		double e = expectation(doubleList);
		for (int i = 0; i < len; i++) {
			deviationSquareSum += Math.pow(doubleList.get(i) - e, 2);
		}
		
		return deviationSquareSum / (len - 1);
	}
	
	/**
	 * 
	 * <p> 
	 * standardDeviation 
	 * </p>
	 * <p> 
	 * 求标准差  sqrt(D(X))
	 * </p>
	 * @param doubleArray
	 * @return double
	 *
	 */
	public static double standardDeviation(double[] doubleArray) {
		return Math.sqrt(Math.abs(variance(doubleArray))); //注意取绝对值
	}
	
	public static double standardDeviation(List<Double> doubleList) {
		return Math.sqrt(Math.abs(variance(doubleList))); //注意取绝对值
	}
	
	public static double sampleStandardDeviation(double[] doubleArray) {
		return Math.sqrt(Math.abs(sampleVariance(doubleArray))); //注意取绝对值
	}
	
	public static double sampleStandardDeviation(List<Double> doubleList) {
		return Math.sqrt(Math.abs(sampleVariance(doubleList))); //注意取绝对值
	}
	
	
	
	/**
	 * 
	 * <p> 
	 * sumCrossedProduct
	 * </p> 
	 * <p> 
	 * 两个数组交叉积求和 sum(X*Y)
	 * </p>
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double
	 *
	 */
	public static double sumCrossedProduct(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		double sumCrossedProduct = 0;
		if (doubleArray1.length != doubleArray2.length) {
			throw new Exception("样本维度不等，请保证两个样本的维度相同！");
		} else {
			for (int i = 0; i < doubleArray1.length; i++) {
				sumCrossedProduct += doubleArray1[i] * doubleArray2[i];
			}
		}
		return sumCrossedProduct;
	}
	
	public static double sumCrossedProduct(List<Double> doubleList1 ,List<Double> doubleList2) throws Exception {
		double sumCrossedProduct = 0;
		if (doubleList1.size() != doubleList2.size()) {
			throw new Exception("样本维度不等，请保证两个样本的维度相同！");
		} else {
			for (int i = 0; i < doubleList1.size(); i++) {
				sumCrossedProduct += doubleList1.get(i) * doubleList2.get(i);
			}
		}
		return sumCrossedProduct;
	}
	
	/**
	 *
	 * <p> 
	 * covariance 
	 * </p>
	 * <p> 
	 * 求两个维数相同样本的协方差  Cov(X,Y) = sum[(X-E(X)) * (Y-E(Y))]/n = sum(X*Y)/n - [E(X) * E(Y)]
	 * </p>
	 * @param doubleArray1
	 * @param doubleArray2
	 * @return double
	 * @throws Exception
	 *
	 */
	public static double covariance(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		
		return sumCrossedProduct(doubleArray1, doubleArray2) / doubleArray1.length - expectation(doubleArray1) * expectation(doubleArray2);
	}
	
	public static double covariance(List<Double> doubleList1 ,List<Double> doubleList2) throws Exception {
		
		return sumCrossedProduct(doubleList1, doubleList2) / doubleList1.size() - expectation(doubleList1) * expectation(doubleList2);
	}
	
//	public static double covariance1(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
//		double e1 = expectation(doubleArray1);
//		double e2 = expectation(doubleArray2);
//		double sum = 0;
//		for (int i = 0; i < doubleArray1.length; i++) {
//			sum += ((doubleArray1[i]-e1) * (doubleArray2[i]-e2));
//		}
//		
//		return sum / doubleArray1.length;
//	}
	
	/**
	 * 
	 * <p> 
	 * correlation 
	 * </p>
	 * <p> 
	 * 求相关系数 r = Cov(X,Y) / [sqrt(D(X)) * sqrt(D(Y))] = [sum(X*Y) - sum(x)*sum(Y)/n] / [sqrt(sum(X^2)-sum(X)^2/n) * sqrt(sum(Y^2)-sum(Y)^2/n)]
	 * </p>
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double 返回结果范围[-1,1]
	 *
	 */
	public static double correlation(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		
		return covariance(doubleArray1, doubleArray2) / (standardDeviation(doubleArray1) * standardDeviation(doubleArray2));
	}
	
	public static double correlation(List<Double> doubleList1 ,List<Double> doubleList2) throws Exception {
		
		return covariance(doubleList1, doubleList2) / (standardDeviation(doubleList1) * standardDeviation(doubleList2));
	}
	
	/**
	 * 
	 * <p> 
	 * includedAngleCosine 
	 * </p>
	 * <p> 
	 * 求夹角余弦 sum(X*Y)/sqrt(sum(X^2) * sum(Y^2))
	 * </p>
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double
	 *
	 */
	public static double includedAngleCosine(double[] doubleArray1, double[] doubleArray2) throws Exception {
		
		return sumCrossedProduct(doubleArray1, doubleArray2) / (Math.sqrt(squareSum(doubleArray1) * squareSum(doubleArray2)));
	}
	
	public static double includedAngleCosine(List<Double> doubleList1 ,List<Double> doubleList2) throws Exception {
		
		return sumCrossedProduct(doubleList1, doubleList2) / (Math.sqrt(squareSum(doubleList1) * squareSum(doubleList2)));
	}
	
	/**
	 * <p> 
	 * kPowerSum 
	 * </p>
	 * <p> 
	 * 每个数据k次方后在求和 sum(X^k)
	 * </p>
	 * @param doubleArray
	 * @param k
	 * @return
	 */
	public static double kPowerSum(double[] doubleArray, int k) {
		double kPowerSum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			kPowerSum += Math.pow(doubleArray[i], k);
		}
		
		return kPowerSum;
	}
	
	public static double kPowerSum(List<Double> doubleList, int k) {
		double kPowerSum = 0;
		for (int i = 0; i < doubleList.size(); i++) {
			kPowerSum += Math.pow(doubleList.get(i), k);
		}
		
		return kPowerSum;
	}
	
	/**
	 * <p>
	 * kOriginMoment 
	 * </p>
	 * <p> 
	 * k阶原点矩 sum(X^k)/n
	 * </p>
	 * @param doubleArray
	 * @param k
	 * @return
	 */
	public static double kOriginMoment(double[] doubleArray, int k) {
		
		return kPowerSum(doubleArray, k) / doubleArray.length;
	}
	
	public static double kOriginMoment(List<Double> doubleList, int k) {
		
		return kPowerSum(doubleList, k) / doubleList.size();
	}
	
	/**
	 * <p> 
	 * kCentralMoment 
	 * </p>
	 * <p> 
	 * k阶中心矩 sum([X-E(X)]^k)/n
	 * </p>
	 * @param doubleArray
	 * @param k
	 * @return
	 */
	public static double kCentralMoment(double[] doubleArray, int k) {
		double deviationSum = 0;
		int len = doubleArray.length;
		for (int i = 0; i < len; i++) {
			deviationSum += Math.pow(doubleArray[i] - expectation(doubleArray), k);
		}
		
		return deviationSum / len;
	}
	
	public static double kCentralMoment(List<Double> doubleList, int k) {
		double deviationSum = 0;
		int len = doubleList.size();
		for (int i = 0; i < len; i++) {
			deviationSum += Math.pow(doubleList.get(i) - expectation(doubleList), k);
		}
		
		return deviationSum / len;
	}
	
	/**
	 * 获取最小值
	 * @param doubleArray
	 * @return
	 */
	public static double getMin(double[] doubleArray) {
		if(doubleArray.length == 0) return 0;
		double min = doubleArray[0];
		for(int i = 1; i < doubleArray.length; ++i) {
			if(doubleArray[i] < min) min = doubleArray[i];
		}
		return min;
	}
	
	/**
	 * 获取最小值
	 * @param doubleList
	 * @return
	 */
	public static double getMin(List<Double> doubleList) {
		if(doubleList.size() == 0) return 0;
		double min = doubleList.get(0);
		for(int i = 1; i < doubleList.size(); ++i) {
			if(doubleList.get(i) < min) min = doubleList.get(i);
		}
		return min;
	}
	
	/**
	 * 获取最大值
	 * @param doubleArray
	 * @return
	 */
	public static double getMax(double[] doubleArray) {
		if(doubleArray.length == 0) return 0;
		double max = doubleArray[0];
		for(int i = 1; i < doubleArray.length; ++i) {
			if(doubleArray[i] > max) max = doubleArray[i];
		}
		return max;
	}
	
	/**
	 * 获取最大值
	 * @param doubleList
	 * @return
	 */
	public static double getMax(List<Double> doubleList) {
		if(doubleList.size() == 0) return 0;
		double max = doubleList.get(0);
		for(int i = 1; i < doubleList.size(); ++i) {
			if(doubleList.get(i) > max) max = doubleList.get(i);
		}
		return max;
	}
	
	/**
	 * 变异系数
	 * @param doubleArray
	 * @return
	 */
	public static double variationCoefficient(double[] doubleArray) {
		int len = doubleArray.length;
		if(len < 2) return 0.0;
		double deviationSquareSum = 0;
		double e = expectation(doubleArray);
		for (int i = 0; i < len; i++) {
			deviationSquareSum += Math.pow(doubleArray[i] - e, 2);
		}	
		return Math.sqrt(deviationSquareSum / (len - 1)) / e;
	}
	
	/**
	 * 变异系数
	 * @param doubleList
	 * @return
	 */
	public static double variationCoefficient(List<Double> doubleList) {
		int len = doubleList.size();
		if(len < 2) return 0.0;
		double deviationSquareSum = 0;
		double e = expectation(doubleList);
		for (int i = 0; i < len; i++) {
			deviationSquareSum += Math.pow(doubleList.get(i) - e, 2);
		}	
		return Math.sqrt(deviationSquareSum / (len - 1)) / e;
	}
	
	public static List<Double> grubbsHypothesis(double[] doubleArray, double alpha) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = average(doubleArray);
		double stdDev = standardDeviation(doubleArray);
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(1.0 - (Math.abs(avag - doubleArray[i]) / stdDev) >= alpha || (avag - doubleArray[i]) == 0.0) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> grubbsHypothesis(List<Double> doubleList, double alpha) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = average(doubleList);
		double stdDev = standardDeviation(doubleList);
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(1.0 - (Math.abs(avag - doubleList.get(i)) / stdDev) >= alpha || (avag - doubleList.get(i)) == 0.0) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
	public static List<Double> grubbsHypothesisOutlier(double[] doubleArray, double alpha) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = average(doubleArray);
		double stdDev = standardDeviation(doubleArray);
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < doubleArray.length; i++) {
			if(1.0 - (Math.abs(avag - doubleArray[i]) / stdDev) < alpha && (avag - doubleArray[i]) != 0.0) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> grubbsHypothesisOutlier(List<Double> doubleList, double alpha) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = average(doubleList);
		double stdDev = standardDeviation(doubleList);
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(1.0 - (Math.abs(avag - doubleList.get(i)) / stdDev) < alpha && (avag - doubleList.get(i)) != 0.0) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
//	public static double[] grubbsHypothesis(double[] doubleArray, double alpha) {
//		int len = doubleArray.length;
//		if(len == 0) return null;
//		double avag = Statistics.average(doubleArray);
//		double stdDev = Statistics.sampleStandardDeviation(doubleArray);
//		//double[] gn = new double[len];
//		double deviationSum = 0;
//		List<Double> gn = new LinkedList<Double>();
//		for (int i = 0; i < doubleArray.length; i++) {
//			if(1.0 - (Math.abs(avag - doubleArray[i]) / stdDev) >= alpha) {
//				gn.add(doubleArray[i]);
//			}
//			//gn.add(Math.abs(avag - doubleArray[i]) / stdDev);
//		}
//		double[] ret = new double[gn.size()];
//		for(int i = 0; i < gn.size(); ++i) {
//			ret[i] = gn.get(i);
//		}
//		return ret;
//	}
	
	
	public static List<Double> threeThetaHypothesis(double[] doubleArray) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = Statistics.average(doubleArray);
		double stdDev = Statistics.sampleStandardDeviation(doubleArray);
		double toplimit = 3.0 * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < doubleArray.length; i++) {
			if(Math.abs(avag - doubleArray[i]) <= toplimit) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> threeThetaHypothesis(List<Double> doubleList) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = Statistics.average(doubleList);
		double stdDev = Statistics.sampleStandardDeviation(doubleList);
		double toplimit = 3.0 * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(Math.abs(avag - doubleList.get(i)) <= toplimit) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
	public static List<Double> threeThetaHypothesisOutlier(double[] doubleArray) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = Statistics.average(doubleArray);
		double stdDev = Statistics.sampleStandardDeviation(doubleArray);
		double toplimit = 3.0 * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < doubleArray.length; i++) {
			if(Math.abs(avag - doubleArray[i]) > toplimit) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> threeThetaHypothesisOutlier(List<Double> doubleList) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = Statistics.average(doubleList);
		double stdDev = Statistics.sampleStandardDeviation(doubleList);
		double toplimit = 3.0 * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(Math.abs(avag - doubleList.get(i)) > toplimit) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
	
	public static List<Double> thetaHypothesis(double[] doubleArray, double multiple) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = Statistics.average(doubleArray);
		double stdDev = Statistics.sampleStandardDeviation(doubleArray);
		double toplimit = multiple * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < doubleArray.length; i++) {
			if(Math.abs(avag - doubleArray[i]) <= toplimit) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> thetaHypothesis(List<Double> doubleList, double multiple) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = Statistics.average(doubleList);
		double stdDev = Statistics.sampleStandardDeviation(doubleList);
		double toplimit = multiple * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(Math.abs(avag - doubleList.get(i)) <= toplimit) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
	public static List<Double> thetaHypothesisOutlier(double[] doubleArray, double multiple) {
		int len = doubleArray.length;
		if(len == 0) return null;
		double avag = Statistics.average(doubleArray);
		double stdDev = Statistics.sampleStandardDeviation(doubleArray);
		double toplimit = multiple * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < doubleArray.length; i++) {
			if(Math.abs(avag - doubleArray[i]) > toplimit) {
				gn.add(doubleArray[i]);
			}
		}
		return gn;
	}
	
	public static List<Double> thetaHypothesisOutlier(List<Double> doubleList, double multiple) {
		int len = doubleList.size();
		if(len == 0) return null;
		double avag = Statistics.average(doubleList);
		double stdDev = Statistics.sampleStandardDeviation(doubleList);
		double toplimit = multiple * stdDev;
		List<Double> gn = new LinkedList<Double>();
		for (int i = 0; i < len; i++) {
			if(Math.abs(avag - doubleList.get(i)) > toplimit) {
				gn.add(doubleList.get(i));
			}
		}
		return gn;
	}
	
	
	public static double[] list2array(List<Double> data) {
		if(data == null) return null;
		int len = data.size();
		if(len == 0) return null;
		double[] ret = new double[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = data.get(i);
		}
		return ret;
	}
	
	/**
	 * 计算四分位数
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static double[] quartile(double minValue, double maxValue) {
		double[] quartile = new double[3];
		if(minValue == maxValue) {
			quartile[0] = minValue;
			quartile[1] = minValue;
			quartile[2] = minValue;
		} else {
			quartile[0] = minValue + (maxValue - minValue) * 0.25;
			quartile[1] = minValue + (maxValue - minValue) * 0.5;
			quartile[2] = minValue + (maxValue - minValue) * 0.75;
		}
		return quartile;
	}
	
	/**
	 * 计算四分位数
	 * @param doubleArray
	 * @return
	 */
	public static double[] quartile(double[] doubleArray) {
		if(doubleArray.length == 0) return null;
		return quartile(ArrayUtils.getMin(doubleArray), ArrayUtils.getMax(doubleArray));
	}
	
	/**
	 * 计算四分位数
	 * @param doubleList
	 * @return
	 */
	public static double[] quartile(List<Double> doubleList) {
		if(doubleList == null || doubleList.size() == 0) return null;
		Collections.sort(doubleList);
		return quartile(doubleList.get(0), doubleList.get(doubleList.size() - 1));
	}
	
	
	

}
