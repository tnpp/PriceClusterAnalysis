package test.range;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import test.range.domain.Range;
import test.range.service.AbstractAnalysis;
import test.range.service.AnalysisFactory;
import test.range.utils.DateUtils;
import test.range.utils.Utils;

public class App2 {
    // static List<Double> prices = Utils.readPricesFromResource("price12851.txt");
    // static List<Double> prices = Utils.readPricesFromResource("price4031.txt");
    // static List<Double> prices = Utils.readPricesFromResource("price6.txt");
    static List<Double> prices = Utils.readPricesFromResource("price5.txt");
    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("args1: Analyzer name, shall be meanshift/gmm/dbscan");
            return;
        }
        AbstractAnalysis analysis = AnalysisFactory.create(args[0]);
        if (analysis == null) {
            System.out.println("Analyzer name error! it shall be meanshift/gmm/dbscan");
            return;
        }
        System.out.println("price count: " + prices.size());
        Date d1 = new Date();
        List<Range> pricesRange = analysis.analysis(prices);
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
