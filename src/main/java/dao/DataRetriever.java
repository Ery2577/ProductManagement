package dao;

import connection.DBConnection;
import model.Category;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataRetriever {

    private Product mapToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(
                rs.getInt("category_id"),
                rs.getString("category_name")
        );

        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("product_name"));
        product.setPrice(rs.getDouble("price"));
        product.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
        product.setCategory(category);
        return product;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM Product_category GROUP BY id, name ORDER BY id";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                categories.add(new Category(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Product> getProductList(int page, int size) {
        List<Product> products = new ArrayList<>();
        int offset = Math.max(0, (page - 1) * size);

        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.price, p.creation_datetime, " +
                "pc.id AS category_id, pc.name AS category_name " +
                "FROM Product p " +
                "JOIN Product_category pc ON p.id = pc.product_id " +
                "ORDER BY p.id " +
                "LIMIT ? OFFSET ?";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByCriteria(String productName,
                                               String categoryName,
                                               Instant creationMin,
                                               Instant creationMax) {
        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 0, 0);
    }

    public List<Product> getProductsByCriteria(String productName,
                                               String categoryName,
                                               Instant creationMin,
                                               Instant creationMax,
                                               int page,
                                               int size) {

        List<Product> products = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id AS product_id, p.name AS product_name, p.price, p.creation_datetime, " +
                        "pc.id AS category_id, pc.name AS category_name " +
                        "FROM Product p " +
                        "JOIN Product_category pc ON p.id = pc.product_id " +
                        "WHERE 1=1 "
        );

        if (productName != null && !productName.trim().isEmpty()) {
            sql.append(" AND p.name ILIKE ?");
            parameters.add("%" + productName + "%");
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append(" AND pc.name ILIKE ?");
            parameters.add("%" + categoryName + "%");
        }

        if (creationMin != null) {
            sql.append(" AND p.creation_datetime >= ?");
            parameters.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            sql.append(" AND p.creation_datetime <= ?");
            parameters.add(Timestamp.from(creationMax));
        }

        sql.append(" ORDER BY p.id");

        if (size > 0 && page > 0) {
            int offset = (page - 1) * size;
            sql.append(" LIMIT ? OFFSET ?");
            parameters.add(size);
            parameters.add(offset);
        }

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}