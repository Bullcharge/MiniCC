package bit.minicc;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preprocessor {
	int Comment(String line, int comment){
		return comment;
	}
	public String run(String iFile) {
		File file = new File(iFile);
		BufferedReader reader = null;
		String res = "";
		String op = "";
		
		try {
			System.out.println("Read the file line by line");
			reader = new BufferedReader(new FileReader(file));
			char tempChar;
			int state = 0;
			int intreader = 0;
			boolean isWhitespace = false; 
			while((intreader = reader.read()) != -1)
			{
				boolean isCom = false;
				tempChar = (char)intreader;/*
				if(tempChar == ' ' || tempChar == '\t' || tempChar == '\n')
				{
					System.out.println("white");
					isWhitespace = true;			
				}
				else 
				{
					if(isWhitespace && (tempChar != ' ' || tempChar != '\n' || tempChar != '\t'))
					{
						System.out.println("black");
						isWhitespace = false;
						res += ' ';
						break;
					}*/
					switch(state)
					{
						case 0:
						{
							if(tempChar == '/')
							{
								state = 1;
							}
							isCom = false;
							break;
						}
						case 1:
						{
							if(tempChar == '/')
							{
								state = 4;
								isCom = true;
							}
							else if(tempChar == '*')
							{
								state = 2;
								isCom = true;
							}
							else
							{	
								tempChar += '/';
								state = 0;
								isCom = false;
							}
							break;
						}
						case 2:
						{
							if(tempChar == '*')
							{
								state = 3;
								isCom = true;
							}
							else
							{
								isCom = true;
							}
							break;
						}
						case 3:
						{
							if(tempChar == '/')
							{
								state = 0;
							}
							else
							{
								state = 2;
							}
							isCom = true;
							break;
						}
						case 4:
						{
							if(tempChar == '\n')
							{
								state = 0;
							}
							isCom = true;
							break;
						}
					}
				
					if(!isCom)
					{
					//System.out.print(tempChar + " ");
						res += tempChar;
					}
				
			}
			reader.close();
			//System.out.print(res);
			boolean boo = false;
			for(int i = 0;i < res.length();i++)
			{
				if(!boo && (res.charAt(i) == ' ' || res.charAt(i) == '\t' || res.charAt(i) == '\n'))
				{
					boo = true;
					op += ' ';
				}
				else 
				{
					if(res.charAt(i) != '\n' && res.charAt(i) != '\t' && res.charAt(i) != ' ')
					{
						op += res.charAt(i);
						boo = false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				}catch(IOException e1) {}
				
			}
		}
		System.out.println(op);
		return op;
	}
}
