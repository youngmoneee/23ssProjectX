import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductSearch {
    final static String driver = "org.mariadb.jdbc.Driver";
    final static String db_url = "jdbc:mariadb://localhost:3306/px";
    final static String userId = "dev";
    final static String userPw = "dev";

    public static void main (String[] args) {
        String  productName;

        try (Scanner sc = new Scanner(System.in)) {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(db_url, userId, userPw)) {
                if (conn == null || conn.isClosed()) throw new SQLException();

                while (true) {
                    System.out.print("상품 명 : ");
                    productName = sc.next();
                    System.out.println();

                    selectProduct(conn, productName);
                    System.out.println("데이터 조회 완료\n");
                    System.out.print("* 종료하려면 exit 입력 : ");
                    if (sc.next().equals("exit")) break;
                    sc.nextLine();
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("DB 연결 실패");
        } catch (SQLException e) {
            System.err.println("데이터 조회 실패");
        } catch (InputMismatchException e) {
            System.err.println("입력 값 매치 실패");
        }
    }

    private static void selectProduct(Connection conn, String productName) throws SQLException {
        String sql = "SELECT * FROM 상품 WHERE 상품명 LIKE CONCAT('%', ?, '%')";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    System.out.println("상품 명 : " + rs.getString("상품명"));
                    System.out.println("가격 : " + rs.getInt("가격"));
                    System.out.println("재고 수량 : " + rs.getInt("재고수량"));
                    System.out.println();
                }
            }
        }
    }
}