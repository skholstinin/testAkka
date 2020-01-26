package ru.test.bot.test3.helpers;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ru.test.bot.test3.models.City;
import ru.test.bot.test3.models.CsvCity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    private static CsvMapper mapper = new CsvMapper();
    private static final String CSV_EXTENSION = ".csv";
    private static CsvSchema schema = mapper.schemaFor(CsvCity.class).withHeader();
    private static ObjectWriter writer = mapper.writer(schema.withLineSeparator("\n"));

    public void writeToCSV(List<City> objects, String fileName) {

        ArrayList<CsvCity> csvCities = new ArrayList<>();
        for (City c : objects) {
            csvCities.add(new CsvCity(c.getId(), c.getName(), c.getType(), c.getLatitude(), c.getLongitude()));
        }

        try {
            writer.writeValue(new File(fileName.concat(CSV_EXTENSION)), csvCities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
