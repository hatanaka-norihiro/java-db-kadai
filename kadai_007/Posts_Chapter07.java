package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        // 投稿データリスト
        String[][] postsList = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
            { "1002", "2023-02-08", "お疲れ様です！", "12" },
            { "1003", "2023-02-09", "今日も頑張ります！", "18" },
            { "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
            { "1002", "2023-02-10", "明日から連休ですね！", "20" }
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "hata1012"
            );

            System.out.println("データベース接続成功");

            // SQLクエリを準備
            String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
            statement = con.prepareStatement(sql);

            // リストの1行目から順番に読み込む
            for (String[] row : postsList) {
            	statement.setString(1, row[0]); 				// user_id
            	statement.setDate(2, Date.valueOf(row[1])); 	// posted_at
            	statement.setString(3, row[2]); 				// post_content
            	statement.setInt(4, Integer.parseInt(row[3])); // likes
            	statement.addBatch();
            }
            
            int[] count = statement.executeBatch();
            System.out.println("レコード追加を実行します");
            System.out.println(count.length + "件のレコードが追加されました");
            
            // レコード検索
            String sqlSearch = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = '1002'";
            statement = con.prepareStatement(sqlSearch);
            rs = statement.executeQuery();
            
            System.out.println("ユーザーIDが1002のレコードを検索しました");
            int recordNum = 1;
            while (rs.next()) {
                Date postedAt = rs.getDate("posted_at");
                String postContent = rs.getString("post_content");
                int likes = rs.getInt("likes");
                System.out.println(recordNum + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
                recordNum++;
            }            
        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (statement != null) statement.close(); } catch (SQLException ignore) {}
            try { if (con != null) con.close(); } catch (SQLException ignore) {}
        }
	}
}
