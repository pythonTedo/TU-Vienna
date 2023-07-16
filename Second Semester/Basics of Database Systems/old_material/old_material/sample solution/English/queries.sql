--- Query 1 -- Number of expensive orders per exchange (i.e. price > 1M euros)
CREATE OR REPLACE VIEW NumberOfExpensiveOrders AS
	SELECT exchange, count(*)
	FROM orders
	WHERE price > 1000000
	GROUP BY exchange;

--- Query 2 -- Transitive closure of holds
CREATE OR REPLACE VIEW AllHolds AS
WITH RECURSIVE tmp(FSID, FDName, FBName, WSID, WDName, WBName) as ( 
       SELECT FSID, FDName, FBName, WSID, WDName, WBName 
	   FROM weighting
       UNION
       SELECT tmp.FSID, tmp.FDName, tmp.FBName, w.WSID, w.WDName, w.WBName 
	   FROM weighting w, tmp 
	   WHERE tmp.WSID = w.FSID AND tmp.WDName = w.FDName AND tmp.WBName = w.FBName
       ) SELECT * FROM tmp;
SELECT * FROM AllHolds;


--- Query 3 -- Distance and commulative product of weights of allholds
CREATE OR REPLACE VIEW WeightingOfHolds AS
WITH RECURSIVE tmp(FSID, FDName, FBName, WSID, WDName, WBName, comWeight, distance) as ( 
       SELECT FSID, FDName, FBName, WSID, WDName, WBName, weight, 1 
	   FROM weighting
       UNION
       SELECT tmp.FSID, tmp.FDName, tmp.FBName, w.WSID, w.WDName, w.WBName, (tmp.comWeight * w.weight)::NUMERIC(10,2), tmp.distance + 1
	   FROM weighting w, tmp 
	   WHERE tmp.WSID = w.FSID AND tmp.WDName = w.FDName AND tmp.WBName = w.FBName
       ) SELECT * FROM tmp;
SELECT * FROM WeightingOfHolds;
