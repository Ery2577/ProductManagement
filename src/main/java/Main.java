import dao.DataRetriever;
import model.Category;
import model.Product;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class Main {
    private static final DataRetriever dataRetriever = new DataRetriever();

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("DÉMARRAGE DES TESTS DataRetriever (Exercice 7)");
        System.out.println("====================================================");

        testGetAllCategories();
        testProductListPagination();
        testProductsByCriteriaFiltres();
        testProductsByCriteriaFiltresPagination();

        System.out.println("====================================================");
        System.out.println("TESTS TERMINÉS. Vérifiez les résultats dans la console.");
        System.out.println("====================================================");
    }


    private static Instant dateMin(String dateStr) {
        if (dateStr == null) return null;
        return LocalDateTime.parse(dateStr + "T00:00:00").toInstant(ZoneOffset.UTC);
    }

    private static Instant dateMax(String dateStr) {
        if (dateStr == null) return null;
        return LocalDateTime.parse(dateStr + "T23:59:59.999").toInstant(ZoneOffset.UTC);
    }


    private static void testGetAllCategories() {
        System.out.println("\n--- 1. getAllCategories() [7.a] ---");
        List<Category> categories = dataRetriever.getAllCategories();
        categories.forEach(System.out::println);
    }


    private static void testProductListPagination() {
        System.out.println("\n--- 2. getProductList (Pagination) [7.b] ---");

        int[][] testCases = {{1, 10}, {1, 5}, {1, 3}, {2, 2}};

        for (int[] testCase : testCases) {
            int page = testCase[0];
            int size = testCase[1];
            System.out.printf("\n[PAGE %d, SIZE %d]\n", page, size);

            List<Product> products = dataRetriever.getProductList(page, size);
            products.forEach(System.out::println);
        }
    }


    private static void testProductsByCriteriaFiltres() {
        System.out.println("\n--- 3. getProductsByCriteria (Filtres seuls) [7.c] ---");

        String[][] testCases = {
                {"Dell", null, null, null},
                {null, "info", null, null},
                {"iPhone", "mobile", null, null},
                {null, null, "2024-02-01", "2024-03-01"},
                {"Samsung", "bureau", null, null},
                {"Sony", "informatique", null, null},
                {null, "audio", "2024-01-01", "2024-12-01"},
                {null, null, null, null}
        };

        for (String[] testCase : testCases) {
            String pn = testCase[0];
            String cn = testCase[1];
            Instant min = dateMin(testCase[2]);
            Instant max = dateMax(testCase[3]);

            System.out.printf("\n[FILTRES: PN='%s', CN='%s', Min=%s, Max=%s]\n", pn, cn, testCase[2], testCase[3]);

            List<Product> products = dataRetriever.getProductsByCriteria(pn, cn, min, max);
            products.forEach(System.out::println);
        }
    }


    private static void testProductsByCriteriaFiltresPagination() {
        System.out.println("\n--- 4. getProductsByCriteria (Filtres et Pagination) [7.d] ---");

        Object[][] testCases = {
                {null, null, null, null, 1, 10},
                {"Dell", null, null, null, 1, 5},
                {null, "informatique", null, null, 1, 10}
        };

        for (Object[] testCase : testCases) {
            String pn = (String) testCase[0];
            String cn = (String) testCase[1];
            int page = (int) testCase[4];
            int size = (int) testCase[5];

            System.out.printf("\n[FILTRES+PAGI: PN='%s', CN='%s', Page=%d, Size=%d]\n", pn, cn, page, size);

            List<Product> products = dataRetriever.getProductsByCriteria(pn, cn, null, null, page, size);
            products.forEach(System.out::println);
        }
    }
}