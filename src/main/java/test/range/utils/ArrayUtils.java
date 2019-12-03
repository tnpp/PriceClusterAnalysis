package test.range.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

	public static void printArray(int[] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		int len = array.length;
		for(int i =0; i < len; ++i) {
			sb.append(array[i]);
			if(i != (len - 1))
				sb.append(",");
		}
		sb.append("}");
		System.out.println(sb.toString());
	}
	
	public static void printArray(double[] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		int len = array.length;
		for(int i =0; i < len; ++i) {
			sb.append(array[i]);
			if(i != (len - 1))
				sb.append(",");
		}
		sb.append("}");
		System.out.println(sb.toString());
	}

	public static void exchangeElements(int[] array, int index1, int index2) {  
        int temp = array[index1];  
        array[index1] = array[index2];  
        array[index2] = temp;  
    }
	
	public static void exchangeElements(double[] array, int index1, int index2) {  
		double temp = array[index1];  
        array[index1] = array[index2];  
        array[index2] = temp;  
    }
	
	public static double getMin(double[] array) {
		if(array.length == 0) return 0.0;
		double minValue = array[0];
		for(double arr : array) {
			if(minValue > arr) minValue = arr;
		}
		return minValue;
	}
	
	public static double getMax(double[] array) {
		if(array.length == 0) return 0.0;
		double maxValue = array[0];
		for(double arr : array) {
			if(maxValue < arr) maxValue = arr;
		}
		return maxValue;
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
	
	public static List<Double> array2list(double[] doubleArray) {
		if(doubleArray == null || doubleArray.length == 0) return null;
		List<Double> doubleList = new ArrayList<Double>(doubleArray.length);
		int len = doubleArray.length;
		for(int i = 0; i < len; ++i) {
			doubleList.add(doubleArray[i]);
		}
		return doubleList;
	}

}
