%{
#include <cstdio>
#include <iostream>
using namespace std;

// stuff from flex that bison needs to know about:
extern "C" int yylex();
extern "C" int yyparse();
extern "C" FILE *yyin;
extern int num_line;

void yyerror(const char *s);
%}


// define the constant-string tokens:
%token INCLUDE TYPE MAIN  
%token '{' '}' '(' ')' '=' ','
%token IDENTIFIER REAL_CONST INTEGER_CONST BOOL_CONST STRING_CONST ARTHM_OPERATOR RELATIONAL_OPERATOR
%token WRITE READ RETURN IF WHILE ENDL


%%
// the first rule defined is the highest-level rule, which in our
// case is just the concept of a whole "program file":

program:
	header body_main{ cout << "done with the given file!" << endl; }
	;

header:includes ENDLS { cout << "include statemets" <<endl; }
	;

includes:
	includes INCLUDE
	| INCLUDE
	;	

body_main:
	TYPE MAIN '(' ')' '{' ENDLS statements ENDLS '}' { cout << "main statemets" <<endl; }
	;

statements:
	statements ENDLS statement
	|statement
	;

statement:
	readStmt 
	|writeStmt
	|assignemtStmt
	|declarationStmt
	|returnStmt
	|conditionStmt
	|loopStmt
	;

readStmt:
	READ IDENTIFIER ';' { cout << "read statement" <<endl; }
	;

writeStmt:
	WRITE message ';' { cout << "write statement" <<endl; }
	;
message: 
	IDENTIFIER
	|const
	;
assignemtStmt:
	IDENTIFIER '=' expression ';' {cout<<"assignment statement"<<endl;}
	;
expression:
	IDENTIFIER|const|factor ARTHM_OPERATOR factor
	;
factor:
	IDENTIFIER
	|const
	;
declarationStmt:
	TYPE identifiers';' { cout << "declaration statement" <<endl; }
	;
identifiers:
	identifiers ',' IDENTIFIER
	| IDENTIFIER
	;
const:
	INTEGER_CONST
	|REAL_CONST
	|STRING_CONST
	|BOOL_CONST {cout<<"constant"<<endl;}
	;

returnStmt:
	RETURN INTEGER_CONST ';' { cout << "return statement" <<endl; }
	;

conditionStmt:
	IF '(' condition ')' ENDLS '{' ENDLS statements ENDLS '}'
	;

loopStmt:
	WHILE '(' condition ')' ENDLS '{' ENDLS statements ENDLS '}'
	;

condition:
	expression RELATIONAL_OPERATOR expression 
	;

ENDLS:
	ENDLS ENDL
	| ENDL
	|
	;
%%

int main(int, char**) {
	// open a file handle to a particular file:
	FILE *myfile = fopen("in.txt", "r");
	// make sure it's valid:
	if (!myfile) {
		cout << "I can't open a.snazzle.file!" << endl;
		return -1;
	}
	// set lex to read from it instead of defaulting to STDIN:
	yyin = myfile;

	// parse through the input until there is no more:	
	do {
		yyparse();
	} while (!feof(yyin));
	
}

void yyerror(const char *s) {
	cout << "EEK, parse error on line " << num_line << "!  Message: " << s << endl;
	// might as well halt now:
	exit(-1);
}