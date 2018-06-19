import bit.minicc.CodeGenerator;
import bit.minicc.Parser;
import bit.minicc.Preprocessor;
import bit.minicc.Scanner;
import bit.minicc.SemanticChecker;

public class MiniCCompiler {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		if(args.length < 1) {
			System.out.println("Usage : toto.c");
			return;
		}else {
			System.out.println(args[0]);
			
		}
		
		Preprocessor pp = new Preprocessor();
		Scanner scanner = new Scanner();
		Parser parser = new Parser();
		SemanticChecker sc = new SemanticChecker();
		CodeGenerator cg = new CodeGenerator();
		
		String s = pp.run(args[0]);
		scanner.run(s);
		parser.run();
		sc.run();
		cg.run();
	}

}
