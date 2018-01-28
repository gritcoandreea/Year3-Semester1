digit                    [0-9]
lowercase_letter         [a-z]
uppercase_letter         [A-Z]
letter                   {uppercase_letter}|{lowercase_letter}
identifier               {uppercase_letter}({letter}|{digit}){1,7}
positive_digit           [1-9]
integer_number           {positive_digit}({digit}*)
integer                  (-?{integer_number})|0
real_number              -?{integer}"."{digit}*
boolean_constant         true|false
reserved_word            int|bool|float|if|else|while|std::cin|std::cout|std::endl|include|iostream|return|main
separator                \+|\-|\*|\/|=|\<|\>|==|\>=|\<=|!=|\>\>|\<\<|\{|\}|;|\(|\)|\[|\]|\.|#

%{
  char symbol_table_consts[100][9];
  int last_id_consts = 0;
  char symbol_table_identifiers[100][9];
  int last_id_identifiers = 0;
%}
%option noyywrap

%%
[digit ideintifier]       {
                            printf("ERROR:  %s\n", yytext);
                            continue;
                          }
{identifier}             {
                            printf("identifier          %s\n", yytext);
                            int found = 0;
							              int i=0;
                            for (; i < last_id_identifiers; i++) {
                              if (strcmp(symbol_table_identifiers[i], yytext) == 0) {
                                found = 1;
                                break;
                              }
                            }
                            if (!found) {
                              strcpy(symbol_table_identifiers[last_id_identifiers++], yytext);
                            }
                         }
{integer}                {
                            printf("integer             %s\n", yytext);
                            int found = 0;
							              int i=0;
                            for (; i < last_id_consts; i++) {
                              if (strcmp(symbol_table_consts[i], yytext) == 0) {
                                found = 1;
                                break;
                              }
                            }
                            if (!found) {
                              strcpy(symbol_table_consts[last_id_consts++], yytext);
                            }
                         }
{real_number}            {
                            printf("real number             %s\n", yytext);
                            int found = 0;
							              int i=0;
                            for (; i < last_id_consts; i++) {
                              if (strcmp(symbol_table_consts[i], yytext) == 0) {
                                found = 1;
                                break;
                              }
                            }
                            if (!found) {
                              strcpy(symbol_table_consts[last_id_consts++], yytext);
                            }
                         }
{boolean_constant}       {
                            printf("boolean                 %s\n", yytext);
                            int found = 0;
						              	int i=0;
                            for (; i < last_id_consts; i++) {
                              if (strcmp(symbol_table_consts[i], yytext) == 0) {
                                found = 1;
                                break;
                              }
                            }
                            if (!found) {
                              strcpy(symbol_table_consts[last_id_consts++], yytext);
                            }
                         }
{reserved_word}          printf("reserved word       %s\n", yytext);
{separator}              printf("separator           %s\n", yytext);
[ \n\t\r]+               printf("");
.                        printf("INVALID CHARACTER:  %s\n", yytext);
%%

int main(int argc, char *argv[]) {
    yyin = fopen(argv[1], "r");
    yylex();

    printf("\nSymbol table constants\n");
	int i=0;
    for (; i < last_id_consts; i++) {
      printf("%d    -    %s\n", i, symbol_table_consts[i]);
    }

    printf("\nSymbol table identifiers\n");
	i=0;
    for (; i < last_id_identifiers; i++) {
      printf("%d    -    %s\n", i, symbol_table_identifiers[i]);
    }
    fclose(yyin);
}
