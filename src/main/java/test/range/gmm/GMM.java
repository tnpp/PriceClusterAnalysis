package test.range.gmm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GMM algorithm
 * @author Zhao Shiyu
 *
 */
public class GMM {
	
    private Component[] components;
    private DataSet data;
    
    private final static int TOTAL_STEP = 50000;

	public GMM(DataSet data) {
        this.data = data;
        this.components = new Component[this.data.components()];
        Double mean = this.data.getMean();
        Double stdev = this.data.getStdev();
        //random initialization of component parameters
        for (int i = 0; i < this.data.components(); i++) {
            Component c = new Component(1.0 / Double.parseDouble(this.data.components() + ""), mean + ((Math.random() - 0.5) * 4), stdev + ((Math.random() - 0.5) * 4));
            this.components[i] = c;
        }
    }

	/**
	 * E step
	 */
    private void Expectation() {
        for (int i = 0; i < this.data.size(); i++) {
            Double[] probs = new Double[this.data.components()];
            for (int j = 0; j < this.components.length; j++) {
                Component c = this.components[j];
                probs[j] = gaussian(this.data.get(i).val(), c.getMean(), c.getStdev()) * c.getWeight();
            }

            //alpha normalize and set probs
            Double sum = 0.0;
            for (Double p : probs)
                sum += p;
            for (int j = 0; j < probs.length; j++) {
                Double normProb = probs[j]/sum;
                //System.out.println(count + "\tprob\t" + normProb);
                this.data.get(i).setProb(j, normProb);
            }
        }
    }
	
	/*public void Expectation() {
        for (int i = 0; i < this.data.size(); i++) {
            Double[] probs = new Double[this.data.components()];
            //alpha normalize and set probs
            Double sum = 0.0;
            for (int j = 0; j < this.components.length; j++) {
                Component c = this.components[j];
                Double d = gaussian(this.data.get(i).val(), c.getMean(), c.getStdev()) * c.getWeight();
                probs[j] = d;
                sum += d;
            }
            for (int j = 0; j < probs.length; j++) {
                Double normProb = probs[j]/sum;
                //System.out.println(count + "\tprob\t" + normProb);
                this.data.get(i).setProb(j, normProb);
            }
        }
    }*/

    /**
     * M step
     */
    private void Maximization() {
        Double newMean = 0.0;
        Double newStdev = 0.0;
        for (int i = 0; i < this.components.length; i++) {
            //MEAN
            for (int j = 0; j < this.data.size(); j++)
                newMean += this.data.get(j).getProb(i) * this.data.get(j).val();
            newMean /= this.data.nI(i);
            this.components[i].setMean(newMean);

            //STDEV
            for (int j = 0; j < this.data.size(); j++)
                newStdev += this.data.get(j).getProb(i) * Math.pow((this.data.get(j).val() - newMean), 2);
            newStdev /= this.data.nI(i);
            newStdev = Math.sqrt(newStdev);
            this.components[i].setStdev(newStdev);

            //WEIGHT
            this.components[i].setWeight(this.data.nI(i) / this.data.size());
        }

    }

    private Double logLike() {
        Double loglike = 0.0;
        for (int i = 0; i < this.data.size(); i++) {
            Double sum = 0.0;
            for (int j = 0; j < this.components.length; j++) {
                Component c = this.components[j];
                Double prob = this.data.get(i).getProb(j);
                Double val = this.data.get(i).val();
                Double gauss = gaussian(val, c.getMean(), c.getStdev());
                if (gauss == 0) {
                    gauss = Double.MIN_NORMAL;
                }
                Double inner = Math.log(gauss)+ Math.log(c.getWeight());
                if (inner.isInfinite() || inner.isNaN()) {
                    return 0.0;
                }
                sum += prob * inner;
            }
            loglike += sum;
        }
        return loglike;
    }

    public void printStats() {
        for (Component c : this.components) {
            System.out.println("C - mean: " + c.getMean() + " stdev: " + c.getStdev() + " weight: " + c.getWeight());
        }
    }
    
    public void printResultData() {
    	int len = this.data.size();
    	int compLen = this.components.length;
    	for(int i = 0; i < len; ++i) {
    		String log = "";
    		log += this.data.get(i).val();
    		for(int j = 0; j < compLen; ++j) {
    			log += "\t" + this.data.get(i).getProb(j);
    		}
    		System.out.println(log);
    	}
    }


