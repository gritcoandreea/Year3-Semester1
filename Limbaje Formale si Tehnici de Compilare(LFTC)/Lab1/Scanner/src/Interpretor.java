import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class which analyzies the program.
 */
public class Interpretor {
    /** List of codifiers(keywords).*/
    private List<Token> tokens = new ArrayList<>();
    /** List of program internal form elements*/
    private List<PIF> pifList = new ArrayList<>();
    /** Symbol Table for identifiers*/
    private SymbolTable identifierSymbolTable  = new SymbolTable();
    /** Symbol Table for constans*/
    private SymbolTable constantSymbolTable  = new SymbolTable();
    /** Position of an element in the symbol table for identifiers*/
    private int identifierSymbolTablePoz = 0;
    /** Position of an element in the symbol table for constans*/
    private int constantSymbolTablePoz = 0;
    /** List of possible errors in the program*/
    private List<String> err = new ArrayList<>();

    /** Main function for the interpretor.
     * Reads the codifiers from a file and stores them.
     * Reads every line from our program from a file and passes it to the analyzer.
     * Also, prints the results.
     */
    public void run() {
        readCodifiers();
        int nrLine = 1;
        try (BufferedReader br = new BufferedReader(new FileReader("input1.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                analyzeLine(line, nrLine);
                nrLine++;
            }
        } catch (Exception e) {
        }

        System.out.println("Errors: ");
        for (String error : err) {
            System.out.println(error);
        }

        System.out.println("\nIdentifier Symbol Table: ");

        print(identifierSymbolTable.getRoot());

        System.out.println("\nConstant Symbol Table: ");
        print(constantSymbolTable.getRoot());
        System.out.println("\nPIF: ");
        for (PIF e : pifList) {
            System.out.println("Type of data: " + e.getType() + " and code " + e.getCode());
        }

    }

    /**
     * This function gets a line and its number in the program and breaks the line after spaces.
     * After that, it analyzes every word in the line and adds it in the PIF list based on what it is:
     *  -1 : keyword
     *  0 : constant
     *  1 : identifier
     *
     *  Also, this function calls the addNode method for constants/identifiers in order to add them in the corresponding symbol table.
     *
     * @param line2
     *          The line which it analyzes.
     * @param nrLine
     *          The number of the line in the program.
     * @throws Exception
     */
    private void analyzeLine(String line2, int nrLine) throws Exception {
        String line = line2.trim();
        String[] list = line.split(" ");
        for (String l : list) {

            if (isKeyword(l.trim())) {
                pifList.add(new PIF(-1, findToken(l)));
            } else if ( validateIdentifier(l.trim())){
                addNode(line,l.trim(),identifierSymbolTablePoz);
                pifList.add(new PIF(1, identifierSymbolTablePoz));
                identifierSymbolTablePoz++;
            }else  if(validateConstant(l.trim())){
                if(constantSymbolTable.getRoot() == null){
                    constantSymbolTable.setRoot(new SymNode("const", l.trim(), l.trim(), constantSymbolTablePoz));
                }else{
                if(!constantSymbolTable.contains(l)){
                    constantSymbolTable.addNode("const", l.trim(),l.trim(), constantSymbolTablePoz);
                    pifList.add(new PIF(0, constantSymbolTablePoz));
                }}
                constantSymbolTablePoz++;
            }
            else if(!l.equals("")){
                err.add("Syntax error at line " + nrLine+ " "+l);
            }
        }

    }

    /**
     *This function adds an identifier/constant in the corresponding symbol table based on  the statement that is analyzed on the given line.
     * if there are multiple lines, they are sent back to the analyzeLine method.
     *
     * @param line
     *          String
     * @param identifier
     *          String
     * @param no
     *          int
     * @throws Exception
     */
    private void addNode(String line, String identifier, int no) throws Exception {
        String[] lines = line.split(";", 2);
        if (lines.length > 1)
        {
            if (lines[1].length() > 0)
            {
                System.out.println("Multiple rows");
                analyzeLine(lines[1], no);
            }
        }
        if (isAssignment(line) &&
                !(line.contains("+=") ||
                        line.contains("*=") ||
                        line.contains("/=") ||
                        line.contains("%=")))
        {
            if (!identifierSymbolTable.contains(identifier))
            {
                String[] el = line.split("=");
                String[] id = el[0].trim().split(" ");
               String[] val = el[1].split(";");
                if(identifierSymbolTable.getRoot() == null)
                {
                    identifierSymbolTable.setRoot(new SymNode(id[0].trim(), id[1].trim(), val[0].trim(), no));
                    return;
                }
                identifierSymbolTable.addNode(id[0].trim(), id[1].trim(), val[0].trim(), no);
            }
            else
            {
                String[] el = line.split("=");
                String[] val = el[1].split(";");
                identifierSymbolTable.SetNode(identifierSymbolTable.get(identifier), val[0].trim());
            }
        }
        else
        {
            if (line.split(" ").length > 4)
            {
                return;
            }
            else
            if (!line.contains("+=") && !line.contains("-=") && !line.contains("*=") && !line.contains("/=" )&& !line.contains("%="))
            {
                String[] el = line.trim().trim().split(" ");
                if (identifierSymbolTable.getRoot() == null)
                {
                    identifierSymbolTable.setRoot(new SymNode(el[0].trim(), el[1].trim(), null, no));
                    return;
                }
                identifierSymbolTable.addNode(el[0].trim(), el[1].trim(), null, no);
            }
        }
    }

    /**
     * This function gets a keyword and if the keyword is in the token list it returns its code,
     * otheriwse it returns a non-existing id in the tokens list.
     * @param l
     *          String
     * @return int
     */
    private int findToken(String l) {
        for (Token t : tokens) {
            if (t.getName().equals(l)) {
                return t.getCode();
            }
        }
        return -999;
    }

    /**
     * Function which reads codifiers from the source file.
     */
    private void readCodifiers() {
        try (BufferedReader br = new BufferedReader(new FileReader("codifiers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String line2 = line.replace(" ", "");
                String[] result = line2.split(":");
                tokens.add(new Token(Integer.parseInt(result[0].trim()), result[1].trim()));
            }
        } catch (IOException e) {
        }
    }

    /**
     * Function which returns true if the given token is a keyword, or false otherwise.
     * @param str
     *          String
     * @return  boolean
     */
    private boolean isKeyword(String str) {
        for (Token token : tokens) {
            if (token.getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function which returns true if the given statement is an assignment, or false otherwise
     * @param c
     *          String
     * @return  boolean
     */
    private boolean isAssignment(String c) {
        if (c.contains(" = ")) {
            return true;
        }
        return false;
    }

    /**
     * Function which returns true if the given token is a valid constant based on a regex, or
     * false otherwise
     * @param str
     *      String
     * @return  boolean
     */
    private boolean validateConstant(String str) {
        return str.matches("[0]|^-|[1-9][0-9]*|^[']{1}[a-zA-Z]*[']{1}$");
    }

    /**
     * Function which returns true if the given identifier is valid based on a regex, or false otherwise
     *
     * @param identifier
     *      String
     * @return boolean
     */
    private boolean validateIdentifier(String identifier) {
            return identifier.matches("^[A-z][A-z0-9]*$") && identifier.length() < 250;
    }

    /**
     * Function which prints a symbol table node
     * @param st
     *          SymNode
     */
    private void print(SymNode st)
    {
        if (st == null)
            return;
        System.out.println("Defined key " + st.getIdentifier() +
                " of type " + st.getType() +
                " with the value " + (st.getValue()!=null? st.getValue(): " null ") +
                " at row " + st.getNr());
        print(st.getLeft());
        print(st.getRight());
    }
}
