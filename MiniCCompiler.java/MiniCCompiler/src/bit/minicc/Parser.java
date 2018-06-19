package bit.minicc;

import java.util.ArrayList;

import bit.minicc.Scanner.TOKEN_TYPE;

/*
 * CMPL_UNIT	: FUNC_LIST
 * FUNC_LIST	: FUNC_DEF FUNC_LIST | e
 * FUNC_DEF		: TYPE_SEPC ID ( ARG_LIST ) CODE_BLOCK
 * TYPE_SPEC	: int | void 
 * ARG_LIST		: ARGUMENT	| ARGUMENT, ARG_LIST | e
 * ARUGMENT		: TYPE_SPEC ID
 * CODE_BLOCK	: { STMT_LIST }
 * STMT_LIST	: STMT STMT_LIST | e
 * STMT			: DECL_STMT | ASSIGN_STMT | RTN_STMT
 * DECL_STMT	: TYPE_SPEC ID ;
 * ASSIGN_STMT	: ID = EXPR ;
 * RTN_STMT		: return EXPR ; 
 * EXPR			: TERM EXPR2
 * EXPR2		: + TERM EXPR2 | - TERM EXPR2 | e
 * TERM			: FACTOR TERM2
 * TERM2		: * FACTOR TERM2 | / FACTOR TERM2 | e
 * FACTOR		: ID | CONST ( EXPR )
 */
/*
 * int main(){
 * 	int i;
 *  i = i + 2 * i;
 *  return i;
 * }
 */


//FUNC_LIST		: FUNC_DEF FUNC_LIST | e
//FUNC_DEF		: TYPE_SEPC ID ( ARG_LIST ) CODE_BLOCK
//STMT_LIST		: STMT STMT_LIST | e
//STMT			: DECL_STMT | ASSIGN_STMT | RTN_STMT | CALL_STMT
//CALL_STMT		: ID (RARG_LIST)
//RARG_
//
//DECL_STMT		: 	TYPE_SPEC ID ;
//RTN_STMT		: return EXPR ;
//ARG_LIST		: ARGUMENT	| ARGUMENT, ARG_LIST | e
//EXPR			: TERM EXPR2
//EXPR2 		: + TERM EXPR2 | - TERM EXPR2 | e
//TERM			: FACTOR TERM2
//TERM2			: * FACTOR TERM2 | / FACTOR TERM2 | e  
//FACTOR		: ID | CONST | ( EXPR )

/*
 * CMPL_UNIT 	=> CMPL_UNIT
 * 				=> FUNC_DEF FUNC_LIST 
 * 				=> TYPE_SEPC ID ( ARG_LIST ) CODE_BLOCK FUNC_LIST
 * 				=> int ID ( ARG_LIST ) CODE_BLOCK FUNC_LIST
 * 				=> int ID ( ) CODE_BLOCK FUNC_LIST  
 * 				=> int ID ( ) { STMT_LIST } FUNC_LIST  
 * 				=> int ID ( ) { STMT STMT_LIST } FUNC_LIST 
 * 				=> int ID ( ) { DECL_STMT STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { TYPE_SPEC ID ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; STMT STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ASSIGN_STMT STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = EXPR ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = TERM EXPR2 ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = FACTOR TERM2 EXPR2 ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST TERM2 EXPR2 ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST ; STMT_LIST } FUNC_LIST
 * 				...
 * 				=> int ID ( ) { int ID ; ID = CONST ; ID = ID + CONST * ID ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST ; ID = ID + CONST * ID ; return EXPR ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST ; ID = ID + CONST * ID ; return ID ; STMT_LIST } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST ; ID = ID + CONST * ID ; return ID ; } FUNC_LIST
 * 				=> int ID ( ) { int ID ; ID = CONST ; ID = ID + CONST * ID ; return ID ; }
 * 
 */

public class Parser {
	private TOKEN_TYPE nextToken;
	private ArrayList<TOKEN_TYPE> tknTypeList;
	private ArrayList<Object> tknValueList;
	
	private int tokenIndex;
	
	public static TreeNode root;
	
