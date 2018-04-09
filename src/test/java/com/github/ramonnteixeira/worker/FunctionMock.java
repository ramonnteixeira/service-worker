package com.github.ramonnteixeira.worker;

public class FunctionMock {
    
    private int count = 0;
    
    public void executeWithDelay() {
        try {
            count();
            Thread.sleep(800);
        } catch (Exception e) {
        }
    }

    public void count() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}
