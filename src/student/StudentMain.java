package student;

import rs.etf.sab.operations.*;
import org.junit.Test;
import student.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.math.BigDecimal;
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

//        TestHandler.createInstance(
//                articleOperations,
//                buyerOperations,
//                cityOperations,
//                generalOperations,
//                orderOperations,
//                shopOperations,
//                transactionOperations
//        );
//
//        TestRunner.runTests();
        
        
        if (true) {
            generalOperations.eraseAll();

            int idA = cityOperations.createCity("A");
            int idB = cityOperations.createCity("B");
            int idC = cityOperations.createCity("C");
            int idD = cityOperations.createCity("D");
            int idE = cityOperations.createCity("E");
            int idF = cityOperations.createCity("F");
            cityOperations.connectCities(idA, idB, 6);
            cityOperations.connectCities(idA, idC, 7);
            cityOperations.connectCities(idA, idD, 3);
            cityOperations.connectCities(idD, idC, 1);
            cityOperations.connectCities(idD, idE, 2);
            cityOperations.connectCities(idD, idF, 5);

            int shopB = shopOperations.createShop("shopB", "B");
            int shopC = shopOperations.createShop("shopC", "C");
            int shopE = shopOperations.createShop("shopE", "E");
            shopOperations.setDiscount(shopE, 30);
            int laptop = articleOperations.createArticle(shopE, "laptop", 1000);
            int monitor = articleOperations.createArticle(shopE, "monitor", 200);
            int stolica = articleOperations.createArticle(shopC, "stolica", 100);
            int sto = articleOperations.createArticle(shopB, "sto", 200);
            shopOperations.increaseArticleCount(laptop, 10);
            shopOperations.increaseArticleCount(monitor, 10);
            shopOperations.increaseArticleCount(stolica, 15);
            shopOperations.increaseArticleCount(sto, 5);

            int buyer = buyerOperations.createBuyer("kupac", idA);
            buyerOperations.increaseCredit(buyer, new BigDecimal("20000"));
            int order = buyerOperations.createOrder(buyer);
            orderOperations.addArticle(order, laptop, 5);
            orderOperations.addArticle(order, monitor, 4);
            orderOperations.addArticle(order, stolica, 3);
            orderOperations.addArticle(order, sto, 4);

            orderOperations.completeOrder(order);
            System.out.println(orderOperations.getSentTime(order).getTime().toString());
//            System.out.println(orderOperations.getRecievedTime(order).toString());
            System.out.println("FinalPrice: " + orderOperations.getFinalPrice(order));
            System.out.println("DiscountSum: " + orderOperations.getDiscountSum(order));

//            generalOperations.eraseAll();
        }

        if (false) {
            generalOperations.eraseAll();

            int cityB = cityOperations.createCity("B");
            int cityC1 = cityOperations.createCity("C1");
            int cityA = cityOperations.createCity("A");
            int cityC2 = cityOperations.createCity("C2");
            int cityC3 = cityOperations.createCity("C3");
            int cityC4 = cityOperations.createCity("C4");
            int cityC5 = cityOperations.createCity("C5");
            cityOperations.connectCities(cityB, cityC1, 8);
            cityOperations.connectCities(cityC1, cityA, 10);
            cityOperations.connectCities(cityA, cityC2, 3);
            cityOperations.connectCities(cityC2, cityC3, 2);
            cityOperations.connectCities(cityC3, cityC4, 1);
            cityOperations.connectCities(cityC4, cityA, 3);
            cityOperations.connectCities(cityA, cityC5, 15);
            cityOperations.connectCities(cityC5, cityB, 2);

//            CityGraph.findShortestPath(cityA, cityB);
//            System.out.println(CityGraph.findMinDistance(cityA, cityB));

//            generalOperations.eraseAll();
        }
    }
}