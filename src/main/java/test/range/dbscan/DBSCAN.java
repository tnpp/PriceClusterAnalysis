package test.range.dbscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBSCAN {
	
	private List<Point> points;
	
	private int minPoints;
	
	private double epsilon;

	public DBSCAN(int minPoints, double epsilon) {
		this.minPoints = minPoints;
		this.epsilon = epsilon;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	public int getMinPoints() {
		return minPoints;
	}
	
	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}
	
	public double getEpsilon() {
		return epsilon;
	}
	
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	/**
	 * 生成多维数据
	 * @param simpleDatas　二维数据，每一行为一个样本数据，列数为样本维数
	 */
	private void creatData(double[][] simpleDatas) {
		int len = simpleDatas.length;
		List<Point> temdata = new ArrayList<Point>(len);
		for(int i = 0; i < len; ++i) {
			temdata.add(new Point(simpleDatas[i], false, false));
		}
		this.points = temdata;
	}
	
	/**
	 * 生成一维数据
	 * @param simpleDatas
	 */
	private void creatData(double[] simpleDatas) {
		int len = simpleDatas.length;
		List<Point> temdata = new ArrayList<Point>(len);
		for(int i = 0; i < len; ++i) {
			temdata.add(new Point(new double[]{simpleDatas[i]}, false, false));
		}
		this.points = temdata;
	}
	
	/**
	 * 聚类函数
	 */
	private void dbscan() {
		int currentCluster = -1;
		int size = this.points.size();
		for(int i = 0; i < size; ++i) {
			if(this.points.get(i).visited) {
				continue;
			}
			Point point = this.points.get(i);
			point.visited = true;
			this.points.set(i, point);
			
			List<Point> neighborPoints = regionQuery(point, this.points);
			if(neighborPoints.size() < this.minPoints) {
				point.noise = true;
				this.points.set(i, point);
			} else {
				++currentCluster;
				expandCluster(point, neighborPoints, currentCluster);
			}
			
		}
		
//		List<Integer> ccc = new LinkedList<Integer>();
//		for(int i = 0; i < size; ++i) {
//			Point point = data.get(i);
//			if(!ccc.contains(point.cluster)) {
//				ccc.add(point.cluster);
//			}
//		}
//		System.err.println(ccc);
//		System.err.println(data.size());
	}
	
	/**
	 * 扩展　Cluster
	 * @param point 核心对象
	 * @param neighborPoints 直接密度可达
	 * @param currentCluster
	 */
	private void expandCluster(Point point, List<Point> neighborPoints, int currentCluster) {
		List<Point> clusterPoints = new LinkedList<Point>();
		point.cluster = currentCluster;
		clusterPoints.add(point);
		int i = 1;
		while(i < neighborPoints.size()) {
			Point dataPoint = neighborPoints.get(i);
			if(!dataPoint.visited) {
				dataPoint.visited = true;
				neighborPoints.set(i, dataPoint);
				List<Point> neighborPointsTmp = regionQuery(dataPoint, neighborPoints);
				if(neighborPointsTmp.size() >= this.minPoints) {
					for(int j = 0; j < neighborPointsTmp.size(); ++j) {
						Point npt = neighborPointsTmp.get(j);
						npt.visited = true;
						neighborPoints.add(npt);
					}
				}
			}
			++i;
			dataPoint.cluster = currentCluster;
		}
		
	}
	
	/**
	 * 计算邻域
	 * @param point
	 * @param points
	 * @return neighborPoints
	 */
	private List<Point> regionQuery(Point point, List<Point> points) {
		int size = points.size();
		List<Point> neighborPoints = new ArrayList<Point>();
		for(int i = 0; i < size; ++i) {
			double dist = distance(points.get(i), point);
			if(dist <= this.epsilon) {
				neighborPoints.add(points.get(i));
			}
		}
		return neighborPoints;
	}
	
	/**
	 * 两点间的距离
	 * @param pointA
	 * @param pointB
	 * @return　double
	 */
	private double distance(double[] pointA, double[] pointB) {
		checkDimensions(pointA, pointB);
		double sum = 0;
		int len = pointA.length;
		for(int i = 0; i < len; ++i) {
			sum += Math.pow(pointA[i] - pointB[i], 2);
		}
        return Math.sqrt(sum);
	}
	
	/**
	 * 两点间的距离
	 * @param pointA
	 * @param pointB
	 * @return　double
	 */
	private double distance(Point pointA, Point pointB) {
		return distance(pointA.value, pointB.value);
	}
	
	/**
	 * 数据样本维数检验
	 * @param pointA
	 * @param pointB
	 */
	private void checkDimensions(double[] pointA, double[] pointB) {
		if (pointA.length != pointB.length) {
			throw new IllegalArgumentException("Dimensions must agree.");
		}
	}
	
	public List<Point> cluster(double[] simpleDatas) {
		this.creatData(simpleDatas);
		this.dbscan();
		return points;
	}
	
	public List<Point> cluster(double[][] simpleDatas) {
		this.creatData(simpleDatas);
		this.dbscan();
		return points;
	}
	
	/**
	 * 高维数据聚类
	 * @param datas
	 * @param minPoints
	 * @param epsilon
	 * @return
	 */
	public static Map<Integer, List<double[]>> cluster(double[][] datas, int minPoints, double epsilon) {
		Map<Integer, List<double[]>> ret = new HashMap<Integer, List<double[]>>();
		DBSCAN dbscan = new DBSCAN(minPoints, epsilon);
		List<Point> points = dbscan.cluster(datas);
		int size = points.size();
		for(int i = 0; i < size; ++i) {
			Point point = points.get(i);
			int cluster = point.cluster;
			List<double[]> clusterDatas;
			if(ret.containsKey(cluster)) {
				clusterDatas = ret.get(cluster);
			} else {
				clusterDatas = new LinkedList<double[]>();
			}
			clusterDatas.add(point.value);
			ret.put(cluster, clusterDatas);
		}
		return ret;
	}
	
	/**
	 *  一维数据聚类
	 * @param datas
	 * @param minPoints
	 * @param epsilon
	 * @return
	 */
	public static Map<Integer, List<Double>> cluster(double[] datas, int minPoints, double epsilon) {
		Map<Integer, List<Double>> ret = new HashMap<Integer, List<Double>>();
		DBSCAN dbscan = new DBSCAN(minPoints, epsilon);
		List<Point> points = dbscan.cluster(datas);
		int size = points.size();
		for(int i = 0; i < size; ++i) {
			Point point = points.get(i);
			int cluster = point.cluster;
			List<Double> clusterDatas;
			if(ret.containsKey(cluster)) {
				clusterDatas = ret.get(cluster);
			} else {
				clusterDatas = new LinkedList<Double>();
			}
			clusterDatas.add(point.value[0]);
			ret.put(cluster, clusterDatas);
		}
		return ret;
	}

}