	public void run(){
		tknTypeList = Scanner.getTknTypeList();
		tknValueList = Scanner.getTknValueList();
		
		tokenIndex = 0;
		
		System.out.println("Parsing...");
		
		getNextToken();
		TreeNode node = cmpl_unit();
		TreeNode.print(node, "");
		System.out.println("the tree is built!");
		
		this.root = node;
	}
	
	public void getNextToken(){
		nextToken = tknTypeList.get(tokenIndex);
	}
	public TreeNode cmpl_unit(){
		ArrayList<TreeNode> fl = func_list();
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_CMPL_UNIT);
		if(fl != null)
			//node.getChildren().add(fl);
		for(int i = fl.size()-1; i > -1; i--) {
			TreeNode fl_inter = new TreeNode();
			fl_inter.setType(TreeNodeType.TNT_FUNC_LIST);
			fl_inter.getChildren().add(fl.get(i));
			node.getChildren().add(fl.get(i));
		}
		return node;
	}
	// FUNC_LIST	: FUNC_DEF FUNC_LIST | e
	public ArrayList<TreeNode> func_list(){
		if(tokenIndex >= tknTypeList.size())
			return null;
		if(nextToken == TOKEN_TYPE.TKN_KW_INT || nextToken == TOKEN_TYPE.TKN_KW_VOID){
			//tokenIndex++;
			TreeNode fd = func_def();
			ArrayList<TreeNode> fl = func_list();
			if(fl == null)
				fl = new ArrayList<TreeNode>();
			TreeNode node = new TreeNode();
			node.setType(TreeNodeType.TNT_FUNC_LIST);
			node.getChildren().add(fd);
			fl.add(fd);
			if(fl != null){
				//node.getChildren().add(fl);
			}
			return fl;
		}else{
			return null;
		}
	}
	//FUNC_DEF		: TYPE_SEPC ID ( ARG_LIST ) CODE_BLOCK
	public TreeNode func_def(){
		TreeNode ts = type_spec();
		
		match_token(TOKEN_TYPE.TKN_ID);
		TreeNode id = new TreeNode();
		id.setType(TreeNodeType.TNT_ID);
		id.setTknIndex(tokenIndex);
		tokenIndex++;
		
		match_token(TOKEN_TYPE.TKN_SP_OPENING);
		tokenIndex++;
		arg_list();
		match_token(TOKEN_TYPE.TKN_SP_CLOSING);
		tokenIndex++;
		TreeNode code = code_block();
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_FUNC_DEF);
		node.getChildren().add(ts);
		node.getChildren().add(id);
		node.getChildren().add(code);
		//node.setChildren(code);
		return node;
	}
	
	public TreeNode code_block(){
		match_token(TOKEN_TYPE.TKN_SP_KB_OPENING);
		tokenIndex++;
		ArrayList<TreeNode> stmt = stmt_list();
		match_token(TOKEN_TYPE.TKN_SP_KB_CLOSING);
		tokenIndex++;
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_CODE_BLOCK);
		for(int i = stmt.size()-1; i > -1; i--)
			node.getChildren().add(stmt.get(i));
		return node;
	}
	
	/*
	 *  * STMT_LIST	: STMT STMT_LIST | e
	 * 
	 */
	public ArrayList<TreeNode> stmt_list(){
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_SP_KB_CLOSING){
			return null;
		}else{
			TreeNode st = stmt();
			ArrayList<TreeNode> stl = stmt_list();
			if(stl == null)
				stl = new ArrayList<TreeNode>();
			
			TreeNode node = new TreeNode();
			node.setType(TreeNodeType.TNT_STMT_LIST);
			node.getChildren().add(st);
			stl.add(st);
  			if(stl != null){
  				//node.getChildren().add(stl);
  			}
			return stl;
		}			
	}
	
	/*
	 *  
	 * STMT			: DECL_STMT | ASSIGN_STMT | RTN_STMT
	 */
	public TreeNode stmt(){
		
		TreeNode node = new TreeNode();
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_KW_RTN){
			node = rtn_stmt();
			//System.out.println("rtn stmt");
		}else if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_KW_INT ||
				tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_KW_VOID){
			node = decl_stmt();
			//System.out.println("decl stmt");
		}else{
			//System.out.println("assign stmt");
			
			if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_ID 
					&& tokenIndex + 1 < tknTypeList.size()
					&& tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_OP_EQUAL)
			{
				node = assign_stmt();
			}
			else {
				node = call_stmt();
			}
		}
		return node;
	}
	
	public TreeNode call_args()
	{
		TreeNode args = new TreeNode();
		args.setType(TreeNodeType.TNT_RARG_LIST);
		
		match_token(TOKEN_TYPE.TKN_SP_OPENING);
		tokenIndex++;
		match_token(TOKEN_TYPE.TKN_SP_CLOSING);
		tokenIndex++;
		match_token(TOKEN_TYPE.TKN_SP_SC);
		tokenIndex++;
		
		return args;
	}
	
	
	public TreeNode call_stmt()
	{
		TreeNode call = new TreeNode();
		call.setType(TreeNodeType.TNT_STMT_CALL);
		
		call.getChildren().add(func_call());
		
		match_token(TOKEN_TYPE.TKN_SP_SC);
		tokenIndex++;
		
		return call;
	}
	
	public TreeNode func_call()
	{
		TreeNode call = new TreeNode();
		call.setType(TreeNodeType.TNT_FUNC_CALL);
		
		TreeNode id = new TreeNode();
		id.setType(TreeNodeType.TNT_ID);
		id.setTknIndex(tokenIndex);
		tokenIndex++;
		
		call.getChildren().add(id);
		
		match_token(TOKEN_TYPE.TKN_SP_OPENING);
		tokenIndex++;
		
		TreeNode args = real_args();
		if(args != null)
		{
			call.getChildren().add(args);
		}
		match_token(TOKEN_TYPE.TKN_SP_CLOSING);
		tokenIndex++;
		
		return call;
	}
	
	public TreeNode real_args(){
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_SP_CLOSING){
			return null;
		}else{
			TreeNode arg = new TreeNode();
			 if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_ID){
				 arg.setType(TreeNodeType.TNT_ID);
			 }else if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_CNST_FLOAT){
				 arg.setType(TreeNodeType.TNT_CSNT_FLOAT);
			 }else if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_CNST_INT){
				 arg.setType(TreeNodeType.TNT_CSNT_INT);
			 }
			 arg.setTknIndex(tokenIndex);
			 tokenIndex++;
			 
			 TreeNode args = new TreeNode();
			 args.setType(TreeNodeType.TNT_FUNC_ARGS);
			 args.getChildren().add(arg);
			 
			 if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_SP_COMMA){
				 TreeNode list = real_args2();
				 args.getChildren().add(list);
			 }
			 
			 return args;
		}
		
	}

	
	public TreeNode real_args2(){
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_SP_CLOSING){
			return null;
		}else{
			match_token(TOKEN_TYPE.TKN_SP_COMMA);
			tokenIndex++;
			
			TreeNode arg = new TreeNode();
			 if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_ID){
				 arg.setType(TreeNodeType.TNT_ID);
			 }else if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_CNST_FLOAT){
				 arg.setType(TreeNodeType.TNT_CSNT_FLOAT);
			 }else if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_CNST_INT){
				 arg.setType(TreeNodeType.TNT_CSNT_INT);
			 }
			 arg.setTknIndex(tokenIndex);
			 tokenIndex++;
			 
			 TreeNode args = new TreeNode();
			 args.setType(TreeNodeType.TNT_FUNC_ARGS);
			 args.getChildren().add(arg);
			 
			 if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_SP_COMMA){
				 TreeNode list = real_args2();
				 args.getChildren().add(list);
			 }
			 
			 return args;
		}
	}

	
	/*
	 * DECL_STMT	: TYPE_SPEC ID ;
	 */
	public TreeNode decl_stmt(){
		TreeNode tps = type_spec();
		match_token(TOKEN_TYPE.TKN_ID);
		tokenIndex++;
		TreeNode id = new TreeNode();
		id.setType(TreeNodeType.TNT_ID);
		match_token(TOKEN_TYPE.TKN_SP_SC);
		tokenIndex++;
		System.out.println("declaration matched!");
		//FACTOR		: ID | CONST ( EXPR )
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_STMT_DCL);
		node.getChildren().add(tps);
		node.getChildren().add(id);
		return node;

	}
	
	/*
	 * RTN_STMT		: return EXPR ; 
	 */
	public TreeNode rtn_stmt(){
		match_token(TOKEN_TYPE.TKN_KW_RTN);
		tokenIndex++;
		match_token(TOKEN_TYPE.TKN_ID);
		tokenIndex++;
		match_token(TOKEN_TYPE.TKN_SP_SC);
		tokenIndex++;
		System.out.println("return matched!");
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_STMT_RTN);
		return node;
	}
	public TreeNode assign_stmt() {
		match_token(TOKEN_TYPE.TKN_ID);
		tokenIndex++;
		match_token(TOKEN_TYPE.TKN_OP_EQUAL);
		tokenIndex++;
		TreeNode ex = expr();
		match_token(TOKEN_TYPE.TKN_SP_SC);
		tokenIndex++;
		System.out.println("assignement matched!");
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_STMT_ASSIGN);
		node.getChildren().add(ex);
		return node;
	}
	
	public TreeNode type_spec(){
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_KW_INT || 
				tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_KW_INT){
			getNextToken();
			tokenIndex++;		
		}else{
			System.out.println("error in type_spec");
			System.exit(1);
		}
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_TPSP);
		return node;
	}
	//ARG_LIST		: ARGUMENT	| ARGUMENT, ARG_LIST | e
	public TreeNode arg_list(){
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_ARG_LIST);
		return node;
	}
	//EXPR			: TERM EXPR2	
	public TreeNode expr() {
		TreeNode t = term();
		TreeNode ex = expr2();
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_EXPR);
		if(t != null)
			node.getChildren().add(t);
		if(ex != null)
			node.getChildren().add(ex);
		return node;
	}
	//EXPR2 		: + TERM EXPR2 | - TERM EXPR2 | e
	public TreeNode expr2() {
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_OP_PLUS || tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_OP_MINUS)
		{
			tokenIndex++;
			TreeNode node = new TreeNode();
			node.setType(TreeNodeType.TNT_EXPR2);
			TreeNode t = term();
			TreeNode ex = expr2();
			if(t != null)
				node.getChildren().add(t);
			if(ex != null)
				node.getChildren().add(ex);
			return node;
		}
		else
		{
			return null;
		}
	}
	//TERM			: FACTOR TERM2
	public TreeNode term() {
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_TERM);
		TreeNode f = factor();
		TreeNode t = term2();
		if(f != null)
			node.getChildren().add(f);
		if(t != null)
			node.getChildren().add(t);
		return node;		
	}
	//TERM2			: * FACTOR TERM2 | / FACTOR TERM2 | e   
	public TreeNode term2() {
		if(tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_OP_MUL || tknTypeList.get(tokenIndex) == TOKEN_TYPE.TKN_OP_DIV)
		{
			tokenIndex++;
			TreeNode node = new TreeNode();
			node.setType(TreeNodeType.TNT_TERM2);
			TreeNode f = factor();
			TreeNode t = term2();
			if(f != null)
				node.getChildren().add(f);
			if(t != null)
				node.getChildren().add(t);
			return node;	
		}
		else
		{
			return null;
		}
	}
	//FACTOR		: ID | CONST ( EXPR )
	public TreeNode factor() {
		
		TreeNode node = new TreeNode();
		node.setType(TreeNodeType.TNT_FACTOR);

		if(tknTypeList.get(tokenIndex) != TOKEN_TYPE.TKN_ID && tknTypeList.get(tokenIndex) != TOKEN_TYPE.TKN_CNST_INT)
		{
			//TreeNode nota = new TreeNode();
			//node.setType(TreeNodeType.TNT_ID);
			//node.getChildren().add(nota);
			return null;
		}
		else 
		{
			tokenIndex++;
			return node;
		}
	}
	
	public void match_token(TOKEN_TYPE token){
		if(tknTypeList.get(tokenIndex) == token){
			
		}else{
			System.out.println("match token error");
			//System.exit(1);
		}
	}
}
