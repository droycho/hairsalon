import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class StylistTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteClientsQuery = "DELETE FROM clients *;";
      String deleteStylistsQuery = "DELETE FROM stylists *;";
      con.createQuery(deleteClientsQuery).executeUpdate();
      con.createQuery(deleteStylistsQuery).executeUpdate();
    }
  }

  @Test
  public void Stylist_instantiatesCorrectly_true() {
    Stylist myStylist = new Stylist("Paul Mitchell");
    assertEquals(true, myStylist instanceof Stylist);
  }
//
//   @Test
//   public void getSname_stylistInstantiatesWithSname_String() {
//     Stylist myStylist = new Stylist("Paul Mitchell");
//     assertEquals("Paul Mitchell", myStylist.getSname());
//   }
//
//   @Test
//   public void all_emptyAtFirst() {
//     assertEquals(Stylist.all().size(), 0);
//   }
//
//   @Test
//   public void equals_returnsTrueIfSsnamesAretheSame() {
//     Stylist firstStylist = new Stylist("Paul Mitchell");
//     Stylist secondStylist = new Stylist("Paul Mitchell");
//     assertTrue(firstStylist.equals(secondStylist));
//   }
//
//   @Test
//   public void save_savesIntoDatabase_true() {
//     Stylist myStylist = new Stylist("Paul Mitchell");
//     myStylist.save();
//     assertTrue(Stylist.all().get(0).equals(myStylist));
//   }
//
//   @Test
//   public void save_assignsIdToObject() {
//     Stylist myStylist = new Stylist("Paul Mitchell");
//     myStylist.save();
//     Stylist savedStylist = Stylist.all().get(0);
//     assertEquals(myStylist.getId(), savedStylist.getId());
//   }
//
//   @Test
//   public void find_findStylistInDatabase_true() {
//     Stylist myStylist = new Stylist("Paul Mitchell");
//     myStylist.save();
//     Stylist savedStylist = Stylist.find(myStylist.getId());
//     assertTrue(myStylist.equals(savedStylist));
//   }
//
//   @Test
//   public void getClients_retrievesALlClientsFromDatabase_clientsList() {
//     Stylist myStylist = new Stylist("Paul Mitchell");
//     myStylist.save();
//     Client firstClient = new Client("Mow the lawn", myStylist.getId());
//     firstClient.save();
//     Client secondClient = new Client("Do the dishes", myStylist.getId());
//     secondClient.save();
//     Client[] clients = new Client[] { firstClient, secondClient };
//     assertTrue(myStylist.getClients().containsAll(Arrays.asList(clients)));
//   }
}
