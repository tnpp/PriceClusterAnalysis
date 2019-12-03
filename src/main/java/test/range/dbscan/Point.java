package test.range.dbscan;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class Point {
	
	/**
	 * 一个样本数据的值，数组长度对应数据维数
	 */
	public double[] value;
	
	/**
	 * <p>
	 * 样本所属的Cluster
	 * </p>
	 * <p>
	 * 默认为<i>-１</i>，表示不属于任何Cluster
	 * </p>
	 */
	public int cluster;
	
	/**
	 * <p>
	 * 该点是否已访问过
	 * </p>
	 * <p>
	 * <i>true</i> 已访问，<i>false</i> 未访问
	 * </p>
	 */
	public boolean visited;
	
	/**
	 * <p>
	 * 该点是否为噪声点 
	 * </p>
	 * <p>
	 * <i>true</i> 为噪声点，　<i>false</i> 不是噪声点
	 * </p>
	 */
	public boolean noise;
	
	/**
	 * 构造函数
	 * @param value 一个样本数据
	 * @param visited　是否访问过
	 * @param noise　是否噪声点
	 */
	public Point(double[] value, boolean visited, boolean noise) {
		this.value = value;
		this.cluster = -1;
		this.visited = visited;
		this.noise = noise;
	}
	
	/**
	 * 构造函数
	 * @param value　一个样本数据
	 * @param cluster　Cluster number
	 * @param visited　是否访问过
	 * @param noise　是否噪声点
	 */
	public Point(double[] value, int cluster, boolean visited, boolean noise) {
		this.value = value;
		this.cluster = cluster;
		this.visited = visited;
		this.noise = noise;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int len = value.length;
		for(int i = 0; i < len; ++i) {
			if(i != (len - 1)) sb.append(value[i]).append("\t");
			else sb.append(value[i]);
		}
		return "{\nCluster: " + cluster + 
				"\nValue: " + sb.toString() + 
				"\nVisited: " + visited + 
				"\nNoise: " + noise + 
				"\n}";
	}
	
	

}
