import dao.DataRetriever;

public class Main {
    public static void main(String[] args) throws Exception {

        DataRetriever dr = new DataRetriever();

        System.out.println("CATEGORIES :");
        System.out.println(dr.getAllCategories());

        System.out.println("\nPRODUITS page 1 (size=3):");
        System.out.println(dr.getProductList(1, 3));

        System.out.println("\nPRODUITS contenant 'bluetooth' :");
        System.out.println(dr.getProductsByCriteria("bluetooth", null, null, null));
    }
}