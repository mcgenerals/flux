package com.baguars.flux.manager;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Performance {

    public ConcurrentLinkedQueue<BetterLong> delays = new ConcurrentLinkedQueue<>();

    public Performance(){}

    public long check(){
        int i = 0;
        long sum = 0;
        for(BetterLong bl : delays){
            if( System.currentTimeMillis() - bl.d >= 1000 * 60){
                delays.remove(bl);
            }else {
                i++;
                sum += bl.i;
            }
        }
        return sum / i;
    }

    public void add(long delay){
        //System.out.println("delay="+delay);
        delays.add( new BetterLong(delay) );
    }

}
