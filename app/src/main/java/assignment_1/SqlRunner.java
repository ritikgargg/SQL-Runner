package assignment_1;

import javax.xml.xpath.XPathExpressionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlRunner {
    /**
     * Executes a select query that returns a single or no record.
     * @param queryId Unique ID of the query in the queries.xml file.
     * @param queryParam Parameter(s) to be used in the query.
     * @param resultType Type of the object that will be returned after
     *                   populating it with the data returned by the SQL.
     * @return The object populated with the SQL results.
     */
    <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType) throws SQLException, XPathExpressionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;

    /**
     * Same as {@link #selectOne(String, Object, Class)} except that this one
     * returns multiple rows.
     * @param queryId
     * @param queryParam
     * @param resultItemType
     * @return
     */
    <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultItemType) throws SQLException, XPathExpressionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;

    /**
     * Execute an update statement and return the number of rows affected.
     * @param queryId
     * @param queryParam
     * @return
     */
    <T> int update(String queryId, T queryParam) throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException;

    /**
     * Execute an insert statement and return the number of rows affected.
     * @param queryId
     * @param queryParam
     * @return
     */
    <T> int insert(String queryId, T queryParam) throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException;

    /**
     * Execute a delete statement and return the number of rows affected.
     * @param queryId
     * @param queryParam
     * @return
     */
    <T> int delete(String queryId, T queryParam) throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException;
}
