----------------------------------------------------------------
-- Computer Science 320 (Fall, 2009)
-- Concepts of Programming Languages
--
-- Assignments 6, 7, and 8
--   Eval.hs

----------------------------------------------------------------
-- Evaluation for Mini-Haskell

module Eval (evalExp) where

import Env
import Err
import Exp
import Val

----------------------------------------------------------------
-- This function is exported to the Main module.

-- Assignment 6, Problem 2, Part B
--evalExp :: Exp -> Error Val
--evalExp e = ev0 e
evalExp :: Exp -> Error Val
evalExp e = ev [] e


----------------------------------------------------------------
-- Functions for evaluating operations applied to values.

-- Assignment 6, Problem 1, Part A
appOp :: Oper -> Val -> Error Val
appOp Plus (VN x) = S (Partial Plus (VN x))
appOp Times (VN x) = S (Partial Times (VN x))
appOp Equal x = S (Partial Equal x)
appOp And (VB x) = S (Partial And (VB x))
appOp Or (VB x) = S (Partial Or (VB x))
appOp Not (VB x) = S (VB (not x))
appOp Cons (VN x) = S (Partial Cons (VN x))
appOp Cons (VB x) = S (Partial Cons (VB x))
appOp Cons (VOp x) = S (Partial Cons (VOp x))
appOp Head (VList x) = S (head x)
appOp Tail (VList x) = S (VList (tail x))
appOp _ _ = Error "The expression syntax is malformed."

-- Assignment 6, Problem 1, Part B
appBinOp :: Oper -> Val -> Val -> Error Val
appBinOp Plus (VN x) (VN y) = S (VN (x + y))
appBinOp Times (VN x) (VN y) = S (VN (x * y))
appBinOp Equal (VN x) (VN y) = S (VB (x == y))
appBinOp And (VB x) (VB y) = S (VB (x && y))
appBinOp Or (VB x) (VB y) = S (VB (x || y))
appBinOp Cons x (VList y) = S (VList (x : y))
appBinOp Cons x (VList []) = S (VList (x : []))
appBinOp Cons x VNil = S (VList (x : []))
appBinOp _ _ _ = Error "The expression syntax is malformed."

----------------------------------------------------------------
-- Function for applying one value to another.

-- Assignment 6, Problem 1, Part C
appVals :: Val -> Val -> Error Val
appVals (VOp op) x = appOp op x
appVals (Partial op val) x = appBinOp op val x
appVals _ _ = Error "The expression syntax is malformed."

----------------------------------------------------------------
-- Function for evaluating an expression with no bindings or
-- variables to a value.

-- Assignment 6, Problem 1, Part D
ev0 :: Exp -> Error Val
ev0 (If x y z) = case (ev0 x) of
					S (VB True) -> (ev0 y)
					S (VB False) -> (ev0 z)
ev0 (App x y) = appVals (extract (ev0 x)) (extract (ev0 y))
				where extract (S a) = a
ev0 (Tuple y) = encase (mapError ev0 y)
				where encase (S a) = S (VTuple a)
ev0 (Tuple []) = S (VNil)
ev0 (N x) = S (VN x)
ev0 (B x) = S (VB x)
ev0 (Op x) = S (VOp x)
ev0 Nil = S (VNil)
ev0 _ = Error "The expression syntax is malformed."

----------------------------------------------------------------
-- Function for evaluating an expression to a value. Note the
-- need for an environment to keep track of variables.

-- Assignment 6, Problem 2, Part A
ev :: Env Val -> Exp -> Error Val
ev env (If x y z) = case (ev env x) of
						S (VB True) -> (ev env y)
						S (VB False) -> (ev env z)
ev env (App x y) = appVals (extract (ev env x)) (extract (ev env y))
					where extract (S a) = a
ev env (Tuple y) = encase (mapError (ev env) y)
					where encase (S a) = S (VTuple a)
ev env (Tuple []) = S (VNil)
ev env (N x) = S (VN x)
ev env (B x) = S (VB x)
ev env (Op x) = S (VOp x)
ev env Nil = S (VNil)
ev env (Var s) = case (findEnv s env) of
					Just x -> S x
					Nothing -> Error ("Variable " ++ s ++ " not found.")
ev env (Let var e1 e2) = case (ev env e1) of
							S x -> ev (updEnv (var!!0) x env) e2
							Error y -> Error ("Expression '" ++ (show e1) ++ "' could not be resolved.")
									
ev _ _ = Error "The expression syntax is malformed."


--eof
