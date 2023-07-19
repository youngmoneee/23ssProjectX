import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductDelete {
    final static String driver = "org.mariadb.jdbc.Driver";
    final static String db_url = "jdbc:mariadb://localhost:3306/px";
    final static String userId = "dev";
    final static String userPw = "dev";

    public static void main (String[] args) {
        int     productCode;

        try (Scanner sc = new Scanner(System.in)) {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(db_url, userId, userPw)) {
                if (conn == null || conn.isClosed()) throw new SQLException();

                while (true) {
                    System.out.print("상품 코드 : ");
                    productCode = sc.nextInt();
                    System.out.println();

                    deleteProduct(conn, productCode);
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

    private static void deleteProduct(Connection conn, int productCode) throws SQLException {
        String sql = "DELETE FROM 상품 WHERE 상품코드 = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productCode);
            int rs = pstmt.executeUpdate();
            System.out.println(rs > 0 ? "삭제 성공" : "존재하지 않음");
        }
    }
}