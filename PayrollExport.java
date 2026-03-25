import java.io.*;
import java.net.URISyntaxException;

/**
 * PayrollExport.java
 *
 * Handles writing payroll records to a CSV file.
 * The file is always created in the same directory as the compiled
 * class file, regardless of where the program is launched from.
 * Automatically writes the header row if the file does not yet exist
 * or is empty, then appends each new record on a new line.
 */
public class PayrollExport {
    private static final String FILE_NAME = "Payroll_2025.csv";

    private static final String CSV_HEADER =
        "Name,Department,Nature of Work,Position,Hours Worked,Rate,Allowance,Net Pay";

    /**
     * Resolves the directory that contains the compiled .class file
     * and returns a File pointing to the CSV inside it.
     * Falls back to the working directory if the path cannot be determined.
     */
    private File resolveOutputFile() {
        try {
            File classLocation = new File(
                PayrollExport.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );
            // getLocation() points to the .class file itself or its parent dir
            File dir = classLocation.isDirectory() ? classLocation : classLocation.getParentFile();
            return new File(dir, FILE_NAME);
        } catch (URISyntaxException e) {
            // Graceful fallback to the current working directory
            System.err.println("Could not resolve class path; using working directory.");
            return new File(FILE_NAME);
        }
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Appends a payroll record to the CSV file.
     * Writes the header first if the file is new or empty.
     *
     * @param name        Employee full name
     * @param department  Department name
     * @param natureWork  Nature of work (Field / Office)
     * @param position    Job position
     * @param hoursWorked Number of hours worked
     * @param rate        Hourly rate
     * @param allowance   Field allowance
     * @param netPay      Computed net pay
     */
    public void write(
            String name,
            String department,
            String natureWork,
            String position,
            String hoursWorked,
            String rate,
            String allowance,
            String netPay) {

        File outputFile = resolveOutputFile();
        boolean needsHeader = fileIsEmptyOrMissing(outputFile);

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {

            if (needsHeader) {
                writer.println(CSV_HEADER);
            }

            String[] fields = { name, department, natureWork, position,
                                 hoursWorked, rate, allowance, netPay };

            writer.println(buildCsvRow(fields));

            System.out.println(
                "\nRecord saved to: " + "\'" + outputFile.getAbsolutePath() + "\'");

        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true if the target file does not exist or has no content.
     * This check is done BEFORE opening the FileWriter so the result
     * is not affected by the writer truncating or creating the file.
     */
    private boolean fileIsEmptyOrMissing(File file) {
        return !file.exists() || file.length() == 0;
    }

    /**
     * Trims, escapes, and joins fields into a single CSV row string.
     */
    private String buildCsvRow(String[] fields) {
        String[] escaped = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            escaped[i] = escapeCsvField(fields[i].trim());
        }
        return String.join(",", escaped);
    }

    /**
     * Wraps a field in double-quotes and escapes any internal double-quotes
     * if the field contains commas, quotes, or newline characters.
     */
    private String escapeCsvField(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }
}