package test.range.meanshift;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ejml.simple.SimpleMatrix;

import test.range.domain.Term;

public class MeanShift {
	
	private final static double MIN_DISTANCE = 0.0001;
	private final static double GROUP_DISTANCE_TOLERANCE = 0.1;
	private final static int TOTAL_STEP = 20000;
	
	public int[] getClusterLabel(SimpleMatrix points, double kernelBandwidth) {
		return groupPoints(shiftPoint(points, kernelBandwidth));
	}
	
	public List<Term> getCluster(SimpleMatrix points, double kernelBandwidth) {
		return groupPoints(points, shiftPoint(points, kernelBandwidth));
	}
	
	private SimpleMatrix shiftPoint(SimpleMatrix points, double kernelBandwidth) {
		SimpleMatrix shiftPoints = points.copy();
		int rows = points.numRows();
		int columns = points.numCols();
		double maxMinDist = 1;
		int iterationNumber = 0;
		boolean[] stillShifting = new boolean[rows];
		while(maxMinDist > MIN_DISTANCE && iterationNumber <= TOTAL_STEP) {
			//System.out.println("STEP: " + iterationNumber + "\tmaxMinDist: " + maxMinDist);
			if(iterationNumber == TOTAL_STEP) System.err.println("MeanShift达到最大迭代上限：" + TOTAL_STEP + ",\tmaxMinDist: " + maxMinDist);
			maxMinDist = 0;
			++iterationNumber;
			for(int i = 0; i < rows; ++i) {
				if(stillShifting[i]) {
					continue;
				}
				SimpleMatrix pNew = shiftPoints.extractMatrix(i, i+1, 0, columns);
				SimpleMatrix pNewStart = pNew;
				pNew = shiftPoint(pNew, points, kernelBandwidth);
				double dist = euclideanDist(pNew, pNewStart);
				if(dist > maxMinDist) maxMinDist = dist;
				if(dist < MIN_DISTANCE) stillShifting[i] = true;
				
				for(int j = 0; j < pNew.getNumElements(); ++j) {
					shiftPoints.set(i, j, pNew.get(j));
				}
			}
			if((iterationNumber % 20000) == 0) System.out.println("STEP: " + iterationNumber + "\tmaxMinDist: " + maxMinDist);
		}
		return shiftPoints;
	}
	
	private SimpleMatrix shiftPoint(SimpleMatrix pointA, SimpleMatrix points, double kernelBandwidth) {
		int rows = points.numRows();
		int columns = points.numCols();
		
		SimpleMatrix distances = new SimpleMatrix(rows, 1);
		for(int i = 0; i < rows; ++i) {
			distances.set(i, 0, pointA.minus(points.extractMatrix(i, i+1, 0, columns)).normF());
		}
		SimpleMatrix pointWeights = gaussian(distances, kernelBandwidth);
		double denominator = pointWeights.elementSum();
		SimpleMatrix tiledWeights = pointWeights;
		if(columns > 1) {
			for(int i = 1; i < columns; ++i) {
				tiledWeights = tiledWeights.combine(0, i, pointWeights);
			}
		}
		SimpleMatrix shPoints = tiledWeights.elementMult(points);
		SimpleMatrix shiftedPoint = new SimpleMatrix(1, columns);
		for(int i = 0; i < columns; ++i) {
			shiftedPoint.set(0, i, shPoints.extractMatrix(0, rows, i, i + 1).elementSum() / denominator);
		}		
		return shiftedPoint;
	}
	
	private double euclideanDist(SimpleMatrix pointA, SimpleMatrix pointB) {
		return pointA.minus(pointB).normF();
		
	}
	
	private SimpleMatrix gaussian(SimpleMatrix distances, double bandwidth) {
		return distances.copy().divide(bandwidth).elementPower(2).divide(-2).elementExp().scale(1/(bandwidth * Math.sqrt(2 * Math.PI)));
	}
	
