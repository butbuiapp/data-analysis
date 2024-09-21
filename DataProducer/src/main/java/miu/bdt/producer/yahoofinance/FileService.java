package miu.bdt.producer.yahoofinance;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import miu.bdt.Constant;

import com.opencsv.CSVReader;

public class FileService {

	public static List<String> getTickers() {
        CSVReader reader = null;
        List<String> tickers = new ArrayList<>();
        try {
            InputStream is = FileService.class.getResourceAsStream(Constant.DATA_TEST);
            if (is == null) {
                throw new IOException();
            }
            reader = new CSVReader(new InputStreamReader(is));
            String[] line;
            while ((line = reader.readNext()) != null) {
                String ticker = line[0];
                tickers.add(ticker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tickers;
    }
}
