-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #2

module Superstring where
	import List
	import Data.List

	test1 = ["ctagcgacat", "aagatagtta", "gctactaaga", "gacatattgt", "tagttactag"]
	test2 = ["101001","010100010100","100101","001010","11010","100","11010"]
	test3 = [x++y | x<-["aab","dcc","aaa"], y<-["dcc","aab"]]
	
	--4.a
	overlap :: (String, String) -> Int
	overlap ([], _) = 0
	overlap (s, s') = if s `isPrefixOf` s' then (length s) else (overlap ((tail s), s'))
	
	--4.b
	contains :: String -> String -> Bool
	contains s s' = s `isInfixOf` s'
	
	--4.c
	o :: String -> String -> String
	o s s' = s ++ drop (overlap (s, s')) s'
	
	--4.d
	naive :: [String] -> String
	naive xs = foldr (o) "" xs
	
	minimize :: (a -> Int) -> a -> a -> a
	minimize obj x y = if obj x < obj y then x else y

	--5.a
	allPairs :: [String] -> [(String, String)]
	allPairs [] = []
	allPairs xs = [ (x, y) | x <- xs, y <- xs, x /= y ]
	
	--5.b
	update :: [String] -> (String, String) -> [String]
	update l (s, s') = s'' : filter (not.(`isInfixOf` s'')) l
			   where s'' = s `o` s'
						
	--5.c
	superstring :: ([String] -> [(String, String)]) -> [String] -> String
	superstring next [] = []
	superstring next [x] = x
	superstring next l = foldr (minimize length) (naive l) [ superstring next x | x <- map (update l) pairs ]
			     where pairs = next l

	--5.d
	optimal :: [String] -> String
	optimal l = superstring allPairs l