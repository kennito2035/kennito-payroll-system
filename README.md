# Kennito Industries Payroll System

A **Computer Programming II laboratory activity** - a desktop payroll calculator built with **Java Swing**. Enter employee details, compute gross pay automatically, view a formatted summary, and export every record to a CSV file saved alongside the program.

---

## Preview

> _A simple, clean form for capturing employee payroll data._

| Field | Description |
|---|---|
| Full Name | Employee's full name |
| Department | Department they belong to |
| Nature of Work | Field or Office |
| Position | Manager, Supervisor, or Staff |
| No. of Hours Worked | Numeric input |

---

## Features

- **Automatic pay computation** — gross pay is calculated from position-based hourly rates and a field work allowance
- **Input validation** — catches empty fields, non-numeric hours, and zero/negative values before processing
- **CSV export** — appends each record to `Payroll_2025.csv`, saved in the same directory as the program
- **Auto-header** — writes the CSV column header automatically on first run
- **Input length caps** — enforces character limits on text fields to prevent overflow

---

## Pay Rates

| Position | Hourly Rate |
|---|---|
| Manager | ₱1,000 |
| Supervisor | ₱700 |
| Staff | ₱300 |

| Nature of Work | Allowance |
|---|---|
| Field | ₱1,000 |
| Office | ₱0 |

**Gross Pay Formula:**
```
Gross Pay = (Hours Worked × Hourly Rate) + Allowance
```

---

## Project Structure

```
├── PayrollGUI.java      # Swing UI — form layout, validation, and event handling
├── PayrollBean.java     # Data model — stores employee info and computes gross pay
├── PayrollExport.java   # File I/O — writes payroll records to CSV
└── Payroll_2025.csv     # Generated on first run (not tracked by Git)
```

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) **8 or higher**
- A terminal or any Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Compile

```bash
javac PayrollGUI.java PayrollBean.java PayrollExport.java
```

### Run

```bash
java PayrollGUI
```

The GUI window will open. Fill in the form and click **Submit** to process a payroll entry.

---

## CSV Output

Records are exported to `Payroll_2025.csv` in the same directory as the compiled `.class` files. The file is created automatically on first use.

**Sample output:**
```
Name,Department,Nature of Work,Position,Hours Worked,Rate,Allowance,Net Pay
Juan dela Cruz,Engineering,Field,Manager,40.0,1000,1000,41000.0
Maria Santos,Finance,Office,Supervisor,35.0,700,0,24500.0
```

---

## Built With

- **Java** — core language
- **Java Swing** — GUI framework
- **Java AWT** — layout managers
