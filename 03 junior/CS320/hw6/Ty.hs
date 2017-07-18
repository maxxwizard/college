----------------------------------------------------------------
-- Computer Science 320 (Fall, 2009)
-- Concepts of Programming Languages
--
-- Assignments 6, 7, and 8
--   Ty.hs

----------------------------------------------------------------
-- Syntax for Mini-Haskell types 

module Ty (typeCheck) 
  where

import Err
import Env
--import Unify
import Exp (Exp(..), Oper(..), showTuple)
import Val

data Ty = TyVar String
        | TyBool
        | TyInt
        | TyTuple [Ty]
        | TyList Ty
        | Ty `TyArrow` Ty
  deriving Eq

----------------------------------------------------------------
-- This function is exported to the Main module.

-- Assignment 6, Problem 3, Part D
typeCheck :: Exp -> Error Ty
typeCheck e =
  case ty0 e of
    Error msg -> Error msg
    S t -> S t

----------------------------------------------------------------
-- Type checking with no variables or bindings.

-- Assignment 6, Problem 3, Part A
tyOp :: Oper -> Ty
tyOp Plus = TyInt `TyArrow` TyInt `TyArrow` TyInt
tyOp Times = TyInt `TyArrow` TyInt `TyArrow` TyInt
tyOp Equal = TyInt `TyArrow` TyInt `TyArrow` TyInt
tyOp And = TyBool `TyArrow` TyBool `TyArrow` TyBool
tyOp Or = TyBool `TyArrow` TyBool `TyArrow` TyBool
tyOp Not = TyBool `TyArrow` TyBool
tyOp Cons = TyInt `TyArrow` (TyList TyInt)
tyOp Head = (TyList TyInt) `TyArrow` TyInt
tyOp Tail = (TyList TyInt) `TyArrow` (TyList TyInt)
tyOp _ =  TyVar "Error: Cannot infer type of operator."

extract (S x) = x
firstArgInFunct :: Ty -> Ty
firstArgInFunct (t1 `TyArrow` t2) = firstArgInFunct t1
firstArgInFunct t = t
lastArgInFunct :: Ty -> Ty
lastArgInFunct (t1 `TyArrow` t2) = t2
lastArgInFunct t = t
-- Assignment 6, Problem 3, Part B
ty0 :: Exp -> Error Ty
ty0 (If x y z) = if (extract (ty0 x) == TyBool) then
					case (extract (ty0 y) == extract (ty0 z)) of -- y and z must have the same type
						True -> ty0 y
						False -> Error "If branches cannot have different return types."
				 else
					Error ("if expression must return a type of Bool: " ++ (show (ty0 x)))
ty0 (App e1 e2) = case extract (ty0 e1) of
					TyArrow t2 t1 -> case extract (ty0 e2) of
										t2 -> S t1
										otherwise -> Error "Parameter does not fit function."
					otherwise -> Error "Function is of incorrect shape."
ty0 (N _) = S TyInt
ty0 (B _) = S TyBool
ty0 (Op o) = S (tyOp o)
ty0 (Tuple t) = encase (mapError ty0 t)
					where encase (S a) = S (TyTuple a)
--ty0 (Tuple []) = S (TyTuple [TyInt])
ty0 Nil = S (TyList TyInt)
ty0 _ = Error "Cannot infer type of expression."
----------------------------------------------------------------
-- Basic type-checking algorithm for expressions with variables
-- and bindings.

-- Assignment 6, Problem 3, Part C
ty1 :: Env Ty -> Exp -> Error Ty
ty1 _ _ = Error "Assignment 6, Problem 3(c) Not Yet Implemented"

----------------------------------------------------------------
-- Type substitution and unification

-- Assignment 6, Problem 4, Part F
--instance Substitutable Ty where

-- Assignment 6, Problem 4, Part G
--instance Unifiable Ty where

----------------------------------------------------------------
-- Infinite List of Fresh Type Variables

-- Assignment 6, Problem 4, Part H
type FreshVars = [Ty]

----------------------------------------------------------------
-- Printing functions for Syntax of Types

instance Show Ty where
  show (TyVar s)   = s
  show TyBool      = "Bool"
  show TyInt       = "Int"
  show (TyList t)  = "[" ++ show t ++ "]"
  show (TyTuple ts) = showTuple (map show ts)

  -- If the left argument of an arrow is an arrow, we need to
  -- add parentheses to make sure the type is not ambiguous.
  show (TyArrow (TyArrow t1 t2) t3) = 
                "("++show (TyArrow t1 t2)++") -> "++show t3
  show (TyArrow t1 t2) = show t1++" -> "++show t2

--eof
