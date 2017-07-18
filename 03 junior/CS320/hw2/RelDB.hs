-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #2

module RelDB where
	data Value = I Int | S String | Null
		deriving (Show, Eq) --so that you can display values and check equality
	type Column = String
	type Row = [Value]
	type Table = ([Column], [Row])
	type Op = Value -> Value -> Value
	type LogicOp = Value -> Value -> Bool
	type AggregateOp = [Value] -> Value
	data OpArg = C Column | V Value
	
	tbl0 :: Table
	tbl0 = (["ColA", "ColB"], [
		   [I 1, I 1],
		   [I 2, I 4],
		   [I 3, I 8]
		   ])
	
	tbl1 = (["Metro area", "Population"], [
			[S "Tokyo", I 32450000],
			[S "Seoul ", I 20550000],
			[S "Mexico City", I 20450000],
			[S "New York City", I 19750000],
			[S "Mumbai", I 19200000],
			[S "Jakarta", I 18900000],
			[S "Sao Paulo", I 18850000],
			[S "Delhi", I 18600000] ] )
			
	tbl2 = (["City", "Country"], [
			[S "Tokyo", S "Japan"],
			[S "Seoul ", S "South Korea"],
			[S "Mexico City", S "Mexico"],
			[S "New York City", S "United States"],
			[S "Mumbai", S "India"],
			[S "Jakarta", S "Indonesia"],
			[S "Sao Paulo", S "Brazil"],
			[S "Delhi", S "India"] ] )
	
	--1.a
	plus :: Op
	plus (I x) (I y) = I (x + y)
	plus _ _ 		 = Null
	
	--1.b
	ccat :: Op
	ccat (S x) (S y) = S (x ++ y)
	ccat _ _ 		 = Null
	
	--1.c
	eqls :: LogicOp
	eqls x y = if (x == y) then True else False
	
	--1.d
	agg :: Op -> Value -> AggregateOp
	agg op (I base) xs = foldr op (I base) xs
	agg op (S base) xs = foldr op (S base) xs
	
	--2.a
	v :: Column -> [(Column, Value)] -> Value
	v col [] = Null
	v col (x : xs) = if (fst x) == col then (snd x) else v col xs
	
	--2.b
	select :: [Column] -> Table -> Table
	select cols tbl = ( [ col | col <- cols, col' <- (fst tbl), col == col' ],
						[ [ val | (col,val) <- item, col' <- cols, col' == col ]
						  | item <- (map (zip (fst tbl)) (snd tbl)) ] )
						  
	--2.c
	aggregate :: AggregateOp -> Column -> Table -> Value
	aggregate op col tbl =	op (concat (snd (select (col:[]) tbl)))
	
	--2.d
	join :: Table -> Table -> Table
	join tbl1 tbl2 = (header, [ (list1 ++ list2) | list1 <- (snd tbl1), list2 <- (snd tbl2) ])
					  where header = ( fst tbl1 ++ fst tbl2 )
					  
	--2.e
	extract :: OpArg -> [(Column, Value)] -> Value
	extract (C arg) row = v arg row --[ snd item | item <- row, arg = fst item ]
	extract (V arg) row = arg
	
	only :: OpArg -> LogicOp -> OpArg -> Table -> Table
	only arg1 op arg2 tbl = (header, [ row | row <- snd tbl, op (extract arg1 (zip (fst tbl) row)) (extract arg2 (zip (fst tbl) row)) ])
			           where header = fst tbl

        --3.a
        tbl3 :: Table
	tbl3 = select [("Population"), ("Country")] (only (C "Metro area") eqls (C "City") (tbl1 `join` tbl2))

	--3.b
	population :: Value
	population = aggregate (agg plus (I 0)) "Population" (only (C "Country") eqls (V (S "India")) (only (C "Metro area") eqls (C "City") (tbl1 `join` tbl2)))