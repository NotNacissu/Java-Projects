
import java.util.*;

// I got some help wiht this algorithm with this website :https://www.geeksforgeeks.org/lzw-lempel-ziv-welch-compression-technique/
public class LempelZiv {
        /**
     * Take uncompressed input as a text string, compress it, and return it as a
     * text string.
     */
    public static String compress(String input) {
        StringBuilder iString = new StringBuilder(input); //store input string
        StringBuilder cString = new StringBuilder(); // store compressed string
        String lookahead = ""; //looks forward in text
        
        int stringmatch; //index of match
        int o; //offset
        int c = 0; //index of position
        int ws = 0; //window size
        
        while(c < input.length()){ //iterate through input
            int l = 1; //length of match
            int pM = 0; //previous match
            boolean match = false; //match found
            
            while(true){ //iterate to find matches
                if(ws < 100){lookahead = iString.substring(0,c);} //set substring->begin to curr
                else {ws = 100; lookahead = iString.substring(c-ws, c);} //set substring->curr-ws to current pos
                
                if(c + l >= iString.length()){ //if end of string 
                    String p = iString.substring(c, iString.length() - (iString.length() - c)); //set patt as substring->curr to end
                    stringmatch = lookahead.indexOf(p, 0); //search for patt
                } else {
                    String p = iString.substring(c, c + l); //set substring->curr to curr+length
                    stringmatch = lookahead.indexOf(p, 0); //search for patt
                }
                
                if(stringmatch != -1 && c + l < iString.length()){ //if match found -> not last char
                    match = true;
                    pM = stringmatch; //store pos of match
                    l++; //increment length
                } 
                
                else { //match not found or last char
                    l--; //decrease length of match
                    if(!match){o = 0;} //not match -> offset 0
                    else {o = (c - pM);} //match -> offset diff of curr and match pos
                    

                    cString.append("[" + o + "|" + l + "|" + input.charAt(c + l) + "]"); //sort string with match info
                    c += l + 1; //increment curr pos by length+1
                    ws += l; //update lookahead ws
                    
                    break;
                }
            }
       }
       return cString.toString(); //return compress
    }
    
    /**
     * Take compressed input as a text string, decompress it, and return it as a
     * text string.
     */
    public static String decompress(String compressed) {
        StringBuilder dString = new StringBuilder(); //store decompressed
        String[] decode = compressed.split("\\]\\[|[|\\[\\]]"); //split string -> array
        int c = 0; //index of pos
        
        for(int i = 1; i < decode.length; i+=3){ //iterate through array
            int o = Integer.parseInt(decode[i]); //offset
            int l = Integer.parseInt(decode[i+1]); //length
            
            if(o == 0 && l == 0){ //o and l both 0
                dString.append(decode[i+2]); //append char at i+2
                c++; //increment pos
            } 
            
            else {
                String t = dString.substring(c - o, c - o + l); //get substring -> o and l
                dString.append(t); //append decompressed with repeated
                dString.append(decode[i+2]); //append char -> i + 2
                c += l + 1; //increment pos by l+1
            }
        }
        
        return dString.toString(); //return decompress
    }

    /**
     * The getInformation method is here for your convenience, you don't need to
     * fill it in if you don't want to. It is called on every run and its return
     * value is displayed on-screen. You can use this to print out any relevant
     * information from your compression.
     */
    public String getInformation() {
        return "";
    }
}
