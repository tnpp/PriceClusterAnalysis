package test.range.service;

import test.range.weka.WekaCluster;

public class AnalysisFactory {

    public static AbstractAnalysis create(String name) {
        if ("meanshift".equals(name)) {
    		return new MeanShiftAnalysis();
        } else if ("gmm".equalsIgnoreCase(name)) {
            return new GMMAnalysis();
        } else if ("dbscan".equalsIgnoreCase(name)) {
            return new DBSCANAnalysis();
        } else if ("weka-em".equalsIgnoreCase(name)) {
            return new WekaCluster("em");
        } else if ("weka-dbscan".equalsIgnoreCase(name)) {
            return new WekaCluster("dbscan");
        } else {
            return null;
    	}
    }
}
