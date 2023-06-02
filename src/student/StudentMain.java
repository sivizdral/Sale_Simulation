package student;

import rs.etf.sab.operations.*;

import org.junit.Assert;
import org.junit.Test;
import student.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.math.BigDecimal;
import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new ci190183_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new ci190183_BuyerOperations();
        CityOperations cityOperations = new ci190183_CityOperations();
        GeneralOperations generalOperations = new ci190183_GeneralOperations();
        OrderOperations orderOperations = new ci190183_OrderOperations();
        ShopOperations shopOperations = new ci190183_ShopOperations();
        TransactionOperations transactionOperations = new ci190183_TransactionOperations();
        
        ShortestPath.getInstance().general = generalOperations;

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
        
    }
}