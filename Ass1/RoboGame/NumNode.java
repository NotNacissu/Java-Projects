
/**
 * Write a description of class NumNode here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public interface NumNode {
  int execute(Robot robot);

  NumNode FUEL_LEFT = new NumNode() {
    public int execute(Robot robot) {
      return robot.getFuel();
    }

    public String toString() {
      return "fuelLeft";
    }
  };

  NumNode OPP_LR = new NumNode() {
    public int execute(Robot robot) {
      return robot.getOpponentLR();
    }

    public String toString() {
      return "oppLR";
    }
  };

  NumNode OPP_FB = new NumNode() {
    public int execute(Robot robot) {
      return robot.getOpponentFB();
    }

    public String toString() {
      return "oppFB";
    }
  };

  NumNode NUM_BARRELS = new NumNode() {
    public int execute(Robot robot) {
      return robot.numBarrels();
    }

    public String toString() {
      return "numBarrels";
    }
  };

  NumNode BARREL_LR = new NumNode() {
    public int execute(Robot robot) {
      return robot.getClosestBarrelLR();
    }

    public String toString() {
      return "barrelLR";
    }
  };

  NumNode BARREL_FB = new NumNode() {
    public int execute(Robot robot) {
      return robot.getClosestBarrelFB();
    }

    public String toString() {
      return "barrelFB";
    }
  };

  NumNode WALL_DIST = new NumNode() {
    public int execute(Robot robot) {
      return robot.getDistanceToWall();
    }

    public String toString() {
      return "wallDist";
    }
  };
}
