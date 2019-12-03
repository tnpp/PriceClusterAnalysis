package test.range.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Utils {
    public static List<Double> readPricesFromResource(String fileName) {
        List<Double> ds = new ArrayList<Double>();
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(fileName)) {
            List<String> list = IOUtils.readLines(is);
            for (String str : list) {
                try {
                    double d = Double.parseDouble(str);
                    ds.add(d);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
