package org.unixlibre.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class SQLExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SQLExecutor.class);
    private final static String sqlComment = "--";
    private final static String endOfStatement = ";";

    private Connection conn;
    private final String sql;

    public SQLExecutor(Connection conn, String sql) {
        this.conn = conn;
        this.sql = sql;
    }

    public void execute() throws SQLException, IOException {
        Statement stmt = null;
        StringBuilder sb = new StringBuilder(1024);
        String line;

        try (BufferedReader reader = new BufferedReader(new StringReader(sql))) {
            stmt = conn.createStatement();

            while ((line = reader.readLine()) != null) {
                if (isSQL(line)) {
                    sb.append(line);
                    if (line.endsWith(endOfStatement)) {
                        sb.deleteCharAt(sb.length() - 1);
                        stmt.execute(sb.toString());
                        sb.setLength(0);
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            logger.debug("SQL = {}", sb.toString());
            throw ex;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException se) {
                    logger.error("SQLException", se);
                }
            }
        }
    }

    boolean isSQL(String s) {
        String line = s.trim();
        return (line.length() > 0) &&
                (!line.startsWith(sqlComment));
    }
}
