package test.range.weka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import test.range.domain.ComputeRange;
import test.range.domain.Range;
import test.range.service.AbstractAnalysis;
import test.range.tools.Cluster;
import test.range.tools.Function;
import test.range.tools.Statistics;
import weka.clusterers.Clusterer;
import weka.clusterers.DBSCAN;
import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class WekaCluster extends AbstractAnalysis {
    private String type;

    public WekaCluster(String type) {
        this.type = type;
    }

    private static final int MIN_POINTS = 4;

    private final static double MIN_EPSILON = 0.1;

    private final static double MAX_EPSILON = 300.0;

    private double kernelEpsilon(List<Double> prices, double eta) {
        if (prices == null || prices.size() == 0.0)
            return 0.0;
        // 曲线右移动两个单位
        double x = Statistics.average(prices) - 2;
        // double epsilon = Function.sigmoid(MAX_EPSILON, 80.0, 0.01, x);
        double epsilon = Function.sigmoid(MAX_EPSILON, 80.0, 0.006, x) - eta;
        if (epsilon < MIN_EPSILON)
            epsilon = MIN_EPSILON;
        return epsilon;
    }

    private Instances getArffInstances(List<Double> prices, String name) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        Attribute attr = new Attribute("price");
        attributes.add(attr);
        Instances instances = new Instances(name, attributes, 0);
        for (double d : prices) {
            Instance instance = new DenseInstance(attributes.size());
            instance.setValue(attr, d);
            instance.setDataset(instances);
            instances.add(instance);
        }
        return instances;
    }

    private Map<Integer, List<Double>> cluster(Clusterer clusterer, Instances instances, List<Double> prices) {
        Map<Integer, List<Double>> map = new HashMap<>();
        try {
            clusterer.buildClusterer(instances);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < instances.numInstances(); i++) {
            try {
                int c = clusterer.clusterInstance(instances.instance(i));
                List<Double> vs = map.get(c);
                if (vs == null) {
                    vs = new ArrayList<>();
                    map.put(c, vs);
                }
                vs.add(prices.get(i));
            } catch (Exception e) {
                System.out.println("NOISE !!!!!!!!!!!!!");
            }
        }

        return map;
    }

    public List<ComputeRange> dbscan(List<Double> prices) {
        Instances instances = getArffInstances(prices, "price_dbscan_clusting");
        int minPoints = MIN_POINTS;
        int maxMinPoints = prices.size() / 10;
        try {
            DBSCAN clusterer = new DBSCAN();
            clusterer.setMinPoints(MIN_POINTS);
            double etaValue = ETA;
            double epsilon = kernelEpsilon(prices, etaValue);
            clusterer.setEpsilon(epsilon);
            Map<Integer, List<Double>> map = cluster(clusterer, instances, prices);
            List<ComputeRange> ranges = resultParser(map, prices.size());
            if (ranges.size() == 0)
                return null;
            double varCoe = ranges.get(0).getVarCoe();
            int fixedNumber = 0;
            double lastVarCoe = varCoe;
            int number = 0;
            while (varCoe > MAX_VAR_COE && MAX_CLUSTER_NUMBER > number) {
                etaValue += ETA_STEP;
                if (minPoints < maxMinPoints)
                    ++minPoints;
                ++number;
                List<ComputeRange> scanRange = resultParser(Cluster.dbscan(prices, minPoints, kernelEpsilon(prices, etaValue)), prices.size());
                if (scanRange == null || scanRange.size() == 0)
                    break;
                ranges = scanRange;
                varCoe = ranges.get(0).getVarCoe();
                System.out.println("DBSCAN 第 " + (number + 1) + " 次聚类，VARCOE = " + varCoe + ", 价格百分比 = " + ranges.get(0).getRangePercent() + "， 下限: "
                        + ranges.get(0).getInterval()[0] + "， 上限: " + ranges.get(0).getInterval()[1]);
                if (lastVarCoe == varCoe)
                    ++fixedNumber;
                else
                    fixedNumber = 0;
                lastVarCoe = varCoe;
                if (fixedNumber >= MAX_CLUSTER_FIXED_NUMBER)
                    break;
            }
            return ranges;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ComputeRange> em(List<Double> prices) {
        Instances instances = getArffInstances(prices, "price_em_clusting");
        try {
            EM clusterer = new EM();// 构造聚类算法
            clusterer.setNumClusters(-1);
            Map<Integer, List<Double>> map = cluster(clusterer, instances, prices);
            return resultParser(map, prices.size());
            /* 打印聚类结果
            System.out.println(clusterer.toString());
            ArffSaver saver = new ArffSaver();
            saver.setInstances(instances);
            saver.setFile(new File("c:\\k_mean1.arff"));
            saver.writeBatch();*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对聚类结果进行解析，并生成区间
     * 
     * @param maps Cluster result
     * @param totalSize 总体样本数
     * @return 区间结果
     */
    private List<ComputeRange> resultParser(Map<Integer, List<Double>> maps, int totalSize) {
        List<ComputeRange> ranges = new LinkedList<ComputeRange>();
        for (Map.Entry<Integer, List<Double>> entry : maps.entrySet()) {
            Collections.sort(entry.getValue());
            ComputeRange range = toRange(entry.getValue(), totalSize);
            if (range != null)
                rangeMarge(ranges, range, totalSize);
        }
        if (ranges != null)
            rangeSort(ranges);
        return ranges;
    }
    @Override
    public List<ComputeRange> analysisList(List<Double> prices) {
        if ("dbscan".equalsIgnoreCase(type)) {
            return dbscan(prices);
        } else {
            return em(prices);
        }
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
