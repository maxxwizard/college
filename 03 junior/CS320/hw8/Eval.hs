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

evalExp :: Exp -> Error Val
evalExp e = ev emptyEnv e

----------------------------------------------------------------
-- Functions for evaluating operations applied to values.

-- Assignment 6, Problem 1, Part A
appOp :: Oper -> Val -> Error Val
appOp Not  (VB b)         = S $ VB $ not b
appOp Head (VList (v:vs)) = S $ v
appOp Tail (VList (v:vs)) = S $ VList vs
appOp Head _              = Error "head applied to empty list"
appOp Tail _              = Error "tail applied to empty list"
appOp Not  _              = Error "not applied to non-boolean"
appOp op v2               = S $ Partial op v2

-- Assignment 6, Problem 1, Part B
appBinOp :: Oper -> Val -> Val -> Error Val
appBinOp Plus  (VN n) (VN n') = S $ VN (n + n')
appBinOp Times (VN n) (VN n') = S $ VN (n * n')
appBinOp Equal (VN n) (VN n') = S $ VB (n == n')
appBinOp And   (VB b) (VB b') = S $ VB (b && b')
appBinOp Or    (VB b) (VB b') = S $ VB (b || b')
appBinOp Cons  v      (VList vs) = S $ VList (v:vs)
appBinOp Cons  v      VNil    = S $ VList (v:[])
appBinOp op v v' =
  Error $ "binary operator " ++ show op 
           ++ "not defined on arguments " 
           ++ (show v) ++ " and " ++ (show v')

----------------------------------------------------------------
-- Function for applying one value to another.

-- Assignment 6, Problem 1, Part C
appVals :: Val -> Val -> Error Val
appVals (VOp op)           v2     = appOp op v2
appVals (Partial op v1 )   v2     = appBinOp op v1 v2

-- Assignment 7, Problem 1, Part A

appVals _ _     = Error "Assignment 7, Problem 1(a) Not Yet Implemented"

-- Assignment 7, Problem 2, Part B

appValExp :: Val -> Exp -> Error Val
appValExp _ _ = Error "Assignment 7, Problem 2(b) Not Yet Implemented"

----------------------------------------------------------------
-- Function for evaluating an expression with no bindings or
-- variables to a value.

-- Assignment 6, Problem 1, Part D
ev0 :: Exp -> Error Val
ev0 Nil     = S VNil
ev0 (N n)   = S (VN n)
ev0 (B b)   = S (VB b)
ev0 (Op op) = S (VOp op)

ev0 (App e1 e2) =
  case (ev0 e1) of
    Error err -> Error err
    S v1 -> 
      case (ev0 e2) of
        Error err -> Error err
        S v2 -> appVals v1 v2

ev0 (If e1 e2 e3) =
  case (ev0 e1) of
    S (VB c)  -> if c then ev0 e2 else ev0 e3
    S _       -> Error "'if' condition not a boolean"
    Error err -> Error err

ev0 (Tuple es) = case (mapError ev0 es) of
                   Error msg -> Error msg
                   S vs -> S $ VTuple vs

ev0 _ = Error "expression too complex for ev0"

----------------------------------------------------------------
-- Function for evaluating an expression to a value. Note the
-- need for an environment to keep track of variables.

-- Assignment 6, Problem 2, Part A
ev :: Env Val -> Exp -> Error Val
ev env Nil     = S VNil
ev env (N n)   = S (VN n)
ev env (B b)   = S (VB b)
ev env (Op op) = S (VOp op)

ev env (Var x) =
  case (findEnv x env) of
    Just x' -> S x'
    Nothing -> Error $ "unbound variable: " ++ x

ev env (App e1 e2) =
  case (ev env e1) of
    Error err -> Error err
    S v1 -> case (ev env e2) of
        Error err -> Error err
        S v2 -> appVals v1 v2

ev env (Tuple es) = case mapError (ev env) es of
                          S vs -> S $ VTuple vs
                          Error msg -> Error msg

ev env (If e1 e2 e3) =
  case (ev env e1) of
    S (VB c)  -> if c then ev env e2 else ev env e3
    S _       -> Error "'if' condition not a boolean"
    Error err -> Error err

ev env (Let [x] e be) =
  case (ev env e) of
    Error err -> Error err
    S v       -> ev (updEnv x v env) be

-- Assignment 6, Problem 4, Part B
-- The parser will never produce a tuple of size
-- one, but this code will still work for tuples
-- of size 0,2,3,4,..., so it is sufficient.

ev env (Let xs e be) =
  case (ev env e) of
    Error err -> Error err
    S (VTuple vs) ->
      if length xs /= length vs then
          Error "tuples of mismatched lengths"
      else
          ev (updEnvL (zip xs vs) env) be
    S _ -> Error "cannot assign non-tuple value to tuple"

----------------------------------------------------------------
-- Helper functions for call-by-name evaluation of Mini-Haskell.

-- Assignment 7, Problem 2, Part A
--instance Substitutable Exp where

--eof
