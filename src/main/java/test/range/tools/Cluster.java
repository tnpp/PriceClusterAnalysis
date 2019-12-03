package test.range.tools;

import java.util.List;
import java.util.Map;

import test.range.dbscan.DBSCAN;
import test.range.domain.Term;
import test.range.gmm.GMM;
import test.range.meanshift.MeanShift;

public class Cluster {
	public static Map<Integer, List<Double>> gmm(double[] datas, int components) {
		return GMM.gmmCluster(datas, components);
	}
	
	public static Map<Integer, List<Double>> gmm(List<Double> datas, int components) {
		return GMM.gmmCluster(list2array(datas), components);
	}
	public static List<Term> meanshift(double[] datas, double kernelBandwidth) {
		return MeanShift.cluster(datas, kernelBandwidth);
	}
	
	public static List<Term> meanshift(List<Double> datas, double kernelBandwidth) {
		return MeanShift.cluster(list2array(datas), kernelBandwidth);
	}
	
	public static List<Term> meanshift(double[][] datas, double kernelBandwidth) {
		return MeanShift.cluster(datas, kernelBandwidth);
	}
	
    public static Map<Integer, List<Double>> dbscan(double[] datas, int minPoints, double epsilon) {
        return DBSCAN.cluster(datas, minPoints, epsilon);
    }

    public static Map<Integer, List<double[]>> dbscan(double[][] datas, int minPoints, double epsilon) {
        return DBSCAN.cluster(datas, minPoints, epsilon);
    }

    public static Map<Integer, List<Double>> dbscan(List<Double> datas, int minPoints, double epsilon) {
        return DBSCAN.cluster(list2array(datas), minPoints, epsilon);
    }
	public static double[] list2array(List<Double> doubleList) {
		if(doubleList == null) return null;
		int len = doubleList.size();
		double[] dataArray = new double[len];
		for(int i = 0; i < len; ++i) {
			dataArray[i] = doubleList.get(i);
		}
		return dataArray;
	}

}
