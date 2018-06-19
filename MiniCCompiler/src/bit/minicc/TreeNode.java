package bit.minicc;

import java.util.ArrayList;

enum TreeNodeType{
	TNT_CMPL_UNIT,
	TNT_FUNC_LIST,
	TNT_FUNC_DEF,
	TNT_FUNC_ARGS,
	TNT_FUNC_CALL,
	
	TNT_CODE_BLOCK,
	TNT_STMT_LIST,
	TNT_STMT_RTN,
	TNT_STMT_DCL,
	TNT_STMT_ASSIGN,
	TNT_STMT_CALL,
	TNT_TPSP,
	TNT_RARG_LIST,
	TNT_ARG_LIST,
	
	TNT_TERM,
	TNT_TERM2,
	TNT_EXPR,
	TNT_EXPR2,
	TNT_FACTOR,
	
	TNT_ID,
	TNT_SP_OP,
	TNT_SP_CL,
	
	TNT_CSNT_INT,
	TNT_CSNT_FLOAT,
	
	TNT_OP_PLUS,
	
	TNT_UNKNOWN;
}

public class TreeNode {
	private TreeNodeType type;
	private ArrayList<TreeNode> children;
	private SymbolTable st;
	private int tknIndex;
	private int reg;
	
	public TreeNode(){
		this.setChildren(new ArrayList<TreeNode>());
		this.setType(TreeNodeType.TNT_UNKNOWN);
	}

	public static void print(TreeNode root, String indent){
		String str = (String) Scanner.getTknValueList().get(root.getTknIndex());
		System.out.println(indent + root.getType() + ":" + str + ":" + root.getReg());
		for(TreeNode node : root.getChildren()){
			print(node, indent + "    ");
		}
	}
	
	public TreeNodeType getType() {
		return type;
	}

	public void setType(TreeNodeType type) {
		this.type = type;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}
	
	public TreeNode getChildByIndex(int index) {
		if(index >= this.children.size()) {
			System.out.println("out of bound");
			System.exit(1);
		}
		return this.children.get(index);
	}
	
	public void setSymbolTable(SymbolTable st) {
		this.st = st;
	}
	
	public SymbolTable getSymbolTable() {
		return this.st;
	}
	
	public int getTknIndex() {
		return tknIndex;
	}
	
	public void setTknIndex(int tknIndex) {
		this.tknIndex = tknIndex;
	}
	
	public int getReg() {
		return reg;
	}

	public void setReg(int reg) {
		this.reg = reg;
	}

}
