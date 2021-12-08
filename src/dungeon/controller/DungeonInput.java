package dungeon.controller;

public class DungeonInput {

  private final String rows;
  private final String columns;
  private final String interconnectivity;
  private final String isWrapping;
  private final String percentage;
  private final String otyugh;

  public DungeonInput(String rows, String columns, String interconnectivity,
                      String percentage, String otyugh,String isWrapping) {
    this.rows = rows;
    this.columns = columns;
    this.interconnectivity = interconnectivity;
    this.isWrapping = isWrapping;
    this.percentage = percentage;
    this.otyugh = otyugh;
  }

  public String getRows() {
    return rows;
  }

  public String getColumns() {
    return columns;
  }

  public String getInterconnectivity() {
    return interconnectivity;
  }

  public String isWrapping() {
    return isWrapping;
  }

  public String getPercentage() {
    return percentage;
  }

  public String getOtyugh() {
    return otyugh;
  }
}
