package bit.minicc;

import java.util.ArrayList;



public class Scanner {

	public static enum TOKEN_TYPE{
	TKN_ID,
	TKN_CNST_INT,
	TKN_CNST_FLOAT,
	
	TKN_OP_PLUS,
	TKN_OP_MINUS,
	TKN_OP_MUL,
	TKN_OP_DIV,
	TKN_OP_EQUAL,
	
	TKN_SP_COMMA,
	TKN_SP_KB_OPENING, // {
	TKN_SP_KB_CLOSING, // }
	TKN_SP_OPENING,    // )
	TKN_SP_CLOSING,    // (
	TKN_SP_SC,
	
	TKN_KW_INT,
	TKN_KW_VOID,
	TKN_KW_RTN,
}
	private static ArrayList<Object> tkn_value;
	private static ArrayList<TOKEN_TYPE> tkn_type;
	
	public static boolean useloop(String[] arr, String targetValue)
	{
		for (int count = 0; count < arr.length; count++)
		{
			if(targetValue.length() > 1 || (arr[count].length() > 1))
			{
				if (arr[count].compareTo(targetValue) == 0)
				{
					return true;
				}
			}		
			else
			{
				if (arr[count].charAt(0) == targetValue.charAt(0))
					return true;
			}
		}
		return false;
	}
	
	public void run(String s) {
		System.out.println("Scanning...");
		String keyword[] = {"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "inline", "int", "long", "register", "restrict", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"};                 
		String operator[] = {"+", "=", "-", "/", "*", "%", "++", "--", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!", "&", "|", "^", "~", "<<", ">>", "+=", "-=", "*=", "/=", "%=", ">>=", "<<=", "&=", "^=", "|=", "?", ":"};
		String separator[] = {"{", "}", ";", ",", "(", ")", "[", "]", ":"};
		ArrayList<String> inter = new ArrayList<String>();
		tkn_value = new ArrayList<Object>();
		tkn_type = new ArrayList<TOKEN_TYPE>();
		String type[];
		String subString = "";
		String sinter = "";
		boolean is_op = false;
		for(int start = 0; start < s.length(); start++)
		{
			sinter = s.substring(start,start+1);
			if(useloop(separator,sinter))
			{
				if(subString != "")
				{
					inter.add(subString);
					tkn_value.add(subString);
				}
				inter.add("" + sinter);
				tkn_value.add("" + sinter);
				subString = "";
			}
			else if(useloop(operator,sinter))
			{
				if(!is_op && (subString != ""))
				{
					inter.add(subString);
					tkn_value.add(subString);
					subString = "";
				}
				subString += sinter;
				is_op = true;
			}
			else if(sinter.charAt(0) != " ".charAt(0))
			{
				if(is_op && (subString != ""))
				{
					inter.add(subString);
					tkn_value.add(subString);
					subString = "";
				}
				subString += sinter;
				is_op = false;
			}
			else
			{
				if(subString != "")
				{
					inter.add(subString);
					tkn_value.add(subString);
					subString = "";
				}
			}
		}
		System.out.println(inter.toString());
		type =  new String[inter.size()];
		//ArrayList<TOKEN_TYPE> tkn_type = new ArrayList<TOKEN_TYPE>();
		for(int start = 0; start < type.length; start++)
		{
			if(useloop(separator, inter.get(start)))
			{
				type[start] = "separator";
				switch (inter.get(start))
				{
					case "(":
						tkn_type.add(TOKEN_TYPE.TKN_SP_OPENING);
						break;
					case ")":
						tkn_type.add(TOKEN_TYPE.TKN_SP_CLOSING);
						break;
					case "{":
						tkn_type.add(TOKEN_TYPE.TKN_SP_KB_OPENING);
						break;
					case "}":
						tkn_type.add(TOKEN_TYPE.TKN_SP_KB_CLOSING);
						break;
					case ";":
						tkn_type.add(TOKEN_TYPE.TKN_SP_SC);
						break;
					case ",":
						tkn_type.add(TOKEN_TYPE.TKN_SP_COMMA);
				}
			}
			else if(useloop(operator, inter.get(start)))
			{
				type[start] = "operator";
				switch (inter.get(start))
				{
					case "+":
						tkn_type.add(TOKEN_TYPE.TKN_OP_PLUS);
						break;
					case "-":
						tkn_type.add(TOKEN_TYPE.TKN_OP_MINUS);
						break;
					case "*":
						tkn_type.add(TOKEN_TYPE.TKN_OP_MUL);
						break;
					case "/":
						tkn_type.add(TOKEN_TYPE.TKN_OP_DIV);
						break;
					case "=":
						tkn_type.add(TOKEN_TYPE.TKN_OP_EQUAL);
						break;
				}
			}
			else if(Character.isLetter(inter.get(start).charAt(0)))
			{
				if(useloop(keyword, inter.get(start)))
				{
					if(inter.get(start).compareTo("int") == 0)
						tkn_type.add(TOKEN_TYPE.TKN_KW_INT);
					if(inter.get(start).compareTo("return") == 0)
						tkn_type.add(TOKEN_TYPE.TKN_KW_RTN);
					type[start] = "keyword";
				}
				else
				{
					type[start] = "identifier";
					tkn_type.add(TOKEN_TYPE.TKN_ID);
				}
			}	
			else if(Character.isDigit(inter.get(start).charAt(0)))
			{
				type[start] = "number";
				tkn_type.add(TOKEN_TYPE.TKN_CNST_INT);
				tkn_value.add(subString);
			}
		}
		
		for(int i = 0; i < type.length; i++)
			System.out.print(inter.get(i) + " " + type[i] + ", ");
	}
	
	public static ArrayList<Object> getTknValueList()
	{
		return tkn_value;
	}
	
	public static ArrayList<TOKEN_TYPE> getTknTypeList()
	{
		return tkn_type;
	}
	
}