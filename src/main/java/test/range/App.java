package test.range;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import test.range.domain.Range;
import test.range.service.AbstractAnalysis;
import test.range.service.AnalysisFactory;
import test.range.utils.DateUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if (args == null || args.length < 2) {
            System.out.println("args1: Analyzer name, shall be meanshift/gmm/dbscan");
            System.out.println("args2: data number");
            return;
        }
        AbstractAnalysis analysis = AnalysisFactory.create(args[0]);
        if (analysis == null) {
            System.out.println("Analyzer name error! it shall be meanshift/gmm/dbscan");
            return;
        }
        int count = 0;
        try {
            count = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Data name shall be int!");
            return;
        }
        if (count < 5) {
            System.out.println("Data name shall be morn than 5!");
            return;
        }
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < count; i++) {
            double d = 30d + 50 * Math.random();
            list.add(d);
        }
        Date d1 = new Date();
        List<Range> pricesRange = analysis.analysis(list);
        String className = analysis.getClass().getName();
        if (StringUtils.isNotBlank(className) && className.contains(".")) {
            className = className.substring(className.lastIndexOf('.') + 1);
        }
        Date d2 = new Date();
        System.out.println(className + " costs " + DateUtils.compareToSecond(d1, d2) + " s");
        print(pricesRange);
    }

    private static void print(List<Range> range) {
        if (CollectionUtils.isEmpty(range)) {
            System.out.println("Empty range");
        } else {
            range.forEach((r) -> System.out.println(r));
        }
    }
}
