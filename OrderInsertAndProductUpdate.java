import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderInsertAndProductUpdate {
    final static String driver = "org.mariadb.jdbc.Driver";
    final static String db_url = "jdbc:mariadb://localhost:3306/px";
    final static String userId = "dev";
    final static String userPw = "dev";

    public static void main (String[] args) {
        int     productCode;
        int     orderAmount;

        try (Scanner sc = new Scanner(System.in)) {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(db_url, userId, userPw)) {
                if (conn == null || conn.isClosed()) throw new SQLException();

                while (true) {
                    System.out.print("상품 코드 : ");
                    productCode = sc.nextInt();
                    System.out.print("주문 수량 : ");
                    orderAmount = sc.nextInt();
                    System.out.println();

                    if (orderAmount <= 0) {
                        System.out.println("주문 수량은 음수 불가");
                        continue;
                    }
                    insertOrderAndUpdateProduct(conn, productCode, orderAmount);
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

    private static void insertOrderAndUpdateProduct(Connection conn, int productCode, int orderAmount) throws SQLException {
        String select = "SELECT 재고수량 FROM 상품 WHERE 상품코드 = ?";
        String insert = "INSERT INTO 상품주문(상품코드, 주문수량) VALUES(?, ?)";
        String update = "UPDATE 상품 SET 재고수량 = 재고수량 - ? WHERE 상품코드 = ?";

        try (
                PreparedStatement selectPstmt = conn.prepareStatement(select);
                PreparedStatement insertPstmt = conn.prepareStatement(insert);
                PreparedStatement updatePstmt = conn.prepareStatement(update);
            ) {
            conn.setAutoCommit(false);  //  트랜잭션 관리

            selectPstmt.setInt(1, productCode);
            try (ResultSet rs = selectPstmt.executeQuery()) {
                if (rs.next()) {
                    int nowAmount= rs.getInt("재고수량");
                    if (nowAmount < orderAmount) {
                        System.out.println("재고 부족");
                        return ;
                    }
                } else {
                    System.out.println("상품 조회 실패");
                    return ;
                }
            }

            insertPstmt.setInt(1, productCode);
            insertPstmt.setInt(2, orderAmount);
            updatePstmt.setInt(1, orderAmount);
            updatePstmt.setInt(2, productCode);
            if (insertPstmt.executeUpdate() > 0 && updatePstmt.executeUpdate() > 0) {
                conn.commit();
                System.out.println("주문 완료");
            } else {
                conn.rollback();
                System.out.println("주문 실패");
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);   //  원상복구
        }
    }
}