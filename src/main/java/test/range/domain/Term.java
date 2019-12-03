package test.range.domain;

import org.ejml.simple.SimpleMatrix;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class Term {
	
	private Integer cluster;
	
	private SimpleMatrix mean;
	
	private SimpleMatrix points;
	
	public Term() {};
	
	public Term(Integer cluster, SimpleMatrix mean, SimpleMatrix points) {
		this.cluster = cluster;
		this.mean = mean;
		this.points = points;		
	}

	public Integer getCluster() {
		return cluster;
	}

	public void setCluster(Integer cluster) {
		this.cluster = cluster;
	}

	public SimpleMatrix getMean() {
		return mean;
	}

	public void setMean(SimpleMatrix mean) {
		this.mean = mean;
	}

	public SimpleMatrix getPoints() {
		return points;
	}

	public void setPoints(SimpleMatrix points) {
		this.points = points;
	}
	
	@Override
	public boolean equals(Object o) {
		Term term = (Term) o;
		SimpleMatrix matrix = term.getMean();
		int rows = matrix.numRows();
		int columns = matrix.numCols();
		if(this.mean.numRows() != rows || this.mean.numCols() != columns)
			return false;
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < columns; ++j) {
				if(this.mean.get(i, j) != matrix.get(i, j))
					return false;
			}
		}
	    return true;
	}
	
	public boolean containsMean(SimpleMatrix mean) {
		int rows = mean.numRows();
		int columns = mean.numCols();
		if(this.mean.numRows() != rows || this.mean.numCols() != columns)
			return false;
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < columns; ++j) {
				System.out.println(this.mean.get(i, j));
				System.out.println(mean.get(i, j));
				if(this.mean.get(i, j) != mean.get(i, j))
					return false;
			}
		}
	    return true;
	}
	
	@Override
	public int hashCode() {
		return cluster.hashCode();
	}
	
	public void clear() {
		this.cluster = null;
		this.mean = null;
		this.points = null;
	}
	
	@Override
	public String toString() {
		return "{\ncluster :\t" + this.cluster +
				"\nmean :\t" + this.mean +
				"\npoints :\t" + this.points +
				"}";
	}
	

}
