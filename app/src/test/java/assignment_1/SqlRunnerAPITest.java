package assignment_1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlRunnerAPITest {
    private SqlRunnerAPI sr;
    @BeforeEach
    void setUp() throws SQLException, ParserConfigurationException, IOException, SAXException {
       sr = new SqlRunnerAPI("src/main/resources/queries.xml", "jdbc:mysql://localhost:3306/sakila", "root", "8832");
    }

    @AfterEach
    void tearDown() throws SQLException {
        sr.close();
    }

    @Test
    @Order(1)
    void selectOne() throws SQLException, XPathExpressionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        Film m = sr.selectOne("get_movie_by_id", 12, Film.class);
        // Assuming that the DB has that movie with film_id=12, this should pass
        assertEquals("ALASKA PHANTOM", m.title);

        Film m2 = sr.selectOne("get_movie_by_id", 10000, Film.class);
        assertNull(m2);

        Actor actorToSearch = new Actor();
        actorToSearch.setActor_id(5);
        actorToSearch.setFirst_name("JOHNNY");

        Actor outputActor = sr.selectOne("get_actor_using_obj", actorToSearch, Actor.class);
        assertEquals(5, outputActor.actor_id);
        assertEquals("JOHNNY", outputActor.first_name);
        assertEquals("LOLLOBRIGIDA", outputActor.last_name);

        /*More than one record as output*/
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
               sr.selectOne("get_all_actors", null, Actor.class);
            }
        });
    }

    @Test
    @Order(2)
    void selectMany() throws SQLException, XPathExpressionException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        int[] arr = {1, 2};

        /*Passing int[]*/
        List<Actor> m = sr.selectMany("get_actors_with_ids", arr, Actor.class);
        assertEquals(m.size(), 2);
        assertTrue(m.get(0).actor_id == 1 || m.get(0).actor_id == 2);
        assertTrue(m.get(1).actor_id == 1 || m.get(1).actor_id == 2);

        /*Passing a String as queryParam*/
        List<Actor> m2 = sr.selectMany("get_actors_by_first_name", "SANDRA", Actor.class);
        assertEquals(m2.size(), 2);
        assertEquals("SANDRA", m2.get(0).first_name);
        assertEquals("SANDRA", m2.get(1).first_name);

        /*Passing an ArrayList as queryParam */
        ArrayList<Integer> arrL = new ArrayList<>();
        arrL.add(3);
        arrL.add(4);

        List<Actor> m3 = sr.selectMany("get_actors_with_ids_al", arrL, Actor.class);

        assertEquals(m3.size(), 2);
        assertTrue(m3.get(0).actor_id == 3 || m3.get(0).actor_id == 4);
        assertTrue(m3.get(1).actor_id == 3 || m3.get(1).actor_id == 4);

        /*Passing a String ArrayList as queryParam */
        ArrayList<String> arrLStr = new ArrayList<>();
        arrLStr.add("SANDRA");
        arrLStr.add("JOHNNY");

        List<Actor> m4 = sr.selectMany("get_actors_with_firstname_al", arrLStr, Actor.class);

        assertEquals(m4.size(), 4);
        for(int i = 0; i < 4; i++){
            assertTrue(m4.get(i).first_name.equals("SANDRA") || m4.get(i).first_name.equals("JOHNNY"));
        }

        /* Trying to execute a query with mistmatching paramType and type of queryParam. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.selectMany("get_actors_with_ids", "Actor", Actor.class);
            }
        });

        /* Searching for a non-existent sql id in the xml file. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.selectMany("does_not_exist", null, Actor.class);
            }
        });
    }

    @Test
    @Order(3)
    void insert() throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException {
        Actor actor = new Actor();
        actor.setActor_id(1000);
        actor.setFirst_name("RITIK");
        actor.setLast_name("GARG");
        actor.setLast_update("2006-02-15 04:34:33");
        int c = sr.insert("insert", actor);
        assertEquals(1, c);

        /* Trying to execute a query with mistmatching paramType and type of queryParam. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.insert("insert", "Actor");
            }
        });

        /* Searching for a non-existent sql id in the xml file. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.insert("does_not_exist", null);
            }
        });
    }

    @Test
    @Order(4)
    void update() throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException {
        Actor actor = new Actor();
        actor.setActor_id(1000);
        actor.setLast_name("MATHEWS");
        int c = sr.update("update_actor_last_name", actor);
        assertEquals(1, c);

        /* Trying to execute a query with mistmatching paramType and type of queryParam. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.update("update_actor_last_name", "xyz");
            }
        });

        /* Searching for a non-existent sql id in the xml file. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.update("does_not_exist", "xyz");
            }
        });
    }



    @Test
    @Order(5)
    void delete() throws SQLException, XPathExpressionException, NoSuchFieldException, IllegalAccessException {
        int c = sr.delete("delete", 1000);
        assertEquals(1, c);

        /* Trying to execute a query with mistmatching paramType and type of queryParam. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.delete("delete", "Actor");
            }
        });

        /* Searching for a non-existent sql id in the xml file. */
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sr.delete("does_not_exist", null);
            }
        });
    }
}
