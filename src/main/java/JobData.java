import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;
    private static ArrayList<HashMap<String, String>> allJobs;

    // Fetch list of all values from loaded data, without duplicates, for a given column
    public static ArrayList<String> findAll(String field) {
        loadData(); // Load data, if not already loaded
        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);
        return values;
    }

    // Fetch all jobs data (no filter applied)
    public static ArrayList<HashMap<String, String>> findAll() {
        loadData(); // Load data, if not already loaded
        return new ArrayList<>(allJobs);
    }

    // Returns results of search in jobs data by key/value, using case-insensitive matching
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        loadData(); // Load data, if not already loaded
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column);

            // Case-insensitive comparison
            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    // Search all columns for the given term with case-insensitive matching
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        loadData(); // Load data, if not already loaded
        ArrayList<HashMap<String, String>> results = new ArrayList<>();

        for (HashMap<String, String> job : allJobs) {
            for (String column : job.keySet()) {
                String jobValue = job.get(column);

                // Case-insensitive comparison
                if (jobValue != null && jobValue.toLowerCase().contains(value.toLowerCase())) {
                    if (!results.contains(job)) {
                        results.add(job);
                    }
                    break; // Exit inner loop once a match is found for this job
                }
            }
        }

        return results;
    }

    // Load job data from the CSV file into a list of hashmaps
    private static void loadData() {
        // Load data only once
        if (isDataLoaded) {
            return;
        }

        try {
            // Open the CSV file and set up column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // Flag the data as loaded, so we don't load it again
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }
}


