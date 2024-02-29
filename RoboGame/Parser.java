import java.util.*;
import java.util.regex.*;

/**
 * See assignment handout for the grammar.
 * You need to implement the parse(..) method and all the rest of the parser.
 * There are several methods provided for you:
 * - several utility methods to help with the parsing
 * See also the TestParser class for testing your code.
 */
public class Parser {


    // Useful Patterns

    static final Pattern NUMPAT = Pattern.compile("-?[1-9][0-9]*|0"); 
    static final Pattern OPENPAREN = Pattern.compile("\\(");
    static final Pattern CLOSEPAREN = Pattern.compile("\\)");
    static final Pattern OPENBRACE = Pattern.compile("\\{");
    static final Pattern CLOSEBRACE = Pattern.compile("\\}");

    //----------------------------------------------------------------
    /**
     * The top of the parser, which is handed a scanner containing
     * the text of the program to parse.
     * Returns the parse tree.
     */
    ProgramNode parse(Scanner s) {
        // Set the delimiter for the scanner.
        s.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");
        // THE PARSER GOES HERE
        ProgramNode tree = parseProg(s);
        // Call the parseProg method for the first grammar rule (PROG) and return the node
        return tree;
    }
    
    
    
    // PROG  ::= [ STMT ]*
    public ProgramNode parseProg(Scanner s) {
        List<ProgramNode> progs = new ArrayList<ProgramNode>();
        if (!s.hasNext())         {fail("No instructions", s);}
        while (s.hasNext()) {
            progs.add(parseSTMT(s));
        }
        if (progs.isEmpty()) {
            fail("Unknown instruction", s);
        }
        return null;
    }
    
    // STMT  ::= ACT ";" | LOOP
    public ProgramNode parseSTMT(Scanner s) {
        if (s.hasNext("action")) {
            s.next(); // Consume "action" token
            if (s.hasNext(";")) {
                s.next(); // Consume ";" token
                return parseAct(s); // Parse and return ActionNode
            } else {
                fail("Missing semicolon after action", s);
            }
        }
        if (s.hasNext("loop")) {
            return parseLoop(s); // Parse and return LoopNode
        }
        return null;
    }

    
    // ACT   ::= "move" | "turnL" | "turnR" | "takeFuel" | "wait"
    public ProgramNode parseAct(Scanner s) {
        if (s.hasNext("move")) {return parseMove(s);}
        if (s.hasNext("turnL")) {return parseTurnL(s);}
        if (s.hasNext("turnR")) {return parseTurnR(s);}
        if (s.hasNext("takeFuel")){return parseTakeFuel(s);}
        if (s.hasNext("wait")){return parseWait(s);}
        fail("Unknown command",s);         
        return null;
    }
    
    /** move */
    public ProgramNode parseMove(Scanner s){
          require("move", "expecting 'move'", s);
          return new MoveNode();
    }
    
    /** turnL */
    public ProgramNode parseTurnL(Scanner s){
          require("turnL", "expecting 'turnL'", s);
          return new turnLNode();
    }
    
    /** turnR */
    public ProgramNode parseTurnR(Scanner s){
          require("turnR", "expecting 'turnR'", s);
          return new turnRNode();
    }
        
    /** takefuel */
    public ProgramNode parseTakeFuel(Scanner s){
          require("takeFuel", "expecting 'takeFuel'", s);
          return new takeFuelNode();
    }
    
    /** idlewait */
    public ProgramNode parseWait(Scanner s){
          require("wait", "expecting 'wait'", s);
          return new idleWaitNode();
    }
    
    // LOOP  ::= "loop" BLOCK
    public ProgramNode parseLoop(Scanner s) {
        if (s.hasNext("loop")) {s.next();}
        
        return null;
    }

    //BLOCK ::= "{" STMT+ "}"
    public ProgramNode parseBlock(Scanner s) {
        if (s.hasNext("{")) {s.next();}
        
        if (s.hasNext("}")) {}
        
        return null;
    }






    //----------------------------------------------------------------
    // utility methods for the parser
    // - fail(..) reports a failure and throws exception
    // - require(..) consumes and returns the next token as long as it matches the pattern
    // - requireInt(..) consumes and returns the next token as an int as long as it matches the pattern
    // - checkFor(..) peeks at the next token and only consumes it if it matches the pattern

    /**
     * Report a failure in the parser.
     */
    static void fail(String message, Scanner s) {
        String msg = message + "\n   @ ...";
        for (int i = 0; i < 5 && s.hasNext(); i++) {
            msg += " " + s.next();
        }
        throw new ParserFailureException(msg + "...");
    }

    /**
     * Requires that the next token matches a pattern if it matches, it consumes
     * and returns the token, if not, it throws an exception with an error
     * message
     */
    static String require(String p, String message, Scanner s) {
        if (s.hasNext(p)) {return s.next();}
        fail(message, s);
        return null;
    }

    static String require(Pattern p, String message, Scanner s) {
        if (s.hasNext(p)) {return s.next();}
        fail(message, s);
        return null;
    }

    /**
     * Requires that the next token matches a pattern (which should only match a
     * number) if it matches, it consumes and returns the token as an integer
     * if not, it throws an exception with an error message
     */
    static int requireInt(String p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {return s.nextInt();}
        fail(message, s);
        return -1;
    }

    static int requireInt(Pattern p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {return s.nextInt();}
        fail(message, s);
        return -1;
    }

    /**
     * Checks whether the next token in the scanner matches the specified
     * pattern, if so, consumes the token and return true. Otherwise returns
     * false without consuming anything.
     */
    static boolean checkFor(String p, Scanner s) {
        if (s.hasNext(p)) {s.next(); return true;}
        return false;
    }

    static boolean checkFor(Pattern p, Scanner s) {
        if (s.hasNext(p)) {s.next(); return true;} 
        return false;
    }

}

// You could add the node classes here or as separate java files.
// (if added here, they must not be declared public or private)
// For example:
//  class BlockNode implements ProgramNode {.....
//     with fields, a toString() method and an execute() method
//

class  MoveNode  implements ProgramNode{
    public MoveNode(){}
    public String toString(){return "move";}
    public void execute(Robot w){
        w.move();
    }
}

class  turnLNode  implements ProgramNode{
    public turnLNode(){}
    public String toString(){return "turnL";}
    public void execute(Robot w){
        w.turnLeft();
    }
}

class  turnRNode  implements ProgramNode{
    public turnRNode(){}
    public String toString(){return "turnR";}
    public void execute(Robot w){
        w.turnRight();
    }
}

class  takeFuelNode  implements ProgramNode{
    public takeFuelNode(){}
    public String toString(){return "takeFuel";}
    public void execute(Robot w){
        w.takeFuel();
    }
}

class  idleWaitNode  implements ProgramNode{
    public idleWaitNode(){}
    public String toString(){return "wait";}
    public void execute(Robot w){
        w.idleWait();
    }
}


