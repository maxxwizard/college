-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #5

module Unify (emp, sub, get, Subst, Substitutable, Unifiable, subst, vars, solved, reduce, unify, combine, resolve) where

	type Subst a = [(String, a)]

	--1.a
	emp :: Subst a
	emp = []
	sub :: String -> a -> Subst a
	sub str v = (str, v) : []

	--1.b
	get :: String -> Subst a -> Maybe a
	get str [] = Nothing
	get str s
			| (fst (head s) == str) = Just (snd (head s))
			| otherwise 			= get str (tail s)
	
	--2
	class Substitutable a where
		subst :: Subst a -> a -> a
		vars :: a -> [String]
		solved :: Subst a -> Bool
		reduce :: Subst a -> Subst a
	
	--3
	class Unifiable a where
		unify :: (Eq a, Substitutable a) => a -> a -> Maybe (Subst a)
		combine :: Maybe (Subst a) -> Maybe (Subst a) -> Maybe (Subst a)
		resolve :: Maybe (Subst a) -> Maybe (Subst a)