package student;

import rs.etf.sab.operations.*;
import org.junit.Test;
import student.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new MyArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new MyBuyerOperations();
        CityOperations cityOperations = new MyCityOperations();
        GeneralOperations generalOperations = new MyGeneralOperations();
        OrderOperations orderOperations = new MyOrderOperations();
        ShopOperations shopOperations = new MyShopOperations();
        TransactionOperations transactionOperations = new MyTransactionOperations();

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