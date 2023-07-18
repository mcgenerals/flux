package com.baguars.flux.check;

import java.util.LinkedList;
import java.util.Queue;

public class Counter {

    long d = 1000;

    public Counter(){}
    public Counter(long d){
        this.d = d;
    }


    private Queue<Long> ticks = new LinkedList<>();

    public void onTick(){
        this.ticks.add(Long.valueOf(System.currentTimeMillis() + d));
    }

    public void onTick(int i){
        long time = System.currentTimeMillis();
        for(int z = 0; z < i; z++){
            this.ticks.add(Long.valueOf(time + d));
        }
    }

    public int getTick() {
        long time = System.currentTimeMillis();
        while (!this.ticks.isEmpty() && ((Long)this.ticks.peek()).longValue() < time)
            this.ticks.remove();
        return this.ticks.size();
    }

}
