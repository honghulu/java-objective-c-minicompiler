# PA6_Advanced Java Continued
program:
	* after check, program is
	public class [ID] {...}
	private class [ID] {...}
	-take out [...] for prgm_list
prgm_list:
	* after check, prgm_list is a sequence of
	...;
	...}
	...{...} or ...{...{...}...}
	epsilon
	-take out [...] by 1 token before ; for var_decl;
	-take out [...] by } for func
	-do nothing for epsilon
func:
	* after check, func is
	void id () {...}
	-take out [...] for stmt_list
var_decl:
	* after check, var_decl is
	int [id]
	-sign to the symbol table
stmt_list:
	* after check, stmt_list is a sequence of
	...;
	...}
	...{...} or ...{...{...}...}
	epsilon
	-take out [...] by ; for stmt
	-take out [...] by } for stmt
	-do nothing for epsilon
stmt:
	* after check, stmt is
	int ...;
	if ...}
	while ...)
	-take out [...] that contain int or id at index 0 and length of stmt is greater than 2 for assignment
	-take out [...] that length of stmt is equal to 2 without ; and by 1 token before ; for var_decl
	-take out [...] that contain if or while at index 0 for control_flow
assignment:
	* after check, assignment is
	int id = ...;
	id = ...;
	-take out [...] for expression
control_flow:
	* after check, control flow is
	if (expr){...}
	while (expr){...}, where expr is an expression contains at least 1 compare operator
	-take out [...] for stmt_list
	-for more boolean expressions, there is only 1 compare operator between them. (...BCE...)


*left child content               -- node content --           right child content    (leaves: leaves description)

subroot of program sequence node  -- program node
itself/leaves                     -- program sequence node --  leaves      (leaves: var decl node/ func node)
                                  -- var decl node --          id node
				  
parameter                         -- func node --              func_stmt_seq
param                             -- parameter --              param_seq
param_seq/param                   -- param_seq --              param
stmt_seq                          -- func_stmt_seq --          ret

itself/leaves                     -- statment sequence node -- leaves      (leaves: statment node)
leaves                            -- statment node                         (leaves: var decl node/ assignment node/ control flow node)
leaves                            -- assignment node --         expression (leaves: var decl node/ id node)
expression                        -- control flow node --       statment sequence node

Expression: terminal:- id, num, function call
argument -- function call --

arg          -- argument -- arg_list
arg/arg_list -- arg_list -- arg
