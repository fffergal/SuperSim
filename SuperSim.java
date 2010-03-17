
/**
 * Write a description of class Simulation here.
 * 
 * @author Fergal Hainey
 * @version 2010.03.02
 */

import java.util.Random;
import java.util.ArrayList;

public class SuperSim
{
    private Random rand;
    private ArrayList<CheckOut> checkOuts;
    // TODO: add another CheckOut, expressCheckOut , initialise it in the constructor - Chiedu
    private final int iterationLimit;
    private int iterationsSoFar;
    private double customerProb;
    private int totalCustomers;
    // Stuff for the normal distribution
    int itemsMean;
    int itemsStandardDeviation;
    int itemsLowerLimit;
    int itemsUpperLimit;
    // Constants
    // TODO: add shopFloorConstant. Remember to change the constructors and add an accessor - Yowana
    // TODO: add expressCheckoutItemsLimit constant, remember the constructors, probs doesn't need an accessor - Chiedu
    final int checkOutConstant;
    // Non-generic fields for diagnostics
    private int totalCustomersProcessed;
    private int totalWaitIterations;

    /**
     * Constructor for objects of class SuperSim
     */
    public SuperSim(int iterations, double customerProb, int itemsMean, int itemsStandardDeviation, int itemsUpperLimit, int itemsLowerLimit, int checkOutConstant)
    {
        // Structure
        rand = new Random();
        checkOuts = new ArrayList<CheckOut>();
        CheckOut checkOut = new CheckOut();
        checkOuts.add(checkOut);
        
        // SuperSim
        iterationsSoFar = 0;
        iterationLimit = iterations;
        
        // Initialising
        this.customerProb = customerProb;
        this.itemsMean = itemsMean;
        this.itemsStandardDeviation = itemsStandardDeviation;
        this.itemsLowerLimit = itemsLowerLimit;
        this.itemsUpperLimit = itemsUpperLimit;
        this.checkOutConstant = checkOutConstant;
        
        // Run the simulation
        for (int i = 0; i < iterations; i++) {
            oneIteration();
        }
    }
    
    public SuperSim(int iterations)
    {
        this(iterations, (0.5/60.0), 11, 4, 1, 30, 5);
    }
    
    private void oneIteration()
    {
        // Move customers who are done out the queues
        int checkOutsCount = checkOuts.size();
        for (int i = 0; i < checkOutsCount; i++) {
            CheckOut checkOut = checkOuts.get(i);
            if (!checkOut.isEmpty() && checkOut.get(0).getDepart() == iterationsSoFar) {
                checkOut.remove(checkOut.get(0));
                totalCustomersProcessed++;
            }
        }
        
        // TODO: move Customers from ShopFloor to CheckOuts - Janie
        
        // Is there a new customer?
        if (rand.nextFloat() < customerProb) {
            newCustomer();
        }
        
        for (CheckOut checkOut : checkOuts) {
            // Start processing customer at front of queue if necessary
            if (!checkOut.isEmpty()) {
                Customer customer = checkOut.get(0);
                if (customer.getDepart() == 0) {
                    customer.process(iterationsSoFar);
                    totalWaitIterations += iterationsSoFar - customer.getArrived();
                }
            }
        }
        
        iterationsSoFar++;
    }
    
    private void newCustomer()
    {
        Customer customer = new Customer(this);
        // TODO: new Customers go to the ShopFloor instead of direct to CheckOuts - Janie
        // TODO: use following code in a loop for the Customers coming out the ShopFloor in oneIteration() - Janie
        // TODO: use the expressCheckout if less than expressCheckoutItemsLimit - Chiedu
        int bestCheckOut = 0;
        int bestCheckOutSize = 4;
        int checkOutCount = checkOuts.size();
        for (int i = 0; i < checkOutCount; i++) {
            int checkOutSize = checkOuts.get(i).size();
            if (checkOutSize < bestCheckOutSize) {
                bestCheckOut = i;
                bestCheckOutSize = checkOutSize;
            }
        }
        if (bestCheckOutSize < 4) {
            checkOuts.get(bestCheckOut).add(customer);
        }
        else {
            CheckOut newCheckOut = new CheckOut();
            newCheckOut.add(customer);
            checkOuts.add(newCheckOut);
        }
        // TODO: keep totalCustomers++ here in newCustomer() - Janie I guess, but don't really have to do anything
        totalCustomers++;
    }
    
    public void printInfo()
    {
        System.out.println(iterationLimit + " iterations");
        System.out.println(customerProb + " probability of a new customer every iteration");
        System.out.println(checkOuts.size() + " Check-Ins needed");
        System.out.println(totalCustomers + " customers in total");
        System.out.println(totalCustomersProcessed + " customers processed");
        System.out.println("mean of " + ((float) totalWaitIterations / (float) totalCustomersProcessed) + " iterations before customer reaches front of queue");
        System.out.println();
    }
    
    // Accessors
    // Distribution
    public double getCustomerProb()
    {
    	return customerProb;
    }
    
    public int getItemsMean()
    {
    	return itemsMean;
    }
    
    public int getItemsStandardDeviation()
    {
    	return itemsStandardDeviation;
    }
    
    public int getItemsLowerLimit()
    {
    	return itemsLowerLimit;
    }
    
    public int getItemsUpperLimit()
    {
    	return itemsUpperLimit;
    }
    // Simulation
    public int getIterationsSoFar()
    {
        return iterationsSoFar;
    }
    
    public Random getRand()
    {
        return rand;
    }
    // Constants
    public int getCheckOutConstant()
    {
        return checkOutConstant;
    }
}
