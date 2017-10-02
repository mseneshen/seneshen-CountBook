package ca.ualberta.seneshen_countbook;

import java.util.Date;

/**
 * Counter objects are responsible for storing information about a specific counter, and performing
 * basic actions such as incrementing, decrementing, and resetting the counter.
 * It also contains getters and setters for the variables that should be modified outside of the class itself.
 *
 * I included checks to make sure that the integer does not overflow, and that the counter does not go negative.
 */
public class Counter {
    private String name;
    private int countInit;
    private int countCurrent;
    private Date lastUpdated;
    private String comment;

    public Counter(String name, int countInit, String comment) {
        this.name = name;
        this.countInit = countInit;
        this.countCurrent = countInit;
        this.comment = comment;

        lastUpdated = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountInit() {
        return countInit;
    }

    public void setCountInit(int countInit) {
        this.countInit = countInit;
    }

    public int getCountCurrent() {
        return countCurrent;
    }

    public void setCountCurrent(int countCurrent) {
        this.countCurrent = countCurrent;
        updateDate();
    }

    public void resetCount() {
        countCurrent = countInit;
        updateDate();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void increment() {
        // Verify that we are not about to cause an integer overflow
        if(countCurrent < 2147483647) {
            this.countCurrent++;
            updateDate();
        }
    }

    public void decrement() {
        // Check to make sure we are staying positive
        if(countCurrent > 0) {
            this.countCurrent--;
            updateDate();
        }
    }

    private void updateDate(){
        lastUpdated = new Date();
    }
}
