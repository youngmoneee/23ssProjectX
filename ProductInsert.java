import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductInsert {
    final static String driver = "org.mariadb.jdbc.Driver";
    final static String db_url = "jdbc:mariadb://localhost:3306/px";
    final static String userId = "dev";
    final static String userPw = "dev";

    public static void main (String[] args) {
        String  productName;
        int     productPrice;
        int     productAmount;

        try (Scanner sc = new Scanner(System.in)) {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(db_url, userId, userPw)) {
                if (conn == null || conn.isClosed()) throw new SQLException();

                while (true) {
                    System.out.print("상품 명 : ");
                    productName = sc.next();
                    System.out.println();
                    System.out.print("상품 가격 : ");
                    productPrice = sc.nextInt();
                    System.out.println();
                    System.out.print("재고 수량 : ");
                    productAmount = sc.nextInt();

                    insertProduct(conn, productName, productPrice, productAmount);
                    System.out.println("데이터 입력 성공\n");
                    System.out.print("* 종료하려면 exit 입력 : ");
                    if (sc.next().equals("exit")) break;
                    sc.nextLine();
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("DB 연결 실패");
        } catch (SQLException e) {
            System.err.println("데이터 입력 실패");
        } catch (InputMismatchException e) {
            System.err.println("입력 값 매치 실패");
        }
    }

    private static void insertProduct(Connection conn, String productName, int productPrice, int productAmount) throws SQLException {
        String sql = "INSERT INTO 상품(상품명, 가격, 재고수량) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            pstmt.setInt(2, productPrice);
            pstmt.setInt(3, productAmount);
            pstmt.execute();
        }
    }
}