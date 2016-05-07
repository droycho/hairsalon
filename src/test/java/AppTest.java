import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }
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

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Rook's Barber");
  }

  @Test
  public void stylistIsCreatedTest() {
    goTo("http://localhost:4567/");

    fill("#sname").with("Paul Mitchell");
    submit("#styleBtn");
    assertThat(pageSource()).contains("Your Stylist has been added to the Roster.");
  }

  @Test
  public void stylistIsDisplayedTest() {
    Stylist myStylist = new Stylist("Paul Mitchell");
    myStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Paul Mitchell");
  }

   @Test
   public void stylistShowPageDiplaySname() {
     goTo("http://localhost:4567/stylists/new");
     fill("#sname").with("Paul Mitchell");
     submit(".btn");
     click("a", withText("View Stylists"));
     click("a", withText("Paul Mitchell"));
     assertThat(pageSource()).contains("Paul Mitchell");
   }

   @Test
   public void stylistClientsFormIsDisplayed() {
     goTo("http://localhost:4567/stylists/new");
     fill("#sname").with("Paul Mitchell");
     submit(".btn");
     click("a", withText("View Stylists"));
     click("a", withText("Paul Mitchell"));
     click("a", withText("Add a new Client"));
     assertThat(pageSource()).contains("Add a Client for Paul Mitchell");
   }

   @Test
   public void allClientsDisplayCnameOnStylistPage() {
     Stylist myStylist = new Stylist ("Paul Mitchell");
     myStylist.save();
     Client firstClient = new Client ("Bill Murray", myStylist.getId());
     firstClient.save();
     Client secondClient = new Client("Tom Hanks", myStylist.getId());
     secondClient.save();
     String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
     goTo(stylistPath);
     assertThat(pageSource()).contains("Bill Murray");
     assertThat(pageSource()).contains("Tom Hanks");
   }

  @Test
  public void clientShowPage() {
    Stylist myStylist = new Stylist("Oribe");
    myStylist.save();
    Client myClient = new Client("Carrot Top", myStylist.getId());
    myClient.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    click("a", withText("Carrot Top"));
    assertThat(pageSource()).contains("Carrot Top");
    assertThat(pageSource()).contains("Return to Oribe");
  }

  @Test
  public void clientUpdate() {
    Stylist myStylist = new Stylist("Oribe");
    myStylist.save();
    Client myClient = new Client("Carrot Top", myStylist.getId());
    myClient.save();
    String clientPath = String.format("http://localhost:4567/stylists/%d/clients/%d", myStylist.getId(), myClient.getId());
    goTo(clientPath);
    fill("#cname").with("Bruce Lee");
    submit("#update-client");
    assertThat(pageSource()).contains("Bruce Lee");
  }

  @Test
  public void clientDelete() {
    Stylist myStylist = new Stylist("Oribe");
    myStylist.save();
    Client myClient = new Client("Carrot Top", myStylist.getId());
    myClient.save();
    String clientPath = String.format("http://localhost:4567/stylists/%d/clients/%d", myStylist.getId(), myClient.getId());
    goTo(clientPath);
    submit("#delete-client");
    assertEquals(0, Client.all().size());
  }

  @Test
  public void stylistDelete() {
    Stylist myStylist = new Stylist("Oribe");
    myStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    submit("#delete-stylist");
    assertEquals(0, Stylist.all().size());
  }
}