	private int[] groupPoints(SimpleMatrix meanPoints) {
		int rows = meanPoints.numRows();
		int cloumns = meanPoints.numCols();
		int[] groupAssignment = new int[rows];
		Map<Integer, List<SimpleMatrix>> groups = new HashMap<Integer, List<SimpleMatrix>>();
		int groupIndex = 0;
		List<SimpleMatrix> group;
		for(int i = 0; i < rows; ++i) {
			SimpleMatrix meanPoint = meanPoints.extractMatrix(i, i + 1, 0, cloumns);
			Integer nearestGroupIndex = determineNearestGroup(meanPoint, groups);
			if(nearestGroupIndex == null) {
				group = new LinkedList<SimpleMatrix>();
				group.add(meanPoint);
				groups.put(groupIndex, group);
				groupAssignment[i] = groupIndex;
				++groupIndex;
			} else {
				group = groups.get(nearestGroupIndex);
				group.add(meanPoint);
				groups.put(nearestGroupIndex, group);
				groupAssignment[i] = nearestGroupIndex;
			}
		}
		return groupAssignment;
	}
	
	private List<Term> groupPoints(SimpleMatrix points, SimpleMatrix meanPoints) {
		int rows = meanPoints.numRows();
		int cloumns = meanPoints.numCols();
		List<Term> terms = new LinkedList<Term>();
		int groupIndex = 0;
		for(int i = 0; i < rows; ++i) {
			SimpleMatrix meanPoint = meanPoints.extractMatrix(i, i + 1, 0, cloumns);
			Integer nearestGroupIndex = determineNearestGroup(meanPoint, terms);
			if(nearestGroupIndex == null) {
				terms.add(new Term(groupIndex, meanPoint, points.extractMatrix(i, i + 1, 0, cloumns)));
				++groupIndex;
			} else {
				Term term = terms.get(nearestGroupIndex);
				SimpleMatrix tmpPoints = term.getPoints();
				term.setPoints(tmpPoints.combine(tmpPoints.numRows(), 0, points.extractMatrix(i, i + 1, 0, cloumns)));
				terms.set(nearestGroupIndex, term);
			}
		}
		return terms;
	}
	
	private Integer determineNearestGroup(SimpleMatrix point, Map<Integer, List<SimpleMatrix>> groups) {
		Integer nearestGroupIndex = null;
		int index = 0;
		for(Map.Entry<Integer, List<SimpleMatrix>> entry : groups.entrySet()) {
			double minDistance = distance2Group(point, entry.getValue());
			if(minDistance < GROUP_DISTANCE_TOLERANCE) nearestGroupIndex = index;
			++index;
		}
		return nearestGroupIndex;
	}
	
	private Integer determineNearestGroup(SimpleMatrix meanPoint, List<Term> terms) {
		Integer nearestGroupIndex = null;
		int index = 0;
		for(Term term : terms) {
			double minDistance = distance2Group(meanPoint, term.getMean());
			if(minDistance < GROUP_DISTANCE_TOLERANCE) nearestGroupIndex = index;
			++index;
		}
		return nearestGroupIndex;
	}
	
	private double distance2Group(SimpleMatrix point, List<SimpleMatrix> group) {
		double minDistance = Double.MAX_VALUE;
		for(SimpleMatrix pt : group) {
			double dist = euclideanDist(point, pt);
			if(dist < minDistance) minDistance = dist;
		}
		return minDistance;
	}
	
	private double distance2Group(SimpleMatrix point, SimpleMatrix group) {
		double minDistance = Double.MAX_VALUE;
		int rows = group.numRows();
		int columns = group.numCols();
		for(int i = 0; i < rows; ++i) {
			SimpleMatrix pt = group.extractMatrix(i, i+1, 0, columns);
			double dist = euclideanDist(point, pt);
			if(dist < minDistance) minDistance = dist;
		}
		return minDistance;
	}
	
	public static List<Term> cluster(SimpleMatrix data, double kernelBandwidth) {
		MeanShift ms = new MeanShift();
		return ms.getCluster(data, kernelBandwidth);
	}
	
	public static List<Term> cluster(double[][] data, double kernelBandwidth) {
		SimpleMatrix matrix = new SimpleMatrix(data);
		MeanShift ms = new MeanShift();
		return ms.getCluster(matrix, kernelBandwidth);
	}
	
	public static List<Term> cluster(double[] data, double kernelBandwidth) {
		int len = data.length;
	    double[][] x = new double[len][1];
	    for(int i = 0; i < len; ++i) {
	    	x[i][0] = data[i];
	    }
	    return cluster(x, kernelBandwidth);
	}
	
}
