digit                    [0-9]
lowercase_letter         [a-z]
uppercase_letter         [A-Z]
letter                   {uppercase_letter}|{lowercase_letter}
identifier               {uppercase_letter}({letter}|{digit}){0,7}
positive_digit           [1-9]
integer_number           {positive_digit}({digit}*)
integer                  (-?{integer_number})|0
real_number              -?{integer}"."{digit}*
boolean_constant         true|false
string_const 			 \"(({letter}|{digit})*)\"
arth_operators 		     \+|\-|\*|\/
relational_operator		 \<|\>|==|\>=|\<=|!=	

%{
#include <iostream>
#include<string.h>
#include "Parser.tab.h"
using namespace std;
#define YY_DECL extern "C" int yylex()
int num_line = 1;
%}

%option noyywrap

%%
[ \t]    ;
\{      							{return '{';}						
\}      							{return '}';}						
\(      							{return '(';}						
\)      							{return ')';}						
\;									{return ';';}
=									{return '=';}
,									{return ',';}
#include<.+>                  		{ return INCLUDE;}
int 								{return TYPE;}
float								{return TYPE;}
main 					     		{ return MAIN;}
{identifier}	 					{ return IDENTIFIER;}
{real_number} 						{return REAL_CONST;}
{digit}|{integer_number}			{return INTEGER_CONST;}
{boolean_constant}					{return BOOL_CONST;}
std::cout<<    						{return WRITE;}
std::cin>>							{return READ;}
{arth_operators}   					{return ARTHM_OPERATOR;}
{relational_operator}				{return RELATIONAL_OPERATOR;}
return								{return RETURN;}
if 									{return IF;}
while								{return WHILE;}
{string_const}						{return STRING_CONST;}
\n 									{num_line++; return ENDL;}
. 									;
%%