    private Double standardGaussian(Double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    private Double gaussian(Double x, Double mu, Double sigma) {
        return standardGaussian((x - mu) / sigma) / sigma;
    }
    
    public void cluster() {
    	Double oldLog = logLike();
        Double newLog = oldLog - 100.0;
        int step = 0;
        // EM algorithm iteration
        do {
            oldLog = newLog;
            Expectation();
            Maximization();
            newLog = logLike();
            //System.out.println(newLog + "\t" + Math.abs(newLog - oldLog));
            step++;
            if(step == TOTAL_STEP) System.err.println("GMM达到最大迭代上限：" + TOTAL_STEP);
        } while (newLog!=0 && Math.abs(newLog - oldLog) > 0.00001 && step <= TOTAL_STEP);
    }
    
    public static int getMaxValueIndex(Double[] values) {
    	int len = values.length, idx = 0;
    	for(int i = 1; i < len; ++i) {
    		if(values[i] >= values[i - 1]) idx = i;
    	}
    	return idx;
    }
    
    public static Map<Integer, List<Double>> gmmCluster(double[] data, int components) {
    	GMM mix = new GMM(new DataSet(data, components));
    	mix.cluster();
    	
    	/*DataSet dataset; 
    	GMM mix;
    	int newComponents = components;
    	int oldComponents = components;
    	int epo = 0, epochs = 3;
    	do {
    		dataset = new DataSet(data, newComponents);
    		mix = new GMM(dataset);
        	mix.cluster();
        	if(newComponents == 0) oldComponents = components;
        	else oldComponents = newComponents;
        	newComponents = 0;
    		for(Component comp : mix.components) {
        		if(comp.getWeight() > 0.10) {
        			++newComponents;
        		}
        	}
    		++epo;
    		System.out.println(newComponents + " / " + oldComponents);
		} while (newComponents != oldComponents && epo <= epochs);*/
    	
        Map<Integer, List<Double>> ret = new HashMap<Integer, List<Double>>();
        int dataLen = mix.data.size();
        for(int i = 0; i < dataLen; ++i) {
        	Datum datum = mix.data.get(i);
        	int clusterNum = getMaxValueIndex(datum.getProb());
        	if(ret.containsKey(clusterNum)) {
        		List<Double> cluster = ret.get(clusterNum);
        		cluster.add(datum.val());
        		ret.put(clusterNum, cluster);
        	} else {
        		List<Double> cluster = new ArrayList<Double>();
        		cluster.add(datum.val());
        		ret.put(clusterNum, cluster);
        	}
        }
//        for(Map.Entry<Integer, List<Double>> entry : ret.entrySet()) {
//        	System.out.println(entry.getKey() + "\t" + entry.getValue());
//        }
        
//        mix.printResultData();
//        mix.printStats();
        return ret;
    }
    
    public static Map<Integer, List<Double>> gmmCluster(String dataFile, int components) {
    	GMM mix = new GMM(new DataSet(dataFile, components));
    	mix.cluster();
        /*Double oldLog = mix.logLike();
        Double newLog = oldLog - 100.0;
        do {
            oldLog = newLog;
            mix.Expectation();
            mix.Maximization();
            newLog = mix.logLike();
            System.out.println(newLog + "\t" + Math.abs(newLog - oldLog));
        }
        while (newLog!=0 && Math.abs(newLog - oldLog) > 0.00001);*/
    	
    	/*DataSet dataset; 
    	GMM mix;
    	int newComponents = components;
    	int oldComponents = components;
    	do {
    		dataset = new DataSet(dataFile, newComponents);
    		mix = new GMM(dataset);
        	mix.cluster();
        	if(newComponents == 0) oldComponents = components;
        	else oldComponents = newComponents;
        	newComponents = 0;
    		for(Component comp : mix.components) {
        		if(comp.getWeight() > 0.10) {
        			++newComponents;
        		}
        	}
    		System.out.println(newComponents + " / " + oldComponents);
		} while (newComponents != oldComponents);*/
    	
    	
    	/*while(newComponents != components) {
    		newComponents = 0;
    		for(Component comp : mix.components) {
        		if(comp.getWeight() > 0.10) {
        			++newComponents;
        		}
        	}
    		if(newComponents != components) {
        		components = newComponents;
        		dataset = new DataSet(dataFile, components);
        		mix = new GMM(dataset);
            	mix.cluster();
        	}
    	}*/
    	
        Map<Integer, List<Double>> ret = new HashMap<Integer, List<Double>>();
        int dataLen = mix.data.size();
        for(int i = 0; i < dataLen; ++i) {
        	Datum datum = mix.data.get(i);
        	int clusterNum = getMaxValueIndex(datum.getProb());
        	if(ret.containsKey(clusterNum)) {
        		List<Double> cluster = ret.get(clusterNum);
        		cluster.add(datum.val());
        		ret.put(clusterNum, cluster);
        	} else {
        		List<Double> cluster = new ArrayList<Double>();
        		cluster.add(datum.val());
        		ret.put(clusterNum, cluster);
        	}
        }
//        for(Map.Entry<Integer, List<Double>> entry : ret.entrySet()) {
//        	System.out.println(entry.getKey() + "\t" + entry.getValue());
//        }
        
        //mix.printResultData();
        mix.printStats();
        return ret;
    }
    
    


}
