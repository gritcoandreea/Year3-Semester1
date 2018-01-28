public class SymbolTable {

    private SymNode root;

    public SymbolTable() {
    }

    public SymbolTable(SymNode root) {
        this.root = root;
    }

    public SymNode getRoot() {
        return root;
    }

    public void setRoot(SymNode root) {
        this.root = root;
    }
    public boolean contains(String key){
        if(get(root,key)!=null){
            return true;
        }
        return false;
    }

    private SymNode get(SymNode node, String key){
        if(node == null){
            return null;
        }

        int cmp = key.compareTo(node.getIdentifier());

        return  cmp < 0 ? get(node.getLeft(), key) : cmp > 0 ? get(node.getRight(), key) : node;
    }

    public void addNode(String type, String key, String value, int no) throws Exception {
        if (key == null)
            throw new Exception("Symbol table: Add: Key is null");
        root = addNode(root, type, key, value, no);
    }

    private SymNode addNode(SymNode node, String type, String key, String value, int no)
    {
        if (node == null) node = new SymNode(type, key, value, no);
        int cmp = key.compareTo(node.getIdentifier());
        if (cmp < 0)
            node.setLeft(addNode(node.getLeft(), type, key, value, no));
        else if (cmp > 0)
            node.setRight(addNode(node.getRight(), type, key, value, no));
        else node.setValue(value);
        return node;
    }

    public void SetNode(SymNode node, String value) throws Exception {
       addNode(node.getType(), node.getIdentifier(), value, node.getNr());
    }

    public SymNode get(String key)
    {
        return get(root, key);
    }

}
