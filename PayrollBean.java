/**
 * PayrollBean.java
 *
 * Represents a single employee payroll record.
 * Encapsulates pay computation logic using position-based rates
 * and nature-of-work allowances.
 */
public class PayrollBean {

    // -------------------------------------------------------------------------
    // Pay Rate Constants
    // -------------------------------------------------------------------------

    public static final int RATE_MANAGER    = 1000;
    public static final int RATE_SUPERVISOR = 700;
    public static final int RATE_STAFF      = 300;

    public static final int ALLOWANCE_FIELD  = 1000;
    public static final int ALLOWANCE_OFFICE = 0;

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private String name;
    private String department;
    private String natureWork;
    private String positionWork;

    private int   rate      = 0;
    private int   allowance = 0;
    private float hoursWorked = 0.0f;
    private float grossPay    = 0.0f;

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Sets the nature of work and updates the allowance accordingly.
     * Recalculates gross pay.
     */
    public void setNatureWork(String natureWork) {
        this.natureWork = natureWork;
        this.allowance  = resolveAllowance(natureWork);
        recalculateGrossPay();
    }

    /**
     * Sets the position and updates the hourly rate accordingly.
     * Recalculates gross pay.
     */
    public void setPositionWork(String positionWork) {
        this.positionWork = positionWork;
        this.rate         = resolveRate(positionWork);
        recalculateGrossPay();
    }

    /**
     * Sets hours worked and recalculates gross pay.
     */
    public void setHoursWorked(float hoursWorked) {
        this.hoursWorked = hoursWorked;
        recalculateGrossPay();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getName()        { return name; }
    public String getDepartment()  { return department; }
    public String getNatureWork()  { return natureWork; }
    public String getPositionWork(){ return positionWork; }
    public float  getHoursWorked() { return hoursWorked; }
    public int    getRate()        { return rate; }
    public int    getAllowance()    { return allowance; }

    /** Returns the pre-computed gross pay. No side effects. */
    public float getGrossPay() {
        return grossPay;
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /** Recomputes grossPay whenever a relevant field changes. */
    private void recalculateGrossPay() {
        grossPay = (hoursWorked * rate) + allowance;
    }

    /** Maps a position string to its corresponding hourly rate. */
    private int resolveRate(String position) {
        switch (position.toLowerCase()) {
            case "manager":    return RATE_MANAGER;
            case "supervisor": return RATE_SUPERVISOR;
            case "staff":      return RATE_STAFF;
            default:           return 0;
        }
    }

    /** Maps a nature-of-work string to its corresponding allowance. */
    private int resolveAllowance(String natureWork) {
        return natureWork.equalsIgnoreCase("field")
            ? ALLOWANCE_FIELD
            : ALLOWANCE_OFFICE;
    }
}