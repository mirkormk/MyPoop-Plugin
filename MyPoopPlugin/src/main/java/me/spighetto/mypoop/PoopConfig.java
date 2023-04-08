package me.spighetto.mypoop;

public class PoopConfig {
    private final int trigger;
    private int limit;
    private final long delay;
    private final Boolean namedPoop;
    private final String colorPoopName;
    private final String poopDisplayName;
    private final String message;
    private final String messageAtLimit;
    private final int wherePrint;
    private Boolean allCropsNearby;
    private double radius;
    private Boolean randomGrow;

    public PoopConfig(
            int trigger,
            int limit,
            long delay,
            Boolean namedPoop,
            String colorPoopName,
            String poopDisplayName,
            String message,
            String messageAtLimit,
            int wherePrint,
            boolean allCropsNearby,
            double radius,
            boolean randomGrow
    ){
        this.trigger = trigger;
        this.limit = limit;
        this.delay = delay;
        this.namedPoop = namedPoop;
        this.colorPoopName = colorPoopName;
        this.poopDisplayName = poopDisplayName;
        this.message = message;
        this.messageAtLimit = messageAtLimit;
        this.wherePrint = wherePrint;
        this.allCropsNearby = allCropsNearby;
        this.radius = radius;
        this.randomGrow = randomGrow;
    }

    public int getTrigger(){
        return trigger;
    }

    public int getLimit(){
        return limit;
    }
    public void setLimit(int limit){
        this.limit = limit;
    }

    public long getDelay(){
        return delay;
    }

    public Boolean getNamedPoop(){
        return namedPoop;
    }

    public String getColorPoopName(){
        return colorPoopName;
    }

    public String getPoopDisplayName(){
        return poopDisplayName;
    }

    public String getMessage(){
        return message;
    }

    public String getMessageAtLimit(){
        return messageAtLimit;
    }

    public int getWherePrint(){
        return wherePrint;
    }

    public Boolean getAllCropsNearby(){return allCropsNearby;}
    public double getRadius(){return radius;}
    public Boolean getRandomGrow(){return randomGrow;}
}
