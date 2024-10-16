package org.example.utility;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.EmployeeProject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<EmployeeProject> readCSV(InputStream inputStream) {
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                int empId = Integer.parseInt(csvRecord.get("EmpID"));
                int projectId = Integer.parseInt(csvRecord.get("ProjectID"));
                LocalDate dateFrom = parseDate(csvRecord.get("DateFrom"));
                LocalDate dateTo = parseDate(csvRecord.get("DateTo"));

                employeeProjects.add(new EmployeeProject(empId, projectId, dateFrom, dateTo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeProjects;
    }

    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("MM-dd-yyyy")
        };

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(dateString, format);
            } catch (DateTimeParseException e) {
                // Ignore and try the next format
            }
        }

        if (dateString.equalsIgnoreCase("NULL")) {
            return LocalDate.now();
        }

        throw new IllegalArgumentException("Invalid date format: " + dateString);
    }
}
